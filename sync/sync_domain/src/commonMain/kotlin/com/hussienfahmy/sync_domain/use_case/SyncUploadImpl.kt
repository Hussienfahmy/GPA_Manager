package com.hussienfahmy.sync_domain.use_case

import com.hussienfahmy.core.domain.auth.repository.AuthRepository
import com.hussienfahmy.core.domain.sync.SyncDirtyTracker
import com.hussienfahmy.core.domain.sync.SyncUpload
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.firstOrNull

class SyncUploadImpl(
    private val pushSubjects: PushSubjects,
    private val pushSettings: PushSettings,
    private val pushSemesters: PushSemesters,
    private val authRepository: AuthRepository,
    private val dirtyTracker: SyncDirtyTracker,
) : SyncUpload {
    override suspend operator fun invoke() {
        // Guest (anonymous) sessions are local-only - nothing is pushed to Firebase.
        if (authRepository.isAnonymousFlow.value == true) return

        val userId = authRepository.userId.filterNotNull().firstOrNull()
            ?: throw IllegalStateException("No user id found")

        if (dirtyTracker.consumeSubjectsChanged()) pushSubjects(userId)
        if (dirtyTracker.consumeSettingsChanged()) pushSettings(userId)
        if (dirtyTracker.consumeSemestersChanged()) pushSemesters(userId)
    }
}
