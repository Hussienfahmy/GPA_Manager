package com.hussienfahmy.myGpaManager.navigation

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.hussienfahmy.myGpaManager.navigation.screens.more.AppMoreScreen
import com.hussienfahmy.myGpaManager.navigation.screens.onboarding.AppOnBoardingGPASubjectsSettings
import com.hussienfahmy.myGpaManager.navigation.screens.onboarding.AppOnBoardingGradesSettingsScreen
import com.hussienfahmy.myGpaManager.navigation.screens.onboarding.AppOnBoardingScreen
import com.hussienfahmy.myGpaManager.navigation.screens.onboarding.AppOnBoardingUserDataScreen
import com.hussienfahmy.onboarding_presentation.sign_in.GoogleAuthUiClient
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.generated.NavGraphs
import com.ramcosta.composedestinations.generated.destinations.AppGPASettingsScreenDestination
import com.ramcosta.composedestinations.generated.destinations.AppGradeSettingsScreenDestination
import com.ramcosta.composedestinations.generated.destinations.AppMoreScreenDestination
import com.ramcosta.composedestinations.generated.destinations.AppOnBoardingGPASubjectsSettingsDestination
import com.ramcosta.composedestinations.generated.destinations.AppOnBoardingGradesSettingsScreenDestination
import com.ramcosta.composedestinations.generated.destinations.AppOnBoardingScreenDestination
import com.ramcosta.composedestinations.generated.destinations.AppOnBoardingUserDataScreenDestination
import com.ramcosta.composedestinations.generated.destinations.AppSubjectSettingsScreenDestination
import com.ramcosta.composedestinations.generated.destinations.AppUserDataScreenDestination
import com.ramcosta.composedestinations.manualcomposablecalls.composable
import com.ramcosta.composedestinations.navigation.dependency
import com.ramcosta.composedestinations.utils.startDestination

@Composable
fun AppDestinationsNavHost(
    navController: NavHostController,
    snackBarHostState: SnackbarHostState,
    googleAuthUiClient: GoogleAuthUiClient,
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

        composable(AppOnBoardingScreenDestination) {
            AppOnBoardingScreen(
                googleAuthUiClient = googleAuthUiClient,
                onSignInSuccess = {
                    navController.navigate(AppOnBoardingUserDataScreenDestination.route) {
                        popUpTo(NavGraphs.onBoarding.startDestination.route) {
                            inclusive = true
                        }
                    }
                }
            )
        }

        composable(AppOnBoardingUserDataScreenDestination) {
            AppOnBoardingUserDataScreen(
                snackBarHostState = snackBarHostState,
                onNextClick = {
                    navController.navigate(AppOnBoardingGradesSettingsScreenDestination.route)
                }
            )
        }

        composable(AppOnBoardingGradesSettingsScreenDestination) {
            AppOnBoardingGradesSettingsScreen(
                onNextClick = {
                    navController.navigate(AppOnBoardingGPASubjectsSettingsDestination.route)
                },
                snackBarHostState = snackBarHostState
            )
        }

        composable(AppOnBoardingGPASubjectsSettingsDestination) {
            AppOnBoardingGPASubjectsSettings(
                onStartClick = {
                    navController.navigate(NavGraphs.root.startDestination.route) {
                        popUpTo(AppOnBoardingUserDataScreenDestination.route) {
                            inclusive = true
                        }
                        launchSingleTop = true
                    }
                },
                snackBarHostState = snackBarHostState
            )
        }
    }
}