package com.hussienfahmy.user_data_presentaion

import android.net.Uri
import com.hussienfahmy.user_data_domain.model.UserData

sealed class UserDataEvent {
    data class UpdateName(val name: String) : UserDataEvent()
    data class UpdateEmail(val email: String) : UserDataEvent()
    data class UploadPhoto(val photoUri: Uri) : UserDataEvent()
    data class UpdateUniversity(val university: String) : UserDataEvent()
    data class UpdateFaculty(val faculty: String) : UserDataEvent()
    data class UpdateDepartment(val department: String) : UserDataEvent()
    data class UpdateLevel(val level: String) : UserDataEvent()
    data class UpdateSemester(val semester: UserData.AcademicInfo.Semester) : UserDataEvent()
    data class UpdateCumulativeGPA(val cumulativeGPA: String) : UserDataEvent()
    data class UpdateCreditHours(val creditHours: String) : UserDataEvent()
}