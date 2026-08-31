package com.hussienfahmy.semester_subjctets_domain.use_case

import com.hussienfahmy.core.data.local.SubjectDao
import com.hussienfahmy.core.domain.sync.SyncDirtyTracker

class ClearGrade(
    private val subjectDao: SubjectDao,
    private val dirtyTracker: SyncDirtyTracker,
) {
    suspend operator fun invoke(request: Request) {
        when (request) {
            is Request.All -> subjectDao.clearAllGrades()
            is Request.ById -> subjectDao.clearGrade(request.id)
        }
        dirtyTracker.markSubjectsChanged()
    }

    sealed class Request {
        object All : Request()
        data class ById(val id: Long) : Request()
    }
}