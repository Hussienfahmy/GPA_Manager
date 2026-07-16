package com.hussienfahmy.semester_marks_domain.use_case

import com.hussienfahmy.core.data.local.SubjectDao
import com.hussienfahmy.core.domain.analytics.AnalyticsLogger
import com.hussienfahmy.semester_marks_domain.model.GradeSyncResult
import kotlinx.coroutines.flow.first

class SyncGradeWithMarks(
    private val subjectDao: SubjectDao,
    private val analyticsLogger: AnalyticsLogger
) {
    suspend operator fun invoke() {
        checkAllSubjects()
            .filterIsInstance<GradeSyncResult.DowngradeRequired>()
            .also {
                analyticsLogger.logGradeSynced(it.size)
            }
            .forEach { result ->
                performSync(result)
            }
    }

    fun checkSubject(subjectWithGrade: SubjectDao.SubjectWithGrades): GradeSyncResult {
        return try {
            val assignedGrade = subjectWithGrade.assignedGrade
            val maxAchievableGrade = subjectWithGrade.maxGradeCanBeAssigned

            // Only check if user has assigned a grade and we have marks entered
            if (assignedGrade == null || subjectWithGrade.subject.semesterMarks == null) {
                return GradeSyncResult.NoChangeNeeded
            }

            // Check if assigned grade is higher than what's achievable (needs downgrade)
            if (assignedGrade.percentage!! > maxAchievableGrade.percentage!!) {
                GradeSyncResult.DowngradeRequired(
                    subjectId = subjectWithGrade.subject.id,
                    fromGrade = assignedGrade,
                    toGrade = maxAchievableGrade
                )
            } else {
                GradeSyncResult.NoChangeNeeded
            }
        } catch (e: Exception) {
            GradeSyncResult.Error("Failed to sync grade: ${e.message}")
        }
    }

    suspend fun performSync(syncResult: GradeSyncResult.DowngradeRequired) {
        subjectDao.updateGrade(syncResult.subjectId, syncResult.toGrade.name)
    }

    suspend fun checkAllSubjects(): List<GradeSyncResult> {
        return try {
            val subjectsWithGrades = subjectDao.subjectsWithAssignedGrade.first()
            subjectsWithGrades.map { subjectWithGrade ->
                checkSubject(subjectWithGrade)
            }
        } catch (e: Exception) {
            listOf(GradeSyncResult.Error("Failed to check subjects: ${e.message}"))
        }
    }
}