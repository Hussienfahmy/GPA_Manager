package com.hussienfahmy.core_ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.unit.dp

/** Radii from the design: 12 pills · 14 tiles · 20 cards · 24 hero cards. */
object MeadowRadius {
    val pill = 12.dp
    val tile = 14.dp
    val card = 20.dp
    val hero = 24.dp
}

private val MeadowShapes = Shapes(
    extraSmall = RoundedCornerShape(MeadowRadius.pill),
    small = RoundedCornerShape(MeadowRadius.tile),
    medium = RoundedCornerShape(MeadowRadius.card),
    large = RoundedCornerShape(MeadowRadius.hero),
    extraLarge = RoundedCornerShape(28.dp),
)

/** Accessors for the Meadow design system, analogous to [MaterialTheme]. */
object MeadowTheme {
    val colors: MeadowColors
        @Composable @ReadOnlyComposable get() = LocalMeadowColors.current

    /** Current screen's tab hue. Set it with [MeadowAccentProvider]. */
    val accent: MeadowAccent
        @Composable @ReadOnlyComposable get() = LocalMeadowAccent.current
}

/** Sets the tab hue for a screen subtree (e.g. Marks amber, History violet). */
@Composable
fun MeadowAccentProvider(accent: MeadowAccent, content: @Composable () -> Unit) {
    CompositionLocalProvider(LocalMeadowAccent provides accent, content = content)
}

@Composable
fun MeadowTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val colors = if (darkTheme) MeadowDarkColors else MeadowLightColors

    // Material mapping keeps not-yet-redesigned screens on the Meadow palette.
    val colorScheme = if (darkTheme) {
        darkColorScheme(
            primary = colors.semester.accent,
            onPrimary = colors.semester.onAccent,
            primaryContainer = colors.semester.container,
            onPrimaryContainer = colors.semester.deep,
            secondary = colors.ink,
            onSecondary = colors.paper,
            secondaryContainer = colors.surfaceSunken,
            onSecondaryContainer = colors.inkMuted,
            tertiary = colors.semester.soft,
            background = colors.paper,
            onBackground = colors.ink,
            surface = colors.card,
            onSurface = colors.ink,
            surfaceVariant = colors.chipBg,
            onSurfaceVariant = colors.inkMuted,
            outline = colors.inkDisabled,
            outlineVariant = colors.divider,
            error = colors.danger,
            onError = colors.onDanger,
            errorContainer = colors.dangerContainer,
            onErrorContainer = colors.onDangerContainer,
            surfaceContainerLowest = colors.navBg,
            surfaceContainerLow = colors.card,
            surfaceContainer = colors.card,
            surfaceContainerHigh = colors.surfaceSunken,
            surfaceContainerHighest = colors.surfaceSunken,
        )
    } else {
        lightColorScheme(
            primary = colors.semester.accent,
            onPrimary = colors.semester.onAccent,
            primaryContainer = colors.semester.container,
            onPrimaryContainer = colors.semester.deep,
            secondary = colors.ink,
            onSecondary = colors.paper,
            secondaryContainer = colors.surfaceSunken,
            onSecondaryContainer = colors.inkMuted,
            tertiary = colors.semester.soft,
            background = colors.paper,
            onBackground = colors.ink,
            surface = colors.card,
            onSurface = colors.ink,
            surfaceVariant = colors.chipBg,
            onSurfaceVariant = colors.inkMuted,
            outline = colors.inkDisabled,
            outlineVariant = colors.divider,
            error = colors.danger,
            onError = colors.onDanger,
            errorContainer = colors.dangerContainer,
            onErrorContainer = colors.onDangerContainer,
            surfaceContainerLowest = colors.navBg,
            surfaceContainerLow = colors.card,
            surfaceContainer = colors.card,
            surfaceContainerHigh = colors.surfaceSunken,
            surfaceContainerHighest = colors.surfaceSunken,
        )
    }

    CompositionLocalProvider(
        LocalMeadowColors provides colors,
        LocalMeadowAccent provides colors.semester,
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = MeadowTypography(),
            shapes = MeadowShapes,
            content = content,
        )
    }
}
