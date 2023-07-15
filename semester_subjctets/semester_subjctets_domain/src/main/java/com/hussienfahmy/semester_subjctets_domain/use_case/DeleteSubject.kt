package com.hussienfahmy.semester_subjctets_domain.use_case

import com.hussienFahmy.core.data.local.SubjectDao

class DeleteSubject(
    private val subjectDao: SubjectDao,
) {
    suspend operator fun invoke(subjectId: Long) {
        subjectDao.delete(subjectId)
    }
}