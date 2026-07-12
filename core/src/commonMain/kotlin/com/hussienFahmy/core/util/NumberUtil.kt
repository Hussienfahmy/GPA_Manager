package com.hussienfahmy.core.util

import kotlin.math.abs
import kotlin.math.ceil
import kotlin.math.floor
import kotlin.math.pow
import kotlin.math.round

// java.math.BigDecimal/RoundingMode and kotlin.text's JVM-only String.format("%.Nf", ...) have no
// Kotlin/Native equivalent, so both functions here are rewritten on plain kotlin.math instead of
// moved as-is. FLAG FOR REVIEW: BigDecimal("$this") parsed the float's *string* (decimal)
// representation, while this.toDouble() below widens the float's *binary* representation - these
// usually agree for the "nice" decimal values GPA/marks entry produces, but it's a real,
// unverified behavioral difference worth double-checking against the old BigDecimal output once a
// real build exists.
fun Float.toTwoDecimals(): Float {
    val factor = 100.0
    val scaled = this.toDouble() * factor
    // RoundingMode.DOWN truncates toward zero (floor for positive, ceil for negative) - not the
    // same as a plain floor(), which would round negative values further from zero.
    val truncated = if (scaled >= 0) floor(scaled) else ceil(scaled)
    return (truncated / factor).toFloat()
}

fun Double.truncate(digits: Int = 4): String {
    val factor = 10.0.pow(digits)
    val truncated = floor(this * factor) / factor
    return truncated.toFixedString(digits)
}

/**
 * Rounds to [digits] decimal places and formats as a fixed-decimal string (e.g. "3.140") -
 * the multiplatform replacement for String.format(Locale.X, "%.Nf", value), which is JVM-only.
 *
 * FLAG FOR REVIEW: this always uses a '.' decimal point, unlike the callers this replaces in
 * GpaFormat.kt/QuickResultCard.kt/ResultCard.kt, which used String.format(Locale.getDefault(),
 * ...) - genuinely locale-sensitive (e.g. a device set to a comma-decimal locale like German
 * would previously show "3,140"). This is a real, user-visible behavior change on those locales,
 * not just a compile-target detail - worth confirming is acceptable (or replacing with a proper
 * locale-aware multiplatform number formatter) before shipping, not something to treat as settled
 * by this rewrite.
 */
fun Double.toFixedString(digits: Int): String {
    val factor = 10.0.pow(digits)
    val isNegative = this < 0
    val scaled = round(abs(this) * factor).toLong()
    val divisor = factor.toLong()
    val intPart = scaled / divisor
    val fracPart = (scaled % divisor).toString().padStart(digits, '0')
    return buildString {
        if (isNegative && (intPart != 0L || fracPart.any { it != '0' })) append('-')
        append(intPart)
        append('.')
        append(fracPart)
    }
}
