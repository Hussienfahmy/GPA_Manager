package com.hussienfahmy.myGpaManager.navigation.screens

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import com.hussienfahmy.core.domain.analytics.AnalyticsValues
import com.hussienfahmy.core_ui.presentation.analytics.TrackScreenTime
import com.hussienfahmy.myGpaManager.navigation.FadeTransitions
import com.hussienfahmy.quick_presentation.QuickScreen
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootGraph

@Destination<RootGraph>(style = FadeTransitions::class)
@Composable
fun AppQuickScreen(
    snackBarHostState: SnackbarHostState
) {
    TrackScreenTime(AnalyticsValues.SCREEN_QUICK)

    QuickScreen(snackBarHostState = snackBarHostState)
}