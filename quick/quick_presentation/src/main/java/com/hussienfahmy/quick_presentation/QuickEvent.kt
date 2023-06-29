package com.hussienfahmy.quick_presentation

import com.hussienfahmy.quick_domain.model.QuickCalculationRequest

sealed class QuickEvent {
    data class Calculate(val calculationRequest: QuickCalculationRequest) : QuickEvent()
}