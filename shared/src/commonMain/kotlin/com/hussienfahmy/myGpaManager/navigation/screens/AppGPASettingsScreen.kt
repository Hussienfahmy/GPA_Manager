package com.hussienfahmy.myGpaManager.navigation.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import com.hussienfahmy.core.domain.analytics.AnalyticsLogger
import com.hussienfahmy.core.domain.analytics.AnalyticsValues
import com.hussienfahmy.core_ui.LocalSpacing
import com.hussienfahmy.core_ui.presentation.analytics.TrackScreenTime
import com.hussienfahmy.gpa_system_sittings_presentaion.GPASettingsScreen
import org.koin.compose.koinInject

@Composable
fun AppGPASettingsScreen() {
    val analyticsLogger = koinInject<AnalyticsLogger>()

    LaunchedEffect(Unit) {
        analyticsLogger.logSettingsAccessed(AnalyticsValues.SETTINGS_TYPE_GPA)
    }

    TrackScreenTime(AnalyticsValues.SCREEN_GPA_SETTINGS)

    GPASettingsScreen(
        modifier = Modifier.padding(horizontal = LocalSpacing.current.medium)
    )
}