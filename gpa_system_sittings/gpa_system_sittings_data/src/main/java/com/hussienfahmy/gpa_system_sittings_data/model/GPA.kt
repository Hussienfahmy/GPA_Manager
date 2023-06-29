package com.hussienfahmy.gpa_system_sittings_data.model

import kotlinx.serialization.Serializable

@Serializable
data class GPA(
    val system: System = System.FOUR,
) {
    enum class System {
        FOUR,
        FIVE,
    }
}