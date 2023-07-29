package com.hussienfahmy.onboarding_presentation.sign_in

sealed class SignInState {
    object Initial : SignInState()
    object Success : SignInState()
}