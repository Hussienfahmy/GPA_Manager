package com.hussienFahmy.myGpaManager.navigation

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.hussienFahmy.myGpaManager.navigation.screens.NavGraphs
import com.hussienFahmy.myGpaManager.navigation.screens.destinations.AppGPASettingsScreenDestination
import com.hussienFahmy.myGpaManager.navigation.screens.destinations.AppGradeSettingsScreenDestination
import com.hussienFahmy.myGpaManager.navigation.screens.destinations.AppMoreScreenDestination
import com.hussienFahmy.myGpaManager.navigation.screens.destinations.AppOnBoardingScreenDestination
import com.hussienFahmy.myGpaManager.navigation.screens.destinations.AppOnBoardingUserDataScreenDestination
import com.hussienFahmy.myGpaManager.navigation.screens.destinations.AppSubjectSettingsScreenDestination
import com.hussienFahmy.myGpaManager.navigation.screens.destinations.AppUserDataScreenDestination
import com.hussienFahmy.myGpaManager.navigation.screens.more.AppMoreScreen
import com.hussienFahmy.myGpaManager.navigation.screens.onboarding.AppOnBoardingScreen
import com.hussienFahmy.myGpaManager.navigation.screens.onboarding.AppOnBoardingUserDataScreen
import com.hussienFahmy.myGpaManager.navigation.screens.startAppDestination
import com.hussienfahmy.onboarding_presentation.sign_in.GoogleAuthUiClient
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.manualcomposablecalls.composable
import com.ramcosta.composedestinations.navigation.dependency

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
                        popUpTo(NavGraphs.onBoarding.startAppDestination.route) {
                            inclusive = true
                        }
                    }
                }
            )
        }

        composable(AppOnBoardingUserDataScreenDestination) {
            AppOnBoardingUserDataScreen(
                snackBarHostState = snackBarHostState,
                onStartClick = {
                    navController.navigate(NavGraphs.root.startAppDestination.route) {
                        popUpTo(AppOnBoardingUserDataScreenDestination.route) {
                            inclusive = true
                        }
                        launchSingleTop = true
                    }
                }
            )
        }
    }
}