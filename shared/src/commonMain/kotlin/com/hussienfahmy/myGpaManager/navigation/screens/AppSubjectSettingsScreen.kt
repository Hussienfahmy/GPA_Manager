package com.hussienfahmy.myGpaManager.navigation.screens

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.hussienfahmy.core.domain.analytics.AnalyticsLogger
import com.hussienfahmy.core.domain.analytics.AnalyticsValues
import com.hussienfahmy.subject_settings_presentation.SubjectsSettingsScreen
import org.koin.compose.koinInject

@Composable
fun AppSubjectSettingsScreen(
    snackBarHostState: SnackbarHostState
) {
    val analyticsLogger = koinInject<AnalyticsLogger>()

    LaunchedEffect(Unit) {
        analyticsLogger.logSettingsAccessed(AnalyticsValues.SETTINGS_TYPE_SUBJECT)
    }

    SubjectsSettingsScreen(snackBarHostState = snackBarHostState)
}