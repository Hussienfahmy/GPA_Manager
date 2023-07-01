package com.hussienfahmy.gpa_system_sittings_presentaion

import androidx.lifecycle.viewModelScope
import com.hussienFahmy.core_ui.presentation.viewmodel.UiViewModel
import com.hussienfahmy.gpa_system_sittings_domain.use_case.ObserveGPASettings
import com.hussienfahmy.gpa_system_sittings_domain.use_case.UpdateGPASystem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GPASettingsViewModel @Inject constructor(
    observeGPA: ObserveGPASettings,
    private val updateGPASystem: UpdateGPASystem,
) : UiViewModel<GPAEvent, GPAState>(initialState = { GPAState.Loading }) {

    init {
        observeGPA().onEach {
            state.value = GPAState.Loaded(it)
        }.launchIn(viewModelScope)
    }

    override fun onEvent(event: GPAEvent) {
        viewModelScope.launch {
            when (event) {
                is GPAEvent.UpdateGPASystem -> updateGPASystem(event.system)
            }
        }
    }
}