package com.hussienfahmy.grades_setting_presentation

import androidx.lifecycle.viewModelScope
import com.hussienfahmy.core.data.local.util.UpdateResult
import com.hussienfahmy.core.domain.analytics.AnalyticsLogger
import com.hussienfahmy.core_ui.presentation.model.UiEvent
import com.hussienfahmy.core_ui.presentation.viewmodel.UiViewModel
import com.hussienfahmy.grades_setting_domain.use_case.GradeSettingsUseCases
import com.hussienfahmy.grades_setting_presentation.model.Mode
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class GradeSettingsViewModel(
    private val gradeSettingsUseCases: GradeSettingsUseCases,
    private val analyticsLogger: AnalyticsLogger,
) : UiViewModel<GradeEvent, GradeSettingsState>(
    initialState = { GradeSettingsState() }
) {

    override fun onEvent(event: GradeEvent) {
        viewModelScope.launch {
            val updateResult = when (event) {
                is GradeEvent.ActivateGrade -> {
                    analyticsLogger.logGradeSystemConfigured(
                        gradeName = event.gradeSetting.name.name,
                        points = event.gradeSetting.points ?: 0.0,
                        percentage = event.gradeSetting.percentage ?: 0.0,
                        isActive = event.isActive
                    )
                    gradeSettingsUseCases.activateGrade(
                        event.gradeSetting,
                        event.isActive
                    )
                }

                is GradeEvent.UpdatePercentage -> {
                    analyticsLogger.logGradeSystemConfigured(
                        gradeName = event.gradeSetting.name.name,
                        points = event.gradeSetting.points ?: 0.0,
                        percentage = event.percentage.toDoubleOrNull() ?: 0.0,
                        isActive = true
                    )
                    gradeSettingsUseCases.updatePercentage(
                        event.gradeSetting,
                        event.percentage
                    )
                }

                is GradeEvent.UpdatePoints -> {
                    analyticsLogger.logGradeSystemConfigured(
                        gradeName = event.gradeSetting.name.name,
                        points = event.points.toDoubleOrNull() ?: 0.0,
                        percentage = event.gradeSetting.percentage ?: 0.0,
                        isActive = true
                    )
                    gradeSettingsUseCases.updatePoints(
                        event.gradeSetting,
                        event.points
                    )
                }
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