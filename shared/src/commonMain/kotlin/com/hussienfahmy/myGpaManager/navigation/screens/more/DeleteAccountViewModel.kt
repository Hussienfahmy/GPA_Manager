package com.hussienfahmy.myGpaManager.navigation.screens.more

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.hussienfahmy.core.domain.auth.repository.AuthRepository
import com.hussienfahmy.core.generated.resources.Res
import com.hussienfahmy.core.generated.resources.delete_account_error
import com.hussienfahmy.core.model.UiText
import com.hussienfahmy.core_ui.presentation.model.UiEvent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import com.hussienfahmy.core.domain.user_data.use_cases.DeleteAccount as DeleteAccountUseCase

class DeleteAccountViewModel(
    authRepository: AuthRepository,
    private val deleteAccountUseCase: DeleteAccountUseCase,
    private val applicationScope: CoroutineScope,
) : ViewModel() {

    // Guests only have local data, so the screen shows a shorter warning for them.
    val isAnonymous: StateFlow<Boolean?> = authRepository.isAnonymousFlow

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    var isDeleting by mutableStateOf(false)
        private set

    fun deleteAccount() {
        if (isDeleting) return
        // applicationScope, not viewModelScope: a successful delete flips auth state and tears
        // this screen (and ViewModel) down before the local wipe finishes.
        applicationScope.launch {
            isDeleting = true
            runCatching { deleteAccountUseCase() }
                .onFailure {
                    isDeleting = false
                    _uiEvent.send(UiEvent.ShowSnackBar(UiText.Resource(Res.string.delete_account_error)))
                }
        }
    }
}
