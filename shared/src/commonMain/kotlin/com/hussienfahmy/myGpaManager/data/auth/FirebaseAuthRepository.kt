package com.hussienfahmy.myGpaManager.data.auth

import com.hussienfahmy.core.domain.auth.repository.AuthRepository
import com.hussienfahmy.core.domain.auth.repository.AuthResult
import com.hussienfahmy.core.domain.auth.repository.AuthUserData
import com.hussienfahmy.core.domain.crash.CrashReporter
import dev.gitlive.firebase.auth.FirebaseAuth
import dev.gitlive.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class FirebaseAuthRepository(
    private val auth: FirebaseAuth,
    scope: CoroutineScope,
    private val crashReporter: CrashReporter
) : AuthRepository {

    // GitLive's authStateChanged Flow replaces the manual addAuthStateListener/callbackFlow
    // wiring the Android SDK needed - same pattern already used for
    // FirebaseUserDataRepository's snapshots Flow.
    override val userId: StateFlow<String?> = auth.authStateChanged
        .map { it?.uid }
        .stateIn(
            scope = scope,
            started = SharingStarted.Lazily,
            initialValue = auth.currentUser?.uid
        )

    override val isSignedInFlow: StateFlow<Boolean?> = userId.map { it != null }
        .stateIn(
            scope = scope,
            started = SharingStarted.Lazily,
            initialValue = null
        )

    override suspend fun signInWithCredential(idToken: String): AuthResult {
        val googleCredential = GoogleAuthProvider.credential(idToken, null)

        return try {
            val user = auth.signInWithCredential(googleCredential).user!!
            AuthResult.Success(
                AuthUserData(
                    id = user.uid,
                    name = user.displayName ?: "",
                    photoUrl = user.photoURL ?: "",
                    email = user.email ?: "",
                )
            )
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            crashReporter.recordException(e, mapOf("operation" to "signInWithCredential"))
            AuthResult.Error(e.message ?: "Unknown error")
        }
    }

    override suspend fun signOut() {
        auth.signOut()
        while (userId.value != null) {
            delay(100)
        }
    }
}
