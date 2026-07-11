package com.hussienfahmy.myGpaManager.navigation.screens.onboarding.models

sealed class AppOnBoardingGPATrackingEvent {
    object ShowAddSheet : AppOnBoardingGPATrackingEvent()
    object HideAddSheet : AppOnBoardingGPATrackingEvent()
    data class DeleteSemesterEvent(val id: Long) : AppOnBoardingGPATrackingEvent()
    data class AddSummarySemester(
        val label: String,
        val gpa: Double,
        val hours: Int,
        val level: Int
    ) : AppOnBoardingGPATrackingEvent()
}
