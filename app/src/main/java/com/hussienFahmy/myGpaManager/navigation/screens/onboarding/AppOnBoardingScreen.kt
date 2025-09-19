package com.hussienfahmy.myGpaManager.navigation.screens.onboarding

import androidx.compose.runtime.Composable
import com.hussienfahmy.myGpaManager.navigation.WelcomeSlideTransitions
import com.hussienfahmy.myGpaManager.navigation.graphs.OnBoardingNavGraph
import com.hussienfahmy.onboarding_presentation.OnBoardingScreen
import com.ramcosta.composedestinations.annotation.Destination

@Destination<OnBoardingNavGraph>(start = true, style = WelcomeSlideTransitions::class)
@Composable
fun AppOnBoardingScreen(
    onSignInSuccess: () -> Unit,
) {
    OnBoardingScreen(onSignInSuccess = onSignInSuccess)
}