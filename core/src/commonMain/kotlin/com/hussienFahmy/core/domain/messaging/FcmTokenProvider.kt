package com.hussienfahmy.core.domain.messaging

/**
 * On-demand access to this device's FCM registration token, backed by the push SDK
 * ([dev.gitlive.firebase.messaging.FirebaseMessaging]). Unlike the platform token callbacks -
 * which only fire on token rotation and usually before sign-in - [currentToken] fetches the token
 * when it's actually needed (right after sign-in).
 */
interface FcmTokenProvider {
    /** The current token, fetching it if necessary. Null if it can't be obtained (e.g. no APNs
     *  token yet on iOS, Play Services unavailable on Android). */
    suspend fun currentToken(): String?

    /** Deletes the token so a fresh one is issued on the next request - used at sign-out so a
     *  later account on this device doesn't inherit push delivery. */
    suspend fun deleteToken()
}
