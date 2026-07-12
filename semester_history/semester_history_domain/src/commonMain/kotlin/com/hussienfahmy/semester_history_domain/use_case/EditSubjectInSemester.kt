package com.hussienfahmy.semester_history_domain.use_case

import com.hussienfahmy.core.data.local.SubjectDao
import com.hussienfahmy.core.data.local.entity.Subject
import com.hussienfahmy.core.data.local.model.GradeName

class EditSubjectInSemester(private val subjectDao: SubjectDao) {
    suspend operator fun invoke(
        subject: Subject,
        name: String,
        creditHours: Double,
        gradeName: GradeName,
        totalMarks: Double = 0.0,
        semesterMarks: Subject.SemesterMarks? = null,
        metadata: Subject.MetaData = Subject.MetaData(),
    ) {
        subjectDao.upsert(
            subject.copy(
                name = name,
                creditHours = creditHours,
                gradeName = gradeName,
                totalMarks = totalMarks,
                semesterMarks = semesterMarks,
                metadata = metadata,
            )
        )
    }
}
