package com.hussienfahmy.onboarding_presentation.sign_in

import androidx.lifecycle.viewModelScope
import com.hussienfahmy.core.domain.analytics.AnalyticsLogger
import com.hussienfahmy.core.domain.analytics.UserPropertyValues
import com.hussienfahmy.core.domain.auth.service.AuthServiceResult
import com.hussienfahmy.core.domain.user_data.repository.UserDataRepository
import com.hussienfahmy.core.model.UiText.DynamicString
import com.hussienfahmy.core_ui.presentation.model.UiEvent.ShowSnackBar
import com.hussienfahmy.core_ui.presentation.viewmodel.UiViewModel
import kotlinx.coroutines.launch

class SignInViewModel(
    private val userDataRepository: UserDataRepository,
    private val analyticsLogger: AnalyticsLogger,
) : UiViewModel<AuthEvent, SignInState>(initialState = {
    SignInState.Initial
}) {

    fun setLoadingState() {
        state.value = SignInState.Loading
    }

    override fun onEvent(event: AuthEvent) {
        when (event) {
            is AuthEvent.OnSignInResult -> {
                when (event.signInResult) {
                    is AuthServiceResult.Success -> {
                        viewModelScope.launch {
                            val isUserExists = userDataRepository.isUserExists()
                            if (!isUserExists) {
                                with(event.signInResult.userData) {
                                    userDataRepository.createUserData(
                                        name = name,
                                        email = email,
                                        photoUrl = photoUrl,
                                        id = id
                                    )
                                }
                            }

                            analyticsLogger.logSignInCompleted(
                                userId = event.signInResult.userData.id,
                                isNewUser = !isUserExists
                            )

                            // Set initial user properties
                            if (!isUserExists) {
                                analyticsLogger.setUserType(UserPropertyValues.USER_TYPE_NEW)
                            } else {
                                analyticsLogger.setUserType(UserPropertyValues.USER_TYPE_RETURNING)
                            }

                            state.value = SignInState.Success
                        }
                    }

                    else -> viewModelScope.launch {
                        state.value = SignInState.Error
                        _uiEvent.send(
                            ShowSnackBar(
                                DynamicString(
                                    (event.signInResult as? AuthServiceResult.Error)?.message
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