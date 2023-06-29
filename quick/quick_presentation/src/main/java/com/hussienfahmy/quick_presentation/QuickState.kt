package com.hussienfahmy.quick_presentation

import com.hussienFahmy.core.domain.user_data.model.UserData

data class QuickState(
    val isLoading: Boolean = true,
    val cumulativeGPA: Float = 0f,
    val cumulativeGPAPercentage: Float = 0f,
    val academicProgress: UserData.AcademicProgress = UserData.AcademicProgress(
        cumulativeGPA = 0.0,
        creditHours = 0
    ),
    val invalidCumulativeGPAInput: Boolean = false,
    val invalidSemesterGPAInput: Boolean = false,
    val invalidTotalHoursInput: Boolean = false,
    val invalidSemesterHoursInput: Boolean = false,
)