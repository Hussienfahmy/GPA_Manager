package com.hussienfahmy.core.domain.auth.repository

import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    val userId: Flow<String>
    val isSignedInFlow: Flow<Boolean?>

    suspend fun signInWithCredential(idToken: String): AuthResult
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