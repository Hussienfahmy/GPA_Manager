package com.hussienfahmy.gpa_system_sittings_domain.model

data class GPA(
    val system: System
) {
    enum class System(val number: Int) {
        FOUR(4),
        FIVE(5),
    }
}