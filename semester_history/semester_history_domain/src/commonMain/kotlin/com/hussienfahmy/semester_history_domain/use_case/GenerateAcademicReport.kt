package com.hussienfahmy.semester_history_domain.use_case

import com.hussienfahmy.core.data.local.GradeDao
import com.hussienfahmy.core.data.local.SemesterDao
import com.hussienfahmy.core.data.local.SubjectDao
import com.hussienfahmy.core.data.local.entity.Subject
import com.hussienfahmy.core.domain.gpa_settings.repository.GPASettingsRepository
import com.hussienfahmy.core.domain.report.AcademicReportData
import com.hussienfahmy.core.domain.report.GradeThreshold
import com.hussienfahmy.core.domain.report.ReportBrandingProvider
import com.hussienfahmy.core.domain.report.ReportTemplate
import com.hussienfahmy.core.domain.report.ReportTemplateRegistry
import com.hussienfahmy.core.domain.report.SemesterSection
import com.hussienfahmy.core.domain.report.SubjectRow
import com.hussienfahmy.core.domain.user_data.repository.UserDataRepository
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first

class GenerateAcademicReport(
    private val subjectDao: SubjectDao,
    private val semesterDao: SemesterDao,
    private val gradeDao: GradeDao,
    private val userDataRepository: UserDataRepository,
    private val gpaSettingsRepository: GPASettingsRepository,
    private val registry: ReportTemplateRegistry,
    private val brandingProvider: ReportBrandingProvider,
) {
    suspend operator fun invoke(template: ReportTemplate = ReportTemplate.DEFAULT): String {
        val userData = userDataRepository.userData.filterNotNull().first()
        val gpa = gpaSettingsRepository.getGPASettings()
        val maxGPA = gpa.system.number.toDouble()

        val allGrades = gradeDao.grades.first()
        val grades = allGrades.associateBy { it.name }

        val gradeThresholds = allGrades
            .filter { it.active && it.points != null && it.percentage != null }
            .sortedByDescending { it.points }
            .map {
                GradeThreshold(
                    symbol = it.name.symbol,
                    points = it.points!!,
                    percentage = it.percentage!!
                )
            }

        val archivedSemesters = semesterDao.getArchived().first()
        val historySections = archivedSemesters.map { semester ->
            val subjects = subjectDao.getSubjectsBySemesterId(semester.id).first()
            SemesterSection(
                label = semester.label,
                semesterGPA = semester.semesterGPA,
                totalCreditHours = semester.totalCreditHours,
                subjects = subjects.map { it.toSubjectRow(grades) },
            )
        }

        val reportData = AcademicReportData(
            studentName = userData.name,
            university = userData.academicInfo.university,
            faculty = userData.academicInfo.faculty,
            department = userData.academicInfo.department,
            level = userData.academicInfo.level,
            cumulativeGPA = userData.academicProgress.cumulativeGPA,
            totalCreditHours = userData.academicProgress.creditHours,
            maxGPA = maxGPA,
            currentSemester = null,
            history = historySections,
            grades = gradeThresholds,
            logoBase64Png = brandingProvider.loadAppIconBase64Png(),
            qrBase64Png = brandingProvider.loadQrCodeBase64Png(),
        )

        return registry.render(template, reportData)
    }

    private fun Subject.toSubjectRow(
        gradesMap: Map<com.hussienfahmy.core.data.local.model.GradeName, com.hussienfahmy.core.data.local.entity.Grade>,
    ): SubjectRow {
        val grade = gradeName?.let { gradesMap[it] }
        return SubjectRow(
            name = name,
            creditHours = creditHours,
            gradeName = gradeName?.symbol,
            gradePoints = grade?.points,
            midterm = semesterMarks?.midterm,
            practical = semesterMarks?.practical,
            oral = semesterMarks?.oral,
            project = semesterMarks?.project,
            finalExam = semesterMarks?.finalExamScore,
            courseTotalMarks = totalMarks,
            midtermAvailable = metadata.midtermAvailable,
            practicalAvailable = metadata.practicalAvailable,
            oralAvailable = metadata.oralAvailable,
            projectAvailable = metadata.projectAvailable,
            finalExamAvailable = metadata.finalExamMaxMarks != null,
        )
    }
}
