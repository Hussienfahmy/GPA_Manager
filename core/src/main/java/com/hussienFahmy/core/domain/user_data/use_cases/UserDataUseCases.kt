package com.hussienFahmy.core.domain.user_data.use_cases

data class UserDataUseCases(
    val observeUserData: ObserveUserData,
    val getUserData: GetUserData,
    val getAcademicProgress: GetAcademicProgress,
    val updateName: UpdateName,
    val uploadPhoto: UploadPhoto,
    val updateEmail: UpdateEmail,
    val updateUniversity: UpdateUniversity,
    val updateFaculty: UpdateFaculty,
    val updateDepartment: UpdateDepartment,
    val updateLevel: UpdateLevel,
    val updateSemester: UpdateSemester,
    val updateCumulativeGPA: UpdateCumulativeGPA,
    val updateCreditHours: UpdateCreditHours
)