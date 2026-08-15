package com.hussienfahmy.myGpaManager.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.hussienfahmy.core.domain.analytics.AnalyticsLogger
import com.hussienfahmy.core_ui.theme.MeadowColors
import com.hussienfahmy.core_ui.theme.MeadowTheme
import org.koin.compose.koinInject

@Composable
fun AppBottomNav(
    appNavigationState: AppNavigationState
) {
    val analyticsLogger = koinInject<AnalyticsLogger>()
    val currentTopLevelRoute = appNavigationState.topLevelRoute

    val colors = MeadowTheme.colors

    Column {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .size(1.dp)
                .background(colors.navBorder)
        )
        PlatformBottomNavContent(
            selectedRoute = currentTopLevelRoute,
            colors = colors,
            onSelect = { destination ->
                analyticsLogger.logBottomNavClicked(destination.name.lowercase())
                appNavigationState.onTabSelected(destination.route)
            },
        )
    }
}

// Android renders Material3's NavigationBar; iOS embeds a real native UITabBar via UIKitView.
@Composable
expect fun PlatformBottomNavContent(
    selectedRoute: AppRoute,
    colors: MeadowColors,
    onSelect: (BottomNavDestination) -> Unit,
)
