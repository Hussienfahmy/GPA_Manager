package com.hussienfahmy.myGpaManager.navigation.screens

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.hussienfahmy.core.domain.analytics.AnalyticsLogger
import com.hussienfahmy.core.domain.analytics.AnalyticsValues
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

    GPASettingsScreen()
}