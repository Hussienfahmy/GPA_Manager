package com.hussienfahmy.onboarding_presentation.sign_in

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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.hussienFahmy.myGpaManager.R
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.tasks.await
import java.util.concurrent.CancellationException

private const val TAG = "GoogleAuthUiClient"

class GoogleAuthUiClient(
    private val context: Context,
    private val credentialManager: CredentialManager,
    private val auth: FirebaseAuth
) : DefaultLifecycleObserver {

    private val _isSignedInFlow = MutableStateFlow<Boolean?>(null)
    val isSignedInFlow = _isSignedInFlow.asStateFlow()

    private val authStateListener = FirebaseAuth.AuthStateListener { firebaseAuth ->
        (firebaseAuth.currentUser != null).let {
            _isSignedInFlow.value = it
        }
    }

    init {
        auth.addAuthStateListener(authStateListener)
    }

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
        val googleCredential = GoogleAuthProvider.getCredential(idToken, null)

        return try {
            auth.signInWithCredential(googleCredential)
                .await()
                .user!!.run {
                    SignInResult.Success(
                        FirebaseUserData(
                            id = uid,
                            name = displayName ?: "",
                            photoUrl = photoUrl.toString(),
                            email = email ?: "",
                        )
                    )
                }
        } catch (e: Exception) {
            Log.e(TAG, "signIn: ", e)
            if (e is CancellationException) throw e
            else SignInResult.Error(e.message ?: "Unknown error")
        }
    }

    suspend fun signOut() {
        try {
            auth.signOut()
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