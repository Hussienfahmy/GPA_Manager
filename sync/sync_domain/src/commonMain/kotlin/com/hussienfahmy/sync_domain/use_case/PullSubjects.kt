package com.hussienfahmy.sync_domain.use_case

import com.hussienfahmy.core.data.local.SubjectDao
import com.hussienfahmy.sync_domain.repository.SyncRepository

class PullSubjects(
    private val repository: SyncRepository,
    private val subjectDao: SubjectDao,
) {
    suspend operator fun invoke(userId: String) {
        val networkSubjects = repository.downloadSubjects(userId) ?: return

        networkSubjects.subjects.forEach {
            val entity = it.toEntity()
            subjectDao.upsert(entity)
        }
    }
}
