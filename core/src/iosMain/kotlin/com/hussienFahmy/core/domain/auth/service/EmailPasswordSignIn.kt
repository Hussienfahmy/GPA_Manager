package com.hussienfahmy.core.domain.auth.service

import com.hussienfahmy.core.domain.crash.CrashReporter
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.FirebaseUser
import dev.gitlive.firebase.auth.auth

// Temporary stand-in for AppleSignIn.kt while this project's Apple Developer account isn't
// enrolled in the paid program yet - Sign in with Apple's entitlement isn't available to personal
// teams. Delete this (and the native email/password fields in OnBoardingScreen.ios.kt) once
// AppleSignIn.kt is wired back in as the "Get Started" action.
class EmailPasswordSignIn(
    private val crashReporter: CrashReporter
) {
    suspend fun signIn(email: String, password: String): AuthServiceResult {
        return try {
            signInOrCreateAccount(email, password).toResult()
        } catch (e: Exception) {
            crashReporter.recordException(e, mapOf("operation" to "emailPasswordSignIn"))
            AuthServiceResult.Error(e.message ?: "Unknown error")
        }
    }

    // This is onboarding's very first screen, so most first-time taps are sign-ups rather than
    // existing users mistyping a password - falls back to account creation instead of forcing a
    // separate sign-in/sign-up toggle onto this temporary UI.
    private suspend fun signInOrCreateAccount(email: String, password: String): FirebaseUser {
        val auth = Firebase.auth
        val existingUser = try {
            auth.signInWithEmailAndPassword(email, password).user
        } catch (e: Exception) {
            null
        }
        return existingUser
            ?: auth.createUserWithEmailAndPassword(email, password).user
            ?: error("Firebase returned no user")
    }

    private fun FirebaseUser.toResult() = AuthServiceResult.Success(
        AuthServiceUserData(
            id = uid,
            name = displayName ?: "",
            photoUrl = photoURL ?: "",
            email = email ?: "",
        )
    )
}
