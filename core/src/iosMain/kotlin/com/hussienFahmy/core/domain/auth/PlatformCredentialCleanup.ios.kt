package com.hussienfahmy.core.domain.auth

import com.hussienfahmy.core.util.PlatformContext

actual class PlatformCredentialCleanup actual constructor(context: PlatformContext) {
    actual suspend fun clear() {
        // No ambient credential store to clear for Sign in with Apple or email/password.
        // TODO: if native Google Sign-In (GIDSignIn) is ever added on iOS, it caches the last
        // signed-in account client-side like Android's CredentialManager does - call
        // GIDSignIn.sharedInstance().signOut() here to match.
    }
}
