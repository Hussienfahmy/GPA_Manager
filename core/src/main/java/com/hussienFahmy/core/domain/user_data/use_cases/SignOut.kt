package com.hussienfahmy.core.domain.user_data.use_cases

import com.hussienfahmy.core.data.local.SubjectDao
import com.hussienfahmy.core.domain.auth.service.AuthService
import com.hussienfahmy.core.domain.sync.SetIsInitialSyncDone
import com.hussienfahmy.core.domain.sync.SyncUpload

class SignOut(
    private val authService: AuthService,
    private val subjectDao: SubjectDao,
    private val syncUpload: SyncUpload,
    private val setIsInitialSyncDone: SetIsInitialSyncDone,
) {
    suspend operator fun invoke() {
        syncUpload()
        authService.signOut()
        subjectDao.deleteAll()
        setIsInitialSyncDone(false)
    }
}