package com.hussienFahmy.core.domain.grades.use_case

import com.hussienFahmy.core.data.local.GradeDao

class GetActiveGrades(
    private val gradeDao: GradeDao,
) {
    operator fun invoke() = gradeDao.getActiveGrades()
}