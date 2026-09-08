package com.hussienfahmy.core.domain.user_data.use_cases

import com.hussienfahmy.core.data.local.SemesterDao
import com.hussienfahmy.core.data.local.SubjectDao
import com.hussienfahmy.core.domain.auth.repository.AuthRepository
import com.hussienfahmy.core.domain.user_data.repository.RemoteUserDataEraser

/**
 * Permanently deletes the current user's account and every trace of their data. Mirrors
 * [SignOut] but is irreversible: the Firebase Auth user is destroyed, not just detached.
 *
 * Remote wipe is best-effort (guests have nothing remote at all); the auth-account deletion is
 * the step that must succeed, so it is the only one allowed to throw.
 */
class DeleteAccount(
    private val clearFcmToken: ClearFcmToken,
    private val authRepository: AuthRepository,
    private val remoteUserDataEraser: RemoteUserDataEraser,
    private val subjectDao: SubjectDao,
    private val semesterDao: SemesterDao,
) {
    suspend operator fun invoke() {
        val userId = authRepository.userId.value ?: return
        val isAnonymous = authRepository.isAnonymousFlow.value == true

        if (!isAnonymous) {
            runCatching { clearFcmToken() }
            remoteUserDataEraser.erase(userId)
        }

        authRepository.deleteCurrentUser()

        subjectDao.deleteAll()
        semesterDao.deleteAll()
    }
}
