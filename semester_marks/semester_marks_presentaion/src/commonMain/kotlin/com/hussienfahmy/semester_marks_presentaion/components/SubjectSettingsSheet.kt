package com.hussienfahmy.semester_marks_presentaion.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
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
import androidx.compose.ui.graphics.SolidColor
import org.jetbrains.compose.resources.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import com.hussienfahmy.core.generated.resources.Res
import com.hussienfahmy.core_ui.presentation.components.meadow.MeadowBottomSheet
import com.hussienfahmy.core_ui.presentation.components.meadow.MeadowSwitch
import com.hussienfahmy.core_ui.presentation.components.meadow.PillButton
import com.hussienfahmy.core_ui.presentation.components.meadow.PillButtonStyle
import com.hussienfahmy.core_ui.presentation.util.toStringWithOptionalDecimals
import com.hussienfahmy.core_ui.theme.CapsLabelStyle
import com.hussienfahmy.core_ui.theme.MeadowTheme

/**
 * Subject setup sheet (design 4b): component toggles + final-exam max marks.
 * Toggles apply immediately; the final-exam value commits on Save.
 */
@Composable
fun SubjectSettingsSheet(
    subjectName: String,
    midtermAvailable: Boolean,
    oralAvailable: Boolean,
    practicalAvailable: Boolean,
    projectAvailable: Boolean,
    finalExamMaxMarks: Double,
    onDismiss: () -> Unit,
    onMidtermCheckChanges: (Boolean) -> Unit,
    onOralCheckChanges: (Boolean) -> Unit,
    onPracticalCheckChanges: (Boolean) -> Unit,
    onProjectCheckChanges: (Boolean) -> Unit,
    onFinalExamMaxMarksSave: (String) -> Unit,
) {
    val colors = MeadowTheme.colors
    val accent = MeadowTheme.accent

    var finalExamInput by remember {
        mutableStateOf(finalExamMaxMarks.toStringWithOptionalDecimals())
    }
    val parsedFinalExam = finalExamInput.toDoubleOrNull()
    val isFinalExamValid = parsedFinalExam != null && parsedFinalExam > 0

    MeadowBottomSheet(onDismiss = onDismiss) {
        Text(
            text = stringResource(Res.string.sheet_setup_title, subjectName),
            style = MaterialTheme.typography.headlineMedium,
            color = colors.ink,
        )
        Text(
            text = stringResource(Res.string.sheet_components_question),
            style = MaterialTheme.typography.bodySmall,
            color = colors.inkFaint,
        )

        Spacer(modifier = Modifier.height(12.dp))

        ToggleRow(
            title = stringResource(Res.string.midterm),
            checked = midtermAvailable,
            onCheckedChange = onMidtermCheckChanges,
        )
        RowDivider()
        ToggleRow(
            title = stringResource(Res.string.oral),
            checked = oralAvailable,
            onCheckedChange = onOralCheckChanges,
        )
        RowDivider()
        ToggleRow(
            title = stringResource(Res.string.practical),
            checked = practicalAvailable,
            onCheckedChange = onPracticalCheckChanges,
        )
        RowDivider()
        ToggleRow(
            title = stringResource(Res.string.project),
            checked = projectAvailable,
            onCheckedChange = onProjectCheckChanges,
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Final-exam max marks — always carries the tab-hue border (design 4b)
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(colors.surfaceSunken, RoundedCornerShape(16.dp))
                .border(
                    width = 2.dp,
                    color = if (isFinalExamValid || finalExamInput.isBlank()) accent.container
                    else colors.fieldErrorBorder,
                    shape = RoundedCornerShape(16.dp),
                )
                .padding(horizontal = 16.dp, vertical = 12.dp),
        ) {
            Text(
                text = stringResource(Res.string.final_exam_label).uppercase(),
                style = CapsLabelStyle.copy(fontSize = 10.5.sp, letterSpacing = 0.06.em),
                color = accent.soft,
                maxLines = 1,
            )
            BasicTextField(
                value = finalExamInput,
                onValueChange = { finalExamInput = it },
                textStyle = MaterialTheme.typography.headlineLarge.copy(
                    fontSize = 18.sp,
                    color = colors.ink,
                ),
                cursorBrush = SolidColor(accent.accent),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth(),
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = buildAnnotatedString {
                append(stringResource(Res.string.final_exam_description_prefix))
                append(" ")
                withStyle(
                    SpanStyle(
                        color = accent.deep,
                        fontWeight = FontWeight.ExtraBold,
                    )
                ) {
                    append(stringResource(Res.string.final_exam_description_emphasis))
                }
                append(" ")
                append(stringResource(Res.string.final_exam_description_suffix))
            },
            style = MaterialTheme.typography.bodySmall,
            color = colors.inkFaint,
        )

        Spacer(modifier = Modifier.height(14.dp))

        PillButton(
            text = stringResource(Res.string.save),
            onClick = {
                onFinalExamMaxMarksSave(finalExamInput)
                onDismiss()
            },
            style = PillButtonStyle.Primary,
            enabled = isFinalExamValid,
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

@Composable
private fun ToggleRow(
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
private fun RowDivider() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(1.dp)
            .background(MeadowTheme.colors.divider),
    )
}
