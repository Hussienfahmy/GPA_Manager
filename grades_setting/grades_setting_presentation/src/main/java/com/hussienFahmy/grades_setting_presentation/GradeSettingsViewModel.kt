package com.hussienfahmy.grades_setting_presentation

import androidx.lifecycle.viewModelScope
import com.hussienfahmy.core.data.local.util.UpdateResult
import com.hussienfahmy.core_ui.presentation.model.UiEvent
import com.hussienfahmy.core_ui.presentation.viewmodel.UiViewModel
import com.hussienfahmy.grades_setting_domain.use_case.GradeSettingsUseCases
import com.hussienfahmy.grades_setting_presentation.model.Mode
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GradeSettingsViewModel @Inject constructor(
    private val gradeSettingsUseCases: GradeSettingsUseCases
) : UiViewModel<GradeEvent, GradeSettingsState>(
    initialState = { GradeSettingsState() }
) {

    override fun onEvent(event: GradeEvent) {
        viewModelScope.launch {
            val updateResult = when (event) {
                is GradeEvent.ActivateGrade -> gradeSettingsUseCases.activateGrade(
                    event.gradeSetting,
                    event.isActive
                )

                is GradeEvent.UpdatePercentage -> gradeSettingsUseCases.updatePercentage(
                    event.gradeSetting,
                    event.percentage
                )

                is GradeEvent.UpdatePoints -> gradeSettingsUseCases.updatePoints(
                    event.gradeSetting,
                    event.points
                )
            }

            if (updateResult is UpdateResult.Failed) {
                _uiEvent.send(
                    UiEvent.ShowSnackBar(
                        message = updateResult.message,
                    )
                )
            }
        }
    }

    fun onModeChange(mode: Mode) {
        state.value = state.value.copy(mode = mode)
    }

    init {
        gradeSettingsUseCases.loadGrades().onEach {
            state.value = state.value.copy(gradesSetting = it, isLoading = false)
        }.launchIn(viewModelScope)
    }
}