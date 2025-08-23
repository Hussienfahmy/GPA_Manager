package com.hussienfahmy.core.domain.auth.service

import kotlinx.coroutines.flow.StateFlow

interface AuthService {
    val isSignedInFlow: StateFlow<Boolean?>

    suspend fun signIn(): AuthServiceResult?
    suspend fun signOut()
}

sealed interface AuthServiceResult {
    data class Success(val userData: AuthServiceUserData) : AuthServiceResult
    data class Error(val message: String) : AuthServiceResult
}

data class AuthServiceUserData(
    val id: String,
    val name: String,
    val photoUrl: String,
    val email: String,
)