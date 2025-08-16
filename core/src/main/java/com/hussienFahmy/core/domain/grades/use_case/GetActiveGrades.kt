package com.hussienfahmy.core.domain.grades.use_case

import com.hussienfahmy.core.data.local.GradeDao

class GetActiveGrades(
    private val gradeDao: GradeDao,
) {
    operator fun invoke() = gradeDao.getActiveGrades()
}