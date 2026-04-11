package com.hussienfahmy.semester_history_presentation

import androidx.lifecycle.viewModelScope
import com.hussienfahmy.core.R
import com.hussienfahmy.core.domain.user_data.model.UserData
import com.hussienfahmy.core.domain.user_data.use_cases.GetUserData
import com.hussienfahmy.core.model.UiText
import com.hussienfahmy.core_ui.presentation.model.UiEvent
import com.hussienfahmy.core_ui.presentation.viewmodel.UiViewModel
import com.hussienfahmy.semester_history_domain.use_case.AddPastSemester
import com.hussienfahmy.semester_history_domain.use_case.ArchiveCurrentSemester
import com.hussienfahmy.semester_history_domain.use_case.CalculateCumulativeFromHistory
import com.hussienfahmy.semester_history_domain.use_case.DeleteSemester
import com.hussienfahmy.semester_history_domain.use_case.EditSemester
import com.hussienfahmy.semester_history_domain.use_case.GetSemesterHistory
import com.hussienfahmy.semester_history_domain.use_case.GetWorkspaceSubjectCount
import com.hussienfahmy.semester_history_domain.use_case.ReorderSemester
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class SemesterHistoryViewModel(
    private val getSemesterHistory: GetSemesterHistory,
    private val archiveCurrentSemester: ArchiveCurrentSemester,
    private val addPastSemester: AddPastSemester,
    private val deleteSemester: DeleteSemester,
    private val editSemester: EditSemester,
    private val calculateCumulativeFromHistory: CalculateCumulativeFromHistory,
    private val reorderSemester: ReorderSemester,
    private val getUserData: GetUserData,
    private val getWorkspaceSubjectCount: GetWorkspaceSubjectCount,
) : UiViewModel<SemesterHistoryEvent, SemesterHistoryState>(initialState = {
    SemesterHistoryState.Loading
}) {

    private val _navigateToDetail = Channel<Long>()
    val navigateToDetail = _navigateToDetail.receiveAsFlow()

    init {
        viewModelScope.launch {
            combine(
                getSemesterHistory(),
                getUserData().filterNotNull(),
                getWorkspaceSubjectCount(),
            ) { semesters, userData, subjectCount ->
                val progress = calculateCumulativeFromHistory(semesters)

                SemesterHistoryState.Loaded(
                    semesters = semesters,
                    cumulativeGPA = progress.cumulativeGPA,
                    totalCreditHours = progress.creditHours,
                    currentLevel = userData.academicInfo.level,
                    currentSemesterNum = when (userData.academicInfo.semester) {
                        UserData.AcademicInfo.Semester.Second -> 2
                        UserData.AcademicInfo.Semester.First -> 1
                    },
                    hasWorkspaceSubjects = subjectCount > 0,
                )
            }.collect { state.value = it }
        }
    }

    override fun onEvent(event: SemesterHistoryEvent) {
        viewModelScope.launch {
            try {
                when (event) {
                    is SemesterHistoryEvent.FinishSemester -> {
                        when (archiveCurrentSemester()) {
                            is ArchiveCurrentSemester.Result.NoSubjects ->
                                _uiEvent.send(UiEvent.ShowSnackBar(UiText.StringResource(R.string.history_no_subjects_in_semester)))

                            is ArchiveCurrentSemester.Result.Success ->
                                _uiEvent.send(UiEvent.ShowSnackBar(UiText.StringResource(R.string.history_semester_archived)))
                        }
                    }

                    is SemesterHistoryEvent.DeleteSemester -> {
                        deleteSemester(event.id)
                        _uiEvent.send(UiEvent.ShowSnackBar(UiText.StringResource(R.string.history_semester_deleted)))
                    }

                    is SemesterHistoryEvent.AddSummarySemester -> {
                        addPastSemester(
                            AddPastSemester.Request.Summary(
                                label = event.label,
                                semesterGPA = event.semesterGPA,
                                totalCreditHours = event.totalCreditHours,
                                level = event.level,
                            )
                        )
                    }

                    is SemesterHistoryEvent.AddDetailedSemester -> {
                        val semesterId = addPastSemester(
                            AddPastSemester.Request.Detailed(
                                label = event.label,
                                level = event.level,
                            )
                        )
                        _navigateToDetail.send(semesterId)
                    }

                    is SemesterHistoryEvent.EditSemesterLabel -> {
                        editSemester(EditSemester.Request.Label(event.id, event.label))
                    }

                    is SemesterHistoryEvent.EditSummarySemester -> {
                        editSemester(
                            EditSemester.Request.SummaryFields(
                                semesterId = event.id,
                                label = event.label,
                                semesterGPA = event.semesterGPA,
                                totalCreditHours = event.totalCreditHours,
                            )
                        )
                    }

                    is SemesterHistoryEvent.MoveSemesterUp -> {
                        reorderSemester(event.id, ReorderSemester.Direction.UP)
                    }

                    is SemesterHistoryEvent.MoveSemesterDown -> {
                        reorderSemester(event.id, ReorderSemester.Direction.DOWN)
                    }
                }
            } catch (_: Exception) {
                _uiEvent.send(UiEvent.ShowSnackBar(UiText.StringResource(R.string.history_error_try_again)))
            }
        }
    }
}
