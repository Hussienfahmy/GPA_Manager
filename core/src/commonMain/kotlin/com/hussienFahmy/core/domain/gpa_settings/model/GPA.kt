package com.hussienfahmy.core.domain.gpa_settings.model

import kotlinx.serialization.Serializable

data class GPA(
    val system: System
) {
    // @Serializable needed for sync_domain.model.CalculationSettings' Firestore round-trip.
    @Serializable
    enum class System(val number: Int) {
        FOUR(4),
        FIVE(5),
    }
}