package com.hussienfahmy.semester_subjctets_domain.model

import com.hussienfahmy.core.data.local.model.GradeName

data class Subject(
    val id: Long,
    val name: String,
    val creditHours: Double,
    val totalMarks: Double,
    val fixedGrade: Boolean,
    val assignedGrade: Grade?,
    val maxGradeCanBeAssigned: Grade,
) {
    val selectedGradeName: GradeName? = assignedGrade?.name
    val maxGradeNameCanBeAssigned: GradeName = maxGradeCanBeAssigned.name
}