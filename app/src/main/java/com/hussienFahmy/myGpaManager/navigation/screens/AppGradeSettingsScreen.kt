package com.hussienfahmy.myGpaManager.navigation.screens

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import com.hussienfahmy.grades_setting_presentation.GradeSettingsScreen
import com.hussienfahmy.myGpaManager.navigation.SlideTransitions
import com.hussienfahmy.myGpaManager.navigation.graphs.MoreNavGraph
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootGraph

@MoreNavGraph
@Destination<RootGraph>(style = SlideTransitions::class)
@Composable
fun AppGradeSettingsScreen(
    snackBarHostState: SnackbarHostState
) {
    GradeSettingsScreen(snackBarHostState = snackBarHostState)
}