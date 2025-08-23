package com.hussienfahmy.myGpaManager.data.auth

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.hussienfahmy.core.domain.auth.repository.AuthRepository
import com.hussienfahmy.core.domain.auth.repository.AuthResult
import com.hussienfahmy.core.domain.auth.repository.AuthUserData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.tasks.await
import java.util.concurrent.CancellationException

class FirebaseAuthRepository(
    private val auth: FirebaseAuth
) : AuthRepository {

    private val _isSignedInFlow = MutableStateFlow<Boolean?>(null)
    override val isSignedInFlow = _isSignedInFlow.asStateFlow()

    private val authStateListeners = mutableSetOf<(String?) -> Unit>()

    private val firebaseAuthStateListener = FirebaseAuth.AuthStateListener { firebaseAuth ->
        val userId = firebaseAuth.currentUser?.uid
        _isSignedInFlow.value = userId != null
        authStateListeners.forEach { it(userId) }
    }

    init {
        auth.addAuthStateListener(firebaseAuthStateListener)
    }

    override suspend fun signInWithCredential(idToken: String): AuthResult {
        val googleCredential = GoogleAuthProvider.getCredential(idToken, null)

        return try {
            auth.signInWithCredential(googleCredential)
                .await()
                .user!!.run {
                    AuthResult.Success(
                        AuthUserData(
                            id = uid,
                            name = displayName ?: "",
                            photoUrl = photoUrl.toString(),
                            email = email ?: "",
                        )
                    )
                }
        } catch (e: Exception) {
            if (e is CancellationException) throw e
            else AuthResult.Error(e.message ?: "Unknown error")
        }
    }

    override suspend fun signOut() {
        auth.signOut()
    }

    override fun getCurrentUserId(): String? {
        return auth.currentUser?.uid
    }

    override fun addAuthStateListener(listener: (String?) -> Unit) {
        authStateListeners.add(listener)
    }

    override fun removeAuthStateListener(listener: (String?) -> Unit) {
        authStateListeners.remove(listener)
    }
}