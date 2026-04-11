package com.hussienfahmy.semester_history_presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import com.hussienfahmy.core.R
import com.hussienfahmy.core_ui.LocalSpacing
import com.hussienfahmy.semester_history_domain.model.Semester

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditSemesterSheet(
    semester: Semester,
    onDismiss: () -> Unit,
    onSaveLabel: (id: Long, label: String) -> Unit,
    onSaveSummary: (id: Long, label: String, gpa: Double, hours: Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    val spacing = LocalSpacing.current
    var label by remember { mutableStateOf(semester.label) }
    var gpa by remember { mutableStateOf(semester.semesterGPA.toString()) }
    var hours by remember { mutableStateOf(semester.totalCreditHours.toString()) }

    val isSummary = semester.type == Semester.Type.SUMMARY

    val isValid = label.isNotBlank()
            && if (isSummary) (
            (gpa.toDoubleOrNull() ?: -1.0) >= 0.0 &&
                    (hours.toIntOrNull() ?: 0) > 0
            ) else true

    ModalBottomSheet(onDismissRequest = onDismiss, modifier = modifier) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(spacing.medium)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(spacing.small),
        ) {
            Text(stringResource(R.string.edit), style = MaterialTheme.typography.titleLarge)

            OutlinedTextField(
                value = label,
                onValueChange = { label = it },
                label = { Text(stringResource(R.string.history_semester_label)) },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
            )

            if (isSummary) {
                OutlinedTextField(
                    value = gpa,
                    onValueChange = { gpa = it },
                    label = { Text(stringResource(R.string.cumulative_gpa)) },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    modifier = Modifier.fillMaxWidth(),
                    isError = gpa.isNotBlank() && (gpa.toDoubleOrNull() ?: -1.0) < 0.0,
                )

                OutlinedTextField(
                    value = hours,
                    onValueChange = { hours = it },
                    label = { Text(stringResource(R.string.credit_hours)) },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth(),
                    isError = hours.isNotBlank() && (hours.toIntOrNull() ?: 0) <= 0,
                )
            }

            Button(
                onClick = {
                    if (isSummary) {
                        onSaveSummary(semester.id, label, gpa.toDouble(), hours.toInt())
                    } else {
                        onSaveLabel(semester.id, label)
                    }
                    onDismiss()
                },
                enabled = isValid,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(stringResource(R.string.save))
            }

            Spacer(modifier = Modifier.height(spacing.medium))
        }
    }
}
