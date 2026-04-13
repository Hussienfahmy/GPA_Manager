package com.hussienfahmy.semester_history_domain.use_case

import com.hussienfahmy.core.data.local.SemesterDao
import com.hussienfahmy.core.data.local.SubjectDao
import com.hussienfahmy.core.data.local.entity.Semester
import com.hussienfahmy.core.domain.sync.SemesterDirtyTracker
import com.hussienfahmy.core.domain.user_data.model.UserData
import com.hussienfahmy.core.domain.user_data.use_cases.GetUserData
import com.hussienfahmy.core.domain.user_data.use_cases.UpdateLevel
import com.hussienfahmy.core.domain.user_data.use_cases.UpdateSemester
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first

class ArchiveCurrentSemester(
    private val semesterDao: SemesterDao,
    private val subjectDao: SubjectDao,
    private val getUserData: GetUserData,
    private val updateLevel: UpdateLevel,
    private val updateSemester: UpdateSemester,
    private val calculateSemesterGPA: CalculateSemesterGPA,
    private val dirtyTracker: SemesterDirtyTracker,
) {
    sealed class Result {
        data object Success : Result()
        data object NoSubjects : Result()
    }

    suspend operator fun invoke(): Result {
        val userData = getUserData().filterNotNull().first()
        val currentLevel = userData.academicInfo.level
        val currentSemesterNum = userData.academicInfo.semester

        val currentSubjects = subjectDao.getAllCurrentSubjects().first()
        if (currentSubjects.isEmpty()) return Result.NoSubjects

        val semesterGPA = calculateSemesterGPA(currentSubjects)
        val totalCreditHours = currentSubjects.sumOf { it.creditHours }.toInt()

        val label = buildLabel(currentLevel, currentSemesterNum)
        val nextOrder = (semesterDao.getMaxOrder() ?: 0) + 1

        val newSemester = Semester(
            label = label,
            level = currentLevel,
            type = Semester.Type.DETAILED,
            semesterGPA = semesterGPA,
            totalCreditHours = totalCreditHours,
            status = Semester.Status.ARCHIVED,
            order = nextOrder,
            archivedAt = System.currentTimeMillis(),
        )

        val semesterId = semesterDao.insert(newSemester)
        subjectDao.linkWorkspaceSubjectsToSemester(semesterId)

        advanceSemester(currentLevel, currentSemesterNum)
        dirtyTracker.markChanged()

        return Result.Success
    }

    private suspend fun advanceSemester(
        currentLevel: Int,
        currentSemester: UserData.AcademicInfo.Semester,
    ) {
        when (currentSemester) {
            UserData.AcademicInfo.Semester.First -> updateSemester(UserData.AcademicInfo.Semester.Second)
            UserData.AcademicInfo.Semester.Second -> {
                updateSemester(UserData.AcademicInfo.Semester.First)
                updateLevel((currentLevel + 1).toString())
            }
        }
    }

    private fun buildLabel(level: Int, semester: UserData.AcademicInfo.Semester): String {
        val semesterName = when (semester) {
            UserData.AcademicInfo.Semester.First -> "1"
            UserData.AcademicInfo.Semester.Second -> "2"
        }
        return "Year $level - Semester $semesterName"
    }
}
