package com.hussienfahmy.quick_domain.model

data class QuickCalculationRequest(
    val cumulativeGPA: String,
    val totalHours: String,
    val semesterGPA: String,
    val semesterHours: String,
)