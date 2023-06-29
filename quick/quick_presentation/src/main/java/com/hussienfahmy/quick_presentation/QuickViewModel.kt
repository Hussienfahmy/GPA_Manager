package com.hussienfahmy.quick_presentation

import androidx.lifecycle.viewModelScope
import com.hussienFahmy.core.domain.user_data.use_cases.GetAcademicProgress
import com.hussienFahmy.core.model.UiText
import com.hussienFahmy.core_ui.presentation.model.UiEvent
import com.hussienFahmy.core_ui.presentation.viewmodel.UiViewModel
import com.hussienFahmy.myGpaManager.core.R
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
            state.value = state.value.copy(
                academicProgress = getAcademicProgress(),
                isLoading = false
            )
        }
    }

    override fun onEvent(event: QuickEvent) {
        val result = when (event) {
            is QuickEvent.Calculate -> quickCalculate(event.calculationRequest)
        }

        viewModelScope.launch {
            when (result) {
                QuickCalculate.Result.InValidCumulativeGPA -> state.value = state.value.copy(
                    invalidCumulativeGPAInput = true,
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
                    cumulativeGPAPercentage = 0.0f,
                    cumulativeGPA = 0.0f,
                )

                QuickCalculate.Result.InValidSemesterHours -> state.value = state.value.copy(
                    invalidSemesterHoursInput = true,
                    invalidTotalHoursInput = false,
                    invalidCumulativeGPAInput = false,
                    invalidSemesterGPAInput = false,
                    cumulativeGPAPercentage = 0.0f,
                    cumulativeGPA = 0.0f,
                )

                QuickCalculate.Result.InValidTotalHours -> state.value = state.value.copy(
                    invalidTotalHoursInput = true,
                    invalidSemesterHoursInput = false,
                    invalidCumulativeGPAInput = false,
                    invalidSemesterGPAInput = false,
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