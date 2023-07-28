package com.hussienFahmy.core.domain.user_data.model

data class UserData(
    val id: String,
    val name: String,
    val photoUrl: String,
    val email: String,
    val isEmailVerified: Boolean,
    val academicInfo: AcademicInfo,
    val academicProgress: AcademicProgress,
) {
    data class AcademicInfo(
        val university: String,
        val faculty: String,
        val department: String,
        val level: Int = 1,
        val semester: Semester
    ) {
        enum class Semester {
            First, Second
        }
    }

    data class AcademicProgress(
        val cumulativeGPA: Double,
        val creditHours: Int
    )
}