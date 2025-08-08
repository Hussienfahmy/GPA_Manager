package com.hussienFahmy.core.data.local.entity

import androidx.test.filters.SmallTest
import com.hussienFahmy.core.data.local.model.GradeName
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test

@SmallTest
class GradeTest {

    private val testGradeB = Grade(
        name = GradeName.B,
        active = true,
    )

    @Test
    fun compareTo_Equals() {
        assertThat(testGradeB.name >= GradeName.B, `is`(true))
        assertThat(testGradeB.name <= GradeName.B, `is`(true))
    }

    @Test
    fun compareTo_More() {
        assertThat(testGradeB.name > GradeName.C, `is`(true))
    }

    @Test
    fun compareTo_Less() {
        assertThat(testGradeB.name < GradeName.A, `is`(true))
    }
}