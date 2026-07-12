package com.hussienfahmy.core.domain.grades.use_case

import com.hussienfahmy.core.data.local.GradeDao

class GetGradeByPoints(
    private val gradeDao: GradeDao,
) {
    suspend operator fun invoke(points: Double) = gradeDao.getGradeByPoints(points)
}