package com.hussienFahmy.myGpaManager.navigation.screens

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import com.hussienfahmy.semester_subjctets_presentaion.SemesterScreen
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph

@RootNavGraph(start = true)
@Destination
@Composable
fun AppSemesterScreen(
    snackBarHostState: SnackbarHostState,
) {
    SemesterScreen(
        snackBarHostState = snackBarHostState,
    )
}