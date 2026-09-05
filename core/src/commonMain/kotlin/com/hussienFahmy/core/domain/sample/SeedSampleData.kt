package com.hussienfahmy.core.domain.sample

import com.hussienfahmy.core.data.local.SemesterDao
import com.hussienfahmy.core.data.local.SubjectDao
import com.hussienfahmy.core.data.local.entity.Semester
import com.hussienfahmy.core.data.local.entity.Subject
import com.hussienfahmy.core.data.local.model.GradeName
import com.hussienfahmy.core.domain.user_data.repository.UserDataRepository

/**
 * Prepares a local-only "demo" account so someone can look around without signing up.
 *
 * Always fills in a placeholder profile (so no screen looks half-built); when [includeHistory] is
 * set it also adds a realistic run of archived semesters, their subjects and a few "current
 * workspace" subjects. The default grade scale is seeded separately by the Room `onCreate`
 * callback, so it is always present by the time this runs. Safe to re-run - it clears the local
 * grade DB first.
 */
class SeedSampleData(
    private val semesterDao: SemesterDao,
    private val subjectDao: SubjectDao,
    private val userDataRepository: UserDataRepository,
) {
    suspend operator fun invoke(includeHistory: Boolean) {
        semesterDao.deleteAll()
        subjectDao.deleteAll()

        seedProfile(includeHistory)
        if (includeHistory) seedHistory()
    }

    // Wrapped: profile writes hit the remote user doc, and a demo must not fail on a flaky
    // network - a placeholder card is a fine fallback.
    private suspend fun seedProfile(includeHistory: Boolean) = runCatching {
        userDataRepository.updateUniversity("Ain Shams University")
        userDataRepository.updateFaculty("Faculty of Engineering")
        userDataRepository.updateDepartment("Computer Engineering")
        userDataRepository.updateLevel(if (includeHistory) 4 else 1)
        if (includeHistory) {
            userDataRepository.updateCumulativeGPA(3.57)
            userDataRepository.updateCreditHours(95)
        }
    }

    private suspend fun seedHistory() {
        // SUMMARY semesters (no subjects: the "summary" row in the PDF report)
        semesterDao.insert(Semester(label = "Year 1, Semester 1", level = 1, type = Semester.Type.SUMMARY, semesterGPA = 3.20, totalCreditHours = 17, status = Semester.Status.ARCHIVED, order = 1))
        semesterDao.insert(Semester(label = "Year 1, Semester 2", level = 1, type = Semester.Type.SUMMARY, semesterGPA = 3.50, totalCreditHours = 18, status = Semester.Status.ARCHIVED, order = 2))

        // DETAILED: midterm + oral only, all grades set, with marks
        val s3id = semesterDao.insert(Semester(label = "Year 2, Semester 1", level = 2, type = Semester.Type.DETAILED, semesterGPA = 3.60, totalCreditHours = 15, status = Semester.Status.ARCHIVED, order = 3))
        listOf(
            Subject(name = "Calculus II", creditHours = 3.0, gradeName = GradeName.A, totalMarks = 100.0, semesterMarks = Subject.SemesterMarks(midterm = 28.0, oral = 9.0), metadata = Subject.MetaData(midtermAvailable = true, oralAvailable = true, practicalAvailable = false, projectAvailable = false), semesterId = s3id),
            Subject(name = "Linear Algebra", creditHours = 3.0, gradeName = GradeName.BPlus, totalMarks = 100.0, semesterMarks = Subject.SemesterMarks(midterm = 22.0, oral = 7.0), metadata = Subject.MetaData(midtermAvailable = true, oralAvailable = true, practicalAvailable = false, projectAvailable = false), semesterId = s3id),
            Subject(name = "Data Structures", creditHours = 3.0, gradeName = GradeName.AMinus, totalMarks = 100.0, semesterMarks = Subject.SemesterMarks(midterm = 25.0, oral = 8.0), metadata = Subject.MetaData(midtermAvailable = true, oralAvailable = true, practicalAvailable = false, projectAvailable = false), semesterId = s3id),
            Subject(name = "Digital Logic Design", creditHours = 3.0, gradeName = GradeName.B, totalMarks = 100.0, semesterMarks = Subject.SemesterMarks(midterm = 20.0, oral = 6.0), metadata = Subject.MetaData(midtermAvailable = true, oralAvailable = true, practicalAvailable = false, projectAvailable = false), semesterId = s3id),
            Subject(name = "Technical Writing", creditHours = 3.0, gradeName = GradeName.A, totalMarks = 100.0, semesterMarks = Subject.SemesterMarks(midterm = 27.0, oral = 10.0), metadata = Subject.MetaData(midtermAvailable = true, oralAvailable = true, practicalAvailable = false, projectAvailable = false), semesterId = s3id),
        ).forEach { subjectDao.upsert(it) }

        // DETAILED: midterm + practical only, no oral, variable marks
        val s4id = semesterDao.insert(Semester(label = "Year 2, Semester 2", level = 2, type = Semester.Type.DETAILED, semesterGPA = 3.60, totalCreditHours = 15, status = Semester.Status.ARCHIVED, order = 4))
        listOf(
            Subject(name = "Operating Systems", creditHours = 3.0, gradeName = GradeName.A, totalMarks = 100.0, semesterMarks = Subject.SemesterMarks(midterm = 28.0, practical = 18.0), metadata = Subject.MetaData(midtermAvailable = true, oralAvailable = false, practicalAvailable = true, projectAvailable = false), semesterId = s4id),
            Subject(name = "Computer Networks", creditHours = 3.0, gradeName = GradeName.BPlus, totalMarks = 100.0, semesterMarks = Subject.SemesterMarks(midterm = 22.0, practical = 14.0), metadata = Subject.MetaData(midtermAvailable = true, oralAvailable = false, practicalAvailable = true, projectAvailable = false), semesterId = s4id),
            Subject(name = "Software Engineering", creditHours = 3.0, gradeName = GradeName.A, totalMarks = 100.0, semesterMarks = Subject.SemesterMarks(midterm = 26.0, practical = 17.0), metadata = Subject.MetaData(midtermAvailable = true, oralAvailable = false, practicalAvailable = true, projectAvailable = false), semesterId = s4id),
            Subject(name = "Numerical Analysis", creditHours = 3.0, gradeName = GradeName.B, totalMarks = 100.0, semesterMarks = Subject.SemesterMarks(midterm = 19.0, practical = 13.0), metadata = Subject.MetaData(midtermAvailable = true, oralAvailable = false, practicalAvailable = true, projectAvailable = false), semesterId = s4id),
            Subject(name = "Discrete Mathematics", creditHours = 3.0, gradeName = GradeName.AMinus, totalMarks = 100.0, semesterMarks = Subject.SemesterMarks(midterm = 24.0, practical = 15.0), metadata = Subject.MetaData(midtermAvailable = true, oralAvailable = false, practicalAvailable = true, projectAvailable = false), semesterId = s4id),
        ).forEach { subjectDao.upsert(it) }

        // DETAILED: all mark types (midterm + practical + oral + project)
        val s5id = semesterDao.insert(Semester(label = "Year 3, Semester 1", level = 3, type = Semester.Type.DETAILED, semesterGPA = 3.90, totalCreditHours = 14, status = Semester.Status.ARCHIVED, order = 5))
        listOf(
            Subject(name = "Compiler Design", creditHours = 3.0, gradeName = GradeName.A, totalMarks = 100.0, semesterMarks = Subject.SemesterMarks(midterm = 29.0, practical = 19.0, oral = 9.0), metadata = Subject.MetaData(midtermAvailable = true, oralAvailable = true, practicalAvailable = true, projectAvailable = false), semesterId = s5id),
            Subject(name = "Artificial Intelligence", creditHours = 3.0, gradeName = GradeName.A, totalMarks = 100.0, semesterMarks = Subject.SemesterMarks(midterm = 27.0, practical = 17.0, oral = 8.0), metadata = Subject.MetaData(midtermAvailable = true, oralAvailable = true, practicalAvailable = true, projectAvailable = false), semesterId = s5id),
            Subject(name = "Web Programming", creditHours = 3.0, gradeName = GradeName.A, totalMarks = 100.0, semesterMarks = Subject.SemesterMarks(midterm = 28.0, practical = 18.0, project = 22.0), metadata = Subject.MetaData(midtermAvailable = true, oralAvailable = false, practicalAvailable = true, projectAvailable = true), semesterId = s5id),
            Subject(name = "Computer Architecture", creditHours = 3.0, gradeName = GradeName.AMinus, totalMarks = 100.0, semesterMarks = Subject.SemesterMarks(midterm = 23.0, oral = 7.0), metadata = Subject.MetaData(midtermAvailable = true, oralAvailable = true, practicalAvailable = false, projectAvailable = false), semesterId = s5id),
            Subject(name = "Professional Ethics", creditHours = 2.0, gradeName = GradeName.A, totalMarks = 100.0, semesterMarks = Subject.SemesterMarks(midterm = 25.0, oral = 8.0), metadata = Subject.MetaData(midtermAvailable = true, oralAvailable = true, practicalAvailable = false, projectAvailable = false), semesterId = s5id),
        ).forEach { subjectDao.upsert(it) }

        // DETAILED: mixed credit hours, midterm only
        val s6id = semesterDao.insert(Semester(label = "Year 3, Semester 2", level = 3, type = Semester.Type.DETAILED, semesterGPA = 3.68, totalCreditHours = 16, status = Semester.Status.ARCHIVED, order = 6))
        listOf(
            Subject(name = "Design and Analysis of Algorithms", creditHours = 3.0, gradeName = GradeName.A, totalMarks = 100.0, semesterMarks = Subject.SemesterMarks(midterm = 29.0), metadata = Subject.MetaData(midtermAvailable = true, oralAvailable = false, practicalAvailable = false, projectAvailable = false), semesterId = s6id),
            Subject(name = "Database Systems", creditHours = 3.0, gradeName = GradeName.A, totalMarks = 100.0, semesterMarks = Subject.SemesterMarks(midterm = 26.0), metadata = Subject.MetaData(midtermAvailable = true, oralAvailable = false, practicalAvailable = false, projectAvailable = false), semesterId = s6id),
            Subject(name = "Computer Graphics", creditHours = 3.0, gradeName = GradeName.B, totalMarks = 100.0, semesterMarks = Subject.SemesterMarks(midterm = 18.0), metadata = Subject.MetaData(midtermAvailable = true, oralAvailable = false, practicalAvailable = false, projectAvailable = false), semesterId = s6id),
            Subject(name = "Physical Education", creditHours = 1.0, gradeName = GradeName.A, totalMarks = 50.0, semesterMarks = null, metadata = Subject.MetaData(midtermAvailable = false, oralAvailable = false, practicalAvailable = false, projectAvailable = false), semesterId = s6id),
            Subject(name = "Engineering Economics", creditHours = 2.0, gradeName = GradeName.BPlus, totalMarks = 100.0, semesterMarks = Subject.SemesterMarks(midterm = 20.0), metadata = Subject.MetaData(midtermAvailable = true, oralAvailable = false, practicalAvailable = false, projectAvailable = false), semesterId = s6id),
            Subject(name = "Islamic Culture", creditHours = 2.0, gradeName = GradeName.A, totalMarks = 100.0, semesterMarks = null, metadata = Subject.MetaData(midtermAvailable = false, oralAvailable = false, practicalAvailable = false, projectAvailable = false), semesterId = s6id),
            Subject(name = "Research Methodology", creditHours = 2.0, gradeName = GradeName.AMinus, totalMarks = 100.0, semesterMarks = Subject.SemesterMarks(midterm = 22.0), metadata = Subject.MetaData(midtermAvailable = true, oralAvailable = false, practicalAvailable = false, projectAvailable = false), semesterId = s6id),
        ).forEach { subjectDao.upsert(it) }

        // Current workspace: this term's courses - graded ones are predictions, the rest have no grade yet
        listOf(
            Subject(name = "Computer Security", creditHours = 3.0, gradeName = GradeName.BPlus, totalMarks = 100.0, semesterMarks = Subject.SemesterMarks(midterm = 22.0, oral = 7.0), metadata = Subject.MetaData(midtermAvailable = true, oralAvailable = true, practicalAvailable = false, projectAvailable = false), semesterId = null),
            Subject(name = "Mobile Application Development", creditHours = 3.0, gradeName = GradeName.A, totalMarks = 100.0, semesterMarks = Subject.SemesterMarks(midterm = 25.0, oral = 8.0, practical = 16.0), metadata = Subject.MetaData(midtermAvailable = true, oralAvailable = true, practicalAvailable = true, projectAvailable = false), semesterId = null),
            Subject(name = "Machine Learning", creditHours = 3.0, gradeName = GradeName.A, totalMarks = 100.0, semesterMarks = Subject.SemesterMarks(midterm = 28.0, practical = 18.0), metadata = Subject.MetaData(midtermAvailable = true, oralAvailable = false, practicalAvailable = true, projectAvailable = false), semesterId = null),
            Subject(name = "Distributed Systems", creditHours = 3.0, gradeName = null, totalMarks = 100.0, semesterMarks = Subject.SemesterMarks(midterm = 18.0), metadata = Subject.MetaData(midtermAvailable = true, oralAvailable = false, practicalAvailable = false, projectAvailable = false), semesterId = null),
            Subject(name = "Cloud Computing", creditHours = 3.0, gradeName = null, totalMarks = 100.0, semesterMarks = Subject.SemesterMarks(midterm = 20.0), metadata = Subject.MetaData(midtermAvailable = true, oralAvailable = false, practicalAvailable = false, projectAvailable = false), semesterId = null),
            Subject(name = "Embedded Systems", creditHours = 3.0, gradeName = GradeName.BPlus, totalMarks = 100.0, semesterMarks = Subject.SemesterMarks(midterm = 23.0, oral = 7.0), metadata = Subject.MetaData(midtermAvailable = true, oralAvailable = true, practicalAvailable = false, projectAvailable = false), semesterId = null),
            Subject(name = "Wireless Communications", creditHours = 3.0, gradeName = GradeName.A, totalMarks = 100.0, semesterMarks = Subject.SemesterMarks(midterm = 26.0, practical = 17.0), metadata = Subject.MetaData(midtermAvailable = true, oralAvailable = false, practicalAvailable = true, projectAvailable = false), semesterId = null),
        ).forEach { subjectDao.upsert(it) }
    }
}
