package com.hussienfahmy.semester_history_presentation

import com.hussienfahmy.core.model.UiText

sealed interface SemesterDetailEvent {
    data class ShowError(val message: UiText) : SemesterDetailEvent
}
