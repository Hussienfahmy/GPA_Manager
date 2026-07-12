package com.hussienfahmy.myGpaManager.navigation.screens.onboarding

import androidx.compose.runtime.Composable
import com.hussienfahmy.onboarding_presentation.OnBoardingScreen

@Composable
fun AppOnBoardingScreen(
    onSignInSuccess: () -> Unit,
) {
    OnBoardingScreen(onSignInSuccess = onSignInSuccess)
}