package com.hussienfahmy.core.domain.user_data.use_cases

import com.hussienfahmy.core.domain.messaging.FcmTokenProvider
import com.hussienfahmy.core.domain.user_data.repository.UserDataRepository

/**
 * Blanks the FCM token on the user's record and rotates the device token. Call before auth
 * sign-out, while the user doc is still writable, so the backend stops pushing to this device and
 * a later account here starts from a fresh token.
 */
class ClearFcmToken(
    private val tokenProvider: FcmTokenProvider,
    private val repository: UserDataRepository,
) {
    suspend operator fun invoke() {
        runCatching { repository.updateFCMToken("") }
        tokenProvider.deleteToken()
    }
}
