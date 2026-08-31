package com.hussienfahmy.onboarding_presentation.sign_in

import androidx.lifecycle.viewModelScope
import com.hussienfahmy.core.domain.analytics.AnalyticsLogger
import com.hussienfahmy.core.domain.analytics.UserPropertyValues
import com.hussienfahmy.core.domain.auth.repository.AuthResult
import com.hussienfahmy.core.domain.sync.SyncDownload
import com.hussienfahmy.core.domain.user_data.repository.UserDataRepository
import com.hussienfahmy.core.domain.user_data.use_cases.RefreshFcmToken
import com.hussienfahmy.core.model.UiText.DynamicString
import com.hussienfahmy.core_ui.presentation.model.UiEvent.ShowSnackBar
import com.hussienfahmy.core_ui.presentation.viewmodel.UiViewModel
import kotlinx.coroutines.launch

class SignInViewModel(
    private val userDataRepository: UserDataRepository,
    private val analyticsLogger: AnalyticsLogger,
    private val syncDownload: SyncDownload,
    private val refreshFcmToken: RefreshFcmToken,
) : UiViewModel<AuthEvent, SignInState>(initialState = {
    SignInState.Initial
}) {

    fun setLoadingState() {
        state.value = SignInState.Loading
    }

    override fun onEvent(event: AuthEvent) {
        when (event) {
            is AuthEvent.OnSignInResult -> {
                when (val signInResult = event.signInResult) {
                    is AuthResult.Success -> {
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

                            // Fetch and push the device's FCM token now that a user record
                            // exists - the platform token callback fires at app launch, before
                            // sign-in, when the write would be a no-op.
                            runCatching { refreshFcmToken() }

                            // Sync data from Firebase before reporting success
                            state.value = SignInState.Syncing
                            syncDownload(signInResult.userData.id)

                            state.value = SignInState.Success
                        }
                    }

                    else -> viewModelScope.launch {
                        state.value = SignInState.Error
                        _uiEvent.send(
                            ShowSnackBar(
                                DynamicString(
                                    (event.signInResult as? AuthResult.Error)?.message
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
