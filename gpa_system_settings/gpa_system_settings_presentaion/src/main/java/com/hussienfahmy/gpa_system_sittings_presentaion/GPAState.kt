package com.hussienfahmy.gpa_system_sittings_presentaion

import com.hussienFahmy.core.domain.gpa_settings.model.GPA

sealed class GPAState {
    object Loading : GPAState()
    data class Loaded(val gpa: GPA) : GPAState()
}