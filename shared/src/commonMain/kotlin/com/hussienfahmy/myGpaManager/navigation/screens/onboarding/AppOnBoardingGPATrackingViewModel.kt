package com.hussienfahmy.myGpaManager.navigation.screens.onboarding

import androidx.lifecycle.viewModelScope
import com.hussienfahmy.core.domain.user_data.model.UserData
import com.hussienfahmy.core_ui.presentation.viewmodel.UiViewModel
import com.hussienfahmy.myGpaManager.navigation.screens.onboarding.models.AppOnBoardingGPATrackingEvent
import com.hussienfahmy.myGpaManager.navigation.screens.onboarding.models.AppOnBoardingGPATrackingState
import com.hussienfahmy.semester_history_domain.model.Semester
import com.hussienfahmy.semester_history_domain.use_case.AddPastSemester
import com.hussienfahmy.semester_history_domain.use_case.CalculateCumulativeFromHistory
import com.hussienfahmy.semester_history_domain.use_case.DeleteSemester
import com.hussienfahmy.semester_history_domain.use_case.GetSemesterHistory
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/**
 * Subject CRUD for a DETAILED semester is fully delegated to
 * [com.hussienfahmy.semester_history_presentation.SemesterDetailRoot] (its own
 * self-contained screen + view model).
 */
class AppOnBoardingGPATrackingViewModel(
    getSemesterHistory: GetSemesterHistory,
    calculateCumulativeFromHistory: CalculateCumulativeFromHistory,
    private val addPastSemester: AddPastSemester,
    private val deleteSemester: DeleteSemester,
) : UiViewModel<AppOnBoardingGPATrackingEvent, AppOnBoardingGPATrackingState>(
    initialState = { AppOnBoardingGPATrackingState() }
) {

    val semesters: StateFlow<List<Semester>> = getSemesterHistory()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val cumulative: StateFlow<UserData.AcademicProgress> = semesters
        .map { semesters ->
            calculateCumulativeFromHistory(semesters)
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            UserData.AcademicProgress(cumulativeGPA = 0.0, creditHours = 0)
        )

    override fun onEvent(event: AppOnBoardingGPATrackingEvent) {
        when (event) {
            is AppOnBoardingGPATrackingEvent.ShowAddSheet ->
                state.value = state.value.copy(showAddSheet = true)

            is AppOnBoardingGPATrackingEvent.HideAddSheet ->
                state.value = state.value.copy(showAddSheet = false)

            is AppOnBoardingGPATrackingEvent.DeleteSemesterEvent ->
                viewModelScope.launch { deleteSemester(event.id) }

            is AppOnBoardingGPATrackingEvent.AddSummarySemester ->
                viewModelScope.launch {
                    addPastSemester(
                        AddPastSemester.Request.Summary(
                            label = event.label,
                            semesterGPA = event.gpa,
                            totalCreditHours = event.hours,
                            level = event.level
                        )
                    )
                }

        }
    }

    // Called directly from the add-sheet's click handler — the caller awaits
    // the new id to navigate straight into it, same as tapping a Detailed
    // card in History.
    suspend fun addDetailedSemester(label: String, level: Int): Long =
        addPastSemester(AddPastSemester.Request.Detailed(label = label, level = level))
}
