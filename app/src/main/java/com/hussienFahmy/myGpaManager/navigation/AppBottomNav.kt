package com.hussienFahmy.myGpaManager.navigation

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import com.hussienFahmy.myGpaManager.navigation.screens.NavGraphs
import com.hussienFahmy.myGpaManager.navigation.screens.startAppDestination
import com.ramcosta.composedestinations.utils.currentDestinationAsState

@Composable
fun AppBottomNav(
    navController: NavHostController
) {
    val currentDestination = navController.currentDestinationAsState().value
        ?: NavGraphs.root.startAppDestination

    NavigationBar {
        BottomNavDestination.values().forEach { destination ->
            NavigationBarItem(
                selected = currentDestination == destination.direction,
                onClick = {
                    navController.navigate(destination.direction.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = {
                    Icon(
                        destination.icon,
                        contentDescription = stringResource(id = destination.label)
                    )
                },
                label = { stringResource(id = destination.label) },
            )
        }
    }
}