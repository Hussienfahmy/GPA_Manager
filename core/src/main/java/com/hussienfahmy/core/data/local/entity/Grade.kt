package com.hussienfahmy.core.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


/**
 * Represents the grade with it's properties to be set to each subject and make easy to calculate
 * also to make the user able to modify the grade properties
 *
 * @property fullName Name readable name make it easy to read and retrieve or update specific grade DON'T CHANGE TO (name) must be unique name to avoid errors when using multimap return as the subject has (name) field
 * @property symbol String e.g A, B, C, D, F
 * @property active Boolean if active the user can use this grade to set it to any subject and we will sure that the grade has percentage and points values
 * @property points Double? the points that the grade have witch use to calculate the GPA
 * @property percentage Double? the percentage that the grade have witch use to calculate the marks
 */
@Entity(tableName = "grade")
data class Grade(
    @PrimaryKey
    @ColumnInfo(name = "full_name")
    val fullName: Name,
    val symbol: String,
    val active: Boolean,
    val points: Double? = null,
    val percentage: Double? = null
) {
    enum class Name {
        F,
        DMinus,
        D,
        DPlus,
        CMinus,
        C,
        CPlus,
        BMinus,
        B,
        BPlus,
        AMinus,
        A,
        APlus,
    }

    companion object {
        /**
         * initial data as Ain Shams University calculate the GPA
         * @return List<Grade>
         */
        fun generateInitialData(): List<Grade> = listOf(
            Grade(Name.F, "F", true, 0.0, 0.0),
            Grade(Name.DMinus, "D-", false),
            Grade(Name.D, "D", true, 2.0, 60.0),
            Grade(Name.DPlus, "D+", false),
            Grade(Name.CMinus, "C-", false),
            Grade(Name.C, "C", true, 2.33, 65.0),
            Grade(Name.CPlus, "C+", true, 2.67, 70.0),
            Grade(Name.BMinus, "B-", false),
            Grade(Name.B, "B", true, 3.0, 75.0),
            Grade(Name.BPlus, "B+", true, 3.33, 80.0),
            Grade(Name.AMinus, "A-", true, 3.67, 85.0),
            Grade(Name.A, "A", true, 4.0, 90.0),
            Grade(Name.APlus, "A+", false)
        )

        // to order any list as same as the order in the enum
        object Comparator : kotlin.Comparator<Grade> {
            override fun compare(o1: Grade, o2: Grade): Int =
                o2.fullName.ordinal.compareTo(o1.fullName.ordinal)
        }
    }
}