package com.hussienfahmy.myGpaManager.navigation.screens

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import com.hussienfahmy.myGpaManager.navigation.SlideTransitions
import com.hussienfahmy.myGpaManager.navigation.graphs.MoreNavGraph
import com.hussienfahmy.subject_settings_presentation.SubjectsSettingsScreen
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootGraph

@MoreNavGraph
@Destination<RootGraph>(style = SlideTransitions::class)
@Composable
fun AppSubjectSettingsScreen(
    snackBarHostState: SnackbarHostState
) {
    SubjectsSettingsScreen(snackBarHostState = snackBarHostState)
}