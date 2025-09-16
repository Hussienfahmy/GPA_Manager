package com.hussienfahmy.myGpaManager.navigation.screens

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.hussienfahmy.core.domain.analytics.AnalyticsLogger
import com.hussienfahmy.core.domain.analytics.AnalyticsValues
import com.hussienfahmy.gpa_system_sittings_presentaion.GPASettingsScreen
import com.hussienfahmy.myGpaManager.navigation.SlideTransitions
import com.hussienfahmy.myGpaManager.navigation.graphs.MoreNavGraph
import com.ramcosta.composedestinations.annotation.Destination
import org.koin.compose.koinInject

@Destination<MoreNavGraph>(style = SlideTransitions::class)
@Composable
fun AppGPASettingsScreen() {
    val analyticsLogger = koinInject<AnalyticsLogger>()

    LaunchedEffect(Unit) {
        analyticsLogger.logSettingsAccessed(AnalyticsValues.SETTINGS_TYPE_GPA)
    }

    GPASettingsScreen()
}