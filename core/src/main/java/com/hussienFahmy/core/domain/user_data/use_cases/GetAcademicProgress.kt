package com.hussienFahmy.core.domain.user_data.use_cases

class GetAcademicProgress(
    private val getUserData: GetUserData
) {
    suspend operator fun invoke() = getUserData().academicProgress
}