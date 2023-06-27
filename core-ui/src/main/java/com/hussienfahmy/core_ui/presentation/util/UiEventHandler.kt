package com.hussienfahmy.core_ui.presentation.util

import android.widget.Toast
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import com.hussienfahmy.core_ui.presentation.model.UiEvent
import kotlinx.coroutines.flow.Flow

@Composable
fun UiEventHandler(
    key: Any? = Unit,
    uiEvent: Flow<UiEvent>,
    snackBarHostState: SnackbarHostState? = null,
    onShowSnackBar: () -> Unit = {},
) {
    val context = LocalContext.current

    LaunchedEffect(key) {
        uiEvent.collect { event ->
            when (event) {
                is UiEvent.ShowSnackBar -> {
                    snackBarHostState?.showSnackbar(
                        message = event.message.asString(context = context)
                    )
                    onShowSnackBar()
                }

                is UiEvent.ShowToast -> {
                    Toast.makeText(context, event.message.asString(context), Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
    }
}