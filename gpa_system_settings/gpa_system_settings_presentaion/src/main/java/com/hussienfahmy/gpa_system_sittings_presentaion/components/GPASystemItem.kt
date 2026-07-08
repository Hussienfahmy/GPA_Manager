package com.hussienfahmy.gpa_system_sittings_presentaion.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hussienfahmy.core.R
import com.hussienfahmy.core.domain.gpa_settings.model.GPA
import com.hussienfahmy.core_ui.presentation.components.meadow.CapsLabel
import com.hussienfahmy.core_ui.theme.MeadowRadius
import com.hussienfahmy.core_ui.theme.MeadowTheme

/** Two selectable scale cards (4.0 / 5.0) + a warning banner (design 4e). */
@Composable
fun GPASystemItem(
    currentGPASystem: GPA.System,
    onGPASystemChanged: (GPA.System) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            ScaleCard(
                scale = GPA.System.FOUR.number,
                detail = stringResource(R.string.gpa_scale_four_detail),
                selected = currentGPASystem == GPA.System.FOUR,
                onClick = { onGPASystemChanged(GPA.System.FOUR) },
                modifier = Modifier.weight(1f),
            )
            ScaleCard(
                scale = GPA.System.FIVE.number,
                detail = stringResource(R.string.gpa_scale_five_detail),
                selected = currentGPASystem == GPA.System.FIVE,
                onClick = { onGPASystemChanged(GPA.System.FIVE) },
                modifier = Modifier.weight(1f),
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        WarningBanner(text = stringResource(R.string.gpa_scale_warning))
    }
}

@Composable
private fun ScaleCard(
    scale: Int,
    detail: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = MeadowTheme.colors
    val accent = MeadowTheme.accent
    val shape = RoundedCornerShape(MeadowRadius.card)

    Box(
        modifier = modifier
            .clip(shape)
            .background(colors.card)
            .border(
                width = 2.5.dp,
                color = if (selected) accent.accent else colors.segmentedBg,
                shape = shape,
            )
            .clickable(
                interactionSource = androidx.compose.runtime.remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick,
            )
            .padding(18.dp),
    ) {
        if (selected) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .size(22.dp)
                    .clip(CircleShape)
                    .background(accent.accent),
            ) {
                Text(
                    text = "✓",
                    style = MaterialTheme.typography.labelSmall,
                    color = accent.onAccent,
                )
            }
        }

        Column {
            Text(
                text = "$scale.0",
                style = MaterialTheme.typography.displayMedium.copy(fontSize = 30.sp),
                color = if (selected) accent.ink else colors.inkDisabled,
            )
            Text(
                text = stringResource(R.string.gpa_scale),
                style = MaterialTheme.typography.titleSmall.copy(fontSize = 12.sp),
                color = if (selected) accent.deep else colors.inkFaint,
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = detail,
                style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp, lineHeight = 16.sp),
                color = if (selected) colors.inkFaint else colors.inkDisabled,
            )
        }
    }
}

@Composable
private fun WarningBanner(text: String) {
    val colors = MeadowTheme.colors

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(9.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(if (colors.isLight) colors.surfaceSunken else colors.card)
            .padding(horizontal = 16.dp, vertical = 12.dp),
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(28.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(colors.marks.container),
        ) {
            Text(
                text = "!",
                style = MaterialTheme.typography.titleSmall,
                color = colors.marks.deep,
            )
        }
        Text(
            text = text,
            style = MaterialTheme.typography.bodySmall,
            color = colors.chipText,
        )
    }
}

@androidx.compose.ui.tooling.preview.Preview
@Composable
private fun GPASystemItemPreview() {
    MeadowTheme(darkTheme = false) {
        com.hussienfahmy.core_ui.theme.MeadowAccentProvider(MeadowTheme.colors.semester) {
            Box(modifier = Modifier.background(MeadowTheme.colors.paper).padding(16.dp)) {
                GPASystemItem(currentGPASystem = GPA.System.FOUR, onGPASystemChanged = {})
            }
        }
    }
}
