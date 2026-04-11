package com.hussienfahmy.myGpaManager.navigation.screens.onboarding

import androidx.lifecycle.viewModelScope
import com.hussienfahmy.core.data.local.entity.Grade
import com.hussienfahmy.core.domain.grades.use_case.GetActiveGrades
import com.hussienfahmy.core.domain.user_data.model.UserData
import com.hussienfahmy.core_ui.presentation.viewmodel.UiViewModel
import com.hussienfahmy.myGpaManager.navigation.screens.onboarding.models.AppOnBoardingGPATrackingEvent
import com.hussienfahmy.myGpaManager.navigation.screens.onboarding.models.AppOnBoardingGPATrackingState
import com.hussienfahmy.semester_history_domain.model.Semester
import com.hussienfahmy.semester_history_domain.use_case.AddPastSemester
import com.hussienfahmy.semester_history_domain.use_case.AddSubjectToSemester
import com.hussienfahmy.semester_history_domain.use_case.CalculateCumulativeFromHistory
import com.hussienfahmy.semester_history_domain.use_case.DeleteSemester
import com.hussienfahmy.semester_history_domain.use_case.EditSemester
import com.hussienfahmy.semester_history_domain.use_case.GetSemesterHistory
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch


class AppOnBoardingGPATrackingViewModel(
    getSemesterHistory: GetSemesterHistory,
    calculateCumulativeFromHistory: CalculateCumulativeFromHistory,
    getActiveGrades: GetActiveGrades,
    private val addPastSemester: AddPastSemester,
    private val deleteSemester: DeleteSemester,
    private val addSubjectToSemester: AddSubjectToSemester,
    private val editSemester: EditSemester,
) : UiViewModel<AppOnBoardingGPATrackingEvent, AppOnBoardingGPATrackingState>(
    initialState = { AppOnBoardingGPATrackingState() }
) {

    val semesters: StateFlow<List<Semester>> = getSemesterHistory()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val cumulative: StateFlow<UserData.AcademicProgress> = semesters
        .map { semesters ->
            calculateCumulativeFromHistory(semesters)
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            UserData.AcademicProgress(cumulativeGPA = 0.0, creditHours = 0)
        )

    val grades: StateFlow<List<Grade>> = getActiveGrades()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    override fun onEvent(event: AppOnBoardingGPATrackingEvent) {
        when (event) {
            is AppOnBoardingGPATrackingEvent.ShowAddSheet ->
                state.value = state.value.copy(showAddSheet = true)

            is AppOnBoardingGPATrackingEvent.HideAddSheet ->
                state.value = state.value.copy(showAddSheet = false)

            is AppOnBoardingGPATrackingEvent.SetAddingSubjectsSemester ->
                state.value = state.value.copy(addingSubjectsToSemesterId = event.id)

            is AppOnBoardingGPATrackingEvent.DeleteSemesterEvent ->
                viewModelScope.launch { deleteSemester(event.id) }

            is AppOnBoardingGPATrackingEvent.AddSummarySemester ->
                viewModelScope.launch {
                    addPastSemester(
                        AddPastSemester.Request.Summary(
                            label = event.label,
                            semesterGPA = event.gpa,
                            totalCreditHours = event.hours,
                            level = event.level
                        )
                    )
                }

            is AppOnBoardingGPATrackingEvent.AddDetailedSemester ->
                viewModelScope.launch {
                    val semesterId = addPastSemester(
                        AddPastSemester.Request.Detailed(
                            label = event.label,
                            level = event.level
                        )
                    )
                    state.value = state.value.copy(addingSubjectsToSemesterId = semesterId)
                }

            is AppOnBoardingGPATrackingEvent.AddSubject ->
                viewModelScope.launch {
                    addSubjectToSemester(
                        event.semesterId,
                        event.name,
                        event.creditHours,
                        event.gradeName
                    )
                    editSemester(EditSemester.Request.RecalculateDetailed(event.semesterId))
                }
        }
    }
}
