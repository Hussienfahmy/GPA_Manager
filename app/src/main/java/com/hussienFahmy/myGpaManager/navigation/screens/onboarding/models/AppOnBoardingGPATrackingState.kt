package com.hussienfahmy.myGpaManager.navigation.screens.onboarding.models

data class AppOnBoardingGPATrackingState(
    val showAddSheet: Boolean = false,
    /** Non-null while the user is inside a DETAILED semester's subject list. */
    val viewingSemesterDetailId: Long? = null,
)
