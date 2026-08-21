package com.hussienfahmy.myGpaManager.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

/**
 * Bottom-nav top-level routes and the screens reachable from them. Navigation 3's back stack is
 * a plain user-owned list rather than a declarative graph, so unlike the old Compose
 * Destinations RootGraph/MoreNavGraph split, all of these live in one flat hierarchy - which
 * per-tab stack a route belongs to is decided at runtime by AppNavigationState/Navigator, not
 * by the route's type.
 */
@Serializable
sealed interface AppRoute : NavKey {

    @Serializable
    data object Semester : AppRoute

    @Serializable
    data object SemesterMarks : AppRoute

    @Serializable
    data object SemesterHistory : AppRoute

    @Serializable
    data class SemesterDetail(val semesterId: Long, val semesterLabel: String) : AppRoute

    @Serializable
    data object Quick : AppRoute

    @Serializable
    data object More : AppRoute

    @Serializable
    data object UserData : AppRoute

    @Serializable
    data object GPASettings : AppRoute

    @Serializable
    data object GradeSettings : AppRoute

    @Serializable
    data object SubjectSettings : AppRoute
}
