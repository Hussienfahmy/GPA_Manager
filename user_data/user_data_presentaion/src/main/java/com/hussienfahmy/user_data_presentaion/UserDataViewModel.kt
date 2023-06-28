package com.hussienfahmy.user_data_presentaion

import androidx.lifecycle.viewModelScope
import com.hussienFahmy.core.data.local.util.UpdateResult
import com.hussienFahmy.core_ui.presentation.model.UiEvent
import com.hussienFahmy.core_ui.presentation.viewmodel.UiViewModel
import com.hussienfahmy.user_data_domain.use_cases.UserDataUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserDataViewModel @Inject constructor(
    private val userDataUseCases: UserDataUseCases,
) : UiViewModel<UserDataEvent, UserDataState>(
    initialState = { UserDataState.Loading }
) {

    init {
        userDataUseCases.observeUserData().onEach {
            state.value = UserDataState.Loaded(it)
        }.launchIn(viewModelScope)
    }

    override fun onEvent(event: UserDataEvent) {
        viewModelScope.launch {
            val updateResult: Any = when (event) {
                is UserDataEvent.UpdateName -> userDataUseCases.updateName(event.name)
                is UserDataEvent.UpdateEmail -> userDataUseCases.updateEmail(event.email)
                is UserDataEvent.UploadPhoto -> userDataUseCases.uploadPhoto(event.photoUri)
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