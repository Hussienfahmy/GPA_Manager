package com.hussienfahmy.core_ui.presentation.components.meadow

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hussienfahmy.core_ui.theme.MeadowRadius
import com.hussienfahmy.core_ui.theme.MeadowTheme

/**
 * Action button inside an expanded card's footer row (Rename / Delete / Reset,
 * Fix grade / Predicted). Rectangular tonal tile, radius 12, equal-width siblings.
 */
enum class CardActionStyle { Neutral, Danger, Accent, Dimmed }

@Composable
fun CardAction(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    style: CardActionStyle = CardActionStyle.Neutral,
) {
    val colors = MeadowTheme.colors
    val accent = MeadowTheme.accent

    val (background, contentColor) = when (style) {
        CardActionStyle.Neutral -> colors.surfaceSunken to colors.inkMuted
        CardActionStyle.Danger -> colors.dangerContainer to colors.onDangerContainer
        CardActionStyle.Accent -> accent.container to accent.deep
        CardActionStyle.Dimmed -> colors.surfaceSunken to colors.navItemIcon
    }

    Text(
        text = text,
        style = MaterialTheme.typography.titleSmall,
        color = contentColor,
        textAlign = TextAlign.Center,
        maxLines = 1,
        modifier = modifier
            .clip(RoundedCornerShape(MeadowRadius.pill))
            .background(background)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick,
            )
            .padding(vertical = 8.dp, horizontal = 4.dp),
    )
}

@Composable
private fun CardActionShowcase() {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .background(MeadowTheme.colors.card)
            .padding(12.dp),
    ) {
        CardAction(text = "Rename", onClick = {}, modifier = Modifier.weight(1f))
        CardAction(
            text = "Delete",
            onClick = {},
            style = CardActionStyle.Danger,
            modifier = Modifier.weight(1f),
        )
        CardAction(
            text = "Lock grade",
            onClick = {},
            style = CardActionStyle.Accent,
            modifier = Modifier.weight(1f),
        )
        CardAction(
            text = "Reset",
            onClick = {},
            style = CardActionStyle.Dimmed,
            modifier = Modifier.weight(1f),
        )
    }
}

@Preview(name = "CardAction · light")
@Composable
private fun CardActionLightPreview() {
    MeadowTheme(darkTheme = false) { CardActionShowcase() }
}

@Preview(name = "CardAction · dark")
@Composable
private fun CardActionDarkPreview() {
    MeadowTheme(darkTheme = true) { CardActionShowcase() }
}
