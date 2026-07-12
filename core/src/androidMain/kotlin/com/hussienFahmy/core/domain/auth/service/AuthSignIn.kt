package com.hussienfahmy.core.domain.auth.service

import android.app.Activity

// Activity is genuinely Android-only - presenting sign-in UI is one of the few truly
// platform-specific things in this app. Split out of AuthService (which now holds only the
// platform-agnostic isSignedInFlow/signOut) so SignOut/MainViewModel/MoreViewModel - which never
// call signIn - can move to commonMain. The iOS equivalent (recommended: Sign in with Apple, per
// the migration plan) is deferred to when iOS sign-in is actually implemented.
interface AuthSignIn {
    suspend fun signIn(activity: Activity): AuthServiceResult?
}
