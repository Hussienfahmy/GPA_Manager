package com.hussienfahmy.onboarding_presentation.sign_in

import androidx.lifecycle.viewModelScope
import com.hussienFahmy.core.domain.user_data.repository.UserDataRepository
import com.hussienFahmy.core.model.UiText
import com.hussienFahmy.core_ui.presentation.model.UiEvent
import com.hussienFahmy.core_ui.presentation.viewmodel.UiViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val userDataRepository: UserDataRepository,
) : UiViewModel<AuthEvent, SignInState>(initialState = {
    SignInState.Initial
}) {

    override fun onEvent(event: AuthEvent) {
        when (event) {
            is AuthEvent.OnSignInResult -> {
                when (event.signInResult) {
                    is SignInResult.Error -> {
                        viewModelScope.launch {
                            _uiEvent.send(
                                UiEvent.ShowSnackBar(
                                    UiText.DynamicString(event.signInResult.message)
                                )
                            )
                        }
                    }

                    is SignInResult.Success -> {
                        viewModelScope.launch {
                            val isUserExists = userDataRepository.isUserExists()
                            if (!isUserExists) {
                                with(event.signInResult.data) {
                                    userDataRepository.createUserData(
                                        name = name,
                                        email = email,
                                        photoUrl = photoUrl,
                                        id = id
                                    )
                                }
                            }

                            state.value = SignInState.Success
                        }
                    }
                }
            }
        }
    }
}