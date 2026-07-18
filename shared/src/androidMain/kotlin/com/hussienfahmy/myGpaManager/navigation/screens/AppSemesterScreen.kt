package com.hussienfahmy.myGpaManager.navigation.screens

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.hussienfahmy.core.domain.analytics.AnalyticsValues
import com.hussienfahmy.core_ui.presentation.analytics.TrackScreenTime
import com.hussienfahmy.semester_subjctets_presentaion.SemesterScreen

@Composable
fun AppSemesterScreen(
    snackBarHostState: SnackbarHostState,
) {
    TrackScreenTime(AnalyticsValues.SCREEN_SEMESTER)

    SemesterScreen(
        modifier = Modifier.fillMaxSize(),
        snackBarHostState = snackBarHostState,
    )
}