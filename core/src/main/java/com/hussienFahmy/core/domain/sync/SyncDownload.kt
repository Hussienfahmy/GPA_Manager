package com.hussienfahmy.core.domain.sync

interface SyncDownload {
    suspend operator fun invoke(userId: String)
}