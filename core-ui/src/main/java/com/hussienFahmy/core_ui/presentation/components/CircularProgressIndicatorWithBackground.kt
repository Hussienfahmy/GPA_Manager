package com.hussienfahmy.core_ui.presentation.components

import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp

@Composable
fun CircularProgressIndicatorWithBackground(
    modifier: Modifier = Modifier,
    color: Color,
    strokeWidth: Dp,
    progress: Float,
    totalProgress: Float = 1f
) {
    CircularProgressIndicator(
        progress = { progress * totalProgress },
        color = color,
        strokeWidth = strokeWidth,
        trackColor = color.copy(alpha = 0.3f),
        modifier = modifier
    )
}

@Preview
@Composable
fun CircularProgressIndicatorWithBackgroundPreview() {
    CircularProgressIndicatorWithBackground(
        color = Color.Blue,
        strokeWidth = Dp(4f),
        progress = 0.5f
    )
}