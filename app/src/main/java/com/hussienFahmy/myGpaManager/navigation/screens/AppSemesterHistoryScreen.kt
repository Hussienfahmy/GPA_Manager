package com.hussienfahmy.myGpaManager.navigation.screens

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import com.hussienfahmy.myGpaManager.navigation.FadeTransitions
import com.hussienfahmy.semester_history_presentation.SemesterHistoryScreen
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootGraph
import com.ramcosta.composedestinations.generated.destinations.AppSemesterDetailScreenDestination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Destination<RootGraph>(style = FadeTransitions::class)
@Composable
fun AppSemesterHistoryScreen(
    snackBarHostState: SnackbarHostState,
    navigator: DestinationsNavigator,
) {
    SemesterHistoryScreen(
        snackBarHostState = snackBarHostState,
        onSemesterClick = { semesterId ->
            navigator.navigate(AppSemesterDetailScreenDestination(semesterId = semesterId))
        },
    )
}
