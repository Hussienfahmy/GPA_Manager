package com.hussienfahmy.gpa_system_sittings_presentaion

import com.hussienfahmy.gpa_system_sittings_domain.model.GPA

sealed class GPAEvent {
    data class UpdateGPASystem(val system: GPA.System) : GPAEvent()
}