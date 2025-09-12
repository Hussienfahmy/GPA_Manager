package com.hussienfahmy.core.domain.sync

interface SyncUpload {
    suspend operator fun invoke()
}