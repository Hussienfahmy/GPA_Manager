package com.hussienfahmy.core.domain.gpa

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.google.common.truth.Truth.assertThat
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@SmallTest
class CalculateWeightedCumulativeGpaTest {

    private val calculate = CalculateWeightedCumulativeGpa()

    @Test
    fun `NpNf hours excluded from weight but included in total hours`() {
        // 12 graded hours at 3.0, plus 3 NP hours - only the 12 should weigh the GPA.
        val result = calculate(
            listOf(
                CalculateWeightedCumulativeGpa.SemesterContribution(
                    gpa = 3.0,
                    gpaCreditHours = 12,
                    totalCreditHours = 15,
                )
            )
        )

        assertThat(result.cumulativeGPA).isWithin(0.001).of(3.0)
        assertThat(result.totalCreditHours).isEqualTo(15)
    }

    @Test
    fun `multiple semesters weighted by gpaCreditHours`() {
        val result = calculate(
            listOf(
                CalculateWeightedCumulativeGpa.SemesterContribution(3.0, 12, 12),
                CalculateWeightedCumulativeGpa.SemesterContribution(4.0, 6, 9),
            )
        )

        // (3.0*12 + 4.0*6) / 18 = 3.333..
        assertThat(result.cumulativeGPA).isWithin(0.001).of(3.333)
        assertThat(result.totalCreditHours).isEqualTo(21)
    }

    @Test
    fun `empty list returns zero`() {
        val result = calculate(emptyList())

        assertThat(result.cumulativeGPA).isEqualTo(0.0)
        assertThat(result.totalCreditHours).isEqualTo(0)
    }

    @Test
    fun `all NpNf semester returns zero gpa without dividing by zero`() {
        val result = calculate(
            listOf(CalculateWeightedCumulativeGpa.SemesterContribution(0.0, 0, 15))
        )

        assertThat(result.cumulativeGPA).isEqualTo(0.0)
        assertThat(result.totalCreditHours).isEqualTo(15)
    }
}
