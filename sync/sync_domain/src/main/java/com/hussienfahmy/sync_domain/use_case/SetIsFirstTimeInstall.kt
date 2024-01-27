package com.hussienfahmy.sync_domain.use_case

import com.hussienfahmy.sync_domain.repository.AppDataRepository

class SetIsFirstTimeInstall(
    private val appDataRepository: AppDataRepository,
) {
    suspend operator fun invoke(isFirstTimeInstall: Boolean) {
        appDataRepository.setFirstTimeInstall(isFirstTimeInstall)
    }
}