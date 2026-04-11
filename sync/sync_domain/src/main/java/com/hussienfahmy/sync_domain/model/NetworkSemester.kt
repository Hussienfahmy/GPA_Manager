package com.hussienfahmy.sync_domain.model

import androidx.annotation.Keep
import com.hussienfahmy.core.data.local.entity.Semester

@Keep
data class NetworkSemester(
    val label: String = "",
    val level: Int = 1,
    val type: Semester.Type = Semester.Type.SUMMARY,
    val semesterGPA: Double = 0.0,
    val totalCreditHours: Int = 0,
    val order: Int = 0,
    val createdAt: Long = 0L,
    val archivedAt: Long? = null,
    val subjects: List<Subject> = emptyList(),
) {
    fun toEntity(): Semester = Semester(
        label = label,
        level = level,
        type = type,
        semesterGPA = semesterGPA,
        totalCreditHours = totalCreditHours,
        status = Semester.Status.ARCHIVED,
        order = order,
        createdAt = createdAt,
        archivedAt = archivedAt,
    )
}

fun Semester.toNetworkSemester(subjects: List<Subject> = emptyList()) = NetworkSemester(
    label = label,
    level = level,
    type = type,
    semesterGPA = semesterGPA,
    totalCreditHours = totalCreditHours,
    order = order,
    createdAt = createdAt,
    archivedAt = archivedAt,
    subjects = subjects,
)
