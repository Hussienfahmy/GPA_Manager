package com.hussienfahmy.semester_subjctets_presentaion.model

sealed class Mode {
    object Normal : Mode()
    data class Predict(
        val targetCumulativeGPA: String = "",
        val reverseSubjects: Boolean = false
    ) : Mode()
}