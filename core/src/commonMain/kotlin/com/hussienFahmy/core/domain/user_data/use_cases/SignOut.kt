package com.hussienfahmy.core.domain.user_data.use_cases

import com.hussienfahmy.core.data.local.SemesterDao
import com.hussienfahmy.core.data.local.SubjectDao
import com.hussienfahmy.core.domain.auth.repository.AuthRepository
import com.hussienfahmy.core.domain.sync.SyncUpload

class SignOut(
    private val authRepository: AuthRepository,
    private val subjectDao: SubjectDao,
    private val semesterDao: SemesterDao,
    private val syncUpload: SyncUpload,
) {
    suspend operator fun invoke() {
        syncUpload()
        authRepository.signOut()
        subjectDao.deleteAll()
        semesterDao.deleteAll()
    }
}
