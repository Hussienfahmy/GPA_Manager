package com.hussienfahmy.sync_domain.use_case

import com.hussienfahmy.sync_domain.repository.AppDataRepository

class GetIsInitialSyncDone(
    private val appDataRepository: AppDataRepository,
) {
    suspend operator fun invoke(): Boolean {
        return appDataRepository.isInitialSyncDone()
    }
}