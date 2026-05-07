package com.hussienfahmy.semester_history_presentation.components

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.gestures.detectTapGestures
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
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import com.hussienfahmy.core.R
import com.hussienfahmy.core.data.local.entity.Grade
import com.hussienfahmy.core.data.local.entity.Subject
import com.hussienfahmy.core.data.local.model.GradeName
import com.hussienfahmy.core.domain.subject_settings.model.SubjectSettings
import com.hussienfahmy.core_ui.LocalSpacing
import com.hussienfahmy.core_ui.presentation.util.toStringWithOptionalDecimals

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun AddSubjectSheet(
    availableGrades: List<Grade>,
    subjectSettings: SubjectSettings?,
    onDismiss: () -> Unit,
    onAdd: (name: String, creditHours: Double, gradeName: GradeName, totalMarks: Double, semesterMarks: Subject.SemesterMarks?, metadata: Subject.MetaData) -> Unit,
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

    val hasExistingMarks = initialSubject?.semesterMarks != null
    var midtermEnabled by remember { mutableStateOf(hasExistingMarks && initialSubject.metadata.midtermAvailable) }
    var practicalEnabled by remember { mutableStateOf(hasExistingMarks && initialSubject.metadata.practicalAvailable) }
    var oralEnabled by remember { mutableStateOf(hasExistingMarks && initialSubject.metadata.oralAvailable) }
    var projectEnabled by remember { mutableStateOf(hasExistingMarks && initialSubject.metadata.projectAvailable) }

    var midtermInput by remember {
        mutableStateOf(
            initialSubject?.semesterMarks?.midterm?.toString() ?: ""
        )
    }
    var practicalInput by remember {
        mutableStateOf(
            initialSubject?.semesterMarks?.practical?.toString() ?: ""
        )
    }
    var oralInput by remember {
        mutableStateOf(
            initialSubject?.semesterMarks?.oral?.toString() ?: ""
        )
    }
    var projectInput by remember {
        mutableStateOf(
            initialSubject?.semesterMarks?.project?.toString() ?: ""
        )
    }

    val anyMarkEnabled = midtermEnabled || practicalEnabled || oralEnabled || projectEnabled

    val totalMarks by remember {
        derivedStateOf {
            val hours = creditHours.toDoubleOrNull() ?: 0.0
            when (subjectSettings?.subjectsMarksDependsOn) {
                SubjectSettings.SubjectsMarksDependsOn.CONSTANT -> subjectSettings.constantMarks
                SubjectSettings.SubjectsMarksDependsOn.CREDIT -> hours * subjectSettings.marksPerCreditHour
                null -> initialSubject?.totalMarks ?: 0.0
            }
        }
    }

    val marksValid =
        (!midtermEnabled || midtermInput.isEmpty() || midtermInput.toDoubleOrNull() != null)
                && (!practicalEnabled || practicalInput.isEmpty() || practicalInput.toDoubleOrNull() != null)
                && (!oralEnabled || oralInput.isEmpty() || oralInput.toDoubleOrNull() != null)
                && (!projectEnabled || projectInput.isEmpty() || projectInput.toDoubleOrNull() != null)

    val isValid = name.isNotBlank()
            && creditHours.toDoubleOrNull() != null
            && selectedGrade != null
            && marksValid

    ModalBottomSheet(onDismissRequest = onDismiss) {
        val focusManager = LocalFocusManager.current
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .pointerInput(Unit) {
                    detectTapGestures {
                        focusManager.clearFocus()
                    }
                }
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
                style = MaterialTheme.typography.labelMedium,
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

            HorizontalDivider(modifier = Modifier.padding(vertical = spacing.small))

            Text(
                text = stringResource(R.string.history_marks_section_title),
                style = MaterialTheme.typography.labelMedium,
            )

            FlowRow(horizontalArrangement = Arrangement.spacedBy(spacing.small)) {
                FilterChip(
                    selected = midtermEnabled,
                    onClick = {
                        midtermEnabled = !midtermEnabled
                    },
                    label = { Text(stringResource(R.string.midterm)) },
                )
                FilterChip(
                    selected = practicalEnabled,
                    onClick = {
                        practicalEnabled = !practicalEnabled
                    },
                    label = { Text(stringResource(R.string.practical)) },
                )
                FilterChip(
                    selected = oralEnabled,
                    onClick = { oralEnabled = !oralEnabled },
                    label = { Text(stringResource(R.string.oral)) },
                )
                FilterChip(
                    selected = projectEnabled,
                    onClick = {
                        projectEnabled = !projectEnabled
                    },
                    label = { Text(stringResource(R.string.project)) },
                )
            }

            if (anyMarkEnabled) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(spacing.small),
                    modifier = Modifier
                        .fillMaxWidth()
                        .animateContentSize(),
                ) {
                    if (midtermEnabled) MarkField(
                        label = stringResource(R.string.midterm),
                        value = midtermInput,
                        onValueChange = { midtermInput = it },
                    )
                    if (practicalEnabled) MarkField(
                        label = stringResource(R.string.practical),
                        value = practicalInput,
                        onValueChange = { practicalInput = it },
                    )
                    if (oralEnabled) MarkField(
                        label = stringResource(R.string.oral),
                        value = oralInput,
                        onValueChange = { oralInput = it },
                    )
                    if (projectEnabled) MarkField(
                        label = stringResource(R.string.project),
                        value = projectInput,
                        onValueChange = { projectInput = it },
                    )
                }

                if (totalMarks > 0) {
                    Text(
                        text = stringResource(
                            R.string.history_total_marks_info,
                            totalMarks.toStringWithOptionalDecimals()
                        ),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }

            Button(
                onClick = {
                    val midterm = if (midtermEnabled) midtermInput.toDoubleOrNull() else null
                    val practical = if (practicalEnabled) practicalInput.toDoubleOrNull() else null
                    val oral = if (oralEnabled) oralInput.toDoubleOrNull() else null
                    val project = if (projectEnabled) projectInput.toDoubleOrNull() else null
                    val semesterMarks =
                        if (midterm != null || practical != null || oral != null || project != null) {
                            Subject.SemesterMarks(
                                midterm = midterm,
                                practical = practical,
                                oral = oral,
                                project = project,
                            )
                        } else null
                    val metadata = Subject.MetaData(
                        midtermAvailable = midtermEnabled,
                        practicalAvailable = practicalEnabled,
                        oralAvailable = oralEnabled,
                        projectAvailable = projectEnabled,
                    )
                    onAdd(
                        name,
                        creditHours.toDouble(),
                        selectedGrade!!.name,
                        totalMarks,
                        semesterMarks,
                        metadata
                    )
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

@Composable
private fun MarkField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    OutlinedTextField(
        modifier = modifier.fillMaxWidth(),
        value = value,
        onValueChange = onValueChange,
        label = { Text(text = label, style = MaterialTheme.typography.bodySmall) },
        isError = value.isNotBlank() && value.toDoubleOrNull() == null,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
        singleLine = true,
    )
}
