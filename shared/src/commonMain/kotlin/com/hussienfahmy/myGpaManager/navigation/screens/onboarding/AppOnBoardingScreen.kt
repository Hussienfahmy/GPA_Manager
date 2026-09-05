package com.hussienfahmy.myGpaManager.navigation.screens.onboarding

import androidx.compose.runtime.Composable
import com.hussienfahmy.onboarding_presentation.OnBoardingScreen

@Composable
fun AppOnBoardingScreen(
    onSignInSuccess: () -> Unit,
    onGuestReady: () -> Unit,
) {
    OnBoardingScreen(
        onSignInSuccess = onSignInSuccess,
        onGuestReady = onGuestReady,
    )
}
