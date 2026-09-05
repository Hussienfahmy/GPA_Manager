package com.hussienfahmy.myGpaManager.navigation

import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.hussienfahmy.core_ui.LocalSpacing
import com.hussienfahmy.myGpaManager.navigation.screens.onboarding.AppOnBoardingAcademicStatusScreen
import com.hussienfahmy.myGpaManager.navigation.screens.onboarding.AppOnBoardingGPASubjectsSettings
import com.hussienfahmy.myGpaManager.navigation.screens.onboarding.AppOnBoardingGPATrackingScreen
import com.hussienfahmy.myGpaManager.navigation.screens.onboarding.AppOnBoardingGradesSettingsScreen
import com.hussienfahmy.myGpaManager.navigation.screens.onboarding.AppOnBoardingInstitutionInfoScreen
import com.hussienfahmy.myGpaManager.navigation.screens.onboarding.AppOnBoardingPersonalInfoScreen
import com.hussienfahmy.myGpaManager.navigation.screens.onboarding.AppOnBoardingScreen
import com.hussienfahmy.myGpaManager.navigation.screens.onboarding.AppOnBoardingSemesterDetailScreen

/**
 * Onboarding is a separate, linear, non-tabbed flow shown before sign-in - unlike AppNavHost's
 * per-tab multi-back-stack setup, this is a single flat NavBackStack.
 */
@Composable
fun OnboardingNavHost(
    backStack: NavBackStack<NavKey>,
    snackBarHostState: SnackbarHostState,
    onSignInSuccess: () -> Unit,
    onOnboardingComplete: () -> Unit,
) {
    val spacing = LocalSpacing.current

    NavDisplay(
        modifier = Modifier.navigationBarsPadding()
            .padding(horizontal = spacing.medium),
        backStack = backStack,
        onBack = { backStack.removeLastOrNull() },
        entryDecorators = listOf(
            rememberSaveableStateHolderNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator(),
        ),
        entryProvider = entryProvider {
            entry<OnboardingRoute.Welcome>(metadata = welcomeSlideTransitionMetadata) {
                AppOnBoardingScreen(
                    onSignInSuccess = onSignInSuccess,
                    // Guest chose "fill with sample data" - the app is already usable, skip the
                    // remaining setup steps.
                    onGuestReady = onOnboardingComplete,
                )
            }

            entry<OnboardingRoute.PersonalInfo>(metadata = slideTransitionMetadata) {
                AppOnBoardingPersonalInfoScreen(
                    onNextClick = { backStack.add(OnboardingRoute.InstitutionInfo) },
                    snackBarHostState = snackBarHostState,
                )
            }

            entry<OnboardingRoute.InstitutionInfo>(metadata = slideTransitionMetadata) {
                AppOnBoardingInstitutionInfoScreen(
                    onNextClick = { backStack.add(OnboardingRoute.AcademicStatus) },
                    snackBarHostState = snackBarHostState,
                    onBackClick = { backStack.removeLastOrNull() },
                )
            }

            entry<OnboardingRoute.AcademicStatus>(metadata = slideTransitionMetadata) {
                AppOnBoardingAcademicStatusScreen(
                    onNextClick = { backStack.add(OnboardingRoute.GPATracking) },
                    snackBarHostState = snackBarHostState,
                    onBackClick = { backStack.removeLastOrNull() },
                )
            }

            entry<OnboardingRoute.GPATracking>(metadata = slideTransitionMetadata) {
                AppOnBoardingGPATrackingScreen(
                    onNextClick = { backStack.add(OnboardingRoute.GradesSettings) },
                    snackBarHostState = snackBarHostState,
                    onBackClick = { backStack.removeLastOrNull() },
                    onOpenSemesterDetail = { semesterId, semesterLabel ->
                        backStack.add(
                            OnboardingRoute.SemesterDetail(
                                semesterId = semesterId,
                                semesterLabel = semesterLabel,
                            )
                        )
                    },
                )
            }

            entry<OnboardingRoute.SemesterDetail>(metadata = slideTransitionMetadata) { route ->
                AppOnBoardingSemesterDetailScreen(
                    semesterId = route.semesterId,
                    semesterLabel = route.semesterLabel,
                    onBackClick = { backStack.removeLastOrNull() },
                )
            }

            entry<OnboardingRoute.GradesSettings>(metadata = slideTransitionMetadata) {
                AppOnBoardingGradesSettingsScreen(
                    onNextClick = { backStack.add(OnboardingRoute.GPASubjectsSettings) },
                    snackBarHostState = snackBarHostState,
                    onBackClick = { backStack.removeLastOrNull() },
                )
            }

            entry<OnboardingRoute.GPASubjectsSettings>(metadata = slideTransitionMetadata) {
                AppOnBoardingGPASubjectsSettings(
                    onStartClick = onOnboardingComplete,
                    onBackClick = { backStack.removeLastOrNull() },
                    snackBarHostState = snackBarHostState,
                )
            }
        },
    )
}
