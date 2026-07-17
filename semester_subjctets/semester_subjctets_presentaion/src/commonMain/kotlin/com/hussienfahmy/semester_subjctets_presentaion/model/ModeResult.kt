package com.hussienfahmy.semester_subjctets_presentaion.model

import com.hussienfahmy.semester_subjctets_domain.use_case.Calculate
import com.hussienfahmy.semester_subjctets_domain.use_case.PredictGrades

sealed class ModeResult(val calculationResult: Calculate.Result) {
    data class Normal(
        val result: Calculate.Result
    ) : ModeResult(result)

    data class Predict(
        val result: Calculate.Result,
        val predictGradesResult: PredictGrades.Result
    ) : ModeResult(result)
}