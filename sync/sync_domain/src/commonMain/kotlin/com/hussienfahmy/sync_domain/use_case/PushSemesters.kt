package com.hussienfahmy.sync_domain.use_case

import com.hussienfahmy.core.data.local.SemesterDao
import com.hussienfahmy.core.data.local.SubjectDao
import com.hussienfahmy.core.domain.gpa.CalculateWeightedCumulativeGpa
import com.hussienfahmy.sync_domain.model.Subject
import com.hussienfahmy.sync_domain.model.toNetworkSemester
import com.hussienfahmy.sync_domain.repository.SyncRepository
import kotlinx.coroutines.flow.first

class PushSemesters(
    private val repository: SyncRepository,
    private val semesterDao: SemesterDao,
    private val subjectDao: SubjectDao,
    private val calculateWeightedCumulativeGpa: CalculateWeightedCumulativeGpa,
) {
    suspend operator fun invoke(userId: String) {
        val semesters = semesterDao.getArchived().first()

        val networkSemesters = semesters.map { semester ->
            val subjects = subjectDao.getSubjectsBySemesterId(semester.id).first()

            val networkSubjects = subjects.map { subject ->
                Subject(
                    id = subject.id,
                    name = subject.name,
                    creditHours = subject.creditHours,
                    gradeName = subject.gradeName,
                    totalMarks = subject.totalMarks,
                    semesterMarks = subject.semesterMarks?.let { marks ->
                        Subject.SemesterMarks(
                            midterm = marks.midterm,
                            practical = marks.practical,
                            oral = marks.oral,
                            project = marks.project,
                        )
                    },
                    metadata = Subject.MetaData(
                        midtermAvailable = subject.metadata.midtermAvailable,
                        practicalAvailable = subject.metadata.practicalAvailable,
                        oralAvailable = subject.metadata.oralAvailable,
                        projectAvailable = subject.metadata.projectAvailable,
                    )
                )
            }
            semester.toNetworkSemester(networkSubjects)
        }

        repository.uploadSemesters(userId = userId, semesters = networkSemesters)

        // Skip when empty - avoids overwriting legacy data with zeros.
        if (semesters.isNotEmpty()) {
            val result = calculateWeightedCumulativeGpa(
                semesters.map {
                    CalculateWeightedCumulativeGpa.SemesterContribution(
                        gpa = it.semesterGPA,
                        gpaCreditHours = it.gpaCreditHours,
                        totalCreditHours = it.totalCreditHours,
                    )
                }
            )
            repository.updateAcademicProgress(
                userId = userId,
                cumulativeGPA = result.cumulativeGPA,
                creditHours = result.totalCreditHours
            )
        }
    }
}
