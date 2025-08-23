package com.hussienfahmy.semester_marks_domain.use_case

import com.hussienfahmy.core.data.local.SubjectDao
import com.hussienfahmy.core.data.local.model.GradeName
import com.hussienfahmy.core.domain.grades.use_case.GetActiveGrades
import com.hussienfahmy.semester_marks_domain.model.Grade
import com.hussienfahmy.semester_marks_domain.model.Subject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class ContinuesCalculation(
    getActiveGrades: GetActiveGrades,
    appScope: CoroutineScope,
    private val subjectDao: SubjectDao,
    private val defaultDispatcher: CoroutineDispatcher,
) {
    val activeGrades = getActiveGrades().map { list ->
        list.filter {
            it.name != GradeName.F
        }
    }.stateIn(
        scope = appScope,
        started = SharingStarted.Lazily,
        initialValue = emptyList()
    )

    operator fun invoke(): Flow<List<Subject>> = combine(
        activeGrades,
        subjectDao.subjectsWithAssignedGrade
    ) { grades, subjects ->
        if (grades.isEmpty()) return@combine listOf()

        subjects.map { (subjectEntity, maxGrade, _) ->
            Subject(
                id = subjectEntity.id,
                name = subjectEntity.name,
                practicalAvailable = subjectEntity.metadata.practicalAvailable,
                midtermAvailable = subjectEntity.metadata.midtermAvailable,
                oralAvailable = subjectEntity.metadata.oralAvailable,
                projectAvailable = subjectEntity.metadata.projectAvailable,
                practicalMarks = subjectEntity.semesterMarks?.practical,
                midtermMarks = subjectEntity.semesterMarks?.midterm,
                oralMarks = subjectEntity.semesterMarks?.oral,
                projectMarks = subjectEntity.semesterMarks?.project,
                courseTotalMarks = subjectEntity.totalMarks,
                grades = activeGrades.value.map { gradeEntity ->
                    val requiredMarksToAchieveThisGrade =
                        (subjectEntity.totalMarks * gradeEntity.percentage!!) / 100.0
                    val marksAchieved = subjectEntity.semesterMarks?.value

                    Grade(
                        symbol = gradeEntity.name.symbol,
                        achievable = if (
                            gradeEntity.name <= maxGrade.name
                            && marksAchieved != null
                        ) {
                            val neededMarks =
                                requiredMarksToAchieveThisGrade - marksAchieved
                            Grade.Achievable.Yes(neededMarks)
                        } else {
                            Grade.Achievable.No
                        },
                    )
                },
            )
        }
    }.flowOn(defaultDispatcher)
}