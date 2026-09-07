package com.hussienfahmy.semester_history_domain.use_case

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.google.common.truth.Truth.assertThat
import com.hussienfahmy.core.domain.gpa.CalculateWeightedCumulativeGpa
import com.hussienfahmy.semester_history_domain.model.Semester
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@SmallTest
class CalculateCumulativeFromHistoryTest {

    private val calculate = CalculateCumulativeFromHistory(CalculateWeightedCumulativeGpa())

    private fun semester(gpa: Double, totalHours: Int, gpaHours: Int) = Semester(
        id = 1,
        label = "test",
        level = 1,
        type = Semester.Type.DETAILED,
        semesterGPA = gpa,
        totalCreditHours = totalHours,
        gpaCreditHours = gpaHours,
        status = Semester.Status.ARCHIVED,
        order = 1,
        createdAt = 0L,
        archivedAt = 0L,
    )

    @Test
    fun `semester with NP hours - cumulative weighted by gpaCreditHours not totalCreditHours`() {
        val result = calculate(
            listOf(semester(gpa = 3.0, totalHours = 15, gpaHours = 12))
        )

        assertThat(result.cumulativeGPA).isWithin(0.001).of(3.0)
        assertThat(result.creditHours).isEqualTo(15)
    }

    @Test
    fun `no semesters returns zero`() {
        val result = calculate(emptyList())

        assertThat(result.cumulativeGPA).isEqualTo(0.0)
        assertThat(result.creditHours).isEqualTo(0)
    }
}
