package com.hussienfahmy.core_ui.presentation.components.meadow

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.hussienfahmy.core_ui.theme.MeadowRadius
import com.hussienfahmy.core_ui.theme.MeadowTheme

/**
 * White card on cream paper (dark: moss card, flat). Radius 20 for cards, 24 for heroes.
 * Shadows are soft and slightly green-tinted, light theme only — dark cards are flat.
 */
@Composable
fun MeadowCard(
    modifier: Modifier = Modifier,
    radius: Dp = MeadowRadius.card,
    contentPadding: PaddingValues = PaddingValues(horizontal = 16.dp, vertical = 14.dp),
    border: BorderStroke? = null,
    elevated: Boolean = false,
    animateSize: Boolean = false,
    content: @Composable ColumnScope.() -> Unit,
) {
    val colors = MeadowTheme.colors
    val shape = RoundedCornerShape(radius)
    val shadowTint = Color(0xFF46503C)

    Column(
        modifier = modifier
            .then(
                if (colors.isLight) Modifier.shadow(
                    elevation = if (elevated) 4.dp else 2.dp,
                    shape = shape,
                    ambientColor = shadowTint,
                    spotColor = shadowTint,
                ) else Modifier
            )
            .clip(shape)
            .background(colors.card)
            .then(if (border != null) Modifier.border(border, shape) else Modifier)
            // animateContentSize bakes in its own clipToBounds() at the point it's applied -
            // must come after shadow(), or the shadow (which draws outside the card's own
            // bounds) gets clipped along with it.
            .then(
                if (animateSize) Modifier.animateContentSize(
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioNoBouncy,
                        stiffness = Spring.StiffnessMedium,
                    )
                ) else Modifier
            )
            .padding(contentPadding),
        content = content,
    )
}

@Composable
private fun MeadowCardShowcase() {
    Box(
        modifier = Modifier
            .background(MeadowTheme.colors.paper)
            .padding(12.dp),
    ) {
        MeadowCard(modifier = Modifier.fillMaxWidth()) {
            Text(text = "Card content", color = MeadowTheme.colors.ink)
        }
    }
}

@Preview(name = "MeadowCard · light")
@Composable
private fun MeadowCardLightPreview() {
    MeadowTheme(darkTheme = false) { MeadowCardShowcase() }
}

@Preview(name = "MeadowCard · dark")
@Composable
private fun MeadowCardDarkPreview() {
    MeadowTheme(darkTheme = true) { MeadowCardShowcase() }
}
