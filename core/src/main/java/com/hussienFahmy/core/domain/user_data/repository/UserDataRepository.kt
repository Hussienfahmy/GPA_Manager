package com.hussienFahmy.core.domain.user_data.repository

import com.hussienFahmy.core.domain.user_data.model.UserData
import kotlinx.coroutines.flow.Flow

interface UserDataRepository {

    suspend fun isUserExists(): Boolean

    suspend fun createUserData(
        id: String,
        name: String,
        photoUrl: String,
        email: String,
        isEmailVerified: Boolean
    )

    fun observeUserData(): Flow<UserData>

    suspend fun getUserData(): UserData

    suspend fun updateName(name: String)

    suspend fun updatePhotoUrl(photoUrl: String)

    suspend fun updateEmail(email: String)

    suspend fun updateUniversity(university: String)

    suspend fun updateFaculty(faculty: String)

    suspend fun updateDepartment(department: String)

    suspend fun updateLevel(level: Int)

    suspend fun updateSemester(semester: UserData.AcademicInfo.Semester)

    suspend fun updateCumulativeGPA(cumulativeGPA: Double)

    suspend fun updateCreditHours(creditHours: Int)

    suspend fun updateFCMToken(fcmToken: String)
}