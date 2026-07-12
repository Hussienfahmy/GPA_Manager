package com.hussienfahmy.core_ui.presentation.user_data

import com.hussienfahmy.core.domain.user_data.model.UserData
import com.hussienfahmy.core.util.PlatformImageSource

sealed class UserDataEvent {
    data class UpdateName(val name: String) : UserDataEvent()
    data class UploadPhoto(val photoUri: PlatformImageSource) : UserDataEvent()
    data class UpdateUniversity(val university: String) : UserDataEvent()
    data class UpdateFaculty(val faculty: String) : UserDataEvent()
    data class UpdateDepartment(val department: String) : UserDataEvent()
    data class UpdateLevel(val level: String) : UserDataEvent()
    data class UpdateSemester(val semester: UserData.AcademicInfo.Semester) : UserDataEvent()
    data class UpdateCumulativeGPA(val cumulativeGPA: String) : UserDataEvent()
    data class UpdateCreditHours(val creditHours: String) : UserDataEvent()
}