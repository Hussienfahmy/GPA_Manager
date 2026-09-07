package com.hussienfahmy.semester_history_domain.use_case

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.google.common.truth.Truth.assertThat
import com.hussienfahmy.core.data.local.GradeDao
import com.hussienfahmy.core.data.local.entity.Grade
import com.hussienfahmy.core.data.local.entity.Subject
import com.hussienfahmy.core.data.local.model.GradeName
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@SmallTest
class CalculateSemesterGPATest {

    private lateinit var gradeDao: GradeDao
    private lateinit var calculateSemesterGPA: CalculateSemesterGPA

    private val grades = listOf(
        Grade(GradeName.A, active = true, points = 4.0, percentage = 90.0),
        Grade(GradeName.B, active = true, points = 3.0, percentage = 75.0),
        // NP/NF never seeded as Grade rows.
    )

    @Before
    fun setUp() {
        gradeDao = mockk()
        every { gradeDao.grades } returns flowOf(grades)
        calculateSemesterGPA = CalculateSemesterGPA(gradeDao)
    }

    @Test
    fun `NP subject excluded from gpa and gpaCreditHours`() = runTest {
        val subjects = listOf(
            Subject(name = "Calc", creditHours = 3.0, gradeName = GradeName.A),
            Subject(name = "Physics", creditHours = 3.0, gradeName = GradeName.B),
            Subject(name = "Seminar", creditHours = 2.0, gradeName = GradeName.NP),
        )

        val result = calculateSemesterGPA(subjects)

        // (4.0*3 + 3.0*3) / 6 = 3.5, the 2 NP hours don't count.
        assertThat(result.gpa).isWithin(0.001).of(3.5)
        assertThat(result.gpaCreditHours).isEqualTo(6)
    }

    @Test
    fun `all NpNf subjects return zero without dividing by zero`() = runTest {
        val subjects = listOf(
            Subject(name = "Seminar", creditHours = 2.0, gradeName = GradeName.NP),
            Subject(name = "Internship", creditHours = 1.0, gradeName = GradeName.NF),
        )

        val result = calculateSemesterGPA(subjects)

        assertThat(result.gpa).isEqualTo(0.0)
        assertThat(result.gpaCreditHours).isEqualTo(0)
    }

    @Test
    fun `subject with null grade excluded same as NpNf`() = runTest {
        val subjects = listOf(
            Subject(name = "Calc", creditHours = 3.0, gradeName = GradeName.A),
            Subject(name = "Pending", creditHours = 3.0, gradeName = null),
        )

        val result = calculateSemesterGPA(subjects)

        assertThat(result.gpa).isWithin(0.001).of(4.0)
        assertThat(result.gpaCreditHours).isEqualTo(3)
    }
}
