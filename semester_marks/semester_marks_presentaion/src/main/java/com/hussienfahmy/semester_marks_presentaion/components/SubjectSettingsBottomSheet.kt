package com.hussienfahmy.semester_marks_presentaion.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
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
import com.hussienfahmy.core_ui.presentation.components.SemesterMarkChooser
import com.hussienfahmy.core_ui.presentation.util.toStringWithOptionalDecimals

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SubjectSettingsBottomSheet(
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
    val spacing = LocalSpacing.current
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    var finalExamInput by remember {
        mutableStateOf(finalExamMaxMarks.toStringWithOptionalDecimals())
    }
    val parsedFinalExam = finalExamInput.toDoubleOrNull()
    val isFinalExamValid = parsedFinalExam != null && parsedFinalExam > 0

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = spacing.medium)
                .padding(bottom = spacing.large),
            verticalArrangement = Arrangement.spacedBy(spacing.small),
        ) {
            Text(
                text = stringResource(R.string.semester_work),
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )

            SemesterMarkChooser(
                title = stringResource(R.string.midterm),
                available = midtermAvailable,
                onCheckChanges = onMidtermCheckChanges,
            )
            SemesterMarkChooser(
                title = stringResource(R.string.oral),
                available = oralAvailable,
                onCheckChanges = onOralCheckChanges,
            )
            SemesterMarkChooser(
                title = stringResource(R.string.practical),
                available = practicalAvailable,
                onCheckChanges = onPracticalCheckChanges,
            )
            SemesterMarkChooser(
                title = stringResource(R.string.project),
                available = projectAvailable,
                onCheckChanges = onProjectCheckChanges,
            )

            HorizontalDivider(modifier = Modifier.padding(vertical = spacing.extraSmall))

            Text(
                text = stringResource(R.string.final_exam),
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Text(
                text = stringResource(R.string.final_exam_description),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = finalExamInput,
                onValueChange = { finalExamInput = it },
                label = { Text(stringResource(R.string.final_exam_label)) },
                isError = finalExamInput.isNotBlank() && !isFinalExamValid,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true,
            )
            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    onFinalExamMaxMarksSave(finalExamInput)
                    onDismiss()
                },
                enabled = isFinalExamValid,
            ) {
                Text(stringResource(R.string.save))
            }
        }
    }
}
