package com.hussienfahmy.core_ui.presentation.components.meadow

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hussienfahmy.core_ui.theme.MeadowTheme

/**
 * Small status badge (BADGES & CHIPS on the reference sheet):
 * [Neutral] "3.0 hrs" / "Fixed" · [Accent] grade badge · [Warn] "Out of reach".
 */
enum class MeadowChipStyle { Neutral, Accent, Warn }

@Composable
fun MeadowChip(
    text: String,
    modifier: Modifier = Modifier,
    style: MeadowChipStyle = MeadowChipStyle.Neutral,
) {
    val colors = MeadowTheme.colors
    val accent = MeadowTheme.accent

    val (background, contentColor) = when (style) {
        MeadowChipStyle.Neutral -> colors.chipBg to colors.chipText
        MeadowChipStyle.Accent -> accent.container to accent.deep
        MeadowChipStyle.Warn -> colors.marks.container to colors.marks.deep
    }

    Text(
        text = text,
        style = MaterialTheme.typography.labelSmall.copy(
            fontWeight = FontWeight.ExtraBold,
            letterSpacing = 0.sp,
        ),
        color = contentColor,
        modifier = modifier
            .clip(CircleShape)
            .background(background)
            .padding(horizontal = 10.dp, vertical = 3.dp),
    )
}

@Composable
private fun MeadowChipShowcase() {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier
            .background(MeadowTheme.colors.paper)
            .padding(12.dp),
    ) {
        MeadowChip(text = "3.0 hrs")
        MeadowChip(text = "B+", style = MeadowChipStyle.Accent)
        MeadowChip(text = "Out of reach", style = MeadowChipStyle.Warn)
    }
}

@Preview(name = "MeadowChip · light")
@Composable
private fun MeadowChipLightPreview() {
    MeadowTheme(darkTheme = false) { MeadowChipShowcase() }
}

@Preview(name = "MeadowChip · dark")
@Composable
private fun MeadowChipDarkPreview() {
    MeadowTheme(darkTheme = true) { MeadowChipShowcase() }
}
