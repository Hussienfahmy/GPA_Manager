package com.hussienfahmy.core.domain.gpa_settings.model

import androidx.annotation.Keep

@Keep
data class GPA(
    val system: System
) {
    @Keep
    enum class System(val number: Int) {
        FOUR(4),
        FIVE(5),
    }
}