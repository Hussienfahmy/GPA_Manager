package com.hussienfahmy.myGpaManager.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hussienfahmy.core.domain.analytics.AnalyticsLogger
import com.hussienfahmy.core_ui.theme.CapsLabelStyle
import com.hussienfahmy.core_ui.theme.MeadowTheme
import org.jetbrains.compose.resources.stringResource
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
        NavigationBar(containerColor = colors.navBg) {
            BottomNavDestination.entries.forEach { destination ->
                val selected = currentTopLevelRoute == destination.route
                val accent = destination.accent(colors)

                NavigationBarItem(
                    selected = selected,
                    onClick = {
                        analyticsLogger.logBottomNavClicked(destination.name.lowercase())
                        appNavigationState.onTabSelected(destination.route)
                    },
                    icon = {
                        Icon(
                            imageVector = destination.icon,
                            contentDescription = stringResource(destination.label),
                            modifier = Modifier.size(17.dp),
                        )
                    },
                    label = {
                        Text(
                            text = stringResource(destination.label),
                            style = CapsLabelStyle().copy(
                                fontSize = 10.5.sp,
                                letterSpacing = 0.sp,
                                fontWeight = if (selected) FontWeight.ExtraBold else FontWeight.Bold,
                            ),
                            maxLines = 1,
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = accent.deep,
                        selectedTextColor = accent.deep,
                        indicatorColor = accent.container,
                        unselectedIconColor = colors.navItemIcon,
                        unselectedTextColor = colors.navItemText,
                    ),
                )
            }
        }
    }
}
