package com.hussienfahmy.myGpaManager.data.sync.model

import androidx.annotation.Keep
import com.hussienfahmy.core.data.local.entity.Semester
import com.hussienfahmy.sync_domain.model.NetworkSemester
import com.hussienfahmy.sync_domain.model.Subject

@Keep
data class FirebaseNetworkSemester(
    val id: Long = 0,
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
    fun toDomain() = NetworkSemester(
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
}

fun NetworkSemester.toFirebase() = FirebaseNetworkSemester(
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
