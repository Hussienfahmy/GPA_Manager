package com.hussienfahmy.core.domain.user_data.repository

/**
 * Best-effort removal of every server-side document and blob belonging to a user, run just
 * before the auth account itself is deleted. Implementations must never throw.
 */
interface RemoteUserDataEraser {
    suspend fun erase(userId: String)
}
