package com.hussienfahmy.myGpaManager.data.auth

import com.hussienfahmy.core.domain.auth.PlatformCredentialCleanup
import com.hussienfahmy.core.domain.auth.repository.AuthRepository
import com.hussienfahmy.core.domain.auth.repository.AuthResult
import com.hussienfahmy.core.domain.auth.repository.AuthUserData
import com.hussienfahmy.core.domain.crash.CrashReporter
import dev.gitlive.firebase.auth.FirebaseAuth
import dev.gitlive.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class FirebaseAuthRepository(
    private val auth: FirebaseAuth,
    scope: CoroutineScope,
    private val crashReporter: CrashReporter,
    private val platformCredentialCleanup: PlatformCredentialCleanup,
) : AuthRepository {

    // Eagerly shared so both flows are already listening (and can be awaited via first{}) the
    // moment a sign-in/sign-out call needs a fresh value, rather than lagging until something
    // else happens to subscribe first.
    override val userId: StateFlow<String?> = auth.authStateChanged
        .map { it?.uid }
        .stateIn(scope, SharingStarted.Eagerly, auth.currentUser?.uid)

    override val isSignedInFlow: StateFlow<Boolean?> = userId.map { it != null }
        .stateIn(scope, SharingStarted.Eagerly, null)

    override val isAnonymousFlow: StateFlow<Boolean?> = auth.authStateChanged
        .map { it?.isAnonymous }
        .stateIn(scope, SharingStarted.Eagerly, auth.currentUser?.isAnonymous)

    override suspend fun signInWithCredential(idToken: String): AuthResult {
        val googleCredential = GoogleAuthProvider.credential(idToken, null)

        return try {
            val user = auth.signInWithCredential(googleCredential).user!!
            // Waits for sign in
            userId.first { it == user.uid }
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

    override suspend fun signInAnonymously(): AuthResult {
        return try {
            val user = auth.signInAnonymously().user!!
            userId.first { it == user.uid }
            AuthResult.Success(AuthUserData(id = user.uid, name = "", photoUrl = "", email = ""))
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            crashReporter.recordException(e, mapOf("operation" to "signInAnonymously"))
            AuthResult.Error(e.message ?: "Unknown error")
        }
    }

    override suspend fun signOut() {
        auth.signOut()
        platformCredentialCleanup.clear()
        userId.first { it == null }
    }
}
