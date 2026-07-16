package com.hussienfahmy.core.model

import androidx.compose.runtime.Composable
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.getString
import org.jetbrains.compose.resources.stringResource

sealed class UiText {
    data class DynamicString(val text: String) : UiText()
    data class Resource(
        val resource: StringResource,
        val args: List<Any> = emptyList(),
    ) : UiText()

    @Composable
    fun asString(): String = when (this) {
        is DynamicString -> text
        is Resource -> if (args.isEmpty()) stringResource(resource) else stringResource(
            resource,
            *args.toTypedArray()
        )
    }

    suspend fun asStringSuspend(): String = when (this) {
        is DynamicString -> text
        is Resource -> if (args.isEmpty()) getString(resource) else getString(
            resource,
            *args.toTypedArray()
        )
    }
}