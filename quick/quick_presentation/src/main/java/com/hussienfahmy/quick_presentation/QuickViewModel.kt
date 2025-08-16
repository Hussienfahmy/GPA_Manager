package com.hussienfahmy.quick_presentation

import androidx.lifecycle.viewModelScope
import com.hussienfahmy.core.domain.user_data.use_cases.GetAcademicProgress
import com.hussienfahmy.core.model.UiText
import com.hussienfahmy.core_ui.presentation.model.UiEvent
import com.hussienfahmy.core_ui.presentation.viewmodel.UiViewModel
import com.hussienfahmy.myGpaManager.core.R
import com.hussienfahmy.quick_domain.use_cases.CalculatePercentage
import com.hussienfahmy.quick_domain.use_cases.QuickCalculate
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class QuickViewModel @Inject constructor(
    getAcademicProgress: GetAcademicProgress,
    private val quickCalculate: QuickCalculate,
    private val calculatePercentage: CalculatePercentage,
) : UiViewModel<QuickEvent, QuickState>(initialState = {
    QuickState()
}) {

    init {
        viewModelScope.launch {
            getAcademicProgress()?.let {
                state.value = state.value.copy(
                    academicProgress = it,
                    isLoading = false
                )
            }
        }
    }

    override fun onEvent(event: QuickEvent) {
        viewModelScope.launch {
            val result = when (event) {
                is QuickEvent.Calculate -> quickCalculate(event.calculationRequest)
            }

            when (result) {
                QuickCalculate.Result.InValidCumulativeGPA -> state.value = state.value.copy(
                    invalidCumulativeGPAInput = true,
                    invalidCumulativeGPAAboveMax = false,
                    invalidSemesterGPAAboveMax = false,
                    invalidTotalHoursInput = false,
                    invalidSemesterGPAInput = false,
                    invalidSemesterHoursInput = false,
                    cumulativeGPAPercentage = 0.0f,
                    cumulativeGPA = 0.0f,
                )

                QuickCalculate.Result.CumulativeGPAAboveMax -> state.value = state.value.copy(
                    invalidCumulativeGPAAboveMax = true,
                    invalidSemesterGPAAboveMax = false,
                    invalidCumulativeGPAInput = false,
                    invalidTotalHoursInput = false,
                    invalidSemesterGPAInput = false,
                    invalidSemesterHoursInput = false,
                    cumulativeGPAPercentage = 0.0f,
                    cumulativeGPA = 0.0f,
                )

                QuickCalculate.Result.InValidSemesterGPA -> state.value = state.value.copy(
                    invalidSemesterGPAInput = true,
                    invalidTotalHoursInput = false,
                    invalidCumulativeGPAInput = false,
                    invalidSemesterHoursInput = false,
                    invalidCumulativeGPAAboveMax = false,
                    invalidSemesterGPAAboveMax = false,
                    cumulativeGPAPercentage = 0.0f,
                    cumulativeGPA = 0.0f,
                )

                QuickCalculate.Result.SemesterGPAAboveMax -> state.value = state.value.copy(
                    invalidCumulativeGPAAboveMax = false,
                    invalidSemesterGPAAboveMax = true,
                    invalidCumulativeGPAInput = false,
                    invalidTotalHoursInput = false,
                    invalidSemesterGPAInput = false,
                    invalidSemesterHoursInput = false,
                    cumulativeGPAPercentage = 0.0f,
                    cumulativeGPA = 0.0f,
                )

                QuickCalculate.Result.InValidSemesterHours -> state.value = state.value.copy(
                    invalidSemesterHoursInput = true,
                    invalidTotalHoursInput = false,
                    invalidCumulativeGPAInput = false,
                    invalidSemesterGPAInput = false,
                    invalidCumulativeGPAAboveMax = false,
                    invalidSemesterGPAAboveMax = false,
                    cumulativeGPAPercentage = 0.0f,
                    cumulativeGPA = 0.0f,
                )

                QuickCalculate.Result.InValidTotalHours -> state.value = state.value.copy(
                    invalidTotalHoursInput = true,
                    invalidSemesterHoursInput = false,
                    invalidCumulativeGPAInput = false,
                    invalidSemesterGPAInput = false,
                    invalidCumulativeGPAAboveMax = false,
                    invalidSemesterGPAAboveMax = false,
                    cumulativeGPAPercentage = 0.0f,
                    cumulativeGPA = 0.0f,
                )

                QuickCalculate.Result.TotalHoursIsZero -> _uiEvent.send(
                    UiEvent.ShowToast(
                        UiText.StringResource(
                            R.string.total_hours_is_zero
                        )
                    )
                )

                is QuickCalculate.Result.Success ->
                    state.value = state.value.copy(
                        cumulativeGPA = result.newCumulativeGPA,
                        cumulativeGPAPercentage = calculatePercentage(result.newCumulativeGPA),
                        invalidCumulativeGPAInput = false,
                        invalidSemesterGPAInput = false,
                        invalidSemesterHoursInput = false,
                        invalidTotalHoursInput = false,
                    )
            }
        }
    }
}