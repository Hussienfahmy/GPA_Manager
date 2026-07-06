package com.hussienfahmy.core_ui.presentation.components.meadow

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hussienfahmy.core_ui.theme.MeadowTheme

/**
 * Two-or-more option segmented control on a pill track (Normal / Predict).
 * Selected segment fills with the tab hue.
 */
@Composable
fun SegmentedToggle(
    options: List<String>,
    selectedIndex: Int,
    onSelect: (index: Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = MeadowTheme.colors
    val accent = MeadowTheme.accent

    Row(
        modifier = modifier
            .clip(CircleShape)
            .background(colors.segmentedBg)
            .padding(4.dp),
    ) {
        options.forEachIndexed { index, option ->
            val selected = index == selectedIndex
            Text(
                text = option,
                style = MaterialTheme.typography.labelMedium.copy(
                    fontSize = 13.sp,
                    fontWeight = if (selected) FontWeight.ExtraBold else FontWeight.Bold,
                ),
                color = if (selected) accent.onAccent else colors.segmentedText,
                modifier = Modifier
                    .clip(CircleShape)
                    .background(if (selected) accent.accent else Color.Transparent)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                    ) { onSelect(index) }
                    .padding(horizontal = 16.dp, vertical = 7.dp),
            )
        }
    }
}

@Composable
private fun SegmentedToggleShowcase() {
    Row(
        modifier = Modifier
            .background(MeadowTheme.colors.paper)
            .padding(12.dp),
    ) {
        SegmentedToggle(options = listOf("Normal", "Predict"), selectedIndex = 0, onSelect = {})
    }
}

@Preview(name = "SegmentedToggle · light")
@Composable
private fun SegmentedToggleLightPreview() {
    MeadowTheme(darkTheme = false) { SegmentedToggleShowcase() }
}

@Preview(name = "SegmentedToggle · dark")
@Composable
private fun SegmentedToggleDarkPreview() {
    MeadowTheme(darkTheme = true) { SegmentedToggleShowcase() }
}
