package com.hussienfahmy.onboarding_presentation.sign_in

import com.hussienfahmy.core.domain.auth.service.AuthServiceResult

sealed class AuthEvent {
    data class OnSignInResult(val signInResult: AuthServiceResult?) : AuthEvent()
}