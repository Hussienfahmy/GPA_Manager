package com.hussienfahmy.core.util

import java.math.BigDecimal
import java.math.RoundingMode
import kotlin.math.floor
import kotlin.math.pow

fun Float.toTwoDecimals(): Float {
    return BigDecimal("$this").setScale(2, RoundingMode.DOWN).toFloat()
}

fun Double.truncate(digits: Int = 4): String {
    val factor = 10.0.pow(digits.toDouble())
    val truncated = floor(this * factor) / factor
    return "%.${digits}f".format(truncated)
}