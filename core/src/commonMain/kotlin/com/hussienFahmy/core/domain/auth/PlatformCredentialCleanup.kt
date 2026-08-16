package com.hussienfahmy.core.domain.auth

import com.hussienfahmy.core.util.PlatformContext

/** Clears any OS-level ambient sign-in state left over after Firebase sign-out. */
expect class PlatformCredentialCleanup(context: PlatformContext) {
    suspend fun clear()
}
