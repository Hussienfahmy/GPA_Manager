package com.hussienfahmy.sync_domain.use_case

import com.hussienfahmy.core.domain.auth.repository.AuthRepository
import com.hussienfahmy.core.domain.sync.SyncDownload

class SyncDownloadImpl(
    private val pullSubjects: PullSubjects,
    private val pullSettings: PullSettings,
    private val pullSemesters: PullSemesters,
    private val migrateExistingUserDataIfNeeded: MigrateExistingUserDataIfNeeded,
    private val authRepository: AuthRepository,
) : SyncDownload {
    override suspend operator fun invoke(userId: String) {
        // Guest (anonymous) sessions are local-only - nothing is pulled from Firebase.
        if (authRepository.isAnonymousFlow.value == true) return

        pullSubjects(userId)
        pullSettings(userId)
        pullSemesters(userId)

        migrateExistingUserDataIfNeeded(userId)
    }
}