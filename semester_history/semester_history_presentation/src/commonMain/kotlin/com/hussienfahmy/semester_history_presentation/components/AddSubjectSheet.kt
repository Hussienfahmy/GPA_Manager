package com.hussienfahmy.semester_history_presentation.components

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import org.jetbrains.compose.resources.stringResource
import androidx.compose.ui.text.style.TextDirection
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.hussienfahmy.core.generated.resources.*
import com.hussienfahmy.core.data.local.entity.Grade
import com.hussienfahmy.core.data.local.entity.Subject
import com.hussienfahmy.core.data.local.model.GradeName
import com.hussienfahmy.core.domain.subject_settings.model.SubjectSettings
import com.hussienfahmy.core_ui.presentation.components.meadow.CapsLabel
import com.hussienfahmy.core_ui.presentation.components.meadow.MeadowBottomSheet
import com.hussienfahmy.core_ui.presentation.components.meadow.MeadowTextField
import com.hussienfahmy.core_ui.presentation.components.meadow.PillButton
import com.hussienfahmy.core_ui.presentation.components.meadow.PillButtonStyle
import com.hussienfahmy.core_ui.presentation.util.toStringWithOptionalDecimals
import com.hussienfahmy.core_ui.theme.MeadowTheme

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun AddSubjectSheet(
    availableGrades: List<Grade>,
    subjectSettings: SubjectSettings?,
    onDismiss: () -> Unit,
    onAdd: (name: String, creditHours: Double, gradeName: GradeName, totalMarks: Double, semesterMarks: Subject.SemesterMarks?, metadata: Subject.MetaData) -> Unit,
    initialSubject: Subject? = null,
) {
    val isEditMode = initialSubject != null
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

    var midtermInput by remember { mutableStateOf(initialSubject?.semesterMarks?.midterm?.toString() ?: "") }
    var practicalInput by remember { mutableStateOf(initialSubject?.semesterMarks?.practical?.toString() ?: "") }
    var oralInput by remember { mutableStateOf(initialSubject?.semesterMarks?.oral?.toString() ?: "") }
    var projectInput by remember { mutableStateOf(initialSubject?.semesterMarks?.project?.toString() ?: "") }
    var finalExamScoreInput by remember { mutableStateOf(initialSubject?.semesterMarks?.finalExamScore?.toString() ?: "") }

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
                && (finalExamScoreInput.isEmpty() || finalExamScoreInput.toDoubleOrNull() != null)

    val isValid = name.isNotBlank()
            && creditHours.toDoubleOrNull() != null
            && selectedGrade != null
            && marksValid

    MeadowBottomSheet(onDismiss = onDismiss) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            Text(
                text = if (isEditMode) stringResource(Res.string.history_edit_subject)
                else stringResource(Res.string.add_subject),
                style = MaterialTheme.typography.headlineMedium,
                color = MeadowTheme.colors.ink,
            )

            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                MeadowTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = stringResource(Res.string.subject_name),
                    modifier = Modifier.weight(2.2f),
                )
                MeadowTextField(
                    value = creditHours,
                    onValueChange = { creditHours = it },
                    label = stringResource(Res.string.credit_hours),
                    isError = creditHours.isNotBlank() && creditHours.toDoubleOrNull() == null,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    modifier = Modifier.weight(1f),
                )
            }

            CapsLabel(text = stringResource(Res.string.history_grade_label))
            FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                availableGrades.sortedByDescending { it.percentage ?: 0.0 }.forEach { grade ->
                    SelectablePill(
                        text = grade.name.symbol,
                        selected = selectedGrade == grade,
                        onClick = { selectedGrade = grade },
                        ltr = true,
                    )
                }
            }
            if (selectedGrade == null) {
                Text(
                    text = stringResource(Res.string.history_assign_grade_hint),
                    style = MaterialTheme.typography.bodySmall,
                    color = MeadowTheme.colors.marks.deep,
                )
            }

            SheetDivider()

            CapsLabel(text = stringResource(Res.string.history_marks_section_title))
            Text(
                text = stringResource(Res.string.history_marks_section_note),
                style = MaterialTheme.typography.bodySmall,
                color = MeadowTheme.colors.inkFaint,
            )
            FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                SelectablePill(stringResource(Res.string.midterm), midtermEnabled) { midtermEnabled = !midtermEnabled }
                SelectablePill(stringResource(Res.string.practical), practicalEnabled) { practicalEnabled = !practicalEnabled }
                SelectablePill(stringResource(Res.string.oral), oralEnabled) { oralEnabled = !oralEnabled }
                SelectablePill(stringResource(Res.string.project), projectEnabled) { projectEnabled = !projectEnabled }
            }

            if (anyMarkEnabled) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .animateContentSize(),
                ) {
                    if (midtermEnabled) MeadowTextField(
                        value = midtermInput,
                        onValueChange = { midtermInput = it },
                        label = stringResource(Res.string.midterm),
                        isError = midtermInput.isNotBlank() && midtermInput.toDoubleOrNull() == null,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        modifier = Modifier.fillMaxWidth(),
                    )
                    if (practicalEnabled) MeadowTextField(
                        value = practicalInput,
                        onValueChange = { practicalInput = it },
                        label = stringResource(Res.string.practical),
                        isError = practicalInput.isNotBlank() && practicalInput.toDoubleOrNull() == null,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        modifier = Modifier.fillMaxWidth(),
                    )
                    if (oralEnabled) MeadowTextField(
                        value = oralInput,
                        onValueChange = { oralInput = it },
                        label = stringResource(Res.string.oral),
                        isError = oralInput.isNotBlank() && oralInput.toDoubleOrNull() == null,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        modifier = Modifier.fillMaxWidth(),
                    )
                    if (projectEnabled) MeadowTextField(
                        value = projectInput,
                        onValueChange = { projectInput = it },
                        label = stringResource(Res.string.project),
                        isError = projectInput.isNotBlank() && projectInput.toDoubleOrNull() == null,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        modifier = Modifier.fillMaxWidth(),
                    )
                }

                if (totalMarks > 0) {
                    Text(
                        text = stringResource(
                            Res.string.history_total_marks_info,
                            totalMarks.toStringWithOptionalDecimals()
                        ),
                        style = MaterialTheme.typography.bodySmall,
                        color = MeadowTheme.colors.inkFaint,
                    )
                }
            }

            SheetDivider()

            CapsLabel(text = stringResource(Res.string.final_exam))
            MeadowTextField(
                value = finalExamScoreInput,
                onValueChange = { finalExamScoreInput = it },
                label = stringResource(Res.string.final_exam_score),
                isError = finalExamScoreInput.isNotBlank() && finalExamScoreInput.toDoubleOrNull() == null,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                modifier = Modifier.fillMaxWidth(),
            )

            Spacer(modifier = Modifier.height(4.dp))

            PillButton(
                text = if (isEditMode) stringResource(Res.string.save) else stringResource(Res.string.add),
                onClick = {
                    val midterm = if (midtermEnabled) midtermInput.toDoubleOrNull() else null
                    val practical = if (practicalEnabled) practicalInput.toDoubleOrNull() else null
                    val oral = if (oralEnabled) oralInput.toDoubleOrNull() else null
                    val project = if (projectEnabled) projectInput.toDoubleOrNull() else null
                    val finalExamScore = finalExamScoreInput.toDoubleOrNull()
                    val semesterMarks =
                        if (midterm != null || practical != null || oral != null || project != null || finalExamScore != null) {
                            Subject.SemesterMarks(
                                midterm = midterm,
                                practical = practical,
                                oral = oral,
                                project = project,
                                finalExamScore = finalExamScore,
                            )
                        } else null
                    val metadata = Subject.MetaData(
                        midtermAvailable = midtermEnabled,
                        practicalAvailable = practicalEnabled,
                        oralAvailable = oralEnabled,
                        projectAvailable = projectEnabled,
                    )
                    onAdd(name, creditHours.toDouble(), selectedGrade!!.name, totalMarks, semesterMarks, metadata)
                    onDismiss()
                },
                style = PillButtonStyle.Primary,
                enabled = isValid,
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}

@Composable
private fun SelectablePill(
    text: String,
    selected: Boolean,
    ltr: Boolean = false,
    onClick: () -> Unit,
) {
    val colors = MeadowTheme.colors
    val accent = MeadowTheme.accent
    val shape = CircleShape

    Text(
        text = text,
        style = if (ltr) {
            MaterialTheme.typography.titleSmall.copy(textDirection = TextDirection.Ltr)
        } else {
            MaterialTheme.typography.titleSmall
        },
        color = if (selected) accent.onAccent else colors.inkMuted,
        modifier = Modifier
            .clip(shape)
            .background(if (selected) accent.accent else colors.surfaceSunken)
            .then(
                if (!selected) Modifier.border(1.dp, colors.divider, shape) else Modifier
            )
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick,
            )
            .padding(horizontal = 14.dp, vertical = 7.dp),
    )
}

@Composable
private fun SheetDivider() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .height(1.dp)
            .background(MeadowTheme.colors.divider),
    )
}
