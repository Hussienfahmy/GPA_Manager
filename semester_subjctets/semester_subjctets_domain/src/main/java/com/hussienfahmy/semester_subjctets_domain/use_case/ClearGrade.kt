package com.hussienfahmy.semester_subjctets_domain.use_case

import com.hussienFahmy.core.data.local.SubjectDao

class ClearGrade(
    private val subjectDao: SubjectDao,
) {
    suspend operator fun invoke(request: Request) {
        when (request) {
            is Request.All -> subjectDao.clearAllGrades()
            is Request.ById -> subjectDao.clearGrade(request.id)
        }
    }

    sealed class Request {
        object All : Request()
        data class ById(val id: Long) : Request()
    }
}