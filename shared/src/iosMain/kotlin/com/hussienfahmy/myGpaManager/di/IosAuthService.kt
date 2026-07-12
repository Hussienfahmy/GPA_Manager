package com.hussienfahmy.myGpaManager.di

import com.hussienfahmy.core.domain.auth.repository.AuthRepository
import com.hussienfahmy.core.domain.auth.service.AuthService
import kotlinx.coroutines.flow.StateFlow

// iOS has no CredentialManager-based sign-in flow (that's Android-only, wrapped by
// GoogleAuthService in :app) - AuthRepository already carries isSignedInFlow/signOut, so this is
// a thin pass-through. Sign in with Apple itself (AppleSignIn.kt in :core) is invoked directly
// from OnBoardingScreen.ios.kt, not through this AuthService binding - it only needs to satisfy
// MainViewModel/SignOut's dependency on isSignedInFlow/signOut().
class IosAuthService(private val authRepository: AuthRepository) : AuthService {
    override val isSignedInFlow: StateFlow<Boolean?> = authRepository.isSignedInFlow

    override suspend fun signOut() = authRepository.signOut()
}
