package com.hussienfahmy.semester_subjctets_presentaion.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import org.jetbrains.compose.resources.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hussienfahmy.core.generated.resources.*
import com.hussienfahmy.core_ui.presentation.components.meadow.MeadowBottomSheet
import com.hussienfahmy.core_ui.presentation.components.meadow.MeadowTextField
import com.hussienfahmy.core_ui.presentation.components.meadow.PillButton
import com.hussienfahmy.core_ui.presentation.components.meadow.PillButtonStyle
import com.hussienfahmy.core_ui.theme.MeadowTheme

/** Rename-subject bottom sheet. */
@Composable
fun RenameSheet(
    onDismiss: () -> Unit,
    onSaveClick: (newName: String) -> Unit,
    subjectCurrentName: String,
) {
    MeadowBottomSheet(onDismiss = onDismiss) {
        RenameSheetContent(
            onDismiss = onDismiss,
            onSaveClick = onSaveClick,
            subjectCurrentName = subjectCurrentName,
        )
    }
}

@Composable
fun RenameSheetContent(
    onDismiss: () -> Unit,
    onSaveClick: (newName: String) -> Unit,
    subjectCurrentName: String,
    modifier: Modifier = Modifier,
) {
    val colors = MeadowTheme.colors

    var newName by remember { mutableStateOf(subjectCurrentName) }

    val save = {
        if (newName.isNotBlank()) {
            onSaveClick(newName)
            onDismiss()
        }
    }

    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = stringResource(Res.string.rename_subject),
            style = MaterialTheme.typography.headlineMedium,
            color = colors.ink,
        )

        Spacer(modifier = Modifier.height(12.dp))

        MeadowTextField(
            value = newName,
            onValueChange = { newName = it },
            label = stringResource(Res.string.new_subject_name),
            isError = newName.isBlank(),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Done,
            ),
            keyboardActions = KeyboardActions(onDone = { save() }),
            modifier = Modifier.fillMaxWidth(),
        )

        Spacer(modifier = Modifier.height(14.dp))

        PillButton(
            text = stringResource(Res.string.save),
            onClick = save,
            style = PillButtonStyle.Primary,
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

@Preview(name = "RenameSheet · light", showBackground = true)
@Composable
private fun RenameSheetLightPreview() {
    MeadowTheme(darkTheme = false) {
        Column(
            modifier = Modifier
                .background(MeadowTheme.colors.card)
                .padding(start = 20.dp, end = 20.dp, top = 20.dp, bottom = 22.dp),
        ) {
            RenameSheetContent(
                onDismiss = {},
                onSaveClick = {},
                subjectCurrentName = "Computer Graphics",
            )
        }
    }
}

@Preview(name = "RenameSheet · dark", showBackground = true)
@Composable
private fun RenameSheetDarkPreview() {
    MeadowTheme(darkTheme = true) {
        Column(
            modifier = Modifier
                .background(MeadowTheme.colors.card)
                .padding(start = 20.dp, end = 20.dp, top = 20.dp, bottom = 22.dp),
        ) {
            RenameSheetContent(
                onDismiss = {},
                onSaveClick = {},
                subjectCurrentName = "Computer Graphics",
            )
        }
    }
}
