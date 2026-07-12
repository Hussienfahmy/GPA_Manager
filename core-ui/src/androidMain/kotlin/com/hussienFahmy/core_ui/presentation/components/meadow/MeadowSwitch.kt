package com.hussienfahmy.core_ui.presentation.components.meadow

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hussienfahmy.core_ui.theme.MeadowTheme

/**
 * Switch from the reference sheet: 46×26 pill track, 20dp thumb.
 * On = tab hue track; off = neutral track. Thumb keeps the card color.
 */
@Composable
fun MeadowSwitch(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = MeadowTheme.colors
    val accent = MeadowTheme.accent

    val trackColor by animateColorAsState(
        targetValue = if (checked) accent.accent else colors.switchTrackOff,
        label = "switchTrack",
    )
    val thumbOffset by animateDpAsState(
        targetValue = if (checked) 20.dp else 0.dp,
        label = "switchThumb",
    )

    Box(
        modifier = modifier
            .size(width = 46.dp, height = 26.dp)
            .clip(CircleShape)
            .background(trackColor)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
            ) { onCheckedChange(!checked) }
            .padding(3.dp),
    ) {
        Box(
            modifier = Modifier
                .offset(x = thumbOffset)
                .size(20.dp)
                .clip(CircleShape)
                .background(colors.card)
                .align(Alignment.CenterStart),
        )
    }
}

@Composable
private fun MeadowSwitchShowcase() {
    Row(
        modifier = Modifier
            .background(MeadowTheme.colors.card)
            .padding(12.dp),
    ) {
        MeadowSwitch(checked = true, onCheckedChange = {})
        MeadowSwitch(
            checked = false,
            onCheckedChange = {},
            modifier = Modifier.padding(start = 12.dp),
        )
    }
}

@Preview(name = "MeadowSwitch · light")
@Composable
private fun MeadowSwitchLightPreview() {
    MeadowTheme(darkTheme = false) { MeadowSwitchShowcase() }
}

@Preview(name = "MeadowSwitch · dark")
@Composable
private fun MeadowSwitchDarkPreview() {
    MeadowTheme(darkTheme = true) { MeadowSwitchShowcase() }
}
