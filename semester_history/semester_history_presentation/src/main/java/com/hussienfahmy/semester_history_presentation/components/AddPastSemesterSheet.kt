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
import org.jetbrains.compose.resources.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.hussienfahmy.core.generated.resources.Res
import com.hussienfahmy.core_ui.presentation.components.meadow.MeadowBottomSheet
import com.hussienfahmy.core_ui.presentation.components.meadow.MeadowTextField
import com.hussienfahmy.core_ui.presentation.components.meadow.PillButton
import com.hussienfahmy.core_ui.presentation.components.meadow.PillButtonStyle
import com.hussienfahmy.core_ui.presentation.components.meadow.SegmentedToggle
import com.hussienfahmy.core_ui.theme.MeadowTheme
import com.hussienfahmy.semester_history_domain.model.Semester

@Composable
fun AddPastSemesterSheet(
    onDismiss: () -> Unit,
    onAddSummary: (label: String, gpa: Double, hours: Int, level: Int) -> Unit,
    onAddDetailed: (label: String, level: Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = MeadowTheme.colors

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

    MeadowBottomSheet(onDismiss = onDismiss, modifier = modifier) {
        Text(
            text = stringResource(Res.string.history_add_past_semester),
            style = MaterialTheme.typography.headlineMedium,
            color = colors.ink,
        )

        Spacer(modifier = Modifier.height(12.dp))

        SegmentedToggle(
            options = listOf(
                stringResource(Res.string.history_type_summary),
                stringResource(Res.string.history_type_detailed),
            ),
            selectedIndex = if (selectedType == Semester.Type.SUMMARY) 0 else 1,
            onSelect = { index ->
                selectedType = if (index == 0) Semester.Type.SUMMARY
                else Semester.Type.DETAILED
            },
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = when (selectedType) {
                Semester.Type.SUMMARY -> stringResource(Res.string.history_summary_type_description)
                Semester.Type.DETAILED -> stringResource(Res.string.history_detailed_type_description)
            },
            style = MaterialTheme.typography.bodySmall,
            color = colors.inkFaint,
        )

        Spacer(modifier = Modifier.height(12.dp))

        MeadowTextField(
            value = label,
            onValueChange = { label = it },
            label = stringResource(Res.string.history_label),
            modifier = Modifier.fillMaxWidth(),
        )

        if (selectedType == Semester.Type.SUMMARY) {
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
            text = stringResource(Res.string.add),
            onClick = {
                when (selectedType) {
                    Semester.Type.SUMMARY -> onAddSummary(label, gpa.toDouble(), hours.toInt(), 0)
                    Semester.Type.DETAILED -> onAddDetailed(label, 0)
                }
                onDismiss()
            },
            style = PillButtonStyle.Primary,
            enabled = isValid,
            modifier = Modifier.fillMaxWidth(),
        )
    }
}
