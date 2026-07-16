package com.hussienfahmy.core.domain.user_data.use_cases

import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first

class GetAcademicProgress(
    private val getUserData: GetUserData
) {
    suspend operator fun invoke() = getUserData().filterNotNull().first().academicProgress
}