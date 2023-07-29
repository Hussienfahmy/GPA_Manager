package com.hussienFahmy.myGpaManager.navigation.screens

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import com.hussienFahmy.core_ui.presentation.user_data.UserDataScreen
import com.hussienFahmy.myGpaManager.navigation.graphs.MoreNavGraph
import com.ramcosta.composedestinations.annotation.Destination

@MoreNavGraph
@Destination
@Composable
fun AppUserDataScreen(
    snackBarHostState: SnackbarHostState,
) {
    UserDataScreen(snackBarHostState = snackBarHostState)
}