package com.hussienfahmy.onboarding_presentation.sign_in

import androidx.lifecycle.viewModelScope
import com.hussienFahmy.core.domain.user_data.repository.UserDataRepository
import com.hussienFahmy.core.model.UiText.DynamicString
import com.hussienFahmy.core_ui.presentation.model.UiEvent.ShowSnackBar
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

                    else -> viewModelScope.launch {
                        _uiEvent.send(
                            ShowSnackBar(
                                DynamicString(
                                    (event.signInResult as? SignInResult.Error)?.message
                                        ?: "Unknown error"
                                )
                            )
                        )
                    }
                }
            }
        }
    }
}