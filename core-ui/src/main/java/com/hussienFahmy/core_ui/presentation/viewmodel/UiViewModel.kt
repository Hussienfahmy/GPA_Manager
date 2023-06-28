package com.hussienFahmy.core_ui.presentation.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.hussienFahmy.core_ui.presentation.model.UiEvent
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow

/**
 * [T] is the type of the event that will be received by [onEvent]
 *
 */
abstract class UiViewModel<T, R> constructor(initialState: () -> R) : ViewModel() {

    protected val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    abstract fun onEvent(event: T)

    var state = mutableStateOf(initialState())
        private set
}