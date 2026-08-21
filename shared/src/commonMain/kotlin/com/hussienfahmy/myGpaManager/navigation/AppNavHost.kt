package com.hussienfahmy.myGpaManager.navigation

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import com.hussienfahmy.myGpaManager.navigation.screens.AppGPASettingsScreen
import com.hussienfahmy.myGpaManager.navigation.screens.AppGradeSettingsScreen
import com.hussienfahmy.myGpaManager.navigation.screens.AppQuickScreen
import com.hussienfahmy.myGpaManager.navigation.screens.AppSemesterDetailScreen
import com.hussienfahmy.myGpaManager.navigation.screens.AppSemesterHistoryScreen
import com.hussienfahmy.myGpaManager.navigation.screens.AppSemesterMarksScreen
import com.hussienfahmy.myGpaManager.navigation.screens.AppSemesterScreen
import com.hussienfahmy.myGpaManager.navigation.screens.AppSubjectSettingsScreen
import com.hussienfahmy.myGpaManager.navigation.screens.AppUserDataScreen
import com.hussienfahmy.myGpaManager.navigation.screens.more.AppMoreScreen

@Composable
fun AppNavHost(
    appNavigationState: AppNavigationState,
    snackBarHostState: SnackbarHostState,
) {
    NavDisplay(
        entries = appNavigationState.entries(
            entryProvider = entryProvider {
                entry<AppRoute.Semester>(metadata = fadeTransitionMetadata) {
                    AppSemesterScreen(snackBarHostState = snackBarHostState)
                }

                entry<AppRoute.SemesterMarks>(metadata = fadeTransitionMetadata) {
                    AppSemesterMarksScreen()
                }

                entry<AppRoute.SemesterHistory>(metadata = fadeTransitionMetadata) {
                    AppSemesterHistoryScreen(
                        snackBarHostState = snackBarHostState,
                        onSemesterClick = { semesterId, label ->
                            appNavigationState.navigate(AppRoute.SemesterDetail(semesterId, label))
                        },
                    )
                }

                entry<AppRoute.SemesterDetail>(metadata = slideTransitionMetadata) { route ->
                    AppSemesterDetailScreen(semesterId = route.semesterId)
                }

                entry<AppRoute.Quick>(metadata = fadeTransitionMetadata) {
                    AppQuickScreen(snackBarHostState = snackBarHostState)
                }

                entry<AppRoute.More>(metadata = fadeTransitionMetadata) {
                    AppMoreScreen(
                        onUserDataCardClick = { appNavigationState.navigate(AppRoute.UserData) },
                        onGPASettingsClick = { appNavigationState.navigate(AppRoute.GPASettings) },
                        onGradeSettingsClick = { appNavigationState.navigate(AppRoute.GradeSettings) },
                        onSubjectSettingsClick = { appNavigationState.navigate(AppRoute.SubjectSettings) },
                    )
                }

                entry<AppRoute.UserData>(metadata = fadeTransitionMetadata) {
                    AppUserDataScreen(snackBarHostState = snackBarHostState)
                }

                entry<AppRoute.GPASettings>(metadata = fadeTransitionMetadata) {
                    AppGPASettingsScreen()
                }

                entry<AppRoute.GradeSettings>(metadata = fadeTransitionMetadata) {
                    AppGradeSettingsScreen(snackBarHostState = snackBarHostState)
                }

                entry<AppRoute.SubjectSettings>(metadata = fadeTransitionMetadata) {
                    AppSubjectSettingsScreen(snackBarHostState = snackBarHostState)
                }
            }
        ),
        onBack = { appNavigationState.goBack() },
    )
}
