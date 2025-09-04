package com.hussienfahmy.semester_subjctets_presentaion.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hussienfahmy.core.R
import com.hussienfahmy.core.data.local.model.GradeName
import com.hussienfahmy.core.util.toTwoDecimals
import com.hussienfahmy.core_ui.LocalSpacing
import com.hussienfahmy.core_ui.presentation.components.CircularProgressIndicatorWithBackground
import com.hussienfahmy.semester_subjctets_domain.use_case.Calculate
import com.hussienfahmy.semester_subjctets_presentaion.model.Mode

@Composable
fun ResultCard(
    modifier: Modifier = Modifier,
    calculationResult: Calculate.Result,
    onTargetGPAChange: (targetGPA: String, reverseOrder: Boolean) -> Unit,
    mode: Mode,
) {
    val context = LocalContext.current
    val spacing = LocalSpacing.current

    Card(shape = RoundedCornerShape(10.dp), modifier = modifier.fillMaxWidth()) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            val animationDuration = 500
            AnimatedVisibility(
                visible = mode is Mode.Predict,
                enter = fadeIn(
                    animationSpec = tween(durationMillis = animationDuration)
                ) + expandVertically(
                    animationSpec = tween(durationMillis = animationDuration)
                )
            ) {
                PredictiveModeControllers(onTargetGPAChange)
            }

            when (calculationResult) {
                is Calculate.Result.Failed -> {
                    calculationResult.message?.asString(context)?.let {
                        Text(
                            text = it,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(spacing.medium),
                            textAlign = TextAlign.Center
                        )
                    }
                }

                is Calculate.Result.Success -> {
                    ResultChart(
                        calculationResult = calculationResult,
                    )
                }
            }
        }
    }
}

@Composable
fun ResultChart(
    calculationResult: Calculate.Result.Success
) {
    Column {
        Row(verticalAlignment = Alignment.CenterVertically) {
            ResultData(
                modifier = Modifier.weight(1f),
                data = calculationResult.semester,
                title = stringResource(R.string.semester),
                color = MaterialTheme.colorScheme.secondary
            )
            ResultData(
                modifier = Modifier.weight(1f),
                data = calculationResult.cumulative,
                title = stringResource(R.string.cumulative),
                color = MaterialTheme.colorScheme.tertiary
            )
            ProgressSummary(
                modifier = Modifier.weight(1.3f),
                semester = calculationResult.semester,
                cumulative = calculationResult.cumulative,
            )
        }
    }
}

@Composable
private fun PredictiveModeControllers(
    onTargetGPAChange: (targetGPA: String, reverseOrder: Boolean) -> Unit,
) {
    val spacing = LocalSpacing.current

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(spacing.small),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        var targetGPA by rememberSaveable { mutableStateOf("") }

        var highGradeToLowCHours by rememberSaveable { mutableStateOf(false) }

        val pushChange = {
            onTargetGPAChange(targetGPA, highGradeToLowCHours)
        }

        LaunchedEffect(targetGPA, highGradeToLowCHours) {
            pushChange()
        }

        OutlinedTextField(
            modifier = Modifier.weight(1f),
            value = targetGPA,
            onValueChange = { targetGPA = it },
            label = {
                Text(
                    text = stringResource(R.string.cumulative_target_gpa),
                    style = MaterialTheme.typography.bodySmall
                )
            },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            isError = targetGPA.toDoubleOrNull() == null
        )
        Row(
            modifier = Modifier
                .weight(1f),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End
        ) {
            Text(
                text = stringResource(R.string.high_grade_to_low_credit_hours),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .pointerInput(Unit) {
                        detectTapGestures {
                            highGradeToLowCHours = !highGradeToLowCHours
                        }
                    }
            )
            Spacer(modifier = Modifier.width(3.dp))
            Checkbox(
                checked = highGradeToLowCHours,
                onCheckedChange = {
                    highGradeToLowCHours = !highGradeToLowCHours
                }
            )
        }
    }
}

@Composable
fun ProgressSummary(
    semester: Calculate.Result.Success.Data,
    cumulative: Calculate.Result.Success.Data,
    modifier: Modifier,
) {
    val spacing = LocalSpacing.current

    val strokeWidth = 2.dp
    val bigScale = 1.8f
    val smallScale = 1.4f

    Box(modifier = modifier.padding(spacing.small), contentAlignment = Alignment.Center) {

        val semesterProgress by animateFloatAsState(targetValue = semester.percentage / 100)
        CircularProgressIndicatorWithBackground(
            color = MaterialTheme.colorScheme.secondary,
            strokeWidth = strokeWidth,
            progress = semesterProgress,
            modifier = Modifier.scale(bigScale)
        )

        val cumulativeProgress by animateFloatAsState(targetValue = cumulative.percentage / 100)
        CircularProgressIndicatorWithBackground(
            color = MaterialTheme.colorScheme.tertiary,
            strokeWidth = strokeWidth,
            progress = cumulativeProgress,
            modifier = Modifier.scale(smallScale)
        )
    }
}

@Composable
fun ResultData(
    data: Calculate.Result.Success.Data,
    title: String,
    color: Color,
    modifier: Modifier
) {
    val spacing = LocalSpacing.current

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.padding(spacing.small)
    ) {
        val gpa by animateFloatAsState(targetValue = data.gpa.toFloat())

        Text(text = title, color = color)
        Text(text = "${gpa.toTwoDecimals()}", color = color)
        Text(
            text = data.grade.symbol,
            color = color,
            style = MaterialTheme.typography.headlineSmall
        )
    }
}

@Preview("Result Card Normal")
@Composable
fun ResultCardNormalPreview() {
    ResultCard(
        calculationResult = Calculate.Result.Success(
            semester = Calculate.Result.Success.Data(
                3.2,
                GradeName.A,
                90.0f
            ),
            cumulative = Calculate.Result.Success.Data(
                2.9,
                GradeName.B,
                90.0f
            )
        ),
        onTargetGPAChange = { _, _ -> },
        mode = Mode.Normal,
    )
}

@Preview("Result Card Predictive")
@Composable
fun ResultCardPredictivePreview() {
    ResultCard(
        calculationResult = Calculate.Result.Success(
            semester = Calculate.Result.Success.Data(
                3.2,
                GradeName.A,
                90.0f
            ),
            cumulative = Calculate.Result.Success.Data(
                2.9,
                GradeName.B,
                90.0f
            )
        ),
        onTargetGPAChange = { _, _ -> },
        mode = Mode.Predict(
            targetCumulativeGPA = "3.5",
            reverseSubjects = false
        ),
    )
}