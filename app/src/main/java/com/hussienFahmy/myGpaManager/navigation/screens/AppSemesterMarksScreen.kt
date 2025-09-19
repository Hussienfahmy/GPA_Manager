package com.hussienfahmy.myGpaManager.navigation.screens

import androidx.compose.runtime.Composable
import com.hussienfahmy.core.domain.analytics.AnalyticsValues
import com.hussienfahmy.core_ui.presentation.analytics.TrackScreenTime
import com.hussienfahmy.myGpaManager.navigation.FadeTransitions
import com.hussienfahmy.semester_marks_presentaion.SemesterMarksScreen
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootGraph

@Destination<RootGraph>(style = FadeTransitions::class)
@Composable
fun AppSemesterMarksScreen() {
    TrackScreenTime(AnalyticsValues.SCREEN_MARKS)

    SemesterMarksScreen()
}