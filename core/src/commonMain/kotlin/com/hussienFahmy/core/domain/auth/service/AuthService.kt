package com.hussienfahmy.core.domain.auth.service

import kotlinx.coroutines.flow.StateFlow

/**
 * The platform-agnostic half of authentication state. Presenting the sign-in UI itself needs a
 * platform handle (Android's Activity, iOS's presenting UIViewController) and lives behind the
 * separate, platform-specific AuthSignIn interface instead - see AuthSignIn.kt (androidMain).
 */
interface AuthService {
    val isSignedInFlow: StateFlow<Boolean?>

    suspend fun signOut()
}
