package com.hussienfahmy.core.domain.user_data.use_cases

import com.hussienfahmy.core.domain.messaging.FcmTokenProvider
import com.hussienfahmy.core.domain.user_data.repository.UserDataRepository

/**
 * Fetches the current device FCM token and writes it onto the signed-in user's record. Call right
 * after sign-in: the platform token callback almost always fires at app launch, before sign-in,
 * when the write is a no-op.
 */
class RefreshFcmToken(
    private val tokenProvider: FcmTokenProvider,
    private val repository: UserDataRepository,
) {
    suspend operator fun invoke() {
        val token = tokenProvider.currentToken() ?: return
        repository.updateFCMToken(token)
    }
}
