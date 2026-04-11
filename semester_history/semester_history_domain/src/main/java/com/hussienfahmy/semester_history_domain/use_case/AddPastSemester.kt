package com.hussienfahmy.semester_history_domain.use_case

import com.hussienfahmy.core.data.local.SemesterDao
import com.hussienfahmy.core.data.local.entity.Semester

class AddPastSemester(
    private val semesterDao: SemesterDao,
) {
    sealed class Request {
        data class Summary(
            val label: String,
            val semesterGPA: Double,
            val totalCreditHours: Int,
            val level: Int,
        ) : Request()

        data class Detailed(
            val label: String,
            val level: Int,
        ) : Request()
    }

    /**
     * Returns the created semester ID (used by Detailed flow to navigate to subject entry).
     */
    suspend operator fun invoke(request: Request): Long {
        val nextOrder = (semesterDao.getMaxOrder() ?: 0) + 1

        return when (request) {
            is Request.Summary -> {
                val semester = Semester(
                    label = request.label,
                    level = request.level,
                    type = Semester.Type.SUMMARY,
                    semesterGPA = request.semesterGPA,
                    totalCreditHours = request.totalCreditHours,
                    status = Semester.Status.ARCHIVED,
                    order = nextOrder,
                    archivedAt = System.currentTimeMillis(),
                )
                semesterDao.insert(semester)
            }

            is Request.Detailed -> {
                val semester = Semester(
                    label = request.label,
                    level = request.level,
                    type = Semester.Type.DETAILED,
                    semesterGPA = 0.0,
                    totalCreditHours = 0,
                    status = Semester.Status.ARCHIVED,
                    order = nextOrder,
                    archivedAt = System.currentTimeMillis(),
                )
                semesterDao.insert(semester)
            }
        }
    }
}
