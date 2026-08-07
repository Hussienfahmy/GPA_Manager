package com.hussienfahmy.myGpaManager.navigation.screens

import androidx.compose.runtime.Composable
import com.hussienfahmy.core.domain.analytics.AnalyticsValues
import com.hussienfahmy.core_ui.presentation.analytics.TrackScreenTime
import com.hussienfahmy.semester_marks_presentaion.SemesterMarksScreen

@Composable
fun AppSemesterMarksScreen() {
    TrackScreenTime(AnalyticsValues.SCREEN_MARKS)

    SemesterMarksScreen()
}