package com.hussienFahmy.core.data.local.entity

import androidx.test.filters.SmallTest
import com.hussienFahmy.core.data.local.model.GradeName
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test

@SmallTest
class GradeTest {

    private val testGradeB = Grade(
        metaData = GradeName.B,
        active = true,
    )

    @Test
    fun compareTo_Equals() {
        assertThat(testGradeB.metaData >= GradeName.B, `is`(true))
        assertThat(testGradeB.metaData <= GradeName.B, `is`(true))
    }

    @Test
    fun compareTo_More() {
        assertThat(testGradeB.metaData > GradeName.C, `is`(true))
    }

    @Test
    fun compareTo_Less() {
        assertThat(testGradeB.metaData < GradeName.A, `is`(true))
    }
}