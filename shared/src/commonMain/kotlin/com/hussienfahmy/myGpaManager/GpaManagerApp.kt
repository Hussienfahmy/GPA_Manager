package com.hussienfahmy.myGpaManager

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.rememberNavBackStack
import com.hussienfahmy.core_ui.LocalSpacing
import com.hussienfahmy.core_ui.presentation.components.OnboardingConstants
import com.hussienfahmy.core_ui.presentation.components.OnboardingProgressIndicator
import com.hussienfahmy.myGpaManager.navigation.AppBottomNav
import com.hussienfahmy.myGpaManager.navigation.AppNavHost
import com.hussienfahmy.myGpaManager.navigation.AppRoute
import com.hussienfahmy.myGpaManager.navigation.OnboardingNavHost
import com.hussienfahmy.myGpaManager.navigation.OnboardingRoute
import com.hussienfahmy.myGpaManager.navigation.rememberAppNavigationState
import com.hussienfahmy.myGpaManager.ui.theme.GPAManagerTheme
import com.mohamedrejeb.calf.permissions.ExperimentalPermissionsApi
import com.mohamedrejeb.calf.permissions.Notification
import com.mohamedrejeb.calf.permissions.Permission
import com.mohamedrejeb.calf.permissions.PermissionStatus
import com.mohamedrejeb.calf.permissions.rememberPermissionState
import org.koin.compose.viewmodel.koinViewModel

private fun getOnboardingStep(route: NavKey?): Int = when (route) {
    OnboardingRoute.Welcome -> OnboardingConstants.Steps.WELCOME
    OnboardingRoute.PersonalInfo -> OnboardingConstants.Steps.PERSONAL_INFO
    OnboardingRoute.InstitutionInfo -> OnboardingConstants.Steps.INSTITUTION_INFO
    OnboardingRoute.AcademicStatus -> OnboardingConstants.Steps.ACADEMIC_STATUS
    OnboardingRoute.GPATracking,
    is OnboardingRoute.SemesterDetail -> OnboardingConstants.Steps.GPA_TRACKING
    OnboardingRoute.GradesSettings -> OnboardingConstants.Steps.GRADES_SETTINGS
    OnboardingRoute.GPASubjectsSettings -> OnboardingConstants.Steps.FINAL_SETUP
    else -> OnboardingConstants.Steps.WELCOME
}

/**
 * The whole app's composition root - both :app's MainActivity.kt (Android) and the iOS entry
 * point's MainViewController.kt call this directly with no arguments.
 */
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun GpaManagerApp() {
    val viewModel: MainViewModel = koinViewModel()

    GPAManagerTheme {
        val spacing = LocalSpacing.current
        val localFocusManager = LocalFocusManager.current
        val snackBarHostState = remember { SnackbarHostState() }
        val isSingedIn by viewModel.isSignedIn.collectAsState()
        val notificationPermissionState = rememberPermissionState(Permission.Notification)

        val appNavigationState = rememberAppNavigationState()
        val onboardingBackStack = rememberNavBackStack(OnboardingRoute.Welcome)
        // Deliberately separate from isSingedIn: sign-in succeeds at onboarding's very first
        // step (Welcome), but the user still has several data-entry steps left before tapping
        // "Start" - isSingedIn flips true mid-onboarding while the flow should keep showing.
        var showOnboarding by remember { mutableStateOf(false) }

        LaunchedEffect(key1 = isSingedIn) {
            if (isSingedIn == false) {
                showOnboarding = true
                // Reset onboarding to its start whenever the user signs out.
                while (onboardingBackStack.size > 1) {
                    onboardingBackStack.removeLastOrNull()
                }
            }
        }

        LaunchedEffect(appNavigationState.topLevelRoute, isSingedIn) {
            if (isSingedIn != true) return@LaunchedEffect

            if (appNavigationState.topLevelRoute == AppRoute.Semester &&
                notificationPermissionState.status !is PermissionStatus.Granted
            ) {
                // request notification permission if not granted after user completes sign in.
                notificationPermissionState.launchPermissionRequest()
            }
        }

        if (showOnboarding) {
            val currentOnboardingRoute = onboardingBackStack.lastOrNull()

            Scaffold(
                modifier = Modifier
                    .fillMaxSize()
                    .pointerInput(Unit) {
                        detectTapGestures(onTap = { localFocusManager.clearFocus() })
                    },
                snackbarHost = { SnackbarHost(hostState = snackBarHostState) },
            ) { paddingValues ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                ) {
                    if (currentOnboardingRoute != OnboardingRoute.Welcome &&
                        currentOnboardingRoute !is OnboardingRoute.SemesterDetail
                    ) {
                        // Show progress indicator outside animated content for steps 2-7
                        OnboardingProgressIndicator(
                            currentStep = getOnboardingStep(currentOnboardingRoute),
                            totalSteps = OnboardingConstants.TOTAL_STEPS,
                            modifier = Modifier.padding(spacing.medium)
                        )
                    }

                    OnboardingNavHost(
                        backStack = onboardingBackStack,
                        snackBarHostState = snackBarHostState,
                        onSignInSuccess = {
                            onboardingBackStack.add(OnboardingRoute.PersonalInfo)
                        },
                        onOnboardingComplete = {
                            while (onboardingBackStack.size > 1) {
                                onboardingBackStack.removeLastOrNull()
                            }
                            showOnboarding = false
                        },
                    )
                }
            }
        } else {
            Scaffold(
                modifier = Modifier
                    .fillMaxSize()
                    .pointerInput(Unit) {
                        detectTapGestures(onTap = { localFocusManager.clearFocus() })
                    },
                snackbarHost = { SnackbarHost(hostState = snackBarHostState) },
                bottomBar = { AppBottomNav(appNavigationState = appNavigationState) }
            ) { paddingValues ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                ) {
                    AppNavHost(
                        appNavigationState = appNavigationState,
                        snackBarHostState = snackBarHostState,
                    )
                }
            }
        }
    }
}
