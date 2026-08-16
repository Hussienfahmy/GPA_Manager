package com.hussienfahmy.semester_subjctets_domain.use_case

import com.hussienfahmy.core.data.local.SubjectDao
import com.hussienfahmy.core.domain.sync.SyncDirtyTracker

class UpdateName(
    private val subjectDao: SubjectDao,
    private val dirtyTracker: SyncDirtyTracker,
) {
    suspend operator fun invoke(id: Long, name: String) {
        subjectDao.updateName(id, name)
        dirtyTracker.markSubjectsChanged()
    }
}