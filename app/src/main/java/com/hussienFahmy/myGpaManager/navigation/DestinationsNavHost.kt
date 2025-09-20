package com.hussienfahmy.myGpaManager.navigation

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.hussienfahmy.myGpaManager.navigation.screens.more.AppMoreScreen
import com.hussienfahmy.myGpaManager.navigation.screens.onboarding.AppOnBoardingAcademicStatusScreen
import com.hussienfahmy.myGpaManager.navigation.screens.onboarding.AppOnBoardingGPASubjectsSettings
import com.hussienfahmy.myGpaManager.navigation.screens.onboarding.AppOnBoardingGPATrackingScreen
import com.hussienfahmy.myGpaManager.navigation.screens.onboarding.AppOnBoardingGradesSettingsScreen
import com.hussienfahmy.myGpaManager.navigation.screens.onboarding.AppOnBoardingInstitutionInfoScreen
import com.hussienfahmy.myGpaManager.navigation.screens.onboarding.AppOnBoardingPersonalInfoScreen
import com.hussienfahmy.myGpaManager.navigation.screens.onboarding.AppOnBoardingScreen
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.generated.NavGraphs
import com.ramcosta.composedestinations.generated.destinations.AppGPASettingsScreenDestination
import com.ramcosta.composedestinations.generated.destinations.AppGradeSettingsScreenDestination
import com.ramcosta.composedestinations.generated.destinations.AppMoreScreenDestination
import com.ramcosta.composedestinations.generated.destinations.AppOnBoardingAcademicStatusScreenDestination
import com.ramcosta.composedestinations.generated.destinations.AppOnBoardingGPASubjectsSettingsDestination
import com.ramcosta.composedestinations.generated.destinations.AppOnBoardingGPATrackingScreenDestination
import com.ramcosta.composedestinations.generated.destinations.AppOnBoardingGradesSettingsScreenDestination
import com.ramcosta.composedestinations.generated.destinations.AppOnBoardingInstitutionInfoScreenDestination
import com.ramcosta.composedestinations.generated.destinations.AppOnBoardingPersonalInfoScreenDestination
import com.ramcosta.composedestinations.generated.destinations.AppOnBoardingScreenDestination
import com.ramcosta.composedestinations.generated.destinations.AppSubjectSettingsScreenDestination
import com.ramcosta.composedestinations.generated.destinations.AppUserDataScreenDestination
import com.ramcosta.composedestinations.manualcomposablecalls.composable
import com.ramcosta.composedestinations.navigation.dependency
import com.ramcosta.composedestinations.utils.startDestination

@Composable
fun AppDestinationsNavHost(
    navController: NavHostController,
    snackBarHostState: SnackbarHostState,
) {
    DestinationsNavHost(
        navGraph = NavGraphs.root,
        navController = navController,
        dependenciesContainerBuilder = {
            dependency(snackBarHostState)
        }
    ) {
        composable(AppMoreScreenDestination) {
            AppMoreScreen(
                onUserDataCardClick = {
                    navController.navigate(AppUserDataScreenDestination.route)
                },
                onGPASettingsClick = {
                    navController.navigate(AppGPASettingsScreenDestination.route)
                },
                onGradeSettingsClick = {
                    navController.navigate(AppGradeSettingsScreenDestination.route)
                },
                onSubjectSettingsClick = {
                    navController.navigate(AppSubjectSettingsScreenDestination.route)
                },
            )
        }

        // Step 1: Welcome & Sign In
        composable(AppOnBoardingScreenDestination) {
            AppOnBoardingScreen(
                onSignInSuccess = {
                    navController.navigate(AppOnBoardingPersonalInfoScreenDestination.route) {
                        popUpTo(NavGraphs.onBoarding.startDestination.route) {
                            inclusive = true
                        }
                    }
                }
            )
        }

        // Step 2: Personal Info
        composable(AppOnBoardingPersonalInfoScreenDestination) {
            AppOnBoardingPersonalInfoScreen(
                onNextClick = {
                    navController.navigate(AppOnBoardingInstitutionInfoScreenDestination.route)
                },
                snackBarHostState = snackBarHostState
            )
        }

        // Step 3: Institution Info
        composable(AppOnBoardingInstitutionInfoScreenDestination) {
            AppOnBoardingInstitutionInfoScreen(
                onNextClick = {
                    navController.navigate(AppOnBoardingAcademicStatusScreenDestination.route)
                },
                snackBarHostState = snackBarHostState,
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }

        // Step 4: Academic Status
        composable(AppOnBoardingAcademicStatusScreenDestination) {
            AppOnBoardingAcademicStatusScreen(
                onNextClick = {
                    navController.navigate(AppOnBoardingGPATrackingScreenDestination.route)
                },
                snackBarHostState = snackBarHostState,
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }

        // Step 5: GPA Tracking
        composable(AppOnBoardingGPATrackingScreenDestination) {
            AppOnBoardingGPATrackingScreen(
                onNextClick = {
                    navController.navigate(AppOnBoardingGradesSettingsScreenDestination.route)
                },
                snackBarHostState = snackBarHostState,
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }

        // Step 6: Grade System Settings
        composable(AppOnBoardingGradesSettingsScreenDestination) {
            AppOnBoardingGradesSettingsScreen(
                onNextClick = {
                    navController.navigate(AppOnBoardingGPASubjectsSettingsDestination.route)
                },
                snackBarHostState = snackBarHostState,
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }

        // Step 7: Final Setup
        composable(AppOnBoardingGPASubjectsSettingsDestination) {
            AppOnBoardingGPASubjectsSettings(
                onStartClick = {
                    do {
                        navController.popBackStack()
                    } while (navController.popBackStack())
                    navController.navigate(NavGraphs.root.startDestination.route)
                },
                onBackClick = {
                    navController.popBackStack()
                },
                snackBarHostState = snackBarHostState
            )
        }
    }
}