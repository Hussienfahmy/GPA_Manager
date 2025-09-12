package com.hussienfahmy.sync_data.repository

import com.hussienfahmy.sync_data.datastore.AppDatastore
import com.hussienfahmy.sync_domain.repository.AppDataRepository

class AppDataRepositoryImpl(
    private val appDatastore: AppDatastore,
) : AppDataRepository {

    override suspend fun isInitialSyncDone(): Boolean {
        return appDatastore.isInitialSyncDone()
    }

    override suspend fun setInitialSyncDone(isFirstTimeInstall: Boolean) {
        appDatastore.setInitialSyncDone(isFirstTimeInstall)
    }

}