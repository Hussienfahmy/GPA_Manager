package com.hussienfahmy.semester_history_domain.use_case

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.util.Base64
import androidx.annotation.DrawableRes
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.createBitmap
import com.hussienfahmy.core.generated.resources.Res
import com.hussienfahmy.core.data.local.GradeDao
import com.hussienfahmy.core.data.local.SemesterDao
import com.hussienfahmy.core.data.local.SubjectDao
import com.hussienfahmy.core.data.local.entity.Subject
import com.hussienfahmy.core.domain.gpa_settings.repository.GPASettingsRepository
import com.hussienfahmy.core.domain.report.AcademicReportData
import com.hussienfahmy.core.domain.report.GradeThreshold
import com.hussienfahmy.core.domain.report.ReportTemplate
import com.hussienfahmy.core.domain.report.ReportTemplateRegistry
import com.hussienfahmy.core.domain.report.SemesterSection
import com.hussienfahmy.core.domain.report.SubjectRow
import com.hussienfahmy.core.domain.user_data.repository.UserDataRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream

class GenerateAcademicReport(
    private val subjectDao: SubjectDao,
    private val semesterDao: SemesterDao,
    private val gradeDao: GradeDao,
    private val userDataRepository: UserDataRepository,
    private val gpaSettingsRepository: GPASettingsRepository,
    private val registry: ReportTemplateRegistry,
    private val context: Context,
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
            logoBase64Png = loadLauncherLogoBase64(),
            qrBase64Png = loadDrawableBase64(R.drawable.qr_code),
        )

        return registry.render(template, reportData)
    }

    private suspend fun loadLauncherLogoBase64(): String? = withContext(Dispatchers.IO) {
        try {
            val drawable = context.packageManager.getApplicationIcon(context.packageName)
            val bitmap = when (drawable) {
                is BitmapDrawable -> drawable.bitmap
                else -> {
                    val size = 192
                    val bm = createBitmap(size, size)
                    val canvas = Canvas(bm)
                    drawable.setBounds(0, 0, size, size)
                    drawable.draw(canvas)
                    bm
                }
            }
            val stream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
            Base64.encodeToString(stream.toByteArray(), Base64.NO_WRAP)
        } catch (_: Exception) {
            null
        }
    }

    private suspend fun loadDrawableBase64(@DrawableRes resId: Int): String? =
        withContext(Dispatchers.IO) {
            try {
                val drawable = ResourcesCompat.getDrawable(context.resources, resId, null)!!
                val bitmap = when (drawable) {
                    is BitmapDrawable -> drawable.bitmap
                    else -> {
                        val w = drawable.intrinsicWidth.takeIf { it > 0 } ?: 256
                        val h = drawable.intrinsicHeight.takeIf { it > 0 } ?: 256
                        val bm = createBitmap(w, h)
                        val canvas = Canvas(bm)
                        drawable.setBounds(0, 0, w, h)
                        drawable.draw(canvas)
                        bm
                    }
                }
                val stream = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
                Base64.encodeToString(stream.toByteArray(), Base64.NO_WRAP)
            } catch (_: Exception) {
                null
            }
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
