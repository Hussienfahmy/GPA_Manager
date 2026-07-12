package com.hussienfahmy.core.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Entity(tableName = "semester")
data class Semester(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val label: String = "",
    val level: Int = 1,
    val type: Type = Type.SUMMARY,
    val semesterGPA: Double = 0.0,
    val totalCreditHours: Int = 0,
    val status: Status = Status.ARCHIVED,
    val order: Int = 0,
    // JVM-only API (java.lang.System) - compiles today because :core only targets androidTarget;
    // needs kotlinx-datetime (or an expect/actual, pending confirmation) before iosMain is added.
    val createdAt: Long = System.currentTimeMillis(),
    val archivedAt: Long? = null,
) {
    // @Serializable on Type only (not the whole Room @Entity) - needed for
    // sync_domain.model.NetworkSemester's Firestore (kotlinx.serialization) round-trip.
    @Serializable
    enum class Type { SUMMARY, DETAILED }
    enum class Status { CURRENT, ARCHIVED }
}
