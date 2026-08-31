package com.hussienfahmy.onboarding_presentation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.hussienfahmy.core.domain.auth.repository.AuthResult

/**
 * The one genuinely platform-specific step in onboarding's sign-in action: Android launches Google
 * Sign-In through CredentialManager (which needs the current Activity), iOS launches Sign in with
 * Apple through AuthenticationServices. Everything else on the welcome screen is shared.
 */
fun interface PlatformSignIn {
    /** Runs the platform sign-in flow. Returns null when the user cancelled or it failed silently. */
    suspend fun signIn(): AuthResult?
}

/** Resolves the platform's sign-in implementation, wired to whatever ambient state it needs. */
@Composable
expect fun rememberPlatformSignIn(): PlatformSignIn

/**
 * The sign-in call-to-action button, styled per platform: the system "Sign in with Apple" button
 * ([platform.AuthenticationServices.ASAuthorizationAppleIDButton]) on iOS - which App Store Review
 * guideline 4.8 and the Apple HIG require whenever Sign in with Apple is offered - and a Google
 * button on Android. Purely visual; [onClick] should drive [PlatformSignIn.signIn].
 */
@Composable
expect fun PlatformSignInButton(
    onClick: () -> Unit,
    enabled: Boolean,
    modifier: Modifier = Modifier,
)
