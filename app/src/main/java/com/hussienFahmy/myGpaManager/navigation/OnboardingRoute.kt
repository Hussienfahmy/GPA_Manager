package com.hussienfahmy.myGpaManager.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

/**
 * Onboarding is a separate, linear, non-tabbed flow shown before sign-in - it does not
 * participate in AppRoute's bottom-nav multi-back-stack machinery at all.
 */
@Serializable
sealed interface OnboardingRoute : NavKey {

    @Serializable
    data object Welcome : OnboardingRoute

    @Serializable
    data object PersonalInfo : OnboardingRoute

    @Serializable
    data object InstitutionInfo : OnboardingRoute

    @Serializable
    data object AcademicStatus : OnboardingRoute

    @Serializable
    data object GPATracking : OnboardingRoute

    @Serializable
    data class SemesterDetail(val semesterId: Long, val semesterLabel: String) : OnboardingRoute

    @Serializable
    data object GradesSettings : OnboardingRoute

    @Serializable
    data object GPASubjectsSettings : OnboardingRoute
}
