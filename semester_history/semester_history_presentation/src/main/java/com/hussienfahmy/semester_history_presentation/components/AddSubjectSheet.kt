package com.hussienfahmy.semester_history_presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
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
import com.hussienfahmy.core.data.local.entity.Grade
import com.hussienfahmy.core.data.local.entity.Subject
import com.hussienfahmy.core.data.local.model.GradeName
import com.hussienfahmy.core_ui.LocalSpacing

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun AddSubjectSheet(
    availableGrades: List<Grade>,
    onDismiss: () -> Unit,
    onAdd: (name: String, creditHours: Double, gradeName: GradeName) -> Unit,
    initialSubject: Subject? = null,
) {
    val isEditMode = initialSubject != null
    val spacing = LocalSpacing.current
    var name by remember { mutableStateOf(initialSubject?.name ?: "") }
    var creditHours by remember {
        mutableStateOf(
            initialSubject?.creditHours?.let {
                if (it % 1.0 == 0.0) it.toInt().toString() else it.toString()
            } ?: ""
        )
    }
    var selectedGrade by remember {
        mutableStateOf(availableGrades.find { it.name == initialSubject?.gradeName })
    }

    val isValid = name.isNotBlank()
            && creditHours.toDoubleOrNull() != null
            && selectedGrade != null

    ModalBottomSheet(onDismissRequest = onDismiss) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(spacing.medium)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(spacing.small),
        ) {
            Text(
                text = if (isEditMode) stringResource(R.string.history_edit_subject) else stringResource(
                    R.string.add_subject
                ),
                style = MaterialTheme.typography.titleLarge,
            )

            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text(stringResource(R.string.subject_name)) },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
            )

            OutlinedTextField(
                value = creditHours,
                onValueChange = { creditHours = it },
                label = { Text(stringResource(R.string.credit_hours)) },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                modifier = Modifier.fillMaxWidth(),
                isError = creditHours.isNotBlank() && creditHours.toDoubleOrNull() == null,
            )

            Text(
                stringResource(R.string.history_grade_label),
                style = MaterialTheme.typography.labelMedium
            )
            FlowRow(horizontalArrangement = Arrangement.spacedBy(spacing.small)) {
                availableGrades.sortedByDescending { it.percentage ?: 0.0 }.forEach { grade ->
                    FilterChip(
                        selected = selectedGrade == grade,
                        onClick = { selectedGrade = grade },
                        label = { Text(grade.name.symbol) },
                    )
                }
            }

            Button(
                onClick = {
                    onAdd(name, creditHours.toDouble(), selectedGrade!!.name)
                    onDismiss()
                },
                enabled = isValid,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(if (isEditMode) stringResource(R.string.save) else stringResource(R.string.add))
            }

            Spacer(modifier = Modifier.height(spacing.medium))
        }
    }
}
