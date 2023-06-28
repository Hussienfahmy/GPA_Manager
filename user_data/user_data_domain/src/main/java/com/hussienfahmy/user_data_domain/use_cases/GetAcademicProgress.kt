package com.hussienfahmy.user_data_domain.use_cases

class GetAcademicProgress(
    private val getUserData: GetUserData
) {
    suspend operator fun invoke() = getUserData().academicProgress
}