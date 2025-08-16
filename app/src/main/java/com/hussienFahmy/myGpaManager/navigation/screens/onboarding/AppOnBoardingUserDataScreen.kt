package com.hussienfahmy.myGpaManager.navigation.screens.onboarding

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import com.hussienfahmy.myGpaManager.navigation.SlideTransitions
import com.hussienfahmy.myGpaManager.navigation.graphs.OnBoardingNavGraph
import com.hussienfahmy.onboarding_presentation.OnBoardingUserDataScreen
import com.ramcosta.composedestinations.annotation.Destination

@Destination<OnBoardingNavGraph>(style = SlideTransitions::class)
@Composable
fun AppOnBoardingUserDataScreen(
    snackBarHostState: SnackbarHostState,
    onNextClick: () -> Unit,
) {
    OnBoardingUserDataScreen(onNextClick = onNextClick, snackBarHostState = snackBarHostState)
}