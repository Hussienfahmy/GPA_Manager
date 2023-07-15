package com.hussienFahmy.core.domain.grades.use_case

import com.hussienFahmy.core.data.local.GradeDao

class GetActiveGradeNames(
    private val gradeDao: GradeDao,
) {
    suspend operator fun invoke() = gradeDao.getActiveGrades()
        .map { it.metaData }
}