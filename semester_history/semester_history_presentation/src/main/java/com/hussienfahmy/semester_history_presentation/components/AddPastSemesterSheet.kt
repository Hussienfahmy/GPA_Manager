package com.hussienfahmy.semester_history_presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
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
fun AddPastSemesterSheet(
    onDismiss: () -> Unit,
    onAddSummary: (label: String, gpa: Double, hours: Int, level: Int) -> Unit,
    onAddDetailed: (label: String, level: Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    val spacing = LocalSpacing.current
    var selectedType by remember { mutableStateOf(Semester.Type.SUMMARY) }
    var label by remember { mutableStateOf("") }
    var gpa by remember { mutableStateOf("") }
    var hours by remember { mutableStateOf("") }

    val isValid = when (selectedType) {
        Semester.Type.SUMMARY ->
            label.isNotBlank()
                    && (gpa.toDoubleOrNull() ?: -1.0) >= 0.0
                    && (hours.toIntOrNull() ?: 0) > 0

        Semester.Type.DETAILED ->
            label.isNotBlank()
    }

    ModalBottomSheet(onDismissRequest = onDismiss, modifier = modifier) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(spacing.medium)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(spacing.small),
        ) {
            Text(
                stringResource(R.string.history_add_past_semester),
                style = MaterialTheme.typography.titleLarge
            )

            Row(horizontalArrangement = Arrangement.spacedBy(spacing.small)) {
                FilterChip(
                    selected = selectedType == Semester.Type.SUMMARY,
                    onClick = { selectedType = Semester.Type.SUMMARY },
                    label = { Text(stringResource(R.string.history_type_summary)) },
                )
                FilterChip(
                    selected = selectedType == Semester.Type.DETAILED,
                    onClick = { selectedType = Semester.Type.DETAILED },
                    label = { Text(stringResource(R.string.history_type_detailed)) },
                )
            }

            Text(
                text = when (selectedType) {
                    Semester.Type.SUMMARY -> stringResource(R.string.history_summary_type_description)
                    Semester.Type.DETAILED -> stringResource(R.string.history_detailed_type_description)
                },
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )

            OutlinedTextField(
                value = label,
                onValueChange = { label = it },
                label = { Text(stringResource(R.string.history_label)) },
                placeholder = {
                    Text(
                        if (selectedType == Semester.Type.SUMMARY)
                            stringResource(R.string.history_semester_label_hint_summary)
                        else
                            stringResource(R.string.history_semester_label_hint_detailed)
                    )
                },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
            )

            if (selectedType == Semester.Type.SUMMARY) {
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
                    when (selectedType) {
                        Semester.Type.SUMMARY -> onAddSummary(
                            label,
                            gpa.toDouble(),
                            hours.toInt(),
                            0
                        )

                        Semester.Type.DETAILED -> onAddDetailed(label, 0)
                    }
                    onDismiss()
                },
                enabled = isValid,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(stringResource(R.string.add))
            }

            Spacer(modifier = Modifier.height(spacing.medium))
        }
    }
}
