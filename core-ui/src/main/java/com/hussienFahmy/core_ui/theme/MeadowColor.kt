package com.hussienfahmy.core_ui.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

/**
 * Meadow design tokens — colors extracted 1:1 from the "GPA Manager Redesign" design doc.
 *
 * Every value here is copied from the design HTML; do not adjust by eye.
 */
@Immutable
data class MeadowAccent(
    /** Main tab hue — filled buttons, selected pills, progress. */
    val accent: Color,
    /** Content on top of [accent]. */
    val onAccent: Color,
    /** Deep readable variant — text on [container], outline-button text. */
    val deep: Color,
    /** Tonal container — tonal buttons, active nav pill, badges. */
    val container: Color,
    /** Soft companion — inner ring, dashed borders, decorative. */
    val soft: Color,
)

@Immutable
data class MeadowColors(
    val isLight: Boolean,

    // Surfaces
    /** Screen background. */
    val paper: Color,
    /** Card background. */
    val card: Color,
    /** Sunken surface inside a card — field rest state, neutral card actions. */
    val surfaceSunken: Color,
    /** Chip / badge background. */
    val chipBg: Color,
    /** Segmented control track. */
    val segmentedBg: Color,
    /** Hairline divider inside cards. */
    val divider: Color,

    // Ink
    /** Primary text. */
    val ink: Color,
    /** Secondary text — body-muted. */
    val inkMuted: Color,
    /** Caps-labels, faint captions. */
    val inkFaint: Color,
    /** Ghost text — the faintest readable text. */
    val inkGhost: Color,
    /** Disabled / rest grade-pill text. */
    val inkDisabled: Color,
    /** Chip / badge text. */
    val chipText: Color,
    /** Unselected segmented-control text. */
    val segmentedText: Color,

    // Rings
    val ringTrack: Color,
    val ringTrackInner: Color,

    // Bottom nav
    val navBg: Color,
    val navBorder: Color,
    val navItemText: Color,
    val navItemIcon: Color,

    // Danger
    val danger: Color,
    val onDanger: Color,
    val dangerContainer: Color,
    val onDangerContainer: Color,

    // Tab accents
    val semester: MeadowAccent,
    val marks: MeadowAccent,
    val history: MeadowAccent,
    val quick: MeadowAccent,
    val more: MeadowAccent,
)

val MeadowLightColors = MeadowColors(
    isLight = true,
    paper = Color(0xFFFAF7F0),
    card = Color(0xFFFFFFFF),
    surfaceSunken = Color(0xFFF6F3EB),
    chipBg = Color(0xFFF3EEE3),
    segmentedBg = Color(0xFFEFEAE0),
    divider = Color(0xFFF3EEE3),
    ink = Color(0xFF22382C),
    inkMuted = Color(0xFF5C6B60),
    inkFaint = Color(0xFF9AA79E),
    inkGhost = Color(0xFFB9C4BC),
    inkDisabled = Color(0xFFB4BFB6),
    chipText = Color(0xFF8A8474),
    segmentedText = Color(0xFF7A857C),
    ringTrack = Color(0xFFEFEAE0),
    ringTrackInner = Color(0xFFF3EEE3),
    navBg = Color(0xFFFFFFFF),
    navBorder = Color(0xFFEFEAE0),
    navItemText = Color(0xFFA8A392),
    navItemIcon = Color(0xFFB4AF9F),
    danger = Color(0xFFB4483A),
    onDanger = Color(0xFFFFFFFF),
    dangerContainer = Color(0xFFFBEAE6),
    onDangerContainer = Color(0xFFB4483A),
    semester = MeadowAccent(
        accent = Color(0xFF3E8464),
        onAccent = Color(0xFFFFFFFF),
        deep = Color(0xFF2E6B4E),
        container = Color(0xFFE3F0E7),
        soft = Color(0xFF8FC7A8),
    ),
    marks = MeadowAccent(
        accent = Color(0xFFC98A2D),
        onAccent = Color(0xFFFFFFFF),
        deep = Color(0xFF8A5F1D),
        container = Color(0xFFF7EDDA),
        soft = Color(0xFFB98A3E),
    ),
    history = MeadowAccent(
        accent = Color(0xFF7D6BC2),
        onAccent = Color(0xFFFFFFFF),
        deep = Color(0xFF5A4AA0),
        container = Color(0xFFECE8F8),
        soft = Color(0xFFA99AE0),
    ),
    quick = MeadowAccent(
        accent = Color(0xFFD96F5B),
        onAccent = Color(0xFFFFFFFF),
        deep = Color(0xFFB25441),
        container = Color(0xFFFAE7E1),
        soft = Color(0xFFE8907B),
    ),
    more = MeadowAccent(
        accent = Color(0xFF46586A),
        onAccent = Color(0xFFFFFFFF),
        deep = Color(0xFF46586A),
        container = Color(0xFFE7ECF0),
        soft = Color(0xFFAFC0CD),
    ),
)

val MeadowDarkColors = MeadowColors(
    isLight = false,
    paper = Color(0xFF151B17),
    card = Color(0xFF1D2620),
    surfaceSunken = Color(0xFF242E27),
    chipBg = Color(0xFF242E27),
    segmentedBg = Color(0xFF20291F),
    divider = Color(0xFF26302A),
    ink = Color(0xFFEDF4EE),
    inkMuted = Color(0xFFAEBCB1),
    inkFaint = Color(0xFF66756A),
    inkGhost = Color(0xFF4E5C52),
    inkDisabled = Color(0xFF5A695E),
    chipText = Color(0xFF8B988E),
    segmentedText = Color(0xFF7E8F82),
    ringTrack = Color(0xFF26302A),
    ringTrackInner = Color(0xFF222B25),
    navBg = Color(0xFF10150F),
    navBorder = Color(0xFF20291F),
    navItemText = Color(0xFF5A695E),
    navItemIcon = Color(0xFF5A695E),
    danger = Color(0xFFE5776A),
    onDanger = Color(0xFF2A100D),
    dangerContainer = Color(0xFF3A2523),
    onDangerContainer = Color(0xFFF0A092),
    semester = MeadowAccent(
        accent = Color(0xFF6FBE93),
        onAccent = Color(0xFF0E2318),
        deep = Color(0xFF8FD1AC),
        container = Color(0xFF233729),
        soft = Color(0xFF3E8464),
    ),
    marks = MeadowAccent(
        accent = Color(0xFFE0B25C),
        onAccent = Color(0xFF2A2210),
        deep = Color(0xFFE9C67F),
        container = Color(0xFF3A3122),
        soft = Color(0xFFC9A253),
    ),
    history = MeadowAccent(
        accent = Color(0xFFA99AE0),
        onAccent = Color(0xFF151B17),
        deep = Color(0xFFBBACEE),
        container = Color(0xFF322D45),
        soft = Color(0xFF7D6BC2),
    ),
    quick = MeadowAccent(
        accent = Color(0xFFE8907B),
        onAccent = Color(0xFF151B17),
        deep = Color(0xFFF0A98F),
        container = Color(0xFF3A2A26),
        soft = Color(0xFFD96F5B),
    ),
    more = MeadowAccent(
        accent = Color(0xFFAFC0CD),
        onAccent = Color(0xFF151B17),
        deep = Color(0xFFAFC0CD),
        container = Color(0xFF2A343B),
        soft = Color(0xFF46586A),
    ),
)

val LocalMeadowColors = staticCompositionLocalOf { MeadowLightColors }

/** The accent (tab hue) of the current screen. Semester green by default. */
val LocalMeadowAccent = compositionLocalOf { MeadowLightColors.semester }
