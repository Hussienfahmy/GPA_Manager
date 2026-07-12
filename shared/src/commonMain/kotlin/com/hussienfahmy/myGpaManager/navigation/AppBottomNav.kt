package com.hussienfahmy.myGpaManager.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hussienfahmy.core.domain.analytics.AnalyticsLogger
import com.hussienfahmy.core_ui.theme.CapsLabelStyle
import com.hussienfahmy.core_ui.theme.MeadowAccent
import com.hussienfahmy.core_ui.theme.MeadowColors
import com.hussienfahmy.core_ui.theme.MeadowTheme
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject

private fun BottomNavDestination.accent(colors: MeadowColors): MeadowAccent = when (this) {
    BottomNavDestination.Semester -> colors.semester
    BottomNavDestination.Marks -> colors.marks
    BottomNavDestination.History -> colors.history
    BottomNavDestination.Quick -> colors.quick
    BottomNavDestination.More -> colors.more
}

@Composable
fun AppBottomNav(
    appNavigationState: AppNavigationState
) {
    val analyticsLogger = koinInject<AnalyticsLogger>()
    // SemesterDetail lives inside the History tab's own stack, so topLevelRoute stays
    // AppRoute.SemesterHistory the whole time it's pushed - no special-casing needed here,
    // unlike the old NavHostController-based currentDestinationAsState() approach.
    val currentTopLevelRoute = appNavigationState.topLevelRoute

    val colors = MeadowTheme.colors

    Column(modifier = Modifier.background(colors.navBg)) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .size(1.dp)
                .background(colors.navBorder)
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 6.dp, end = 6.dp, top = 8.dp, bottom = 6.dp)
                .navigationBarsPadding()
        ) {
            BottomNavDestination.entries.forEach { destination ->
                val selected = currentTopLevelRoute == destination.route
                val accent = destination.accent(colors)

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(2.dp),
                    modifier = Modifier
                        .weight(1f)
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null,
                        ) {
                            analyticsLogger.logBottomNavClicked(destination.name.lowercase())
                            appNavigationState.onTabSelected(destination.route)
                        },
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(if (selected) accent.container else Color.Transparent)
                            .padding(horizontal = 18.dp, vertical = 4.dp),
                    ) {
                        Icon(
                            imageVector = destination.icon,
                            contentDescription = stringResource(destination.label),
                            tint = if (selected) accent.deep else colors.navItemIcon,
                            modifier = Modifier.size(17.dp),
                        )
                    }
                    Text(
                        text = stringResource(destination.label),
                        style = CapsLabelStyle().copy(
                            fontSize = 10.5.sp,
                            letterSpacing = 0.sp,
                            fontWeight = if (selected) FontWeight.ExtraBold else FontWeight.Bold,
                        ),
                        color = if (selected) accent.deep else colors.navItemText,
                        maxLines = 1,
                    )
                }
            }
        }
    }
}
