package com.hussienfahmy.core.data.local.entity

import androidx.annotation.Keep
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "semester")
@Keep
data class Semester(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val label: String = "",
    val level: Int = 1,
    val type: Type = Type.SUMMARY,
    val semesterGPA: Double = 0.0,
    val totalCreditHours: Int = 0,
    val status: Status = Status.ARCHIVED,
    val order: Int = 0,
    val createdAt: Long = System.currentTimeMillis(),
    val archivedAt: Long? = null,
) {
    enum class Type { SUMMARY, DETAILED }
    enum class Status { CURRENT, ARCHIVED }
}
