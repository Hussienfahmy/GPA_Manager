package com.hussienfahmy.sync_domain.repository

interface AppDataRepository {

    suspend fun isFirstTimeInstall(): Boolean

    suspend fun setFirstTimeInstall(isFirstTimeInstall: Boolean)
}