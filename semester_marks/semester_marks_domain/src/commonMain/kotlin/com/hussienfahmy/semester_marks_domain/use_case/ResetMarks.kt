package com.hussienfahmy.semester_marks_domain.use_case

import com.hussienfahmy.core.data.local.SubjectDao
import com.hussienfahmy.core.domain.sync.SyncDirtyTracker

class ResetMarks(
    private val subjectDao: SubjectDao,
    private val dirtyTracker: SyncDirtyTracker,
) {
    suspend operator fun invoke(subjectId: Long) {
        subjectDao.updateMidterm(subjectId, null)
        subjectDao.updatePractical(subjectId, null)
        subjectDao.updateOral(subjectId, null)
        dirtyTracker.markSubjectsChanged()
    }
}