package com.hussienFahmy.myGpaManager.navigation.screens

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import com.hussienfahmy.user_data_presentaion.UserDataScreen
import com.ramcosta.composedestinations.annotation.Destination

@Destination
@Composable
fun AppUserDataScreen(
    snackBarHostState: SnackbarHostState,
) {
    UserDataScreen(snackBarHostState = snackBarHostState)
}