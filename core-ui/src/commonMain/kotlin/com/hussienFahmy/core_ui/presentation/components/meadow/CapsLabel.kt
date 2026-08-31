package com.hussienfahmy.core_ui.presentation.components.meadow

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hussienfahmy.core_ui.theme.CapsLabelStyle
import com.hussienfahmy.core_ui.theme.MeadowTheme

/** Uppercased section label — 11/800 with +8% tracking ("CUMULATIVE GPA", "TARGET"). */
@Composable
fun CapsLabel(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = MeadowTheme.colors.inkFaint,
) {
    Text(
        text = text.uppercase(),
        style = CapsLabelStyle(),
        color = color,
        modifier = modifier,
        maxLines = 1,
    )
}

@Preview(name = "CapsLabel · light")
@Composable
private fun CapsLabelLightPreview() {
    MeadowTheme(darkTheme = false) {
        CapsLabel(
            text = "Cumulative GPA",
            modifier = Modifier
                .background(MeadowTheme.colors.card)
                .padding(12.dp),
        )
    }
}

@Preview(name = "CapsLabel · dark")
@Composable
private fun CapsLabelDarkPreview() {
    MeadowTheme(darkTheme = true) {
        CapsLabel(
            text = "Cumulative GPA",
            modifier = Modifier
                .background(MeadowTheme.colors.card)
                .padding(12.dp),
        )
    }
}
