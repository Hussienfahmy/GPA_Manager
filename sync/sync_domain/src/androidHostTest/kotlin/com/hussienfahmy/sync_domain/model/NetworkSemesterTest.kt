package com.hussienfahmy.sync_domain.model

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.google.common.truth.Truth.assertThat
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@SmallTest
class NetworkSemesterTest {

    @Test
    fun `missing gpaCreditHours falls back to totalCreditHours - pre-8_2_0 doc`() {
        val networkSemester = NetworkSemester(totalCreditHours = 18, gpaCreditHours = null)

        val entity = networkSemester.toEntity()

        assertThat(entity.gpaCreditHours).isEqualTo(18)
    }

    @Test
    fun `present gpaCreditHours is kept as-is`() {
        val networkSemester = NetworkSemester(totalCreditHours = 18, gpaCreditHours = 15)

        val entity = networkSemester.toEntity()

        assertThat(entity.gpaCreditHours).isEqualTo(15)
    }
}
