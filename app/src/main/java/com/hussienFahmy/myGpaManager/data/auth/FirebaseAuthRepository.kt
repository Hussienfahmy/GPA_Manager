package com.hussienfahmy.myGpaManager.data.auth

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.hussienfahmy.core.domain.auth.repository.AuthRepository
import com.hussienfahmy.core.domain.auth.repository.AuthResult
import com.hussienfahmy.core.domain.auth.repository.AuthUserData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.tasks.await
import java.util.concurrent.CancellationException

class FirebaseAuthRepository(
    private val auth: FirebaseAuth,
    scope: CoroutineScope
) : AuthRepository {

    private val _userId = MutableStateFlow<String?>(null)
    override val userId: Flow<String?> = _userId
        .onStart {
            auth.addAuthStateListener(firebaseAuthStateListener)
        }.onCompletion {
            auth.removeAuthStateListener(firebaseAuthStateListener)
        }.stateIn(
            scope = scope,
            started = SharingStarted.Eagerly,
            initialValue = null
        )

    override val isSignedInFlow = userId.map { it != null }
        .stateIn(
            scope = scope,
            started = SharingStarted.Eagerly,
            initialValue = false
        )


    private val firebaseAuthStateListener = FirebaseAuth.AuthStateListener { firebaseAuth ->
        val userId = firebaseAuth.currentUser?.uid
        _userId.value = userId
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
        while (auth.currentUser?.uid != null) {
            delay(100)
        }
        _userId.value = null
    }
}