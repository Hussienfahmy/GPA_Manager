package com.hussienfahmy.core_ui.presentation.user_data

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import com.hussienfahmy.core.data.local.util.UpdateResult
import com.hussienfahmy.core.domain.analytics.AnalyticsLogger
import com.hussienfahmy.core.domain.analytics.AnalyticsValues
import com.hussienfahmy.core.domain.user_data.use_cases.UserDataUseCases
import com.hussienfahmy.core_ui.presentation.model.UiEvent
import com.hussienfahmy.core_ui.presentation.viewmodel.UiViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class UserDataViewModel(
    private val userDataUseCases: UserDataUseCases,
    private val analyticsLogger: AnalyticsLogger,
) : UiViewModel<UserDataEvent, UserDataState>(
    initialState = { UserDataState.Loading }
) {

    val customState = userDataUseCases.observeUserData()
        .map { data ->
            data?.let {
                UserDataState.Loaded(it)
            } ?: UserDataState.Loading
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(1000),
            initialValue = UserDataState.Loading
        )

    var uploadingPhoto by mutableStateOf(false)

    override fun onEvent(event: UserDataEvent) {
        viewModelScope.launch {
            val updateResult: Any = when (event) {
                is UserDataEvent.UpdateName -> userDataUseCases.updateName(event.name)
                is UserDataEvent.UploadPhoto -> {
                    uploadingPhoto = true
                    userDataUseCases.uploadPhoto(event.photoUri)
                    uploadingPhoto = false
                }
                is UserDataEvent.UpdateUniversity -> {
                    val result = userDataUseCases.updateUniversity(event.university)
                    if (result is UpdateResult.Success) {
                        updateUserAcademicProperties()
                    }
                    result
                }
                is UserDataEvent.UpdateFaculty -> {
                    val result = userDataUseCases.updateFaculty(event.faculty)
                    if (result is UpdateResult.Success) {
                        updateUserAcademicProperties()
                    }
                    result
                }
                is UserDataEvent.UpdateDepartment -> {
                    val result = userDataUseCases.updateDepartment(event.department)
                    if (result is UpdateResult.Success) {
                        updateUserAcademicProperties()
                    }
                    result
                }
                is UserDataEvent.UpdateLevel -> {
                    val result = userDataUseCases.updateLevel(event.level)
                    if (result is UpdateResult.Success) {
                        updateUserAcademicProperties()
                    }
                    result
                }
                is UserDataEvent.UpdateSemester -> userDataUseCases.updateSemester(event.semester)
                is UserDataEvent.UpdateCumulativeGPA -> userDataUseCases.updateCumulativeGPA(event.cumulativeGPA)
                is UserDataEvent.UpdateCreditHours -> userDataUseCases.updateCreditHours(event.creditHours)
            }

            if (updateResult is UpdateResult.Failed) {
                _uiEvent.send(
                    UiEvent.ShowSnackBar(
                        message = updateResult.message,
                    )
                )
            }
        }
    }

    private suspend fun updateUserAcademicProperties() {
        try {
            // Note: User properties could be set here when profile data changes
            // For now, we'll track the update events themselves
            // Future enhancement: Get current user data and update analytics properties
        } catch (e: Exception) {
            // Handle error silently for analytics
        }
    }
}