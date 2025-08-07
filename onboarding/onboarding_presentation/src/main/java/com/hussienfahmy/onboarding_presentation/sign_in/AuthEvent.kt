package com.hussienfahmy.onboarding_presentation.sign_in

sealed class AuthEvent {
    data class OnSignInResult(val signInResult: SignInResult?) : AuthEvent()
}