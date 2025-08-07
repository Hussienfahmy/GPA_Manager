package com.hussienFahmy.myGpaManager.navigation.screens

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import com.hussienFahmy.grades_setting_presentation.GradeSettingsScreen
import com.hussienFahmy.myGpaManager.navigation.graphs.MoreNavGraph
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootGraph

@MoreNavGraph
@Destination<RootGraph>
@Composable
fun AppGradeSettingsScreen(
    snackBarHostState: SnackbarHostState
) {
    GradeSettingsScreen(snackBarHostState = snackBarHostState)
}