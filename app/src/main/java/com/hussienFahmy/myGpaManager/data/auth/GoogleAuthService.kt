package com.hussienfahmy.myGpaManager.data.auth

import android.app.Activity
import com.hussienfahmy.core.domain.auth.service.AuthService
import com.hussienfahmy.core.domain.auth.service.AuthServiceResult
import com.hussienfahmy.core.domain.auth.service.AuthServiceUserData
import kotlinx.coroutines.flow.StateFlow

class GoogleAuthService(
    private val googleAuthUiClient: GoogleAuthUiClient
) : AuthService {

    override val isSignedInFlow: StateFlow<Boolean?> = googleAuthUiClient.isSignedInFlow

    override suspend fun signIn(activity: Activity): AuthServiceResult? {
        return when (val result = googleAuthUiClient.signIn(activity)) {
            is SignInResult.Success -> AuthServiceResult.Success(
                AuthServiceUserData(
                    id = result.userData.id,
                    name = result.userData.name,
                    photoUrl = result.userData.photoUrl,
                    email = result.userData.email
                )
            )

            is SignInResult.Error -> AuthServiceResult.Error(result.message)
            null -> null
        }
    }

    override suspend fun signOut() {
        googleAuthUiClient.signOut()
    }
}