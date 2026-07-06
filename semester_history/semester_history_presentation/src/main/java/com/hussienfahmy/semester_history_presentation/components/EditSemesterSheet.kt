package com.hussienfahmy.semester_history_presentation.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.hussienfahmy.core.R
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

    MeadowBottomSheet(onDismiss = onDismiss, modifier = modifier) {
        Text(
            text = stringResource(R.string.edit),
            style = MaterialTheme.typography.headlineMedium,
            color = colors.ink,
        )

        Spacer(modifier = Modifier.height(12.dp))

        MeadowTextField(
            value = label,
            onValueChange = { label = it },
            label = stringResource(R.string.history_semester_label),
            isError = label.isBlank(),
            modifier = Modifier.fillMaxWidth(),
        )

        if (isSummary) {
            Spacer(modifier = Modifier.height(8.dp))
            MeadowTextField(
                value = gpa,
                onValueChange = { gpa = it },
                label = stringResource(R.string.cumulative_gpa),
                isError = gpa.isNotBlank() && (gpa.toDoubleOrNull() ?: -1.0) < 0.0,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                modifier = Modifier.fillMaxWidth(),
            )

            Spacer(modifier = Modifier.height(8.dp))
            MeadowTextField(
                value = hours,
                onValueChange = { hours = it },
                label = stringResource(R.string.credit_hours),
                isError = hours.isNotBlank() && (hours.toIntOrNull() ?: 0) <= 0,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth(),
            )
        }

        Spacer(modifier = Modifier.height(14.dp))

        PillButton(
            text = stringResource(R.string.save),
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
