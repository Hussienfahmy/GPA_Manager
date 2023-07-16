package com.hussienFahmy.myGpaManager.navigation.screens

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import com.hussienfahmy.subject_settings_presentation.SubjectsSettingsScreen
import com.ramcosta.composedestinations.annotation.Destination

@Destination
@Composable
fun AppSubjectSettingsScreen(
    snackBarHostState: SnackbarHostState
) {
    SubjectsSettingsScreen(snackBarHostState = snackBarHostState)
}