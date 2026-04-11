package com.hussienfahmy.semester_history_domain.use_case

import com.hussienfahmy.core.data.local.SubjectDao
import com.hussienfahmy.core.data.local.entity.Subject
import com.hussienfahmy.core.data.local.model.GradeName

class AddSubjectToSemester(private val subjectDao: SubjectDao) {
    suspend operator fun invoke(
        semesterId: Long,
        name: String,
        creditHours: Double,
        gradeName: GradeName?
    ) {
        subjectDao.upsert(
            Subject(
                name = name,
                creditHours = creditHours,
                gradeName = gradeName,
                semesterId = semesterId,
            )
        )
    }
}
