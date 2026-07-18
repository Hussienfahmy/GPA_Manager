package com.hussienfahmy.grades_setting_domain.use_case

import com.hussienfahmy.core.data.local.GradeDao
import com.hussienfahmy.core.data.local.entity.Grade
import com.hussienfahmy.grades_setting_domain.model.GradeSetting
import kotlinx.coroutines.flow.map

class LoadGrades(
    private val gradeDao: GradeDao,
) {
    operator fun invoke() = gradeDao.grades.map { list ->
        list.sortedWith(Grade.Companion.Comparator)
            .map { GradeSetting(it) }
    }
}