package com.hussienfahmy.myGpaManager.navigation.screens.onboarding.models

sealed class AppOnBoardingGPATrackingEvent {
    object ShowAddSheet : AppOnBoardingGPATrackingEvent()
    object HideAddSheet : AppOnBoardingGPATrackingEvent()

    /** Enter (non-null id) or leave (null) a DETAILED semester's subject list. */
    data class ViewSemesterDetail(val id: Long?) : AppOnBoardingGPATrackingEvent()

    data class DeleteSemesterEvent(val id: Long) : AppOnBoardingGPATrackingEvent()
    data class AddSummarySemester(
        val label: String,
        val gpa: Double,
        val hours: Int,
        val level: Int
    ) : AppOnBoardingGPATrackingEvent()

    data class AddDetailedSemester(val label: String, val level: Int) :
        AppOnBoardingGPATrackingEvent()
}
