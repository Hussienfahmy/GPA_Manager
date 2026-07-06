package com.hussienfahmy.core_ui.presentation.components.meadow

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.hussienfahmy.core_ui.theme.MeadowTheme

/**
 * The hero's two concentric progress rings: outer = cumulative, inner = semester.
 * Rings spring from 0 to value on appearance; a dashed inner ring marks a
 * prediction target (aspiration, not fact).
 */
@Composable
fun GpaRings(
    outerProgress: Float,
    innerProgress: Float,
    modifier: Modifier = Modifier,
    size: Dp = 112.dp,
    outerStroke: Dp = 9.dp,
    innerStroke: Dp = 9.dp,
    innerDashed: Boolean = false,
) {
    val colors = MeadowTheme.colors
    val accent = MeadowTheme.accent

    val animatedOuter by animateFloatAsState(
        targetValue = outerProgress.coerceIn(0f, 1f),
        animationSpec = spring(stiffness = Spring.StiffnessLow),
        label = "outerRing",
    )
    val animatedInner by animateFloatAsState(
        targetValue = innerProgress.coerceIn(0f, 1f),
        animationSpec = spring(stiffness = Spring.StiffnessLow),
        label = "innerRing",
    )

    Canvas(modifier = modifier.size(size)) {
        val outerStrokePx = outerStroke.toPx()
        val innerStrokePx = innerStroke.toPx()
        // Proportions from the design: outer r48, inner r34 in a 112 viewBox (r/56).
        val outerRadius = this.size.minDimension / 2f * (48f / 56f)
        val innerRadius = this.size.minDimension / 2f * (34f / 56f)

        drawRing(colors.ringTrack, outerRadius, outerStrokePx, 1f)
        drawRing(accent.accent, outerRadius, outerStrokePx, animatedOuter)
        drawRing(colors.ringTrackInner, innerRadius, innerStrokePx, 1f)
        if (innerDashed) {
            drawRing(
                accent.soft, innerRadius, innerStrokePx, 1f,
                pathEffect = PathEffect.dashPathEffect(
                    floatArrayOf(4.dp.toPx(), 7.dp.toPx()), 0f
                ),
            )
        } else {
            drawRing(accent.soft, innerRadius, innerStrokePx, animatedInner)
        }
    }
}

private fun DrawScope.drawRing(
    color: Color,
    radius: Float,
    strokeWidth: Float,
    progress: Float,
    pathEffect: PathEffect? = null,
) {
    if (progress <= 0f) return
    drawArc(
        color = color,
        startAngle = -90f,
        sweepAngle = 360f * progress,
        useCenter = false,
        topLeft = Offset(center.x - radius, center.y - radius),
        size = Size(radius * 2, radius * 2),
        style = Stroke(
            width = strokeWidth,
            cap = if (progress < 1f) StrokeCap.Round else StrokeCap.Butt,
            pathEffect = pathEffect,
        ),
    )
}

@Composable
private fun GpaRingsShowcase() {
    Row(
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier
            .background(MeadowTheme.colors.paper)
            .padding(12.dp),
    ) {
        GpaRings(outerProgress = 0.86f, innerProgress = 0.838f)
        GpaRings(
            outerProgress = 0.85f,
            innerProgress = 1f,
            size = 76.dp,
            outerStroke = 7.dp,
            innerStroke = 5.dp,
            innerDashed = true,
        )
    }
}

@Preview(name = "GpaRings · light")
@Composable
private fun GpaRingsLightPreview() {
    MeadowTheme(darkTheme = false) { GpaRingsShowcase() }
}

@Preview(name = "GpaRings · dark")
@Composable
private fun GpaRingsDarkPreview() {
    MeadowTheme(darkTheme = true) { GpaRingsShowcase() }
}
