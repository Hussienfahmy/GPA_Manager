package com.hussienfahmy.grades_setting_domain.use_case

import com.google.common.truth.Truth.*
import com.hussienfahmy.core.data.local.GradeDao
import com.hussienfahmy.core.data.local.SubjectDao
import com.hussienfahmy.core.data.local.entity.Grade
import com.hussienfahmy.core.data.local.model.GradeName
import com.hussienfahmy.core.data.local.util.UpdateResult
import com.hussienfahmy.grades_setting_domain.model.GradeSetting
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test


@OptIn(ExperimentalCoroutinesApi::class)
class ActivateGradeTest {

    private lateinit var activateGrade: ActivateGrade
    private lateinit var subjectDao: SubjectDao
    private lateinit var gradeDao: GradeDao
    private lateinit var getGradeByName: GetGradeByName

    @Before
    fun setUp() {
        subjectDao = mockk(relaxed = true)

        gradeDao = mockk(relaxed = true)

        getGradeByName = mockk<GetGradeByName>(relaxed = true)

        activateGrade = ActivateGrade(
            subjectDao = subjectDao,
            gradeDao = gradeDao,
            getGradeByName = getGradeByName
        )
    }

    @Test
    fun `DeActivate SubjectsClearTheGrade ReturnSuccess`() = runTest {
        val testGrade = Grade(
            metaData = GradeName.A,
            percentage = null,
            points = null,
            active = true
        )

        coEvery { getGradeByName.invoke(any()) } returns testGrade

        val result = activateGrade(GradeSetting(testGrade), false)

        coEvery { subjectDao.clearUnAvailableGrades() }
        assertThat(result).isInstanceOf(UpdateResult.Success::class.java)
    }

    @Test
    fun `Activate PercentageNullAndPointsNull ReturnFailed`() = runTest {
        val testGrade = Grade(
            metaData = GradeName.A,
            percentage = null,
            points = null,
            active = false
        )

        coEvery { getGradeByName.invoke(any()) } returns testGrade

        val result = activateGrade(GradeSetting(testGrade), true)
        assertThat(result).isInstanceOf(UpdateResult.Failed::class.java)
    }

    @Test
    fun `Activate PercentageAndPointsNotNull ReturnFailed`() = runTest {
        val testGrade = Grade(
            metaData = GradeName.A,
            percentage = 85.0,
            points = 3.67,
            active = false
        )
        coEvery { getGradeByName.invoke(any()) } returns testGrade

        val result = activateGrade(GradeSetting(testGrade), true)
        assertThat(result).isInstanceOf(UpdateResult.Success::class.java)
    }
}