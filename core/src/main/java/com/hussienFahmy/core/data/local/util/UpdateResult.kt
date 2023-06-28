package com.hussienFahmy.core.data.local.util

import com.hussienFahmy.core.model.UiText

/**
 * Represents the result of an update operation.
 */
sealed class UpdateResult {
    object Success : UpdateResult()
    data class Failed(val message: UiText) : UpdateResult()
}