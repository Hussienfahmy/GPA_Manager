package com.hussienFahmy.core_ui.presentation.util

import android.widget.Toast
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import com.hussienFahmy.core_ui.presentation.model.UiEvent
import kotlinx.coroutines.flow.Flow

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun UiEventHandler(
    key: Any? = Unit,
    uiEvent: Flow<UiEvent>,
    snackBarHostState: SnackbarHostState? = null,
) {
    val context = LocalContext.current
    val keyboardController = LocalSoftwareKeyboardController.current

    LaunchedEffect(key) {
        uiEvent.collect { event ->
            when (event) {
                is UiEvent.ShowSnackBar -> {
                    keyboardController?.hide()
                    snackBarHostState?.showSnackbar(
                        message = event.message.asString(context = context)
                    )
                }

                is UiEvent.ShowToast -> {
                    Toast.makeText(context, event.message.asString(context), Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
    }
}