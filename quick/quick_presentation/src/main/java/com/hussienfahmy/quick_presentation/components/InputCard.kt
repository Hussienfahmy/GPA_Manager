package com.hussienfahmy.quick_presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.hussienfahmy.core.R
import com.hussienfahmy.core.domain.user_data.model.UserData
import com.hussienfahmy.core_ui.LocalSpacing
import com.hussienfahmy.quick_domain.model.QuickCalculationRequest

@Composable
fun InputCard(
    modifier: Modifier,
    academicProgress: UserData.AcademicProgress,
    invalidCumulativeGPAInput: Boolean,
    invalidCumulativeGPAAboveMax: Boolean,
    invalidSemesterGPAInput: Boolean,
    invalidSemesterGPAAboveMax: Boolean,
    invalidTotalHoursInput: Boolean,
    invalidSemesterHoursInput: Boolean,
    onCalculate: (QuickCalculationRequest) -> Unit,
) {
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

    val spacing = LocalSpacing.current

    Card(
        shape = RoundedCornerShape(spacing.medium),
        modifier = modifier
    ) {
        ConstraintLayout(
            modifier = Modifier
                .fillMaxSize()
                .padding(all = spacing.small)
        ) {
            val (
                title,
                spinnerContent,
                cumulativeGPARef,
                totalHoursRef,
                semesterGPARef,
                semesterHoursRef
            ) = createRefs()

            val verticalCenterGuideLine = createGuidelineFromStart(0.5f)

            Text(
                text = stringResource(R.string.gpa_quick_calculator),
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.constrainAs(title) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                }
            )

            Row(
                horizontalArrangement = Arrangement.End,
                modifier = Modifier.constrainAs(spinnerContent) {
                    end.linkTo(parent.end)
                    top.linkTo(title.bottom, margin = spacing.small)
                },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.use_my_information),
                    modifier = Modifier.clickable {
                        useMyAcademicProgress = !useMyAcademicProgress
                    })
                Spacer(modifier = Modifier.width(5.dp))
                Switch(
                    checked = useMyAcademicProgress,
                    onCheckedChange = { useMyAcademicProgress = !useMyAcademicProgress }
                )
            }

            OutlinedTextField(
                value = cumulativeGPA,
                onValueChange = { cumulativeGPA = it },
                isError = invalidCumulativeGPAInput or invalidCumulativeGPAAboveMax,
                label = {
                    Text(
                        text = stringResource(R.string.current_cumulative_gpa),
                        style = MaterialTheme.typography.bodySmall
                    )
                },
                supportingText = {
                    val supportingText = when {
                        invalidCumulativeGPAInput -> stringResource(R.string.invalid_input)
                        invalidCumulativeGPAAboveMax -> stringResource(R.string.above_max)
                        else -> null
                    }
                    if (supportingText != null) {
                        Text(
                            text = supportingText,
                            style = MaterialTheme.typography.bodySmall.copy(
                                fontSize = 10.sp
                            ),
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                },
                enabled = !useMyAcademicProgress,
                modifier = Modifier.constrainAs(cumulativeGPARef) {
                    top.linkTo(spinnerContent.bottom)
                    bottom.linkTo(semesterGPARef.top)
                    linkTo(
                        start = parent.start,
                        end = verticalCenterGuideLine,
                        endMargin = spacing.small
                    )
                    this.width = Dimension.fillToConstraints
                },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )

            OutlinedTextField(
                value = totalHours,
                onValueChange = { totalHours = it },
                isError = invalidTotalHoursInput,
                label = {
                    Text(
                        text = stringResource(R.string.current_total_hours),
                        style = MaterialTheme.typography.bodySmall
                    )
                },
                supportingText = {
                    val supportingText = when {
                        invalidTotalHoursInput -> stringResource(R.string.invalid_input)
                        else -> null
                    }
                    if (supportingText != null) {
                        Text(
                            text = supportingText,
                            style = MaterialTheme.typography.bodySmall.copy(
                                fontSize = 10.sp
                            ),
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                },
                enabled = !useMyAcademicProgress,
                modifier = Modifier.constrainAs(totalHoursRef) {
                    centerVerticallyTo(cumulativeGPARef)
                    linkTo(
                        start = verticalCenterGuideLine,
                        end = parent.end,
                        startMargin = spacing.small
                    )
                    this.width = Dimension.fillToConstraints
                },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )

            OutlinedTextField(
                value = semesterGPA,
                onValueChange = { semesterGPA = it },
                isError = invalidSemesterGPAInput or invalidSemesterGPAAboveMax,
                label = {
                    Text(
                        text = stringResource(R.string.expected_semester_gpa),
                        style = MaterialTheme.typography.bodySmall
                    )
                },
                supportingText = {
                    val supportingText = when {
                        invalidSemesterGPAInput -> stringResource(R.string.invalid_input)
                        invalidSemesterGPAAboveMax -> stringResource(R.string.above_max)
                        else -> null
                    }
                    if (supportingText != null) {
                        Text(
                            text = supportingText,
                            style = MaterialTheme.typography.bodySmall.copy(
                                fontSize = 10.sp
                            ),
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                },
                modifier = Modifier.constrainAs(semesterGPARef) {
                    top.linkTo(cumulativeGPARef.bottom)
                    bottom.linkTo(parent.bottom)
                    linkTo(
                        start = parent.start,
                        end = verticalCenterGuideLine,
                        endMargin = spacing.small
                    )
                    this.width = Dimension.fillToConstraints
                },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )

            OutlinedTextField(
                value = semesterHours,
                onValueChange = { semesterHours = it },
                isError = invalidSemesterHoursInput,
                label = {
                    Text(
                        text = stringResource(R.string.semester_credit_hours),
                        style = MaterialTheme.typography.bodySmall
                    )
                },
                supportingText = {
                    val supportingText = when {
                        invalidSemesterHoursInput -> stringResource(R.string.invalid_input)
                        else -> null
                    }
                    if (supportingText != null) {
                        Text(
                            text = supportingText,
                            style = MaterialTheme.typography.bodySmall.copy(
                                fontSize = 10.sp
                            ),
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                },
                modifier = Modifier.constrainAs(semesterHoursRef) {
                    centerVerticallyTo(semesterGPARef)
                    linkTo(
                        start = verticalCenterGuideLine,
                        end = parent.end,
                        startMargin = spacing.small
                    )
                    this.width = Dimension.fillToConstraints
                },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
        }
    }
}

@Preview
@Composable
fun InputCardPreview() {
    InputCard(
        modifier = Modifier.fillMaxWidth(),
        academicProgress = UserData.AcademicProgress(
            cumulativeGPA = 3.5,
            creditHours = 120,
        ),
        invalidCumulativeGPAInput = false,
        invalidSemesterGPAInput = true,
        invalidTotalHoursInput = false,
        invalidSemesterHoursInput = false,
        invalidCumulativeGPAAboveMax = true,
        invalidSemesterGPAAboveMax = false,
        onCalculate = {}
    )
}