package com.hussienfahmy.onboarding_presentation.sign_in

import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.BeginSignInRequest.GoogleIdTokenRequestOptions
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.hussienFahmy.myGpaManager.R
import kotlinx.coroutines.tasks.await
import java.util.concurrent.CancellationException

private const val TAG = "GoogleAuthUiClient"

class GoogleAuthUiClient(
    private val context: Context,
    private val oneTapClient: SignInClient,
) : DefaultLifecycleObserver {
    private val auth = Firebase.auth

    var isSignedIn by mutableStateOf<Boolean?>(null)
        private set

    val authStateListener = FirebaseAuth.AuthStateListener { firebaseAuth ->
        isSignedIn = firebaseAuth.currentUser != null
    }

    override fun onResume(owner: LifecycleOwner) {
        super.onResume(owner)
        auth.addAuthStateListener(authStateListener)
    }

    override fun onPause(owner: LifecycleOwner) {
        super.onPause(owner)
        auth.removeAuthStateListener(authStateListener)
    }

    suspend fun signIn(): IntentSender? {
        val result = try {
            oneTapClient.beginSignIn(
                buildSignInRequest()
            ).await()
        } catch (e: Exception) {
            Log.e(TAG, "signIn: ", e)
            if (e is CancellationException) throw e else null
        }

        return result?.pendingIntent?.intentSender
    }

    suspend fun signInWithIntent(intent: Intent): SignInResult {
        val credential = oneTapClient.getSignInCredentialFromIntent(intent)
        val googleIdToken = credential.googleIdToken
        val googleCredential = GoogleAuthProvider.getCredential(googleIdToken, null)

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
            oneTapClient.signOut().await()
            auth.signOut()
        } catch (e: Exception) {
            Log.e(TAG, "signIn: ", e)
            if (e is CancellationException) throw e
        }
    }

    private fun buildSignInRequest(): BeginSignInRequest {
        return BeginSignInRequest.Builder()
            .setGoogleIdTokenRequestOptions(
                GoogleIdTokenRequestOptions.builder()
                    .setSupported(true)
                    .setFilterByAuthorizedAccounts(false)
                    .setServerClientId(context.getString(R.string.default_web_client_id))
                    .build()
            )
            .setAutoSelectEnabled(true)
            .build()
    }
}