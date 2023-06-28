package com.hussienfahmy.user_data_data.local.model

import kotlinx.serialization.Serializable

@Serializable
internal data class UserData(
    val name: String = "User",
    val photoUrl: String = "",
    val email: String = "email@user.com",
    val isEmailVerified: Boolean = false,
    val academicInfo: AcademicInfo = AcademicInfo(),
    val academicProgress: AcademicProgress = AcademicProgress(),
) {
    @Serializable
    data class AcademicInfo(
        val university: String = "University",
        val faculty: String = "Faculty",
        val department: String = "Department",
        val level: Int = 1,
        val semester: Semester = Semester.First
    ) {
        enum class Semester {
            First, Second
        }
    }

    @Serializable
    data class AcademicProgress(
        val cumulativeGPA: Double = 0.0,
        val creditHours: Int = 0
    )
}