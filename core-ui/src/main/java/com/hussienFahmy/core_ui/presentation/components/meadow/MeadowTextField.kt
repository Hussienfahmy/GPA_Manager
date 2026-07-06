package com.hussienfahmy.core_ui.presentation.components.meadow

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import com.hussienfahmy.core_ui.theme.CapsLabelStyle
import com.hussienfahmy.core_ui.theme.MeadowTheme

/**
 * Text field from the reference sheet. Rest: sunken tile with a caps label.
 * Focused: white tile + 2dp tab-hue border. Error: red border + label + value.
 */
@Composable
fun MeadowTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    isError: Boolean = false,
    singleLine: Boolean = true,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
) {
    val colors = MeadowTheme.colors
    val accent = MeadowTheme.accent
    val shape = RoundedCornerShape(16.dp)

    val interactionSource = remember { MutableInteractionSource() }
    val focused by interactionSource.collectIsFocusedAsState()

    val background = when {
        isError -> colors.fieldErrorBg
        focused -> colors.card
        else -> colors.surfaceSunken
    }
    val borderColor = when {
        isError -> colors.fieldErrorBorder
        focused -> accent.accent
        else -> null
    }
    val labelColor = when {
        isError -> colors.onDangerContainer
        focused -> accent.deep
        else -> colors.navItemIcon
    }
    val valueColor = if (isError) colors.onDangerContainer else colors.ink

    Column(
        modifier = modifier
            .clip(shape)
            .background(background)
            .then(
                if (borderColor != null) Modifier.border(2.dp, borderColor, shape)
                else Modifier
            )
            .padding(horizontal = 14.dp, vertical = 11.dp),
    ) {
        Text(
            text = label.uppercase(),
            style = CapsLabelStyle.copy(fontSize = 10.sp, letterSpacing = 0.05.em),
            color = labelColor,
            maxLines = 1,
        )

        Spacer(modifier = Modifier.height(2.dp))

        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            textStyle = MaterialTheme.typography.titleLarge.copy(
                fontSize = 15.sp,
                fontWeight = FontWeight.ExtraBold,
                color = valueColor,
            ),
            cursorBrush = SolidColor(accent.accent),
            singleLine = singleLine,
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            interactionSource = interactionSource,
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

@Composable
private fun MeadowTextFieldShowcase() {
    Column(
        modifier = Modifier
            .background(MeadowTheme.colors.paper)
            .padding(12.dp),
    ) {
        MeadowTextField(value = "3.5", onValueChange = {}, label = "Rest")
        Spacer(modifier = Modifier.height(8.dp))
        MeadowTextField(value = "4.3", onValueChange = {}, label = "Above max (4.0)", isError = true)
    }
}

@Preview(name = "MeadowTextField · light")
@Composable
private fun MeadowTextFieldLightPreview() {
    MeadowTheme(darkTheme = false) { MeadowTextFieldShowcase() }
}

@Preview(name = "MeadowTextField · dark")
@Composable
private fun MeadowTextFieldDarkPreview() {
    MeadowTheme(darkTheme = true) { MeadowTextFieldShowcase() }
}
