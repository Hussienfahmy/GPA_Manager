package com.hussienfahmy.quick_presentation.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hussienFahmy.core_ui.LocalSpacing
import com.hussienFahmy.myGpaManager.core.R

@Composable
fun QuickResultCard(
    modifier: Modifier,
    cumulativeGPA: Float,
    cumulativeGPAPercentage: Float,
) {
    val spacing = LocalSpacing.current

    Card(shape = RoundedCornerShape(spacing.medium), modifier = modifier) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .padding(spacing.medium)
        ) {
            Text(
                text = stringResource(R.string.cumulative_gpa_will_be),
                style = MaterialTheme.typography.titleLarge
            )

            val spec = tween<Float>(durationMillis = 900)
            val progress by animateFloatAsState(
                targetValue = cumulativeGPAPercentage / 100,
                animationSpec = spec
            )
            val cumulativeGPAProgress by animateFloatAsState(
                targetValue = cumulativeGPA,
                animationSpec = spec
            )

            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = String.format("%.2f", cumulativeGPAProgress),
                    style = MaterialTheme.typography.titleLarge,
                )

                CircularProgressIndicatorWithBackground(
                    modifier = Modifier.size(150.dp),
                    color = MaterialTheme.colorScheme.primary,
                    strokeWidth = 7.dp,
                    totalProgress = 0.75f,
                    progress = progress
                )
            }
        }
    }
}

@Preview
@Composable
fun QuickResultCardPreview() {
    QuickResultCard(
        modifier = Modifier.size(300.dp),
        cumulativeGPA = 3.5f,
        cumulativeGPAPercentage = 0.5f
    )
}