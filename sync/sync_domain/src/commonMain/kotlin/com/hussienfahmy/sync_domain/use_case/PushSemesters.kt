package com.hussienfahmy.sync_domain.use_case

import com.hussienfahmy.core.data.local.SemesterDao
import com.hussienfahmy.core.data.local.SubjectDao
import com.hussienfahmy.sync_domain.model.Subject
import com.hussienfahmy.sync_domain.model.toNetworkSemester
import com.hussienfahmy.sync_domain.repository.SyncRepository
import kotlinx.coroutines.flow.first

class PushSemesters(
    private val repository: SyncRepository,
    private val semesterDao: SemesterDao,
    private val subjectDao: SubjectDao,
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

        // Keep academicProgress in user document in sync with the computed cumulative GPA.
        // Only update when semesters exist — otherwise we'd overwrite legacy data with zeros.
        if (semesters.isNotEmpty()) {
            var totalPoints = 0.0
            var totalHours = 0
            semesters.forEach { semester ->
                totalPoints += semester.semesterGPA * semester.totalCreditHours
                totalHours += semester.totalCreditHours
            }
            val cumulativeGPA = if (totalHours == 0) 0.0 else totalPoints / totalHours
            repository.updateAcademicProgress(
                userId = userId,
                cumulativeGPA = cumulativeGPA,
                creditHours = totalHours
            )
        }
    }
}
