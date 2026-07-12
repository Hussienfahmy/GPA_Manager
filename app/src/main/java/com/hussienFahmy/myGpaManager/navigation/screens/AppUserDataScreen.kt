package com.hussienfahmy.myGpaManager.navigation.screens

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.hussienfahmy.core_ui.presentation.user_data.UserDataScreen

@Composable
fun AppUserDataScreen(
    snackBarHostState: SnackbarHostState,
) {
    UserDataScreen(
        modifier = Modifier.fillMaxSize(),
        snackBarHostState = snackBarHostState
    )
}