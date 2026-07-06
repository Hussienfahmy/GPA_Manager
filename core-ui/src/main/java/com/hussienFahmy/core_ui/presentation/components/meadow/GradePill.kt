package com.hussienfahmy.core_ui.presentation.components.meadow

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.style.TextDirection
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.hussienfahmy.core_ui.theme.MeadowRadius
import com.hussienfahmy.core_ui.theme.MeadowTheme

/**
 * Grade pill states from the reference sheet.
 * Solid = fact · dashed = prediction, everywhere in the app.
 */
enum class GradePillState {
    /** Unselected, tappable. */
    Rest,

    /** The assigned grade — solid tab hue + soft shadow. */
    Selected,

    /** Unreachable / not tappable — faded. */
    Locked,

    /** Grade the user fixed in Predict mode — solid. */
    Fixed,

    /** Grade the planner suggests — dashed outline. */
    Suggested,
}

/** 2dp dashed rounded border — the app-wide "prediction, not fact" affordance. */
fun Modifier.dashedBorder(color: Color, radius: Dp): Modifier = drawBehind {
    val stroke = Stroke(
        width = 2.dp.toPx(),
        pathEffect = PathEffect.dashPathEffect(floatArrayOf(6.dp.toPx(), 5.dp.toPx()), 0f),
    )
    drawRoundRect(
        color = color,
        style = stroke,
        cornerRadius = CornerRadius(radius.toPx(), radius.toPx()),
    )
}

@Composable
fun GradePill(
    text: String,
    state: GradePillState,
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
) {
    val colors = MeadowTheme.colors
    val accent = MeadowTheme.accent
    val shape = RoundedCornerShape(MeadowRadius.pill)

    val selectedLike = state == GradePillState.Selected || state == GradePillState.Fixed

    // Tap pop: 0.9 -> (overshoot) -> 1, spring — from the reference sheet.
    val scale = remember { Animatable(1f) }
    LaunchedEffect(selectedLike) {
        if (selectedLike) {
            scale.snapTo(0.9f)
            scale.animateTo(
                targetValue = 1f,
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessMedium,
                ),
            )
        }
    }

    val textColor = when (state) {
        GradePillState.Rest, GradePillState.Locked -> colors.inkDisabled
        GradePillState.Selected, GradePillState.Fixed -> accent.onAccent
        GradePillState.Suggested -> accent.deep
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .scale(scale.value)
            .height(34.dp)
            .then(
                if (selectedLike && colors.isLight) Modifier.shadow(
                    elevation = 3.dp,
                    shape = shape,
                    ambientColor = accent.accent,
                    spotColor = accent.accent,
                ) else Modifier
            )
            .clip(shape)
            .background(if (selectedLike) accent.accent else Color.Transparent)
            .then(
                if (state == GradePillState.Suggested) Modifier.dashedBorder(
                    color = accent.accent,
                    radius = MeadowRadius.pill,
                ) else Modifier
            )
            .then(
                if (onClick != null && state != GradePillState.Locked) Modifier.clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = onClick,
                ) else Modifier
            )
            .alpha(if (state == GradePillState.Locked) 0.5f else 1f),
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.titleSmall.copy(textDirection = TextDirection.Ltr),
            color = textColor,
            maxLines = 1,
        )
    }
}

@Composable
private fun GradePillShowcase() {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier
            .background(MeadowTheme.colors.card)
            .padding(12.dp),
    ) {
        GradePill(text = "B+", state = GradePillState.Rest, modifier = Modifier.width(44.dp))
        GradePill(text = "B+", state = GradePillState.Selected, modifier = Modifier.width(44.dp))
        GradePill(text = "B+", state = GradePillState.Locked, modifier = Modifier.width(44.dp))
        GradePill(text = "B+", state = GradePillState.Fixed, modifier = Modifier.width(64.dp))
        GradePill(text = "B+", state = GradePillState.Suggested, modifier = Modifier.width(64.dp))
    }
}

@Preview(name = "GradePill · light")
@Composable
private fun GradePillLightPreview() {
    MeadowTheme(darkTheme = false) { GradePillShowcase() }
}

@Preview(name = "GradePill · dark")
@Composable
private fun GradePillDarkPreview() {
    MeadowTheme(darkTheme = true) { GradePillShowcase() }
}
