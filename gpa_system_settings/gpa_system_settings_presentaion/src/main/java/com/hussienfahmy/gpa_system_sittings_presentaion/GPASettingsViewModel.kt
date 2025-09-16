package com.hussienfahmy.gpa_system_sittings_presentaion

import androidx.lifecycle.viewModelScope
import com.hussienfahmy.core.domain.analytics.AnalyticsLogger
import com.hussienfahmy.core.domain.analytics.AnalyticsValues
import com.hussienfahmy.core.domain.gpa_settings.use_case.UpdateGPASystem
import com.hussienfahmy.core_ui.presentation.viewmodel.UiViewModel
import com.hussienfahmy.gpa_system_sittings_domain.use_case.ObserveGPASettings
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class GPASettingsViewModel(
    observeGPA: ObserveGPASettings,
    private val updateGPASystem: UpdateGPASystem,
    private val analyticsLogger: AnalyticsLogger,
) : UiViewModel<GPAEvent, GPAState>(initialState = { GPAState.Loading }) {

    init {
        observeGPA().onEach {
            state.value = GPAState.Loaded(it)
        }.launchIn(viewModelScope)
    }

    override fun onEvent(event: GPAEvent) {
        viewModelScope.launch {
            when (event) {
                is GPAEvent.UpdateGPASystem -> {
                    val fromSystem = when ((state.value as? GPAState.Loaded)?.gpa?.system) {
                        com.hussienfahmy.core.domain.gpa_settings.model.GPA.System.FOUR -> AnalyticsValues.GPA_SYSTEM_4_POINT
                        com.hussienfahmy.core.domain.gpa_settings.model.GPA.System.FIVE -> AnalyticsValues.GPA_SYSTEM_5_POINT
                        else -> AnalyticsValues.GPA_SYSTEM_4_POINT
                    }

                    val toSystem = when (event.system) {
                        com.hussienfahmy.core.domain.gpa_settings.model.GPA.System.FOUR -> AnalyticsValues.GPA_SYSTEM_4_POINT
                        com.hussienfahmy.core.domain.gpa_settings.model.GPA.System.FIVE -> AnalyticsValues.GPA_SYSTEM_5_POINT
                    }

                    analyticsLogger.logGpaSystemChanged(
                        fromSystem = fromSystem,
                        toSystem = toSystem
                    )

                    updateGPASystem(event.system)
                }
            }
        }
    }
}