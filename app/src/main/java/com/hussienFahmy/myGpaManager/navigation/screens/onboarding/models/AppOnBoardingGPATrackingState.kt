package com.hussienfahmy.myGpaManager.navigation.screens.onboarding.models

import com.hussienfahmy.core.domain.subject_settings.model.SubjectSettings

data class AppOnBoardingGPATrackingState(
    val showAddSheet: Boolean = false,
    val addingSubjectsToSemesterId: Long? = null,
    val subjectSettings: SubjectSettings? = null,
)
