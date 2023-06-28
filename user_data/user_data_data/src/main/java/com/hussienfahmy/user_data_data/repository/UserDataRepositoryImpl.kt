package com.hussienfahmy.user_data_data.repository

import com.hussienfahmy.user_data_data.local.UserDataStore
import com.hussienfahmy.user_data_data.mapper.toUserData
import com.hussienfahmy.user_data_domain.model.UserData
import com.hussienfahmy.user_data_domain.repository.UserDataRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import com.hussienfahmy.user_data_data.local.model.UserData as UserDataLocal

internal class UserDataRepositoryImpl(
    private val userDataStore: UserDataStore,
) : UserDataRepository {
    override fun observeUserData(): Flow<UserData> {
        return userDataStore.observeUserData().map { it.toUserData() }
    }

    override suspend fun getUserData(): UserData {
        return userDataStore.getUserData().toUserData()
    }

    override suspend fun updateName(name: String) {
        userDataStore.updateName(name)
    }

    override suspend fun updatePhotoUrl(photoUrl: String) {
        userDataStore.updatePhotoUrl(photoUrl)
    }

    override suspend fun updateEmail(email: String) {
        userDataStore.updateEmail(email)
    }

    override suspend fun updateUniversity(university: String) {
        userDataStore.updateUniversity(university)
    }

    override suspend fun updateFaculty(faculty: String) {
        userDataStore.updateFaculty(faculty)
    }

    override suspend fun updateDepartment(department: String) {
        userDataStore.updateDepartment(department)
    }

    override suspend fun updateLevel(level: Int) {
        userDataStore.updateLevel(level)
    }

    override suspend fun updateSemester(semester: UserData.AcademicInfo.Semester) {
        userDataStore.updateSemester(
            when (semester) {
                UserData.AcademicInfo.Semester.First -> UserDataLocal.AcademicInfo.Semester.First
                UserData.AcademicInfo.Semester.Second -> UserDataLocal.AcademicInfo.Semester.Second
            }
        )
    }

    override suspend fun updateCumulativeGPA(cumulativeGPA: Double) {
        userDataStore.updateCumulativeGPA(cumulativeGPA)
    }

    override suspend fun updateCreditHours(creditHours: Int) {
        userDataStore.updateCreditHours(creditHours)
    }
}