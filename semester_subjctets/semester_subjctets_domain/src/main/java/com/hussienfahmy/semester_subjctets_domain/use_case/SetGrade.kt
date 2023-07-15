package com.hussienfahmy.semester_subjctets_domain.use_case

import com.hussienFahmy.core.data.local.SubjectDao
import com.hussienFahmy.core.data.local.model.GradeName

class SetGrade(
    private val subjectDao: SubjectDao,
) {
    suspend operator fun invoke(id: Long, grade: GradeName) {
        subjectDao.updateGrade(id, grade)
    }
}