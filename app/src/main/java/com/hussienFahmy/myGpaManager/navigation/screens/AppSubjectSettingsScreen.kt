package com.hussienFahmy.myGpaManager.navigation.screens

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import com.hussienFahmy.myGpaManager.navigation.MoreNavGraph
import com.hussienfahmy.subject_settings_presentation.SubjectsSettingsScreen
import com.ramcosta.composedestinations.annotation.Destination

@MoreNavGraph
@Destination
@Composable
fun AppSubjectSettingsScreen(
    snackBarHostState: SnackbarHostState
) {
    SubjectsSettingsScreen(snackBarHostState = snackBarHostState)
}