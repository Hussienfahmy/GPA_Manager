package com.hussienfahmy.onboarding_presentation.sign_in

import com.hussienfahmy.core.domain.auth.repository.AuthResult

sealed class AuthEvent {
    data class OnSignInResult(val signInResult: AuthResult?) : AuthEvent()
}