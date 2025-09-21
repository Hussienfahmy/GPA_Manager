package com.hussienfahmy.semester_marks_presentaion

import androidx.lifecycle.viewModelScope
import com.hussienfahmy.core.data.local.util.UpdateResult
import com.hussienfahmy.core.domain.analytics.AnalyticsLogger
import com.hussienfahmy.core.domain.analytics.AnalyticsValues
import com.hussienfahmy.core_ui.presentation.model.UiEvent
import com.hussienfahmy.core_ui.presentation.viewmodel.UiViewModel
import com.hussienfahmy.semester_marks_domain.use_case.SemesterMarksUseCases
import kotlinx.coroutines.launch

class SemesterMarksViewModel(
    private val useCases: SemesterMarksUseCases,
    private val analyticsLogger: AnalyticsLogger,
) : UiViewModel<SemesterMarksEvent, SemesterMarksState>(initialState = {
    SemesterMarksState.Loading
}) {

    init {
        viewModelScope.launch {
            useCases.continuesCalculation().collect { subjects ->
                state.value = SemesterMarksState.Calculated(subjects = subjects)
            }
        }
    }

    override fun onEvent(event: SemesterMarksEvent) {
        viewModelScope.launch {
            val result: Any = when (event) {
                is SemesterMarksEvent.ChangeMidtermMark -> {
                    analyticsLogger.logMarksEntered(
                        subjectId = event.subjectId,
                        markType = AnalyticsValues.ASSESSMENT_MIDTERM,
                        isComplete = event.mark.isNotBlank()
                    )
                    useCases.changeMidtermMarks(
                        event.subjectId,
                        event.mark
                    )
                }

                is SemesterMarksEvent.ChangeOralMark -> {
                    analyticsLogger.logMarksEntered(
                        subjectId = event.subjectId,
                        markType = AnalyticsValues.ASSESSMENT_ORAL,
                        isComplete = event.mark.isNotBlank()
                    )
                    useCases.changeOralMarks(
                        event.subjectId,
                        event.mark
                    )
                }

                is SemesterMarksEvent.ChangePracticalMark -> {
                    analyticsLogger.logMarksEntered(
                        subjectId = event.subjectId,
                        markType = AnalyticsValues.ASSESSMENT_PRACTICAL,
                        isComplete = event.mark.isNotBlank()
                    )
                    useCases.changePracticalMarks(
                        event.subjectId,
                        event.mark
                    )
                }

                is SemesterMarksEvent.ChangeProjectMark -> {
                    analyticsLogger.logMarksEntered(
                        subjectId = event.subjectId,
                        markType = AnalyticsValues.ASSESSMENT_PROJECT,
                        isComplete = event.mark.isNotBlank()
                    )
                    useCases.changeProjectMarks(
                        event.subjectId,
                        event.mark
                    )
                }

                is SemesterMarksEvent.ResetMarks -> useCases.resetMarks(event.subjectId)
                is SemesterMarksEvent.SetMidtermAvailability -> {
                    useCases.setMidtermAvailable(
                        event.subjectId,
                        event.isAvailable
                    )
                    useCases.changeMidtermMarks(event.subjectId, "0")
                }

                is SemesterMarksEvent.SetOralAvailability -> {
                    useCases.setOralAvailable(
                        event.subjectId,
                        event.isAvailable
                    )
                    useCases.changeOralMarks(event.subjectId, "0")
                }

                is SemesterMarksEvent.SetPracticalAvailability -> {
                    useCases.setPracticalAvailable(
                        event.subjectId,
                        event.isAvailable
                    )
                    useCases.changePracticalMarks(event.subjectId, "0")
                }

                is SemesterMarksEvent.SetProjectAvailability -> {
                    useCases.setProjectAvailable(
                        event.subjectId,
                        event.isAvailable
                    )
                    useCases.changeProjectMarks(event.subjectId, "0")
                }

                is SemesterMarksEvent.OnScreenExit -> {
                    useCases.syncGradeWithMarks()
                    return@launch
                }
            }

            if (result is UpdateResult.Failed) {
                _uiEvent.send(UiEvent.ShowToast(result.message))
            }
        }
    }
}