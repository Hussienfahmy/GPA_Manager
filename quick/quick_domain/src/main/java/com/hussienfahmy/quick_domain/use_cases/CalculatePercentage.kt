package com.hussienfahmy.quick_domain.use_cases

class CalculatePercentage {

    // todo use from the settings
    private val gpaSystemNumber = 4

    operator fun invoke(
        cumulativeGPA: Float,
    ): Float {
        return cumulativeGPA / gpaSystemNumber * 100
    }
}