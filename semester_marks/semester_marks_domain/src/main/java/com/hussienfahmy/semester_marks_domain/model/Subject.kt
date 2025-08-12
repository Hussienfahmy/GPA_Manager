package com.hussienfahmy.semester_marks_domain.model

data class Subject(
    val id: Long,
    val name: String,
    val practicalAvailable: Boolean,
    val midtermAvailable: Boolean,
    val oralAvailable: Boolean,
    val projectAvailable: Boolean,
    val practicalMarks: Double?,
    val midtermMarks: Double?,
    val oralMarks: Double?,
    val projectMarks: Double?,
    val courseTotalMarks: Double,
    val grades: List<Grade>
) {
    val courseMarks: Double
        get() {
            var output = 0.0
            midtermMarks?.let { output += it }
            practicalMarks?.let { output += it }
            oralMarks?.let { output += it }
            projectMarks?.let { output += it }

            return output
        }
}