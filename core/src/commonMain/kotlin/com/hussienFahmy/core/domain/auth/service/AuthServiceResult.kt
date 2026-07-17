package com.hussienfahmy.core.domain.auth.service

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
