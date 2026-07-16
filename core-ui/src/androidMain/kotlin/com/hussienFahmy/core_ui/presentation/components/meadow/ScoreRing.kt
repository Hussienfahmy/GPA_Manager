package com.hussienfahmy.core_ui.presentation.components.meadow

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.hussienfahmy.core_ui.theme.MeadowTheme

/**
 * Single big progress ring with content in its center (Quick hero, Welcome
 * mark). Sweeps with a spring on value change.
 */
@Composable
fun ScoreRing(
    progress: Float,
    modifier: Modifier = Modifier,
    size: Dp = 150.dp,
    stroke: Dp = 11.dp,
    content: @Composable () -> Unit,
) {
    val colors = MeadowTheme.colors
    val accent = MeadowTheme.accent

    val animatedProgress by animateFloatAsState(
        targetValue = progress.coerceIn(0f, 1f),
        animationSpec = spring(stiffness = Spring.StiffnessLow),
        label = "scoreRing",
    )

    Box(contentAlignment = Alignment.Center, modifier = modifier.size(size)) {
        Canvas(modifier = Modifier.size(size)) {
            val strokePx = stroke.toPx()
            val radius = (this.size.minDimension - strokePx) / 2f

            drawCircle(
                color = colors.ringTrack,
                radius = radius,
                style = Stroke(width = strokePx),
            )
            if (animatedProgress > 0f) {
                drawArc(
                    color = accent.accent,
                    startAngle = -90f,
                    sweepAngle = 360f * animatedProgress,
                    useCenter = false,
                    topLeft = Offset(center.x - radius, center.y - radius),
                    size = Size(radius * 2, radius * 2),
                    style = Stroke(
                        width = strokePx,
                        cap = if (animatedProgress < 1f) StrokeCap.Round else StrokeCap.Butt,
                    ),
                )
            }
        }
        content()
    }
}

@Composable
private fun ScoreRingShowcase() {
    val colors = MeadowTheme.colors

    Column(
        modifier = Modifier
            .background(colors.card)
            .padding(12.dp),
    ) {
        ScoreRing(progress = 0.917f) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "3.67",
                    style = MaterialTheme.typography.displayMedium,
                    color = colors.ink,
                )
                Text(
                    text = "91.7%",
                    style = MaterialTheme.typography.labelMedium,
                    color = colors.inkFaint,
                )
            }
        }
    }
}

@Preview(name = "ScoreRing · light")
@Composable
private fun ScoreRingLightPreview() {
    MeadowTheme(darkTheme = false) { ScoreRingShowcase() }
}

@Preview(name = "ScoreRing · dark")
@Composable
private fun ScoreRingDarkPreview() {
    MeadowTheme(darkTheme = true) { ScoreRingShowcase() }
}
