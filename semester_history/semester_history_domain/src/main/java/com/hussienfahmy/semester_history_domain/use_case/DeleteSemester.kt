package com.hussienfahmy.semester_history_domain.use_case

import com.hussienfahmy.core.data.local.SemesterDao
import com.hussienfahmy.core.data.local.SubjectDao
import com.hussienfahmy.core.data.local.entity.Semester
import com.hussienfahmy.core.domain.sync.SemesterDirtyTracker
import kotlinx.coroutines.flow.first

class DeleteSemester(
    private val semesterDao: SemesterDao,
    private val subjectDao: SubjectDao,
    private val dirtyTracker: SemesterDirtyTracker,
) {
    suspend operator fun invoke(semesterId: Long) {
        val semester = semesterDao.getById(semesterId) ?: return

        if (semester.type == Semester.Type.DETAILED) {
            val subjects = subjectDao.getSubjectsBySemesterId(semesterId).first()
            subjects.forEach { subjectDao.delete(it.id) }
        }

        semesterDao.delete(semesterId)
        dirtyTracker.markChanged()
    }
}
