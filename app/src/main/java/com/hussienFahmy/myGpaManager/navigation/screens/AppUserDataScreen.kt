package com.hussienfahmy.myGpaManager.navigation.screens

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.hussienfahmy.core_ui.presentation.user_data.UserDataScreen
import com.hussienfahmy.myGpaManager.navigation.FadeTransitions
import com.hussienfahmy.myGpaManager.navigation.graphs.MoreNavGraph
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootGraph

@MoreNavGraph
@Destination<RootGraph>(style = FadeTransitions::class)
@Composable
fun AppUserDataScreen(
    snackBarHostState: SnackbarHostState,
) {
    UserDataScreen(
        modifier = Modifier.fillMaxSize(),
        snackBarHostState = snackBarHostState
    )
}