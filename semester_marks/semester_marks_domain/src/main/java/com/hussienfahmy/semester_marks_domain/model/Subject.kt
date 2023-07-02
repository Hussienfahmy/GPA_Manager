package com.hussienfahmy.semester_marks_domain.model

data class Subject(
    val id: Long,
    val name: String,
    val practicalAvailable: Boolean,
    val midtermAvailable: Boolean,
    val oralAvailable: Boolean,
    val practicalMarks: Double?,
    val midtermMarks: Double?,
    val oralMarks: Double?,
    val courseTotalMarks: Double,
    val grades: List<Grade>
) {
    val courseMarks: Double
        get() {
            var output = 0.0
            midtermMarks?.let { output += it }
            practicalMarks?.let { output += it }
            oralMarks?.let { output += it }

            return output
        }
}