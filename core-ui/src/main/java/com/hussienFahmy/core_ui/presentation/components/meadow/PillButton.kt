package com.hussienfahmy.core_ui.presentation.components.meadow

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hussienfahmy.core_ui.theme.MeadowTheme

/**
 * Rounded-pill button styles from the component reference sheet.
 * Pressed = scale .97 + darken 8% (spring). Primary takes the current tab's hue.
 */
enum class PillButtonStyle { Primary, Tonal, Outline, Text, Danger, TonalDanger }

@Composable
fun PillButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    style: PillButtonStyle = PillButtonStyle.Primary,
    enabled: Boolean = true,
    compact: Boolean = false,
) {
    val colors = MeadowTheme.colors
    val accent = MeadowTheme.accent

    val interactionSource = remember { MutableInteractionSource() }
    val pressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (pressed) 0.97f else 1f,
        animationSpec = spring(stiffness = Spring.StiffnessMediumLow),
        label = "pillScale",
    )

    fun Color.darkenedIfPressed() = if (pressed) lerp(this, Color.Black, 0.08f) else this

    val background: Color
    val contentColor: Color
    var borderColor: Color? = null

    when {
        !enabled -> {
            background = colors.segmentedBg
            contentColor = colors.navItemIcon
        }

        else -> when (style) {
            PillButtonStyle.Primary -> {
                background = accent.accent.darkenedIfPressed()
                contentColor = accent.onAccent
            }

            PillButtonStyle.Tonal -> {
                background = accent.container.darkenedIfPressed()
                contentColor = accent.deep
            }

            PillButtonStyle.Outline -> {
                background = Color.Transparent
                contentColor = accent.deep
                borderColor = accent.accent
            }

            PillButtonStyle.Text -> {
                background = Color.Transparent
                contentColor = colors.inkMuted
            }

            PillButtonStyle.Danger -> {
                background = colors.danger.darkenedIfPressed()
                contentColor = colors.onDanger
            }

            PillButtonStyle.TonalDanger -> {
                background = colors.dangerContainer.darkenedIfPressed()
                contentColor = colors.onDangerContainer
            }
        }
    }

    val padding = if (compact) {
        PaddingValues(horizontal = 16.dp, vertical = 8.dp)
    } else {
        PaddingValues(horizontal = 22.dp, vertical = 11.dp)
    }

    Text(
        text = text,
        style = if (compact) MaterialTheme.typography.labelMedium
        else MaterialTheme.typography.labelLarge,
        color = contentColor,
        modifier = modifier
            .scale(scale)
            .clip(CircleShape)
            .background(background)
            .then(
                if (borderColor != null) Modifier.border(2.dp, borderColor, CircleShape)
                else Modifier
            )
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                enabled = enabled,
                onClick = onClick,
            )
            .padding(padding),
    )
}

@Composable
private fun PillButtonShowcase() {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier
            .background(MeadowTheme.colors.paper)
            .padding(12.dp),
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            PillButton(text = "Primary", onClick = {}, style = PillButtonStyle.Primary)
            PillButton(text = "Tonal", onClick = {}, style = PillButtonStyle.Tonal)
            PillButton(text = "Outline", onClick = {}, style = PillButtonStyle.Outline)
        }
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            PillButton(text = "Text", onClick = {}, style = PillButtonStyle.Text)
            PillButton(text = "Delete", onClick = {}, style = PillButtonStyle.Danger)
            PillButton(text = "Tonal danger", onClick = {}, style = PillButtonStyle.TonalDanger)
        }
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            PillButton(text = "Disabled", onClick = {}, enabled = false)
            PillButton(text = "Compact", onClick = {}, compact = true)
        }
    }
}

@Preview(name = "PillButton · light")
@Composable
private fun PillButtonLightPreview() {
    MeadowTheme(darkTheme = false) { PillButtonShowcase() }
}

@Preview(name = "PillButton · dark")
@Composable
private fun PillButtonDarkPreview() {
    MeadowTheme(darkTheme = true) { PillButtonShowcase() }
}
