package com.hussienfahmy.core.data.local.util

import com.hussienfahmy.core.model.UiText

/**
 * Represents the result of an database update operation.
 */
sealed class UpdateResult {
    object Success : UpdateResult()
    data class Failed(val message: UiText) : UpdateResult()
}