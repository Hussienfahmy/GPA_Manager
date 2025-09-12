package com.hussienfahmy.core_ui.presentation.user_data

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import com.hussienfahmy.core.data.local.util.UpdateResult
import com.hussienfahmy.core.domain.user_data.use_cases.UserDataUseCases
import com.hussienfahmy.core_ui.presentation.model.UiEvent
import com.hussienfahmy.core_ui.presentation.viewmodel.UiViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class UserDataViewModel(
    private val userDataUseCases: UserDataUseCases,
) : UiViewModel<UserDataEvent, UserDataState>(
    initialState = { UserDataState.Loading }
) {

    val customState = userDataUseCases.observeUserData().filterNotNull()
        .map {
            UserDataState.Loaded(it)
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
                is UserDataEvent.UpdateUniversity -> userDataUseCases.updateUniversity(event.university)
                is UserDataEvent.UpdateFaculty -> userDataUseCases.updateFaculty(event.faculty)
                is UserDataEvent.UpdateDepartment -> userDataUseCases.updateDepartment(event.department)
                is UserDataEvent.UpdateLevel -> userDataUseCases.updateLevel(event.level)
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
}