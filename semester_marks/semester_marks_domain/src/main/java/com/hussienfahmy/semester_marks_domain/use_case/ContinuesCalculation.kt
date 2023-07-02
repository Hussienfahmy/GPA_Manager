package com.hussienfahmy.semester_marks_domain.use_case

import com.hussienFahmy.core.data.local.GradeDao
import com.hussienFahmy.core.data.local.SubjectDao
import com.hussienFahmy.core.data.local.model.GradeName
import com.hussienfahmy.semester_marks_domain.model.Grade
import com.hussienfahmy.semester_marks_domain.model.Subject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class ContinuesCalculation(
    private val subjectDao: SubjectDao,
    private val gradeDao: GradeDao,
    private val backgroundDispatcher: CoroutineDispatcher,
) {
    suspend operator fun invoke(): Flow<List<Subject>> =
        withContext(backgroundDispatcher) {
            val activeGrades = gradeDao.getActiveGrades().filter {
                it.metaData != GradeName.F
            }

            return@withContext subjectDao.subjectsWithAssignedGrade.map {
                it.map { (subjectEntity, _) ->
                    Subject(
                        id = subjectEntity.id,
                        name = subjectEntity.name,
                        practicalAvailable = subjectEntity.metadata.practicalAvailable,
                        midtermAvailable = subjectEntity.metadata.midtermAvailable,
                        oralAvailable = subjectEntity.metadata.oralAvailable,
                        practicalMarks = subjectEntity.semesterMarks?.practical,
                        midtermMarks = subjectEntity.semesterMarks?.midterm,
                        oralMarks = subjectEntity.semesterMarks?.oral,
                        courseTotalMarks = subjectEntity.totalMarks,
                        grades = activeGrades.map { gradeEntity ->
                            val requiredMarksToAchieveThisGrade =
                                (subjectEntity.totalMarks * gradeEntity.percentage!!) / 100.0
                            val marksAchieved = subjectEntity.semesterMarks?.value

                            Grade(
                                symbol = gradeEntity.metaData.symbol,
                                achievable = if (
                                    gradeEntity.metaData <= subjectEntity.metadata.maxGradeNameCanAchieve
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
            }
        }
}