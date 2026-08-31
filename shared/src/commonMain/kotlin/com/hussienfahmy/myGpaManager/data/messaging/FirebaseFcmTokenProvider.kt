package com.hussienfahmy.myGpaManager.data.messaging

import com.hussienfahmy.core.domain.messaging.FcmTokenProvider
import dev.gitlive.firebase.messaging.FirebaseMessaging

class FirebaseFcmTokenProvider(
    private val messaging: FirebaseMessaging,
) : FcmTokenProvider {

    // getToken() throws when the token can't be produced (no network, no APNs token yet); callers
    // treat null as "skip for now" and the platform callback will push it once it's ready.
    override suspend fun currentToken(): String? =
        runCatching { messaging.getToken() }.getOrNull()

    override suspend fun deleteToken() {
        runCatching { messaging.deleteToken() }
    }
}
