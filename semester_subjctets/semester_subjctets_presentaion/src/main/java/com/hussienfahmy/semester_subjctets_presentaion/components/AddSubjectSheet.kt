package com.hussienfahmy.semester_subjctets_presentaion.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import org.jetbrains.compose.resources.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hussienfahmy.core.generated.resources.Res
import com.hussienfahmy.core_ui.presentation.components.meadow.MeadowBottomSheet
import com.hussienfahmy.core_ui.presentation.components.meadow.MeadowSwitch
import com.hussienfahmy.core_ui.presentation.components.meadow.MeadowTextField
import com.hussienfahmy.core_ui.presentation.components.meadow.PillButton
import com.hussienfahmy.core_ui.presentation.components.meadow.PillButtonStyle
import com.hussienfahmy.core_ui.theme.MeadowTheme

/** Add-subject bottom sheet (design 4b setup-sheet anatomy). */
@Composable
fun AddSubjectSheet(
    onAddSubject: (
        subjectName: String,
        creditHours: String,
        midtermAvailable: Boolean,
        practicalAvailable: Boolean,
        oralAvailable: Boolean,
        projectAvailable: Boolean,
    ) -> Unit,
    onDismiss: () -> Unit,
) {
    MeadowBottomSheet(onDismiss = onDismiss) {
        AddSubjectSheetContent(onAddSubject = onAddSubject, onDismiss = onDismiss)
    }
}

@Composable
fun AddSubjectSheetContent(
    onAddSubject: (
        subjectName: String,
        creditHours: String,
        midtermAvailable: Boolean,
        practicalAvailable: Boolean,
        oralAvailable: Boolean,
        projectAvailable: Boolean,
    ) -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = MeadowTheme.colors

    var subjectName by remember { mutableStateOf("") }
    var creditHours by remember { mutableStateOf("") }
    var midtermAvailable by remember { mutableStateOf(true) }
    var practicalAvailable by remember { mutableStateOf(false) }
    var oralAvailable by remember { mutableStateOf(true) }
    var projectAvailable by remember { mutableStateOf(false) }

    var validSubjectName by remember { mutableStateOf(true) }
    var validCreditHours by remember { mutableStateOf(true) }

    val validate = {
        validSubjectName = subjectName.isNotBlank()
        validCreditHours = creditHours.isNotBlank() && creditHours.all { it.isDigit() }
        validSubjectName && validCreditHours
    }

    val saveSubject = {
        onAddSubject(
            subjectName,
            creditHours,
            midtermAvailable,
            practicalAvailable,
            oralAvailable,
            projectAvailable,
        )
    }

    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = stringResource(Res.string.add_subject),
            style = MaterialTheme.typography.headlineMedium,
            color = colors.ink,
        )

        Spacer(modifier = Modifier.height(12.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            MeadowTextField(
                value = subjectName,
                onValueChange = {
                    subjectName = it
                    if (!validSubjectName) validSubjectName = it.isNotBlank()
                },
                label = stringResource(Res.string.subject_name),
                isError = !validSubjectName,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next,
                ),
                modifier = Modifier.weight(2.2f),
            )
            MeadowTextField(
                value = creditHours,
                onValueChange = {
                    creditHours = it.filter { char -> char.isDigit() }
                    if (!validCreditHours) validCreditHours = creditHours.isNotBlank()
                },
                label = stringResource(Res.string.hours),
                isError = !validCreditHours,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done,
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        if (validate()) {
                            saveSubject()
                            onDismiss()
                        }
                    },
                ),
                modifier = Modifier.weight(1f),
            )
        }

        Spacer(modifier = Modifier.height(14.dp))

        Text(
            text = stringResource(Res.string.sheet_components_question),
            style = MaterialTheme.typography.bodySmall,
            color = colors.inkFaint,
        )

        Spacer(modifier = Modifier.height(4.dp))

        ComponentToggleRow(
            title = stringResource(Res.string.midterm),
            checked = midtermAvailable,
            onCheckedChange = { midtermAvailable = it },
        )
        SheetDivider()
        ComponentToggleRow(
            title = stringResource(Res.string.oral),
            checked = oralAvailable,
            onCheckedChange = { oralAvailable = it },
        )
        SheetDivider()
        ComponentToggleRow(
            title = stringResource(Res.string.practical),
            checked = practicalAvailable,
            onCheckedChange = { practicalAvailable = it },
        )
        SheetDivider()
        ComponentToggleRow(
            title = stringResource(Res.string.project),
            checked = projectAvailable,
            onCheckedChange = { projectAvailable = it },
        )

        Spacer(modifier = Modifier.height(14.dp))

        PillButton(
            text = stringResource(Res.string.add_and_close),
            onClick = {
                if (validate()) {
                    saveSubject()
                    onDismiss()
                }
            },
            style = PillButtonStyle.Primary,
            modifier = Modifier.fillMaxWidth(),
        )

        Spacer(modifier = Modifier.height(8.dp))

        PillButton(
            text = stringResource(Res.string.add_another_subject),
            onClick = {
                if (validate()) {
                    saveSubject()
                    subjectName = ""
                    creditHours = ""
                }
            },
            style = PillButtonStyle.Tonal,
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

@Composable
private fun ComponentToggleRow(
    title: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
) {
    val colors = MeadowTheme.colors

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 11.dp),
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            color = if (checked) colors.ink else colors.chipText,
            modifier = Modifier.weight(1f),
        )
        MeadowSwitch(checked = checked, onCheckedChange = onCheckedChange)
    }
}

@Composable
private fun SheetDivider() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(1.dp)
            .background(MeadowTheme.colors.divider),
    )
}

@Preview(name = "AddSubjectSheet · light", showBackground = true)
@Composable
private fun AddSubjectSheetLightPreview() {
    MeadowTheme(darkTheme = false) {
        Column(
            modifier = Modifier
                .background(MeadowTheme.colors.card)
                .padding(start = 20.dp, end = 20.dp, top = 20.dp, bottom = 22.dp),
        ) {
            AddSubjectSheetContent(onAddSubject = { _, _, _, _, _, _ -> }, onDismiss = {})
        }
    }
}

@Preview(name = "AddSubjectSheet · dark", showBackground = true)
@Composable
private fun AddSubjectSheetDarkPreview() {
    MeadowTheme(darkTheme = true) {
        Column(
            modifier = Modifier
                .background(MeadowTheme.colors.card)
                .padding(start = 20.dp, end = 20.dp, top = 20.dp, bottom = 22.dp),
        ) {
            AddSubjectSheetContent(onAddSubject = { _, _, _, _, _, _ -> }, onDismiss = {})
        }
    }
}
