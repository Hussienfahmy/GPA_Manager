package com.hussienfahmy.user_data_data.local

import android.content.Context
import androidx.datastore.dataStore
import com.hussienfahmy.user_data_data.local.model.UserData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

internal class UserDataStore(
    context: Context
) {
    private val Context.dataStore by dataStore("calculation_settings", UserDataSerializer)

    private val datastore = context.dataStore

    fun observeUserData(): Flow<UserData> {
        return datastore.data
    }

    suspend fun getUserData(): UserData {
        return datastore.data.first()
    }

    suspend fun updateName(name: String) {
        datastore.updateData {
            it.copy(name = name)
        }
    }

    suspend fun updatePhotoUrl(photoUrl: String) {
        datastore.updateData {
            it.copy(photoUrl = photoUrl)
        }
    }

    suspend fun updateEmail(email: String) {
        datastore.updateData {
            it.copy(email = email)
        }
    }

    suspend fun updateUniversity(university: String) {
        datastore.updateData {
            it.copy(academicInfo = it.academicInfo.copy(university = university))
        }
    }

    suspend fun updateFaculty(faculty: String) {
        datastore.updateData {
            it.copy(academicInfo = it.academicInfo.copy(faculty = faculty))
        }
    }

    suspend fun updateDepartment(department: String) {
        datastore.updateData {
            it.copy(academicInfo = it.academicInfo.copy(department = department))
        }
    }

    suspend fun updateLevel(level: Int) {
        datastore.updateData {
            it.copy(academicInfo = it.academicInfo.copy(level = level))
        }
    }

    suspend fun updateSemester(semester: UserData.AcademicInfo.Semester) {
        datastore.updateData {
            it.copy(academicInfo = it.academicInfo.copy(semester = semester))
        }
    }

    suspend fun updateCumulativeGPA(cumulativeGPA: Double) {
        datastore.updateData {
            it.copy(academicProgress = it.academicProgress.copy(cumulativeGPA = cumulativeGPA))
        }
    }

    suspend fun updateCreditHours(creditHours: Int) {
        datastore.updateData {
            it.copy(academicProgress = it.academicProgress.copy(creditHours = creditHours))
        }
    }
}