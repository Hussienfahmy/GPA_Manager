package com.hussienFahmy.myGpaManager.navigation

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.hussienFahmy.myGpaManager.navigation.screens.NavGraphs
import com.hussienFahmy.myGpaManager.navigation.screens.destinations.AppGPASettingsScreenDestination
import com.hussienFahmy.myGpaManager.navigation.screens.destinations.AppGradeSettingsScreenDestination
import com.hussienFahmy.myGpaManager.navigation.screens.destinations.AppMoreScreenDestination
import com.hussienFahmy.myGpaManager.navigation.screens.destinations.AppSubjectSettingsScreenDestination
import com.hussienFahmy.myGpaManager.navigation.screens.destinations.AppUserDataScreenDestination
import com.hussienFahmy.myGpaManager.navigation.screens.more.AppMoreScreen
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.manualcomposablecalls.composable
import com.ramcosta.composedestinations.navigation.dependency

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
    }
}