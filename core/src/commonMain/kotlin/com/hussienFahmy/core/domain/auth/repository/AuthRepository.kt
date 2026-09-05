package com.hussienfahmy.core.domain.auth.repository

import kotlinx.coroutines.flow.StateFlow

interface AuthRepository {
    val userId: StateFlow<String?>
    val isSignedInFlow: StateFlow<Boolean?>
    val isAnonymousFlow: StateFlow<Boolean?>

    suspend fun signInWithCredential(idToken: String): AuthResult

    suspend fun signInAnonymously(): AuthResult

    suspend fun signOut()
}

sealed interface AuthResult {
    data class Success(val userData: AuthUserData) : AuthResult
    data class Error(val message: String) : AuthResult
}

data class AuthUserData(
    val id: String,
    val name: String,
    val photoUrl: String,
    val email: String,
)
