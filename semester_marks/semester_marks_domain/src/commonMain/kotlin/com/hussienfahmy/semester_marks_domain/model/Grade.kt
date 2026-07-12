package com.hussienfahmy.semester_marks_domain.model

data class Grade(
    val symbol: String,
    val achievable: Achievable,
) {
    sealed class Achievable {
        object No : Achievable()
        data class Yes(val neededMarks: Double) : Achievable()
    }
}
