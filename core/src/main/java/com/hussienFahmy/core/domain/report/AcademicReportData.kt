package com.hussienfahmy.core.domain.report

data class AcademicReportData(
    val studentName: String,
    val university: String,
    val faculty: String,
    val department: String,
    val level: Int,
    val cumulativeGPA: Double,
    val totalCreditHours: Int,
    val maxGPA: Double,
    val currentSemester: SemesterSection?,
    val history: List<SemesterSection>,
    val grades: List<GradeThreshold>,
    val logoBase64Png: String? = null,
    val qrBase64Png: String? = null,
)

data class GradeThreshold(
    val symbol: String,
    val points: Double,
    val percentage: Double,
)

data class SemesterSection(
    val label: String,
    val semesterGPA: Double,
    val totalCreditHours: Int,
    val subjects: List<SubjectRow>,
)

data class SubjectRow(
    val name: String,
    val creditHours: Double,
    val gradeName: String?,
    val gradePoints: Double?,
    val midterm: Double?,
    val practical: Double?,
    val oral: Double?,
    val project: Double?,
    val finalExam: Double?,
    val courseTotalMarks: Double,
    val midtermAvailable: Boolean,
    val practicalAvailable: Boolean,
    val oralAvailable: Boolean,
    val projectAvailable: Boolean,
    val finalExamAvailable: Boolean,
)
