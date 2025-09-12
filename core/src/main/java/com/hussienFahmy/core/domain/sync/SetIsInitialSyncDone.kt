package com.hussienfahmy.core.domain.sync

interface SetIsInitialSyncDone {
    suspend operator fun invoke(isInitialSyncDone: Boolean)
}