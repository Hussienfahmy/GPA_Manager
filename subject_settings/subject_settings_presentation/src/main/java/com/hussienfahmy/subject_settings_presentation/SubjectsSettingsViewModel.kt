package com.hussienfahmy.subject_settings_presentation

import androidx.lifecycle.viewModelScope
import com.hussienfahmy.core.data.local.util.UpdateResult
import com.hussienfahmy.core.domain.analytics.AnalyticsLogger
import com.hussienfahmy.core.domain.analytics.AnalyticsValues
import com.hussienfahmy.core_ui.presentation.model.UiEvent
import com.hussienfahmy.core_ui.presentation.viewmodel.UiViewModel
import com.hussienfahmy.subject_settings_domain.use_case.SubjectSettingsUseCases
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class SubjectsSettingsViewModel(
    private val useCases: SubjectSettingsUseCases,
    private val analyticsLogger: AnalyticsLogger,
) : UiViewModel<SubjectSettingsEvent, SubjectSettingsState>(initialState = {
    SubjectSettingsState.Loading
}) {
    init {
        useCases.observeSubjectSettings()
            .onEach {
                state.value = SubjectSettingsState.Success(it)
            }.launchIn(viewModelScope)
    }

    override fun onEvent(event: SubjectSettingsEvent) {
        viewModelScope.launch {
            val result = when (event) {
                is SubjectSettingsEvent.UpdateConstantMarks -> {
                    analyticsLogger.logSubjectSettingsUpdated(
                        settingType = AnalyticsValues.SUBJECT_SETTING_CONSTANT_MARKS,
                        newValue = event.constantMarks
                    )
                    useCases.updateConstantMarks(event.constantMarks)
                }
                is SubjectSettingsEvent.UpdateMarksPerCreditHour -> {
                    analyticsLogger.logSubjectSettingsUpdated(
                        settingType = AnalyticsValues.SUBJECT_SETTING_MARKS_PER_CREDIT,
                        newValue = event.marksPerCreditHour
                    )
                    useCases.updateMarksPerCreditHour(
                        event.marksPerCreditHour
                    )
                }

                is SubjectSettingsEvent.UpdateSubjectMarksDependsOn -> {
                    analyticsLogger.logSubjectSettingsUpdated(
                        settingType = AnalyticsValues.SUBJECT_SETTING_MARKS_DEPENDS_ON,
                        newValue = event.dependsOn.name
                    )
                    useCases.updateSubjectsDependsOn(
                        event.dependsOn
                    )
                }
            }

            if (result is UpdateResult.Failed) {
                _uiEvent.send(
                    UiEvent.ShowSnackBar(result.message)
                )
            }
        }
    }
}