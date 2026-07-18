package com.hussienfahmy.semester_history_presentation.components

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import org.jetbrains.compose.resources.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hussienfahmy.core.generated.resources.*
import com.hussienfahmy.core_ui.presentation.components.meadow.CapsLabel
import com.hussienfahmy.core_ui.presentation.components.meadow.GpaTooltipBox
import com.hussienfahmy.core_ui.presentation.components.meadow.asGpa
import com.hussienfahmy.core_ui.presentation.components.meadow.MeadowCard
import com.hussienfahmy.core_ui.presentation.components.meadow.PillButton
import com.hussienfahmy.core_ui.presentation.components.meadow.PillButtonStyle
import com.hussienfahmy.core_ui.theme.MeadowAccentProvider
import com.hussienfahmy.core_ui.theme.MeadowRadius
import com.hussienfahmy.core_ui.theme.MeadowTheme

/** Cumulative hero (design 2b): GPA + journey stats + Export PDF. */
@Composable
fun CumulativeGpaCard(
    cumulativeGPA: Double,
    totalCreditHours: Int,
    semestersCount: Int,
    modifier: Modifier = Modifier,
    onExportClick: (() -> Unit)? = null,
    isExporting: Boolean = false,
) {
    val colors = MeadowTheme.colors
    val accent = MeadowTheme.accent

    MeadowCard(
        modifier = modifier.fillMaxWidth(),
        radius = MeadowRadius.hero,
        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 16.dp),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Column(modifier = Modifier.weight(1f)) {
                CapsLabel(text = stringResource(Res.string.cumulative_gpa))

                val animatedGpa by animateFloatAsState(
                    targetValue = cumulativeGPA.toFloat(),
                    animationSpec = spring(stiffness = Spring.StiffnessLow),
                    label = "historyCumulative",
                )
                GpaTooltipBox(fullValue = cumulativeGPA) {
                    Text(
                        text = animatedGpa.asGpa(),
                        style = MaterialTheme.typography.displayLarge.copy(fontSize = 38.sp),
                        color = accent.ink,
                    )
                }

                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    text = stringResource(
                        Res.string.history_hero_subtitle,
                        totalCreditHours,
                        semestersCount,
                    ),
                    style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp),
                    color = colors.inkFaint,
                )
            }

            if (onExportClick != null) {
                PillButton(
                    text = stringResource(Res.string.export_pdf),
                    onClick = onExportClick,
                    style = PillButtonStyle.Outline,
                    enabled = !isExporting,
                    compact = true,
                )
            }
        }
    }
}

@Preview(name = "CumulativeGpaCard · light")
@Composable
private fun CumulativeGpaCardLightPreview() {
    MeadowTheme(darkTheme = false) {
        MeadowAccentProvider(MeadowTheme.colors.history) {
            CumulativeGpaCard(
                cumulativeGPA = 3.53,
                totalCreditHours = 101,
                semestersCount = 6,
                onExportClick = {},
                isExporting = false,
            )
        }
    }
}

@Preview(name = "CumulativeGpaCard · dark")
@Composable
private fun CumulativeGpaCardDarkPreview() {
    MeadowTheme(darkTheme = true) {
        MeadowAccentProvider(MeadowTheme.colors.history) {
            CumulativeGpaCard(
                cumulativeGPA = 3.53,
                totalCreditHours = 101,
                semestersCount = 6,
                onExportClick = {},
                isExporting = false,
            )
        }
    }
}
