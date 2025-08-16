package com.hussienfahmy.grades_setting_domain.use_case

import com.hussienfahmy.core.data.local.GradeDao
import com.hussienfahmy.core.data.local.model.GradeName

class GetGradeByName(
    private val gradeDao: GradeDao,
) {
    suspend operator fun invoke(gradeName: GradeName) = gradeDao.getGradeByName(gradeName)
}
