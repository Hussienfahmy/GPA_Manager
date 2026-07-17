package com.hussienfahmy.myGpaManager.navigation.screens

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import com.hussienfahmy.core.domain.analytics.AnalyticsValues
import com.hussienfahmy.core_ui.presentation.analytics.TrackScreenTime
import com.hussienfahmy.quick_presentation.QuickScreen

@Composable
fun AppQuickScreen(
    snackBarHostState: SnackbarHostState
) {
    TrackScreenTime(AnalyticsValues.SCREEN_QUICK)

    QuickScreen(snackBarHostState = snackBarHostState)
}