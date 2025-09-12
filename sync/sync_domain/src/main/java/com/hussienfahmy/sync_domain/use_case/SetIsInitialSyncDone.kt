package com.hussienfahmy.sync_domain.use_case

import com.hussienfahmy.sync_domain.repository.AppDataRepository

class SetIsInitialSyncDone(
    private val appDataRepository: AppDataRepository,
) {
    suspend operator fun invoke(isFirstTimeInstall: Boolean) {
        appDataRepository.setInitialSyncDone(isFirstTimeInstall)
    }
}