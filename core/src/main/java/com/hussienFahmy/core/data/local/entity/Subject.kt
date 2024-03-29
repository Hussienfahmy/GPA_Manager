package com.hussienFahmy.core.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.hussienFahmy.core.data.local.model.GradeName

/**
 * Subject is the main object of the app
 *
 * @property id Long unique id of the subject
 * @property name String name of the subject (visible to user)
 * @property creditHours Int credit hours of the subject (1,2,3,4,5)
 * @property gradeName Name? the grade name which is set to this subject
 * @property totalMarks Int total marks of the subject
 * @property semesterMarks SemesterMark semester mark of the subject (midterm, oral, practical)
 * @property metadata MetaData the other data related to this subject that not fixed and used in calculation
 */
@Entity(tableName = "subject")
data class Subject(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String = "",
    val creditHours: Double = 0.0,
    val gradeName: GradeName? = null,
    val totalMarks: Double = 0.0,
    @Embedded val semesterMarks: SemesterMarks? = null,
    @Embedded val metadata: MetaData = MetaData()
) {
    /**
     * Represents the semester mark related to the subject
     * @property midterm Double? midterm marks (null means the user didn't take this exam yet so there is no marks)
     * @property practical Double? practical marks (null means the user didn't take this exam yet so there is no marks)
     * @property oral Double? oral marks (null means the user didn't take this exam yet so there is no marks)
     * @property value Double the sum of all the marks
     */
    data class SemesterMarks(
        val midterm: Double? = null,
        val practical: Double? = null,
        val oral: Double? = null,
    ) {
        // room will create the SemesterMark object only if
        // one of the fields is not null so am sure we will get a value (number)
        val value: Double
            get() {
                var output = 0.0
                midterm?.let { output += it }
                practical?.let { output += it }
                oral?.let { output += it }

                return output
            }
    }

    /**
     * other data related to the subject
     * @property maxGradeNameCanAchieve Name the max grade the user can achieve in this subject (depends on the semester mark value)
     * and always be calculated when retrieve the data from database
     * @constructor
     */
    data class MetaData(
        @ColumnInfo(name = "max_grade") val maxGradeNameCanAchieve: GradeName = GradeName.A,
        @ColumnInfo(defaultValue = "1") val midtermAvailable: Boolean = true,
        @ColumnInfo(defaultValue = "1") val practicalAvailable: Boolean = true,
        @ColumnInfo(defaultValue = "1") val oralAvailable: Boolean = true,
    )
}