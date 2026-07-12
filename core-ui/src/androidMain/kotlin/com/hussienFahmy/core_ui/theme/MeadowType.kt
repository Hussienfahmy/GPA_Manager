package com.hussienfahmy.core_ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import com.hussienfahmy.core_ui.R

/**
 * Nunito, bundled in the APK (latin subset; non-latin glyphs fall back to the system font).
 *
 * Design weight rules: 900 numerals/heroes · 800 titles · 700 body · 800 caps-labels.
 */
val Nunito = FontFamily(
    Font(R.font.nunito_400, FontWeight.Normal),
    Font(R.font.nunito_600, FontWeight.SemiBold),
    Font(R.font.nunito_700, FontWeight.Bold),
    Font(R.font.nunito_800, FontWeight.ExtraBold),
    Font(R.font.nunito_900, FontWeight.Black),
)

/** Hero numeral — 46/900 (cumulative GPA). */
val HeroNumber = TextStyle(
    fontFamily = Nunito,
    fontWeight = FontWeight.Black,
    fontSize = 46.sp,
    lineHeight = 46.sp,
)

/** Compact hero numeral — 36/900 (predict "needed" GPA). */
val HeroNumberSmall = TextStyle(
    fontFamily = Nunito,
    fontWeight = FontWeight.Black,
    fontSize = 36.sp,
    lineHeight = 36.sp,
)

/** Caps-label — 11/800, +8% tracking (CUMULATIVE GPA, TARGET…). */
val CapsLabelStyle = TextStyle(
    fontFamily = Nunito,
    fontWeight = FontWeight.ExtraBold,
    fontSize = 11.sp,
    letterSpacing = 0.08.em,
)

val MeadowTypography = Typography(
    displayLarge = HeroNumber,
    displayMedium = HeroNumberSmall,
    displaySmall = TextStyle(
        fontFamily = Nunito,
        fontWeight = FontWeight.Black,
        fontSize = 22.sp,
    ),
    headlineLarge = TextStyle(
        fontFamily = Nunito,
        fontWeight = FontWeight.Black,
        fontSize = 18.sp,
    ),
    headlineMedium = TextStyle(
        fontFamily = Nunito,
        fontWeight = FontWeight.Black,
        fontSize = 17.sp,
    ),
    headlineSmall = TextStyle(
        fontFamily = Nunito,
        fontWeight = FontWeight.Black,
        fontSize = 16.sp,
    ),
    titleLarge = TextStyle(
        fontFamily = Nunito,
        fontWeight = FontWeight.ExtraBold,
        fontSize = 16.sp,
    ),
    titleMedium = TextStyle(
        fontFamily = Nunito,
        fontWeight = FontWeight.ExtraBold,
        fontSize = 14.sp,
    ),
    titleSmall = TextStyle(
        fontFamily = Nunito,
        fontWeight = FontWeight.ExtraBold,
        fontSize = 12.5.sp,
    ),
    bodyLarge = TextStyle(
        fontFamily = Nunito,
        fontWeight = FontWeight.Bold,
        fontSize = 14.sp,
        lineHeight = 20.sp,
    ),
    bodyMedium = TextStyle(
        fontFamily = Nunito,
        fontWeight = FontWeight.SemiBold,
        fontSize = 12.5.sp,
        lineHeight = 19.sp,
    ),
    bodySmall = TextStyle(
        fontFamily = Nunito,
        fontWeight = FontWeight.Bold,
        fontSize = 11.5.sp,
        lineHeight = 17.sp,
    ),
    labelLarge = TextStyle(
        fontFamily = Nunito,
        fontWeight = FontWeight.ExtraBold,
        fontSize = 13.5.sp,
    ),
    labelMedium = TextStyle(
        fontFamily = Nunito,
        fontWeight = FontWeight.ExtraBold,
        fontSize = 12.5.sp,
    ),
    labelSmall = TextStyle(
        fontFamily = Nunito,
        fontWeight = FontWeight.ExtraBold,
        fontSize = 11.sp,
    ),
)
