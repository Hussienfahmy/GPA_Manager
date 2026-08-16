package com.hussienfahmy.core.domain.auth

import android.app.Activity
import android.util.Log
import androidx.credentials.Credential
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.hussienfahmy.core.domain.auth.repository.AuthRepository
import com.hussienfahmy.core.domain.auth.repository.AuthResult
import com.hussienfahmy.core.domain.crash.CrashReporter
import java.util.concurrent.CancellationException

private const val TAG = "GoogleAuthUiClient"

class GoogleAuthUiClient(
    private val credentialManager: CredentialManager,
    private val authRepository: AuthRepository,
    private val crashReporter: CrashReporter
) {
    suspend fun signIn(activity: Activity): AuthResult? {
        return try {
            val result = credentialManager.getCredential(
                request = request,
                context = activity,
            )
            handleSignIn(result.credential)
        } catch (e: Exception) {
            Log.e(TAG, "signIn: ", e)
            if (e is CancellationException) throw e
            crashReporter.recordException(e, mapOf("operation" to "signIn"))
            null
        }
    }

    private suspend fun handleSignIn(credential: Credential): AuthResult? {
        return if (credential is CustomCredential && credential.type == GoogleIdTokenCredential.Companion.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
            val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)
            authRepository.signInWithCredential(googleIdTokenCredential.idToken)
        } else {
            Log.w(TAG, "Credential is not of type Google ID!")
            null
        }
    }

    private val googleIdOption
        get() = GetGoogleIdOption.Builder()
            .setServerClientId(GOOGLE_WEB_CLIENT_ID)
            .setFilterByAuthorizedAccounts(false)
            .setAutoSelectEnabled(true)
            .build()

    private val request
        get() = GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption)
            .build()
}
