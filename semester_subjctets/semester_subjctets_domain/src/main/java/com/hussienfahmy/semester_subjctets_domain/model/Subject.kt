package com.hussienfahmy.semester_subjctets_domain.model

import com.hussienFahmy.core.data.local.model.GradeName

data class Subject(
    val id: Long,
    val name: String,
    val creditHours: Double,
    val totalMarks: Double,
    val fixedGrade: Boolean,
    val selectedGradeName: GradeName?,
    val maxGradeNameCanBeAssigned: GradeName
)