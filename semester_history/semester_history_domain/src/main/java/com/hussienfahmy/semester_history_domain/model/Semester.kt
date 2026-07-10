package com.hussienfahmy.semester_history_domain.model

import com.hussienfahmy.core.data.local.entity.Semester as SemesterEntity

data class Semester(
    val id: Long,
    val label: String,
    val level: Int,
    val type: Type,
    val semesterGPA: Double,
    val totalCreditHours: Int,
    val status: Status,
    val order: Int,
    val createdAt: Long,
    val archivedAt: Long?,
    /** True when a DETAILED semester has a subject with no grade assigned. */
    val hasMissingGrade: Boolean = false,
) {
    enum class Type { SUMMARY, DETAILED }
    enum class Status { CURRENT, ARCHIVED }
}

fun SemesterEntity.toDomain(hasMissingGrade: Boolean = false) = Semester(
    id = id,
    label = label,
    level = level,
    type = when (type) {
        SemesterEntity.Type.SUMMARY -> Semester.Type.SUMMARY
        SemesterEntity.Type.DETAILED -> Semester.Type.DETAILED
    },
    semesterGPA = semesterGPA,
    totalCreditHours = totalCreditHours,
    status = when (status) {
        SemesterEntity.Status.CURRENT -> Semester.Status.CURRENT
        SemesterEntity.Status.ARCHIVED -> Semester.Status.ARCHIVED
    },
    order = order,
    createdAt = createdAt,
    archivedAt = archivedAt,
    hasMissingGrade = hasMissingGrade,
)
