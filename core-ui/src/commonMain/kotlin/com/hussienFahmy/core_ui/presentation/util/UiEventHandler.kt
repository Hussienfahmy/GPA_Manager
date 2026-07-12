package com.hussienfahmy.core_ui.presentation.util

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import com.hussienfahmy.core_ui.presentation.model.UiEvent
import kotlinx.coroutines.flow.Flow

// Android's Toast has no Compose Multiplatform equivalent, and the only real caller
// (QuickViewModel) already passes a snackBarHostState - routing ShowToast through the same
// snackbar path as ShowSnackBar drops the Toast dependency entirely rather than needing a
// platform-specific expect/actual for something this narrow.
@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun UiEventHandler(
    key: Any? = Unit,
    uiEvent: Flow<UiEvent>,
    snackBarHostState: SnackbarHostState? = null,
) {
    val keyboardController = LocalSoftwareKeyboardController.current

    LaunchedEffect(key) {
        uiEvent.collect { event ->
            when (event) {
                is UiEvent.ShowSnackBar -> {
                    keyboardController?.hide()
                    snackBarHostState?.showSnackbar(
                        message = event.message.asStringSuspend()
                    )
                }

                is UiEvent.ShowToast -> {
                    keyboardController?.hide()
                    snackBarHostState?.showSnackbar(
                        message = event.message.asStringSuspend()
                    )
                }
            }
        }
    }
}
