package com.hussienfahmy.semester_history_presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hussienfahmy.core.generated.resources.*
import com.hussienfahmy.core_ui.presentation.components.meadow.MeadowBottomSheet
import com.hussienfahmy.core_ui.presentation.components.meadow.MeadowTextField
import com.hussienfahmy.core_ui.presentation.components.meadow.PillButton
import com.hussienfahmy.core_ui.presentation.components.meadow.PillButtonStyle
import com.hussienfahmy.core_ui.theme.MeadowTheme
import com.hussienfahmy.semester_history_domain.model.Semester

@Composable
fun EditSemesterSheet(
    semester: Semester,
    onDismiss: () -> Unit,
    onSaveLabel: (id: Long, label: String) -> Unit,
    onSaveSummary: (id: Long, label: String, gpa: Double, hours: Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    MeadowBottomSheet(onDismiss = onDismiss, modifier = modifier) {
        EditSemesterSheetContent(
            semester = semester,
            onDismiss = onDismiss,
            onSaveLabel = onSaveLabel,
            onSaveSummary = onSaveSummary,
        )
    }
}

@Composable
fun EditSemesterSheetContent(
    semester: Semester,
    onDismiss: () -> Unit,
    onSaveLabel: (id: Long, label: String) -> Unit,
    onSaveSummary: (id: Long, label: String, gpa: Double, hours: Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = MeadowTheme.colors

    var label by remember { mutableStateOf(semester.label) }
    var gpa by remember { mutableStateOf(semester.semesterGPA.toString()) }
    var hours by remember { mutableStateOf(semester.totalCreditHours.toString()) }

    val isSummary = semester.type == Semester.Type.SUMMARY

    val isValid = label.isNotBlank()
            && if (isSummary) (
            (gpa.toDoubleOrNull() ?: -1.0) >= 0.0 &&
                    (hours.toIntOrNull() ?: 0) > 0
            ) else true

    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = stringResource(Res.string.edit),
            style = MaterialTheme.typography.headlineMedium,
            color = colors.ink,
        )

        Spacer(modifier = Modifier.height(12.dp))

        MeadowTextField(
            value = label,
            onValueChange = { label = it },
            label = stringResource(Res.string.history_semester_label),
            isError = label.isBlank(),
            modifier = Modifier.fillMaxWidth(),
        )

        if (isSummary) {
            Spacer(modifier = Modifier.height(8.dp))
            MeadowTextField(
                value = gpa,
                onValueChange = { gpa = it },
                label = stringResource(Res.string.cumulative_gpa),
                isError = gpa.isNotBlank() && (gpa.toDoubleOrNull() ?: -1.0) < 0.0,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                modifier = Modifier.fillMaxWidth(),
            )

            Spacer(modifier = Modifier.height(8.dp))
            MeadowTextField(
                value = hours,
                onValueChange = { hours = it },
                label = stringResource(Res.string.credit_hours),
                isError = hours.isNotBlank() && (hours.toIntOrNull() ?: 0) <= 0,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth(),
            )
        }

        Spacer(modifier = Modifier.height(14.dp))

        PillButton(
            text = stringResource(Res.string.save),
            onClick = {
                if (isSummary) {
                    onSaveSummary(semester.id, label, gpa.toDouble(), hours.toInt())
                } else {
                    onSaveLabel(semester.id, label)
                }
                onDismiss()
            },
            style = PillButtonStyle.Primary,
            enabled = isValid,
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

private val previewSummarySemester = Semester(
    id = 1L,
    label = "Year 2 - Semester 1",
    level = 2,
    type = Semester.Type.SUMMARY,
    semesterGPA = 3.5,
    totalCreditHours = 18,
    status = Semester.Status.ARCHIVED,
    order = 0,
    createdAt = 0L,
    archivedAt = 0L,
)

@Composable
private fun EditSemesterSheetShowcase() {
    Column(
        modifier = Modifier
            .background(MeadowTheme.colors.card)
            .padding(start = 20.dp, end = 20.dp, top = 20.dp, bottom = 22.dp),
    ) {
        EditSemesterSheetContent(
            semester = previewSummarySemester,
            onDismiss = {},
            onSaveLabel = { _, _ -> },
            onSaveSummary = { _, _, _, _ -> },
        )
    }
}

@Preview(name = "EditSemesterSheet · light", showBackground = true)
@Composable
private fun EditSemesterSheetLightPreview() {
    MeadowTheme(darkTheme = false) { EditSemesterSheetShowcase() }
}

@Preview(name = "EditSemesterSheet · dark", showBackground = true)
@Composable
private fun EditSemesterSheetDarkPreview() {
    MeadowTheme(darkTheme = true) { EditSemesterSheetShowcase() }
}

@Preview(name = "EditSemesterSheet · AR", showBackground = true, locale = "ar")
@Composable
private fun EditSemesterSheetArPreview() {
    MeadowTheme(darkTheme = false) { EditSemesterSheetShowcase() }
}
