package com.hussienFahmy.myGpaManager.navigation.screens

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import com.hussienFahmy.grades_setting_presentation.GradeSettingsScreen
import com.hussienFahmy.myGpaManager.navigation.MoreNavGraph
import com.ramcosta.composedestinations.annotation.Destination

@MoreNavGraph
@Destination
@Composable
fun AppGradeSettingsScreen(
    snackBarHostState: SnackbarHostState
) {
    GradeSettingsScreen(snackBarHostState = snackBarHostState)
}