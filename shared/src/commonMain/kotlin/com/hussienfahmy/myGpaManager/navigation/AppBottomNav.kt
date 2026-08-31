package com.hussienfahmy.myGpaManager.navigation

import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteType
import androidx.compose.runtime.Composable
import com.hussienfahmy.core.domain.analytics.AnalyticsLogger
import com.hussienfahmy.core_ui.theme.MeadowColors
import com.hussienfahmy.core_ui.theme.MeadowTheme
import org.koin.compose.koinInject

@Composable
fun AppBottomNav(
    appNavigationState: AppNavigationState,
    navigationSuiteType: NavigationSuiteType,
) {
    val analyticsLogger = koinInject<AnalyticsLogger>()
    val currentTopLevelRoute = appNavigationState.topLevelRoute

    val colors = MeadowTheme.colors

    PlatformNavContent(
        selectedRoute = currentTopLevelRoute,
        colors = colors,
        navigationSuiteType = navigationSuiteType,
        onSelect = { destination ->
            analyticsLogger.logBottomNavClicked(destination.name.lowercase())
            appNavigationState.onTabSelected(destination.route)
        },
    )
}

@Composable
expect fun PlatformNavContent(
    selectedRoute: AppRoute,
    colors: MeadowColors,
    navigationSuiteType: NavigationSuiteType,
    onSelect: (BottomNavDestination) -> Unit,
)
