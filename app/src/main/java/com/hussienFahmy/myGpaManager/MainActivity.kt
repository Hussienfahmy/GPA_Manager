package com.hussienfahmy.myGpaManager

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.hussienfahmy.core.util.PermissionHelper
import com.hussienfahmy.core_ui.LocalSpacing
import com.hussienfahmy.core_ui.presentation.components.OnboardingConstants
import com.hussienfahmy.core_ui.presentation.components.OnboardingProgressIndicator
import com.hussienfahmy.myGpaManager.navigation.AppBottomNav
import com.hussienfahmy.myGpaManager.navigation.AppDestinationsNavHost
import com.hussienfahmy.myGpaManager.ui.theme.GPAManagerTheme
import com.ramcosta.composedestinations.generated.NavGraphs
import com.ramcosta.composedestinations.generated.destinations.AppOnBoardingAcademicStatusScreenDestination
import com.ramcosta.composedestinations.generated.destinations.AppOnBoardingGPASubjectsSettingsDestination
import com.ramcosta.composedestinations.generated.destinations.AppOnBoardingGPATrackingScreenDestination
import com.ramcosta.composedestinations.generated.destinations.AppOnBoardingGradesSettingsScreenDestination
import com.ramcosta.composedestinations.generated.destinations.AppOnBoardingInstitutionInfoScreenDestination
import com.ramcosta.composedestinations.generated.destinations.AppOnBoardingPersonalInfoScreenDestination
import com.ramcosta.composedestinations.generated.destinations.AppOnBoardingScreenDestination
import com.ramcosta.composedestinations.utils.startDestination
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : ComponentActivity() {

    private val viewModel by viewModel<MainViewModel>()

    private fun getOnboardingStep(destination: String?): Int = when (destination) {
        AppOnBoardingScreenDestination.route -> OnboardingConstants.Steps.WELCOME
        AppOnBoardingPersonalInfoScreenDestination.route -> OnboardingConstants.Steps.PERSONAL_INFO
        AppOnBoardingInstitutionInfoScreenDestination.route -> OnboardingConstants.Steps.INSTITUTION_INFO
        AppOnBoardingAcademicStatusScreenDestination.route -> OnboardingConstants.Steps.ACADEMIC_STATUS
        AppOnBoardingGPATrackingScreenDestination.route -> OnboardingConstants.Steps.GPA_TRACKING
        AppOnBoardingGradesSettingsScreenDestination.route -> OnboardingConstants.Steps.GRADES_SETTINGS
        AppOnBoardingGPASubjectsSettingsDestination.route -> OnboardingConstants.Steps.FINAL_SETUP
        else -> OnboardingConstants.Steps.WELCOME
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Request notification permission
        if (!PermissionHelper.hasNotificationPermission(this)) {
            PermissionHelper.requestNotificationPermission(this) { granted ->
                /* no-op */
            }
        }

        setContent {
            GPAManagerTheme {
                val spacing = LocalSpacing.current
                val localFocusManager = LocalFocusManager.current
                val snackBarHostState = remember { SnackbarHostState() }
                val navController = rememberNavController()
                val currentBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = currentBackStackEntry?.destination?.route
                val isSingedIn by viewModel.isSignedIn.collectAsState()
                val onboardingRoutes =
                    remember { NavGraphs.onBoarding.destinations.map { it.route } }

                LaunchedEffect(key1 = isSingedIn) {
                    if (isSingedIn == false) {
                        navController.navigate(NavGraphs.onBoarding.startDestination.route) {
                            popUpTo(NavGraphs.root.startDestination.route) {
                                inclusive = true
                            }
                        }
                    }
                }

                Scaffold(
                    modifier = Modifier
                        .fillMaxSize()
                        .pointerInput(Unit) {
                            detectTapGestures(onTap = {
                                localFocusManager.clearFocus()
                            })
                        },
                    snackbarHost = { SnackbarHost(hostState = snackBarHostState) },
                    bottomBar = {
                        if (currentDestination !in onboardingRoutes) {
                            AppBottomNav(navController = navController)
                        }
                    }
                ) { paddingValues ->
                    Column(modifier = Modifier.padding(paddingValues)) {
                        val isOnboardingScreen = currentDestination in onboardingRoutes
                        val currentStep = getOnboardingStep(currentDestination)

                        if (isOnboardingScreen && currentDestination != AppOnBoardingScreenDestination.route) {
                            // Show progress indicator outside animated content for steps 2-7
                            OnboardingProgressIndicator(
                                currentStep = currentStep,
                                totalSteps = OnboardingConstants.TOTAL_STEPS,
                                modifier = Modifier.padding(spacing.medium)
                            )
                        }

                        AppDestinationsNavHost(
                            navController = navController,
                            snackBarHostState = snackBarHostState,
                        )
                    }
                }
            }
        }
    }
}