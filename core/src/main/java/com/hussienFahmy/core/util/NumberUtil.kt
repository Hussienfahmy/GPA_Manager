package com.hussienFahmy.core.util

import java.math.BigDecimal
import java.math.RoundingMode

fun Float.toTwoDecimals(): Float {
    return BigDecimal("$this").setScale(2, RoundingMode.DOWN).toFloat()
}