package com.hussienFahmy.core_ui.presentation.model

import com.hussienFahmy.core.model.UiText

sealed class UiEvent {
    data class ShowToast(val message: UiText) : UiEvent()
    data class ShowSnackBar(val message: UiText) : UiEvent()
}