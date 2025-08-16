package com.hussienfahmy.core.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.hussienfahmy.core.data.local.model.GradeName


/**
 * Represents the grade with it's properties to be set to each subject and make easy to calculate
 * also to make the user able to modify the grade properties
 *
 * @property active Boolean if active the user can use this grade to set it to any subject and we will sure that the grade has percentage and points values
 * @property points Double? the points that the grade have witch use to calculate the GPA
 * @property percentage Double? the percentage that the grade have witch use to calculate the marks
 */
@Entity(tableName = "grade")
data class Grade(
    @ColumnInfo(name = "meta_data")
    @PrimaryKey
    val name: GradeName,
    val active: Boolean,
    val points: Double? = null,
    val percentage: Double? = null
) {


    companion object {
        /**
         * initial data as Ain Shams University calculate the GPA
         * @return List<Grade>
         */
        fun generateInitialData(): List<Grade> = listOf(
            Grade(GradeName.F, true, 0.0, 0.0),
            Grade(GradeName.DMinus, false),
            Grade(GradeName.D, true, 2.0, 60.0),
            Grade(GradeName.DPlus, false),
            Grade(GradeName.CMinus, false),
            Grade(GradeName.C, true, 2.33, 65.0),
            Grade(GradeName.CPlus, true, 2.67, 70.0),
            Grade(GradeName.BMinus, false),
            Grade(GradeName.B, true, 3.0, 75.0),
            Grade(GradeName.BPlus, true, 3.33, 80.0),
            Grade(GradeName.AMinus, true, 3.67, 85.0),
            Grade(GradeName.A, true, 4.0, 90.0),
            Grade(GradeName.APlus, false)
        )

        // to order any list as same as the order in the enum
        object Comparator : kotlin.Comparator<Grade> {
            override fun compare(o1: Grade, o2: Grade): Int =
                o2.name.ordinal.compareTo(o1.name.ordinal)
        }
    }
}