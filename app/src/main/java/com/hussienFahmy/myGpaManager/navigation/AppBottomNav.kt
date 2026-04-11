package com.hussienfahmy.myGpaManager.navigation

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import com.hussienfahmy.core.domain.analytics.AnalyticsLogger
import com.ramcosta.composedestinations.generated.NavGraphs
import com.ramcosta.composedestinations.generated.destinations.AppSemesterDetailScreenDestination
import com.ramcosta.composedestinations.utils.currentDestinationAsState
import com.ramcosta.composedestinations.utils.startDestination
import org.koin.compose.koinInject

@Composable
fun AppBottomNav(
    navController: NavHostController
) {
    val analyticsLogger = koinInject<AnalyticsLogger>()
    val currentDestination = navController.currentDestinationAsState().value
        ?: NavGraphs.root.startDestination

    // Treat SemesterDetail as a child of the History tab
    val isOnSemesterDetail = currentDestination == AppSemesterDetailScreenDestination
    val effectiveDestination =
        if (isOnSemesterDetail) BottomNavDestination.History.direction else currentDestination

    NavigationBar {
        BottomNavDestination.entries.forEach { destination ->
            NavigationBarItem(
                selected = effectiveDestination == destination.direction,
                onClick = {
                    // Log bottom navigation click
                    analyticsLogger.logBottomNavClicked(destination.name.lowercase())

                    if (isOnSemesterDetail) {
                        // Pop the detail screen (returns to History)
                        navController.popBackStack()
                        // If History was tapped, we're done
                        if (destination == BottomNavDestination.History) return@NavigationBarItem
                        // For other tabs, continue to navigate after popping
                    }

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
                label = { Text(stringResource(id = destination.label)) },
            )
        }
    }
}