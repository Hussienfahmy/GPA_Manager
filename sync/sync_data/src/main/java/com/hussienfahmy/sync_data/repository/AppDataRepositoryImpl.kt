package com.hussienfahmy.sync_data.repository

import com.hussienfahmy.sync_data.datastore.AppDatastore
import com.hussienfahmy.sync_domain.repository.AppDataRepository

class AppDataRepositoryImpl(
    private val appDatastore: AppDatastore,
) : AppDataRepository {

    override suspend fun isFirstTimeInstall(): Boolean {
        return appDatastore.isFirstTimeInstall()
    }

    override suspend fun setFirstTimeInstall(isFirstTimeInstall: Boolean) {
        appDatastore.setFirstTimeInstall(isFirstTimeInstall)
    }

}