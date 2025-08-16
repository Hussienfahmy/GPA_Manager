package com.hussienfahmy.myGpaManager.navigation.screens.onboarding

import androidx.compose.runtime.Composable
import com.hussienfahmy.myGpaManager.navigation.SlideTransitions
import com.hussienfahmy.myGpaManager.navigation.graphs.OnBoardingNavGraph
import com.hussienfahmy.onboarding_presentation.OnBoardingScreen
import com.hussienfahmy.onboarding_presentation.sign_in.GoogleAuthUiClient
import com.ramcosta.composedestinations.annotation.Destination

@Destination<OnBoardingNavGraph>(start = true, style = SlideTransitions::class)
@Composable
fun AppOnBoardingScreen(
    onSignInSuccess: () -> Unit,
    googleAuthUiClient: GoogleAuthUiClient,
) {
    OnBoardingScreen(onSignInSuccess = onSignInSuccess, googleAuthUiClient = googleAuthUiClient)
}