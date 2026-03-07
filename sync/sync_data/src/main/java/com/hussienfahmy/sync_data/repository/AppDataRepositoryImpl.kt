package com.hussienfahmy.sync_data.repository

import android.util.Log
import com.hussienfahmy.sync_data.datastore.AppDatastore
import com.hussienfahmy.sync_domain.repository.AppDataRepository

class AppDataRepositoryImpl(
    private val appDatastore: AppDatastore,
) : AppDataRepository {

    override suspend fun isInitialSyncDone(): Boolean {
        return appDatastore.isInitialSyncDone()
    }

    override suspend fun setInitialSyncDone(isFirstTimeInstall: Boolean) {
        Log.d("DEBUG_TAG", "setInitialSyncDone: setting to ${isFirstTimeInstall}")
        appDatastore.setInitialSyncDone(isFirstTimeInstall)
        Log.d("DEBUG_TAG", "setInitialSyncDone: new value = ${isInitialSyncDone()}")
    }

}