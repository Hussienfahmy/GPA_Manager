package com.hussienfahmy.core.domain.gpa_settings.model

data class GPA(
    val system: System
) {
    enum class System(val number: Int) {
        FOUR(4),
        FIVE(5),
    }
}