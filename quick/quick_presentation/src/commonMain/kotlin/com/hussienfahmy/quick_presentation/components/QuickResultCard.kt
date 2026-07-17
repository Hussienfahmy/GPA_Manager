package com.hussienfahmy.quick_presentation.components

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import org.jetbrains.compose.resources.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hussienfahmy.core.generated.resources.*
import com.hussienfahmy.core_ui.presentation.components.meadow.CapsLabel
import com.hussienfahmy.core_ui.presentation.components.meadow.GpaTooltipBox
import com.hussienfahmy.core_ui.presentation.components.meadow.asGpa
import com.hussienfahmy.core_ui.presentation.components.meadow.MeadowCard
import com.hussienfahmy.core_ui.presentation.components.meadow.ScoreRing
import com.hussienfahmy.core_ui.theme.MeadowAccentProvider
import com.hussienfahmy.core_ui.theme.MeadowRadius
import com.hussienfahmy.core_ui.theme.MeadowTheme
import java.util.Locale

/**
 * Result-first hero (design 2c): "YOUR CUMULATIVE WOULD BE" + big coral ring.
 * Dims while the inputs are invalid.
 */
@Composable
fun QuickResultCard(
    modifier: Modifier = Modifier,
    cumulativeGPA: Float,
    cumulativeGPAPercentage: Float,
    inputsValid: Boolean = true,
) {
    val colors = MeadowTheme.colors
    val accent = MeadowTheme.accent

    MeadowCard(
        modifier = modifier.fillMaxWidth(),
        radius = MeadowRadius.hero,
        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 22.dp),
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            // Decorative sparks
            Box(
                modifier = Modifier
                    .offset(x = 14.dp, y = 2.dp)
                    .size(5.dp)
                    .alpha(0.6f)
                    .clip(CircleShape)
                    .background(accent.accent)
            )
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .offset(x = (-20).dp, y = 20.dp)
                    .size(4.dp)
                    .alpha(0.55f)
                    .clip(CircleShape)
                    .background(colors.marks.accent)
            )

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .alpha(if (inputsValid) 1f else 0.3f),
            ) {
                CapsLabel(text = stringResource(Res.string.cumulative_gpa_will_be))

                Spacer(modifier = Modifier.height(12.dp))

                val animatedGpa by animateFloatAsState(
                    targetValue = cumulativeGPA,
                    animationSpec = spring(stiffness = Spring.StiffnessLow),
                    label = "quickGpa",
                )

                ScoreRing(progress = cumulativeGPAPercentage / 100f) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        GpaTooltipBox(fullValue = cumulativeGPA) {
                            Text(
                                text = animatedGpa.asGpa(),
                                style = MaterialTheme.typography.displayMedium,
                                color = accent.ink,
                            )
                        }
                        Text(
                            text = String.format(
                                Locale.getDefault(), "%.1f%%", cumulativeGPAPercentage
                            ),
                            style = MaterialTheme.typography.labelMedium,
                            color = colors.inkFaint,
                        )
                    }
                }
            }
        }
    }
}

@Preview(name = "QuickResult · light")
@Composable
private fun QuickResultCardLightPreview() {
    MeadowTheme(darkTheme = false) {
        MeadowAccentProvider(MeadowTheme.colors.quick) {
            QuickResultCard(cumulativeGPA = 3.67f, cumulativeGPAPercentage = 91.7f)
        }
    }
}

@Preview(name = "QuickResult · dark")
@Composable
private fun QuickResultCardDarkPreview() {
    MeadowTheme(darkTheme = true) {
        MeadowAccentProvider(MeadowTheme.colors.quick) {
            QuickResultCard(cumulativeGPA = 3.67f, cumulativeGPAPercentage = 91.7f)
        }
    }
}
