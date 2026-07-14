package com.hussienfahmy.core_ui.theme

import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.resources.Font
import com.hussienfahmy.core_ui.generated.resources.Res
import com.hussienfahmy.core_ui.generated.resources.nunito_400
import com.hussienfahmy.core_ui.generated.resources.nunito_600
import com.hussienfahmy.core_ui.generated.resources.nunito_700
import com.hussienfahmy.core_ui.generated.resources.nunito_800
import com.hussienfahmy.core_ui.generated.resources.nunito_900

/**
 * Nunito, bundled as a Compose Multiplatform resource (latin subset; non-latin glyphs fall back
 * to the system font). Compose Multiplatform's Font() is @Composable-only (it needs the resource
 * environment from composition), so this - and everything built from it below - has to be a
 * function, not a top-level val like the old Android-resource-based version.
 *
 * Design weight rules: 900 numerals/heroes · 800 titles · 700 body · 800 caps-labels.
 */
@Composable
private fun nunitoFontFamily(): FontFamily = FontFamily(
    Font(Res.font.nunito_400, FontWeight.Normal),
    Font(Res.font.nunito_600, FontWeight.SemiBold),
    Font(Res.font.nunito_700, FontWeight.Bold),
    Font(Res.font.nunito_800, FontWeight.ExtraBold),
    Font(Res.font.nunito_900, FontWeight.Black),
)

/** Hero numeral — 46/900 (cumulative GPA). */
@Composable
fun HeroNumber(): TextStyle = TextStyle(
    fontFamily = nunitoFontFamily(),
    fontWeight = FontWeight.Black,
    fontSize = 46.sp,
    lineHeight = 46.sp,
)

/** Compact hero numeral — 36/900 (predict "needed" GPA). */
@Composable
fun HeroNumberSmall(): TextStyle = TextStyle(
    fontFamily = nunitoFontFamily(),
    fontWeight = FontWeight.Black,
    fontSize = 36.sp,
    lineHeight = 36.sp,
)

/** Caps-label — 11/800, +8% tracking (CUMULATIVE GPA, TARGET…). */
@Composable
fun CapsLabelStyle(): TextStyle = TextStyle(
    fontFamily = nunitoFontFamily(),
    fontWeight = FontWeight.ExtraBold,
    fontSize = 11.sp,
    letterSpacing = 0.08.em,
)

@Composable
fun MeadowTypography(): Typography {
    val nunito = nunitoFontFamily()
    return Typography(
        displayLarge = HeroNumber(),
        displayMedium = HeroNumberSmall(),
        displaySmall = TextStyle(
            fontFamily = nunito,
            fontWeight = FontWeight.Black,
            fontSize = 22.sp,
        ),
        headlineLarge = TextStyle(
            fontFamily = nunito,
            fontWeight = FontWeight.Black,
            fontSize = 18.sp,
        ),
        headlineMedium = TextStyle(
            fontFamily = nunito,
            fontWeight = FontWeight.Black,
            fontSize = 17.sp,
        ),
        headlineSmall = TextStyle(
            fontFamily = nunito,
            fontWeight = FontWeight.Black,
            fontSize = 16.sp,
        ),
        titleLarge = TextStyle(
            fontFamily = nunito,
            fontWeight = FontWeight.ExtraBold,
            fontSize = 16.sp,
        ),
        titleMedium = TextStyle(
            fontFamily = nunito,
            fontWeight = FontWeight.ExtraBold,
            fontSize = 14.sp,
        ),
        titleSmall = TextStyle(
            fontFamily = nunito,
            fontWeight = FontWeight.ExtraBold,
            fontSize = 12.5.sp,
        ),
        bodyLarge = TextStyle(
            fontFamily = nunito,
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp,
            lineHeight = 20.sp,
        ),
        bodyMedium = TextStyle(
            fontFamily = nunito,
            fontWeight = FontWeight.SemiBold,
            fontSize = 12.5.sp,
            lineHeight = 19.sp,
        ),
        bodySmall = TextStyle(
            fontFamily = nunito,
            fontWeight = FontWeight.Bold,
            fontSize = 11.5.sp,
            lineHeight = 17.sp,
        ),
        labelLarge = TextStyle(
            fontFamily = nunito,
            fontWeight = FontWeight.ExtraBold,
            fontSize = 13.5.sp,
        ),
        labelMedium = TextStyle(
            fontFamily = nunito,
            fontWeight = FontWeight.ExtraBold,
            fontSize = 12.5.sp,
        ),
        labelSmall = TextStyle(
            fontFamily = nunito,
            fontWeight = FontWeight.ExtraBold,
            fontSize = 11.sp,
        ),
    )
}
