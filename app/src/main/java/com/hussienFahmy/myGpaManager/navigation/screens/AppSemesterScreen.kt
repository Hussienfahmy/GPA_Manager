package com.hussienFahmy.myGpaManager.navigation.screens

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import com.hussienFahmy.myGpaManager.navigation.SlideTransitions
import com.hussienfahmy.semester_subjctets_presentaion.SemesterScreen
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootGraph

@Destination<RootGraph>(start = true, style = SlideTransitions::class)
@Composable
fun AppSemesterScreen(
    snackBarHostState: SnackbarHostState,
) {
    SemesterScreen(
        snackBarHostState = snackBarHostState,
    )
}