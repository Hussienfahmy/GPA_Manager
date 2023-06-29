package com.hussienfahmy.gpa_system_sittings_presentaion

import com.hussienfahmy.gpa_system_sittings_domain.model.GPA

sealed class GPAState {
    object Loading : GPAState()
    data class Loaded(val gpa: GPA) : GPAState()
}