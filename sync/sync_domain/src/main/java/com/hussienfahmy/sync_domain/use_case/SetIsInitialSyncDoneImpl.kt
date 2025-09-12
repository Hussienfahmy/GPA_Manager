package com.hussienfahmy.sync_domain.use_case

import com.hussienfahmy.core.domain.sync.SetIsInitialSyncDone
import com.hussienfahmy.sync_domain.repository.AppDataRepository

class SetIsInitialSyncDoneImpl(
    private val appDataRepository: AppDataRepository,
) : SetIsInitialSyncDone {
    override suspend operator fun invoke(isInitialSyncDone: Boolean) {
        appDataRepository.setInitialSyncDone(isInitialSyncDone)
    }
}