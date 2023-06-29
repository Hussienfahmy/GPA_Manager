package com.hussienfahmy.quick_presentation.components

import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp

@Composable
fun CircularProgressIndicatorWithBackground(
    modifier: Modifier = Modifier,
    backgroundColor: Color = Color.LightGray,
    color: Color,
    strokeWidth: Dp,
    progress: Float,
    totalProgress: Float = 1f
) {
    // background
    CircularProgressIndicator(
        progress = totalProgress,
        color = backgroundColor,
        strokeWidth = strokeWidth,
        modifier = modifier
    )

    // foreground
    CircularProgressIndicator(
        progress = progress * totalProgress,
        color = color,
        strokeWidth = strokeWidth,
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