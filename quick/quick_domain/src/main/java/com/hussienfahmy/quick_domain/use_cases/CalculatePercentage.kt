package com.hussienfahmy.quick_domain.use_cases

import com.hussienfahmy.core.domain.gpa_settings.use_case.GetGPASettings

class CalculatePercentage(
    private val getGPASettings: GetGPASettings,
) {
    suspend operator fun invoke(
        cumulativeGPA: Float,
    ): Float {
        val gpaSystemNumber = getGPASettings().system.number
        return cumulativeGPA / gpaSystemNumber * 100
    }
}