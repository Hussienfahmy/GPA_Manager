package com.hussienfahmy.core_ui.presentation.util

fun Double.toStringWithOptionalDecimals(): String {
    return if (this % 1.0 == 0.0) {
        this.toInt().toString()
    } else {
        this.toString()
    }
}
