package com.hussienfahmy.semester_marks_domain.use_case

import com.hussienfahmy.core.data.local.SubjectDao

class ResetMarks(
    private val subjectDao: SubjectDao,
) {
    suspend operator fun invoke(subjectId: Long) {
        subjectDao.updateMidterm(subjectId, null)
        subjectDao.updatePractical(subjectId, null)
        subjectDao.updateOral(subjectId, null)
    }
}