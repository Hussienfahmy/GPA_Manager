package com.hussienfahmy.semester_marks_presentaion.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import com.hussienfahmy.core_ui.theme.CapsLabelStyle
import com.hussienfahmy.core_ui.theme.MeadowRadius
import com.hussienfahmy.core_ui.theme.MeadowTheme

/**
 * A component-mark tile (design 2a/4b): sunken tile with a caps label and the
 * mark. Editing happens in place — focusing turns on the tab-hue border, no
 * dialogs for marks. When [editable] is false the tile is read-only ("–" when
 * empty).
 */
@Composable
fun MarkTile(
    title: String,
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    editable: Boolean = true,
) {
    val colors = MeadowTheme.colors
    val accent = MeadowTheme.accent
    val shape = RoundedCornerShape(MeadowRadius.tile)

    val interactionSource = remember { MutableInteractionSource() }
    val focused by interactionSource.collectIsFocusedAsState()

    val empty = value.isBlank()

    Column(
        modifier = modifier
            .background(if (focused) colors.card else colors.surfaceSunken, shape)
            .then(
                if (focused) Modifier.border(2.dp, accent.accent, shape)
                else Modifier
            )
            .padding(horizontal = 12.dp, vertical = 9.dp),
    ) {
        Text(
            text = title.uppercase(),
            style = CapsLabelStyle().copy(fontSize = 10.5.sp, letterSpacing = 0.06.em),
            color = when {
                focused -> accent.deep
                empty -> colors.inkDisabled
                else -> colors.navItemText
            },
            maxLines = 1,
        )

        if (editable) {
            BasicTextField(
                value = value,
                onValueChange = onValueChange,
                textStyle = MaterialTheme.typography.titleLarge.copy(
                    fontSize = 16.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = accent.ink,
                ),
                cursorBrush = SolidColor(accent.accent),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                interactionSource = interactionSource,
                modifier = Modifier.fillMaxWidth(),
            )
        } else {
            Text(
                text = if (empty) "–" else value,
                style = MaterialTheme.typography.titleLarge.copy(fontSize = 16.sp),
                color = if (empty) colors.inkDisabled else accent.ink,
                maxLines = 1,
            )
        }
    }
}

@Composable
private fun MarkTileShowcase() {
    Row(
        modifier = Modifier
            .background(MeadowTheme.colors.card)
            .padding(12.dp),
    ) {
        MarkTile(
            title = "Midterm",
            value = "15.0",
            onValueChange = {},
            modifier = Modifier.weight(1f),
        )
        MarkTile(
            title = "Oral",
            value = "",
            onValueChange = {},
            editable = false,
            modifier = Modifier
                .weight(1f)
                .padding(start = 10.dp),
        )
    }
}

@Preview(name = "MarkTile · light")
@Composable
private fun MarkTileLightPreview() {
    MeadowTheme(darkTheme = false) { MarkTileShowcase() }
}

@Preview(name = "MarkTile · dark")
@Composable
private fun MarkTileDarkPreview() {
    MeadowTheme(darkTheme = true) { MarkTileShowcase() }
}
