package com.hussienfahmy.semester_subjctets_domain.use_case

import com.hussienfahmy.core.data.local.SubjectDao
import com.hussienfahmy.core.domain.sync.SyncDirtyTracker

class DeleteSubject(
    private val subjectDao: SubjectDao,
    private val dirtyTracker: SyncDirtyTracker,
) {
    suspend operator fun invoke(subjectId: Long) {
        subjectDao.delete(subjectId)
        dirtyTracker.markSubjectsChanged()
    }
}