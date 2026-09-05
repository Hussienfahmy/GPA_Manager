package com.hussienfahmy.onboarding_presentation.sign_in

import androidx.lifecycle.viewModelScope
import com.hussienfahmy.core.domain.auth.repository.AuthRepository
import com.hussienfahmy.core.domain.auth.repository.AuthResult
import com.hussienfahmy.core.domain.auth.repository.AuthUserData
import com.hussienfahmy.core.domain.auth.use_cases.CompleteSignIn
import com.hussienfahmy.core.domain.sample.SeedSampleData
import com.hussienfahmy.core.model.UiText.DynamicString
import com.hussienfahmy.core_ui.presentation.model.UiEvent.ShowSnackBar
import com.hussienfahmy.core_ui.presentation.viewmodel.UiViewModel
import kotlinx.coroutines.launch

class SignInViewModel(
    private val authRepository: AuthRepository,
    private val completeSignIn: CompleteSignIn,
    private val seedSampleData: SeedSampleData,
) : UiViewModel<AuthEvent, SignInState>(initialState = {
    SignInState.Initial
}) {

    fun setLoadingState() {
        state.value = SignInState.Loading
    }

    /**
     * "Explore the demo" - starts a local-only guest session and skips the rest of onboarding.
     * [withSampleData] chooses between a pre-filled example and an empty app.
     */
    fun startGuest(withSampleData: Boolean) {
        viewModelScope.launch {
            state.value = SignInState.Loading
            when (val result = authRepository.signInAnonymously()) {
                is AuthResult.Success -> {
                    state.value = SignInState.Syncing
                    completeSignIn(result.userData)
                    seedSampleData(includeHistory = withSampleData)
                    state.value = SignInState.GuestReady
                }

                is AuthResult.Error -> fail(result.message)
            }
        }
    }

    override fun onEvent(event: AuthEvent) {
        when (event) {
            is AuthEvent.OnSignInResult -> when (val signInResult = event.signInResult) {
                is AuthResult.Success -> viewModelScope.launch {
                    completeAndSucceed(signInResult.userData)
                }

                is AuthResult.Error -> viewModelScope.launch { fail(signInResult.message) }
                null -> viewModelScope.launch { fail("Unknown error") }
            }
        }
    }

    private suspend fun completeAndSucceed(userData: AuthUserData) {
        state.value = SignInState.Syncing
        completeSignIn(userData)
        state.value = SignInState.Success
    }

    private suspend fun fail(message: String) {
        state.value = SignInState.Error
        _uiEvent.send(ShowSnackBar(DynamicString(message)))
    }
}
