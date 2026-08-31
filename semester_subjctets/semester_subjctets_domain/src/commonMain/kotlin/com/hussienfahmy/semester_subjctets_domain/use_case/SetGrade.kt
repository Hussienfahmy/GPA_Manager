package com.hussienfahmy.semester_subjctets_domain.use_case

import com.hussienfahmy.core.data.local.SubjectDao
import com.hussienfahmy.core.data.local.model.GradeName
import com.hussienfahmy.core.domain.sync.SyncDirtyTracker

class SetGrade(
    private val subjectDao: SubjectDao,
    private val dirtyTracker: SyncDirtyTracker,
) {
    suspend operator fun invoke(id: Long, grade: GradeName?) {
        subjectDao.updateGrade(id, grade)
        dirtyTracker.markSubjectsChanged()
    }
}