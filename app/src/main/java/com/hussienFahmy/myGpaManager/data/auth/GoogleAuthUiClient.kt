package com.hussienfahmy.myGpaManager.data.auth

import android.content.Context
import android.util.Log
import androidx.credentials.ClearCredentialStateRequest
import androidx.credentials.Credential
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.lifecycle.DefaultLifecycleObserver
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.hussienfahmy.core.domain.auth.repository.AuthRepository
import com.hussienfahmy.core.domain.auth.repository.AuthResult
import com.hussienfahmy.myGpaManager.R
import kotlinx.coroutines.flow.StateFlow
import java.util.concurrent.CancellationException

private const val TAG = "GoogleAuthUiClient"

class GoogleAuthUiClient(
    private val context: Context,
    private val credentialManager: CredentialManager,
    private val authRepository: AuthRepository
) : DefaultLifecycleObserver {

    val isSignedInFlow: StateFlow<Boolean?> = authRepository.isSignedInFlow as StateFlow<Boolean?>

    suspend fun signIn(): SignInResult? {
        return try {
            val result = credentialManager.getCredential(
                request = request,
                context = context,
            )
            handleSignIn(result.credential)
        } catch (e: Exception) {
            Log.e(TAG, "signIn: ", e)
            if (e is CancellationException) throw e else null

            null
        }
    }

    private suspend fun handleSignIn(credential: Credential): SignInResult? {
        return if (credential is CustomCredential && credential.type == GoogleIdTokenCredential.Companion.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
            val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)

            signInWithCredential(googleIdTokenCredential.idToken)
        } else {
            Log.w(TAG, "Credential is not of type Google ID!")
            null
        }
    }

    suspend fun signInWithCredential(idToken: String): SignInResult {
        return when (val result = authRepository.signInWithCredential(idToken)) {
            is AuthResult.Success -> SignInResult.Success(
                FirebaseUserData(
                    id = result.userData.id,
                    name = result.userData.name,
                    photoUrl = result.userData.photoUrl,
                    email = result.userData.email,
                )
            )

            is AuthResult.Error -> SignInResult.Error(result.message)
        }
    }

    suspend fun signOut() {
        try {
            authRepository.signOut()
            val clearRequest = ClearCredentialStateRequest()
            credentialManager.clearCredentialState(clearRequest)
        } catch (e: Exception) {
            Log.e(TAG, "signIn: ", e)
            if (e is CancellationException) throw e
        }
    }

    private val googleIdOption
        get() = GetGoogleIdOption.Builder()
            .setServerClientId(context.getString(R.string.default_web_client_id))
            .setFilterByAuthorizedAccounts(false)
            .setAutoSelectEnabled(true)
            .build()

    private val request
        get() = GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption)
            .build()
}

sealed interface SignInResult {
    data class Success(val userData: FirebaseUserData) : SignInResult
    data class Error(val message: String) : SignInResult
}

data class FirebaseUserData(
    val id: String,
    val name: String,
    val photoUrl: String,
    val email: String,
)