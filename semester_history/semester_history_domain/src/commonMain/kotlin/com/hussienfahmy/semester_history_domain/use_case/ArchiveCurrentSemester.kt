package com.hussienfahmy.semester_history_domain.use_case

import com.hussienfahmy.core.data.local.SemesterDao
import com.hussienfahmy.core.data.local.SubjectDao
import com.hussienfahmy.core.data.local.entity.Semester
import com.hussienfahmy.core.domain.sync.SyncDirtyTracker
import com.hussienfahmy.core.domain.user_data.use_cases.GetUserData
import com.hussienfahmy.core.domain.user_data.use_cases.UpdateLevel
import com.hussienfahmy.core.domain.user_data.use_cases.UpdateSemester
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlin.time.Clock

class ArchiveCurrentSemester(
    private val semesterDao: SemesterDao,
    private val subjectDao: SubjectDao,
    private val getUserData: GetUserData,
    private val updateLevel: UpdateLevel,
    private val updateSemester: UpdateSemester,
    private val calculateSemesterGPA: CalculateSemesterGPA,
    private val dirtyTracker: SyncDirtyTracker,
) {
    sealed class Result {
        data object Success : Result()
        data object NoSubjects : Result()
    }

    suspend operator fun invoke(): Result {
        val userData = getUserData().filterNotNull().first()
        val currentLevel = userData.academicInfo.level
        val currentSemester = userData.academicInfo.semester

        val currentSubjects = subjectDao.getAllCurrentSubjects().first()
        if (currentSubjects.isEmpty()) return Result.NoSubjects

        val semesterGPA = calculateSemesterGPA(currentSubjects)
        val totalCreditHours = currentSubjects.sumOf { it.creditHours }.toInt()

        val label = SemesterProgression.label(currentLevel, currentSemester)
        val nextOrder = (semesterDao.getMaxOrder() ?: 0) + 1

        val newSemester = Semester(
            label = label,
            level = currentLevel,
            type = Semester.Type.DETAILED,
            semesterGPA = semesterGPA,
            totalCreditHours = totalCreditHours,
            status = Semester.Status.ARCHIVED,
            order = nextOrder,
            archivedAt = Clock.System.now().toEpochMilliseconds(),
        )

        val semesterId = semesterDao.insert(newSemester)
        subjectDao.linkWorkspaceSubjectsToSemester(semesterId)

        val (nextSemester, nextLevel) = SemesterProgression.next(currentSemester, currentLevel)
        updateSemester(nextSemester)
        if (nextLevel != currentLevel) updateLevel(nextLevel.toString())
        dirtyTracker.markSemestersChanged()

        return Result.Success
    }
}
