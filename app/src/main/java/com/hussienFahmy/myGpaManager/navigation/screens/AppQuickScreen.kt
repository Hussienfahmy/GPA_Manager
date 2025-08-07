package com.hussienFahmy.myGpaManager.navigation.screens

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import com.hussienfahmy.quick_presentation.QuickScreen
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootGraph

@Destination<RootGraph>
@Composable
fun AppQuickScreen(
    snackBarHostState: SnackbarHostState
) {
    QuickScreen(snackBarHostState = snackBarHostState)
}