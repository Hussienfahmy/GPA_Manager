package com.hussienfahmy.sync_domain.repository

interface AppDataRepository {

    suspend fun isInitialSyncDone(): Boolean

    suspend fun setInitialSyncDone(isFirstTimeInstall: Boolean)
}