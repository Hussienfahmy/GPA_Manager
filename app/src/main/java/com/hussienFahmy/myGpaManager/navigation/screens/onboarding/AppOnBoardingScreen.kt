package com.hussienFahmy.myGpaManager.navigation.screens.onboarding

import androidx.compose.runtime.Composable
import com.hussienFahmy.myGpaManager.navigation.graphs.OnBoardingNavGraph
import com.hussienfahmy.onboarding_presentation.OnBoardingScreen
import com.hussienfahmy.onboarding_presentation.sign_in.GoogleAuthUiClient
import com.ramcosta.composedestinations.annotation.Destination

@OnBoardingNavGraph(start = true)
@Destination
@Composable
fun AppOnBoardingScreen(
    onSignInSuccess: () -> Unit,
    googleAuthUiClient: GoogleAuthUiClient,
) {
    OnBoardingScreen(onSignInSuccess = onSignInSuccess, googleAuthUiClient = googleAuthUiClient)
}