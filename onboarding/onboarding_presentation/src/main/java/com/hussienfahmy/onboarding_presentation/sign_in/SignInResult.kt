package com.hussienfahmy.onboarding_presentation.sign_in

sealed class SignInResult {
    data class Success(val data: FirebaseUserData) : SignInResult()
    data class Error(val message: String) : SignInResult()
}