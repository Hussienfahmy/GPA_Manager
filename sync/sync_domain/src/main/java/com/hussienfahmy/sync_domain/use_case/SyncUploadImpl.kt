package com.hussienfahmy.sync_domain.use_case

import com.hussienfahmy.core.domain.auth.repository.AuthRepository
import com.hussienfahmy.core.domain.sync.SyncUpload
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.firstOrNull

class SyncUploadImpl(
    private val pushSubjects: PushSubjects,
    private val pushSettings: PushSettings,
    private val pushSemesters: PushSemesters,
    private val authRepository: AuthRepository
) : SyncUpload {
    override suspend operator fun invoke() {
        val userId = authRepository.userId.filterNotNull().firstOrNull()
            ?: throw IllegalStateException("No user id found")

        pushSubjects(userId)
        pushSettings(userId)
        pushSemesters(userId)
    }
}
