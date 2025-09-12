package com.hussienfahmy.sync_domain.use_case

import com.hussienfahmy.core.domain.sync.SyncDownload

class SyncDownloadImpl(
    private val pullSubjects: PullSubjects,
    private val pullSettings: PullSettings,
    private val setIsInitialSyncDone: SetIsInitialSyncDone,
) : SyncDownload {
    override suspend operator fun invoke() {
        pullSubjects()
        pullSettings()
        setIsInitialSyncDone(true)
    }
}