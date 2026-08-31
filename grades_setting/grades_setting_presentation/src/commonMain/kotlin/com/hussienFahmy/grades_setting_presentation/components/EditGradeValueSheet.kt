package com.hussienfahmy.grades_setting_presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import org.jetbrains.compose.resources.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextDirection
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hussienfahmy.core.generated.resources.*
import com.hussienfahmy.core_ui.presentation.components.meadow.MeadowBottomSheet
import com.hussienfahmy.core_ui.presentation.components.meadow.MeadowTextField
import com.hussienfahmy.core_ui.presentation.components.meadow.PillButton
import com.hussienfahmy.core_ui.presentation.components.meadow.PillButtonStyle
import com.hussienfahmy.core_ui.theme.MeadowRadius
import com.hussienfahmy.core_ui.theme.MeadowTheme
import org.koin.compose.viewmodel.koinViewModel

/**
 * Numeric edit sheet for a grade's points / percentage (design 4d).
 * Icon tile shows the grade symbol; a single bordered field with the keypad up.
 */
@Composable
fun EditGradeValueSheet(
    symbol: String,
    title: String,
    subtitle: String,
    fieldLabel: String,
    value: String,
    onDismiss: () -> Unit,
    onSaveClick: (newValue: String) -> Unit,
    viewModel: EditTextDialogViewModel = koinViewModel(),
) {
    LaunchedEffect(value) {
        viewModel.onValueChanged(value)
    }

    MeadowBottomSheet(onDismiss = onDismiss) {
        EditGradeValueSheetContent(
            symbol = symbol,
            title = title,
            subtitle = subtitle,
            fieldLabel = fieldLabel,
            value = viewModel.value,
            onValueChange = viewModel::onValueChanged,
            onDismiss = onDismiss,
            onSaveClick = { onSaveClick(viewModel.value) },
        )
    }
}

@Composable
fun EditGradeValueSheetContent(
    symbol: String,
    title: String,
    subtitle: String,
    fieldLabel: String,
    value: String,
    onValueChange: (String) -> Unit,
    onDismiss: () -> Unit,
    onSaveClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = MeadowTheme.colors
    val accent = MeadowTheme.accent

    val save = {
        onSaveClick()
        onDismiss()
    }

    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.fillMaxWidth(),
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(38.dp)
                    .clip(RoundedCornerShape(13.dp))
                    .background(accent.container),
            ) {
                Text(
                    text = symbol,
                    style = MaterialTheme.typography.headlineSmall.copy(
                        textDirection = TextDirection.Ltr
                    ),
                    color = accent.deep,
                )
            }
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.headlineSmall,
                    color = colors.ink,
                )
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = colors.inkFaint,
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        MeadowTextField(
            value = value,
            onValueChange = onValueChange,
            label = fieldLabel,
            outlined = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done,
            ),
            keyboardActions = KeyboardActions(onDone = { save() }),
            modifier = Modifier.fillMaxWidth(),
        )

        Spacer(modifier = Modifier.height(14.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.End),
        ) {
            PillButton(
                text = stringResource(Res.string.cancel),
                onClick = onDismiss,
                style = PillButtonStyle.Text,
                compact = true,
            )
            PillButton(
                text = stringResource(Res.string.save),
                onClick = save,
                style = PillButtonStyle.Primary,
                compact = true,
            )
        }
    }
}

@Composable
private fun EditGradeValueSheetShowcase() {
    Column(
        modifier = Modifier
            .background(MeadowTheme.colors.card)
            .padding(start = 20.dp, end = 20.dp, top = 20.dp, bottom = 22.dp),
    ) {
        EditGradeValueSheetContent(
            symbol = "A+",
            title = "Percentage for A+",
            subtitle = "Percentage this grade starts from",
            fieldLabel = "Percentage",
            value = "90",
            onValueChange = {},
            onDismiss = {},
            onSaveClick = {},
        )
    }
}

@Preview(name = "EditGradeValueSheet · light", showBackground = true)
@Composable
private fun EditGradeValueSheetLightPreview() {
    MeadowTheme(darkTheme = false) { EditGradeValueSheetShowcase() }
}

@Preview(name = "EditGradeValueSheet · AR", showBackground = true, locale = "ar")
@Composable
private fun EditGradeValueSheetArPreview() {
    MeadowTheme(darkTheme = false) { EditGradeValueSheetShowcase() }
}
