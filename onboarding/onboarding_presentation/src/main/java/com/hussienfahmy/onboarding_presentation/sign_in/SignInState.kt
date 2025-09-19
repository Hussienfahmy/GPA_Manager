package com.hussienfahmy.onboarding_presentation.sign_in

sealed class SignInState {
    object Initial : SignInState()
    object Loading : SignInState()
    object Success : SignInState()
    object Error : SignInState()
}