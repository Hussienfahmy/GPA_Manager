package com.hussienfahmy.core_ui.presentation.viewmodel

import androidx.lifecycle.ViewModel
import com.hussienfahmy.core_ui.presentation.model.UiEvent
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow

abstract class EventViewModel<T>: ViewModel() {

    protected val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    abstract fun onEvent(event: T)
}