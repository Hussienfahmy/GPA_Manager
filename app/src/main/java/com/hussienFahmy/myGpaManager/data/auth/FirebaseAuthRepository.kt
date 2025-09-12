package com.hussienfahmy.myGpaManager.data.auth

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.hussienfahmy.core.domain.auth.repository.AuthRepository
import com.hussienfahmy.core.domain.auth.repository.AuthResult
import com.hussienfahmy.core.domain.auth.repository.AuthUserData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.util.concurrent.CancellationException

class FirebaseAuthRepository(
    private val auth: FirebaseAuth,
    private val scope: CoroutineScope
) : AuthRepository {

    private val _userId = MutableSharedFlow<String>(replay = 1)
    override val userId: Flow<String> = _userId
        .onStart {
            auth.addAuthStateListener(firebaseAuthStateListener)
        }.onCompletion {
            auth.removeAuthStateListener(firebaseAuthStateListener)
        }.filter { it.isNotBlank() }

    private val _isSignedInFlow = MutableStateFlow<Boolean?>(null)
    override val isSignedInFlow = _isSignedInFlow.asStateFlow()


    private val firebaseAuthStateListener = FirebaseAuth.AuthStateListener { firebaseAuth ->
        val userId = firebaseAuth.currentUser?.uid
        _isSignedInFlow.value = userId != null
        if (userId != null) {
            scope.launch {
                _userId.emit(userId)
            }
        }
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
}