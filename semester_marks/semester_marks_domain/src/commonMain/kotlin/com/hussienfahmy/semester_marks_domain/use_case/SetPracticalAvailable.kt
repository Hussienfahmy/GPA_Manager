package com.hussienfahmy.semester_marks_domain.use_case

import com.hussienfahmy.core.data.local.SubjectDao
import com.hussienfahmy.core.domain.sync.SyncDirtyTracker

class SetPracticalAvailable(
    private val subjectDao: SubjectDao,
    private val dirtyTracker: SyncDirtyTracker,
) {
    suspend operator fun invoke(subjectId: Long, isAvailable: Boolean) {
        subjectDao.setPracticalAvailability(subjectId, isAvailable)
        dirtyTracker.markSubjectsChanged()
    }
}