package com.hussienfahmy.sync_domain.use_case

import com.hussienfahmy.core.domain.sync.SyncDownload

class SyncDownloadImpl(
    private val pullSubjects: PullSubjects,
    private val pullSettings: PullSettings,
    private val pullSemesters: PullSemesters,
    private val migrateExistingUserDataIfNeeded: MigrateExistingUserDataIfNeeded,
) : SyncDownload {
    override suspend operator fun invoke(userId: String) {
        pullSubjects(userId)
        pullSettings(userId)
        pullSemesters(userId)

        migrateExistingUserDataIfNeeded(userId)
    }
}