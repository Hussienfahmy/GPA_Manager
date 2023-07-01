package com.hussienfahmy.gpa_system_sittings_presentaion

import com.hussienFahmy.core.domain.gpa_settings.model.GPA

sealed class GPAEvent {
    data class UpdateGPASystem(val system: GPA.System) : GPAEvent()
}