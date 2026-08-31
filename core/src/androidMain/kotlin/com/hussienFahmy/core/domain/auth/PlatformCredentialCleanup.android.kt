package com.hussienfahmy.core.domain.auth

import androidx.credentials.ClearCredentialStateRequest
import androidx.credentials.CredentialManager
import com.hussienfahmy.core.util.PlatformContext

actual class PlatformCredentialCleanup actual constructor(context: PlatformContext) {
    private val credentialManager = CredentialManager.create(context)

    actual suspend fun clear() {
        credentialManager.clearCredentialState(ClearCredentialStateRequest())
    }
}
