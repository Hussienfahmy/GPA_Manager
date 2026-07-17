package com.hussienfahmy.core.domain.auth.service

import android.app.Activity
import kotlinx.coroutines.flow.StateFlow

// Activity is genuinely Android-only - presenting sign-in UI is one of the few truly
// platform-specific things in this app. Deferred cross-platform decoupling to the iOS phase per
// the migration plan; AuthServiceResult/AuthServiceUserData (the pure data half of what used to
// be this same file) already live in commonMain - see AuthServiceResult.kt.
interface AuthService {
    val isSignedInFlow: StateFlow<Boolean?>

    suspend fun signIn(activity: Activity): AuthServiceResult?
    suspend fun signOut()
}
