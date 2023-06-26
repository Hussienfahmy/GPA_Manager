package com.hussienfahmy.core.data.local.entity

import androidx.test.filters.SmallTest
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test

@SmallTest
class GradeTest {

    private val testGradeB = Grade(
        fullName = Grade.Name.B,
        "B",
        true,
    )

    @Test
    fun compareTo_Equals() {
        assertThat(testGradeB.fullName >= Grade.Name.B, `is`(true))
        assertThat(testGradeB.fullName <= Grade.Name.B, `is`(true))
    }

    @Test
    fun compareTo_More() {
        assertThat(testGradeB.fullName > Grade.Name.C, `is`(true))
    }

    @Test
    fun compareTo_Less() {
        assertThat(testGradeB.fullName < Grade.Name.A, `is`(true))
    }
}