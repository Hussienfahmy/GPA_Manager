package com.hussienfahmy.user_data_domain.repository

import com.hussienfahmy.user_data_domain.model.UserData
import kotlinx.coroutines.flow.Flow

interface UserDataRepository {

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
}