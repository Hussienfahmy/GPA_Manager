package com.hussienfahmy.semester_history_domain.use_case

import com.hussienfahmy.core.data.local.SemesterDao
import com.hussienfahmy.core.data.local.SubjectDao
import com.hussienfahmy.core.domain.sync.SemesterDirtyTracker
import kotlinx.coroutines.flow.first

class EditSemester(
    private val semesterDao: SemesterDao,
    private val subjectDao: SubjectDao,
    private val calculateSemesterGPA: CalculateSemesterGPA,
    private val dirtyTracker: SemesterDirtyTracker,
) {
    sealed class Request {
        data class SummaryFields(
            val semesterId: Long,
            val label: String,
            val semesterGPA: Double,
            val totalCreditHours: Int,
        ) : Request()

        data class Label(
            val semesterId: Long,
            val label: String,
        ) : Request()

        /**
         * Recalculates a DETAILED semester's GPA from its linked subjects.
         * Called after adding/removing/changing grades of subjects in a past semester.
         */
        data class RecalculateDetailed(val semesterId: Long) : Request()
    }

    suspend operator fun invoke(request: Request) {
        when (request) {
            is Request.SummaryFields -> {
                val existing = semesterDao.getById(request.semesterId) ?: return
                semesterDao.update(
                    existing.copy(
                        label = request.label,
                        semesterGPA = request.semesterGPA,
                        totalCreditHours = request.totalCreditHours,
                    )
                )
                dirtyTracker.markChanged()
            }

            is Request.Label -> {
                val existing = semesterDao.getById(request.semesterId) ?: return
                semesterDao.update(existing.copy(label = request.label))
                dirtyTracker.markChanged()
            }

            is Request.RecalculateDetailed -> {
                val existing = semesterDao.getById(request.semesterId) ?: return
                val subjects = subjectDao.getSubjectsBySemesterId(request.semesterId).first()
                val semesterGPA = calculateSemesterGPA(subjects)
                val totalCreditHours = subjects.sumOf { it.creditHours }.toInt()
                semesterDao.update(
                    existing.copy(
                        semesterGPA = semesterGPA,
                        totalCreditHours = totalCreditHours,
                    )
                )
                dirtyTracker.markChanged()
            }
        }
    }
}
