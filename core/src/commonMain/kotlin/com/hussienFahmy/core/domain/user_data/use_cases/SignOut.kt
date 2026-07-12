package com.hussienfahmy.core.domain.user_data.use_cases

import com.hussienfahmy.core.data.local.SemesterDao
import com.hussienfahmy.core.data.local.SubjectDao
import com.hussienfahmy.core.domain.auth.service.AuthService
import com.hussienfahmy.core.domain.sync.SyncUpload

class SignOut(
    private val authService: AuthService,
    private val subjectDao: SubjectDao,
    private val semesterDao: SemesterDao,
    private val syncUpload: SyncUpload,
) {
    suspend operator fun invoke() {
        syncUpload()
        authService.signOut()
        subjectDao.deleteAll()
        semesterDao.deleteAll()
    }
}
