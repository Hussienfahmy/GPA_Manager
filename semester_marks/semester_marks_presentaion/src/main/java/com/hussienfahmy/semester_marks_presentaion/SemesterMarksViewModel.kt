package com.hussienfahmy.semester_marks_presentaion

import androidx.lifecycle.viewModelScope
import com.hussienFahmy.core.data.local.util.UpdateResult
import com.hussienFahmy.core_ui.presentation.model.UiEvent
import com.hussienFahmy.core_ui.presentation.viewmodel.UiViewModel
import com.hussienfahmy.semester_marks_domain.use_case.SemesterMarksUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SemesterMarksViewModel @Inject constructor(
    private val useCases: SemesterMarksUseCases,
) : UiViewModel<SemesterMarksEvent, SemesterMarksState>(initialState = {
    SemesterMarksState.Loading
}) {

    init {
        viewModelScope.launch {
            useCases.continuesCalculation().collect {
                state.value = SemesterMarksState.Calculated(it)
            }
        }
    }

    override fun onEvent(event: SemesterMarksEvent) {
        viewModelScope.launch {
            val result: Any = when (event) {
                is SemesterMarksEvent.ChangeMidtermMark -> useCases.changeMidtermMarks(
                    event.subjectId,
                    event.mark
                )

                is SemesterMarksEvent.ChangeOralMark -> useCases.changeOralMarks(
                    event.subjectId,
                    event.mark
                )

                is SemesterMarksEvent.ChangePracticalMark -> useCases.changePracticalMarks(
                    event.subjectId,
                    event.mark
                )

                is SemesterMarksEvent.ChangeProjectMark -> useCases.changeProjectMarks(
                    event.subjectId,
                    event.mark
                )

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
            }

            if (result is UpdateResult.Failed) {
                _uiEvent.send(UiEvent.ShowToast(result.message))
            }
        }
    }
}