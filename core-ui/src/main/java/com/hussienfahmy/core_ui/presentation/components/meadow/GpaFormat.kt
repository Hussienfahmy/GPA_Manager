package com.hussienfahmy.core_ui.presentation.components.meadow

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.PlainTooltip
import androidx.compose.material3.Text
import androidx.compose.material3.TooltipBox
import androidx.compose.material3.TooltipDefaults
import androidx.compose.material3.rememberTooltipState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import java.util.Locale
import kotlin.math.floor
import kotlin.math.pow

/** The 3-decimal figure shown throughout the app for any GPA value. */
fun Float.asGpa(): String = String.format(Locale.getDefault(), "%.3f", this)
fun Double.asGpa(): String = toFloat().asGpa()

/**
 * Truncates rather than rounds. Round-half-up can push a figure that claims an
 * upper bound (e.g. "max achievable GPA") up to or past a target it hasn't
 * actually reached. Use this for any GPA number presented as a ceiling, never
 * for a plain reported value.
 */
fun Float.asGpaFloor(decimals: Int = 3): String {
    val factor = 10.0.pow(decimals)
    val truncated = floor(this * factor) / factor
    return String.format(Locale.getDefault(), "%.${decimals}f", truncated)
}
fun Double.asGpaFloor(decimals: Int = 3): String = toFloat().asGpaFloor(decimals)

/** Higher-precision string for the tooltip revealing the value behind a rounded GPA figure. */
fun Float.asGpaFull(): String = String.format(Locale.getDefault(), "%.6f", this)
fun Double.asGpaFull(): String = toFloat().asGpaFull()

/**
 * Wraps a rounded GPA figure with a tooltip showing the unrounded value —
 * long-press on touch, hover on mouse/stylus/pointer input (Material3
 * TooltipBox default gesture handling).
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GpaTooltipBox(
    fullValue: Float,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    TooltipBox(
        positionProvider = TooltipDefaults.rememberPlainTooltipPositionProvider(),
        tooltip = { PlainTooltip { Text(fullValue.asGpaFull()) } },
        state = rememberTooltipState(),
        modifier = modifier,
        content = content,
    )
}

@Composable
fun GpaTooltipBox(
    fullValue: Double,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) = GpaTooltipBox(fullValue = fullValue.toFloat(), modifier = modifier, content = content)
