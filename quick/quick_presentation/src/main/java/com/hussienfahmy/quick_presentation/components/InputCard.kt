package com.hussienfahmy.quick_presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import org.jetbrains.compose.resources.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hussienfahmy.core.generated.resources.Res
import com.hussienfahmy.core.domain.user_data.model.UserData
import com.hussienfahmy.core_ui.presentation.components.meadow.MeadowCard
import com.hussienfahmy.core_ui.presentation.components.meadow.MeadowSwitch
import com.hussienfahmy.core_ui.presentation.components.meadow.MeadowTextField
import com.hussienfahmy.core_ui.theme.MeadowAccentProvider
import com.hussienfahmy.core_ui.theme.MeadowTheme
import com.hussienfahmy.quick_domain.model.QuickCalculationRequest

/**
 * Quick calculator inputs (design 2c): "Use my information" toggle card, then
 * a 2×2 tile grid — auto-filled tiles go muted, manual tiles keep a coral
 * outline. Result recomputes on each keystroke.
 */
@Composable
fun InputCard(
    modifier: Modifier = Modifier,
    academicProgress: UserData.AcademicProgress,
    invalidCumulativeGPAInput: Boolean,
    invalidCumulativeGPAAboveMax: Boolean,
    invalidSemesterGPAInput: Boolean,
    invalidSemesterGPAAboveMax: Boolean,
    invalidTotalHoursInput: Boolean,
    invalidSemesterHoursInput: Boolean,
    onCalculate: (QuickCalculationRequest) -> Unit,
) {
    val colors = MeadowTheme.colors

    var useMyAcademicProgress by remember { mutableStateOf(false) }

    var cumulativeGPA by remember { mutableStateOf("") }
    var totalHours by remember { mutableStateOf("") }

    if (useMyAcademicProgress) {
        cumulativeGPA = academicProgress.cumulativeGPA.toString()
        totalHours = academicProgress.creditHours.toString()
    }

    var semesterGPA by remember { mutableStateOf("") }
    var semesterHours by remember { mutableStateOf("") }

    // send the inputs to view model to be calculated on when any inputs change
    LaunchedEffect(cumulativeGPA, totalHours, semesterGPA, semesterHours) {
        val skip = listOf(
            cumulativeGPA,
            totalHours,
            semesterGPA,
            semesterHours,
        ).any {
            it.isBlank()
        }
        if (skip) return@LaunchedEffect

        onCalculate(
            QuickCalculationRequest(
                cumulativeGPA = cumulativeGPA,
                totalHours = totalHours,
                semesterGPA = semesterGPA,
                semesterHours = semesterHours,
            )
        )
    }

    Column(modifier = modifier) {
        // "Use my information" card
        MeadowCard(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(horizontal = 18.dp, vertical = 14.dp),
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = stringResource(Res.string.use_my_information),
                        style = MaterialTheme.typography.titleMedium,
                        color = MeadowTheme.accent.ink,
                    )
                    Text(
                        text = stringResource(Res.string.use_my_information_hint),
                        style = MaterialTheme.typography.bodySmall,
                        color = colors.inkFaint,
                    )
                }
                MeadowSwitch(
                    checked = useMyAcademicProgress,
                    onCheckedChange = { useMyAcademicProgress = it },
                )
            }
        }

        Spacer(modifier = Modifier.height(11.dp))

        // Current cumulative + total hours (auto when the toggle is on)
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            MeadowTextField(
                value = cumulativeGPA,
                onValueChange = { cumulativeGPA = it },
                label = if (useMyAcademicProgress) {
                    "${stringResource(Res.string.current_cumulative_gpa)} · ${stringResource(Res.string.auto)}"
                } else when {
                    invalidCumulativeGPAAboveMax ->
                        "${stringResource(Res.string.current_cumulative_gpa)} — ${stringResource(Res.string.above_max)}"

                    invalidCumulativeGPAInput ->
                        "${stringResource(Res.string.current_cumulative_gpa)} — ${stringResource(Res.string.invalid_input)}"

                    else -> stringResource(Res.string.current_cumulative_gpa)
                },
                isError = !useMyAcademicProgress &&
                        (invalidCumulativeGPAInput || invalidCumulativeGPAAboveMax),
                enabled = !useMyAcademicProgress,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.weight(1f),
            )
            MeadowTextField(
                value = totalHours,
                onValueChange = { totalHours = it },
                label = if (useMyAcademicProgress) {
                    "${stringResource(Res.string.current_total_hours)} · ${stringResource(Res.string.auto)}"
                } else if (invalidTotalHoursInput) {
                    "${stringResource(Res.string.current_total_hours)} — ${stringResource(Res.string.invalid_input)}"
                } else {
                    stringResource(Res.string.current_total_hours)
                },
                isError = !useMyAcademicProgress && invalidTotalHoursInput,
                enabled = !useMyAcademicProgress,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.weight(1f),
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Expected semester GPA + hours — always manual, coral outline
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            MeadowTextField(
                value = semesterGPA,
                onValueChange = { semesterGPA = it },
                label = when {
                    invalidSemesterGPAAboveMax ->
                        "${stringResource(Res.string.expected_semester_gpa)} — ${stringResource(Res.string.above_max)}"

                    invalidSemesterGPAInput ->
                        "${stringResource(Res.string.expected_semester_gpa)} — ${stringResource(Res.string.invalid_input)}"

                    else -> stringResource(Res.string.expected_semester_gpa)
                },
                isError = invalidSemesterGPAInput || invalidSemesterGPAAboveMax,
                outlined = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.weight(1f),
            )
            MeadowTextField(
                value = semesterHours,
                onValueChange = { semesterHours = it },
                label = if (invalidSemesterHoursInput) {
                    "${stringResource(Res.string.semester_credit_hours)} — ${stringResource(Res.string.invalid_input)}"
                } else {
                    stringResource(Res.string.semester_credit_hours)
                },
                isError = invalidSemesterHoursInput,
                outlined = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.weight(1f),
            )
        }
    }
}

@Preview(name = "QuickInputs · light", showBackground = true)
@Composable
private fun InputCardLightPreview() {
    MeadowTheme(darkTheme = false) {
        MeadowAccentProvider(MeadowTheme.colors.quick) {
            InputCard(
                modifier = Modifier.fillMaxWidth(),
                academicProgress = UserData.AcademicProgress(
                    cumulativeGPA = 3.5,
                    creditHours = 101,
                ),
                invalidCumulativeGPAInput = false,
                invalidSemesterGPAInput = false,
                invalidTotalHoursInput = false,
                invalidSemesterHoursInput = false,
                invalidCumulativeGPAAboveMax = false,
                invalidSemesterGPAAboveMax = true,
                onCalculate = {},
            )
        }
    }
}

@Preview(name = "QuickInputs · dark", showBackground = true)
@Composable
private fun InputCardDarkPreview() {
    MeadowTheme(darkTheme = true) {
        MeadowAccentProvider(MeadowTheme.colors.quick) {
            InputCard(
                modifier = Modifier.fillMaxWidth(),
                academicProgress = UserData.AcademicProgress(
                    cumulativeGPA = 3.5,
                    creditHours = 101,
                ),
                invalidCumulativeGPAInput = false,
                invalidSemesterGPAInput = false,
                invalidTotalHoursInput = false,
                invalidSemesterHoursInput = false,
                invalidCumulativeGPAAboveMax = false,
                invalidSemesterGPAAboveMax = false,
                onCalculate = {},
            )
        }
    }
}
