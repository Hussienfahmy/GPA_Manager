package com.hussienFahmy.core.domain.grades.use_case

import com.hussienFahmy.core.data.local.GradeDao
import kotlinx.coroutines.flow.map

class GetActiveGradeNames(
    private val gradeDao: GradeDao,
) {
    operator fun invoke() = gradeDao.getActiveGrades()
        .map {
            it.map { it.name }
        }
}