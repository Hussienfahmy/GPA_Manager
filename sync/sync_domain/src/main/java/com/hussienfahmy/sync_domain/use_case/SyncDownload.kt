package com.hussienfahmy.sync_domain.use_case

class SyncDownload(
    private val pullSubjects: PullSubjects,
    private val pullSettings: PullSettings,
    private val setIsInitialSyncDone: SetIsInitialSyncDone,
) {
    suspend operator fun invoke() {
        pullSubjects()
        pullSettings()
        setIsInitialSyncDone(true)
    }
}