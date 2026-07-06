package com.hussienfahmy.semester_subjctets_presentaion.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hussienfahmy.core.R
import com.hussienfahmy.core.data.local.model.GradeName
import com.hussienfahmy.core_ui.presentation.components.meadow.CapsLabel
import com.hussienfahmy.core_ui.presentation.components.meadow.GpaRings
import com.hussienfahmy.core_ui.presentation.components.meadow.MeadowCard
import com.hussienfahmy.core_ui.presentation.components.meadow.MeadowChip
import com.hussienfahmy.core_ui.presentation.components.meadow.MeadowChipStyle
import com.hussienfahmy.core_ui.presentation.components.meadow.dashedBorder
import com.hussienfahmy.core_ui.theme.CapsLabelStyle
import com.hussienfahmy.core_ui.theme.MeadowRadius
import com.hussienfahmy.core_ui.theme.MeadowTheme
import com.hussienfahmy.semester_subjctets_domain.use_case.Calculate
import com.hussienfahmy.semester_subjctets_domain.use_case.PredictGrades
import com.hussienfahmy.semester_subjctets_presentaion.model.Mode
import com.hussienfahmy.semester_subjctets_presentaion.model.ModeResult
import java.util.Locale

/**
 * The hero card. Normal mode: cumulative GPA + semester GPA + progress rings.
 * Predict mode: compact hero — target field, reachability, needed GPA and the
 * low-credit-hours option in a single card (design 4a).
 */
@Composable
fun ResultCard(
    modifier: Modifier = Modifier,
    modeResult: ModeResult,
    onTargetGPAChange: (targetGPA: String, reverseOrder: Boolean) -> Unit,
    mode: Mode,
) {
    when (mode) {
        Mode.Normal -> NormalHero(
            modifier = modifier,
            calculationResult = modeResult.calculationResult,
        )

        is Mode.Predict -> PredictHero(
            modifier = modifier,
            modeResult = modeResult,
            onTargetGPAChange = onTargetGPAChange,
        )
    }
}

@Composable
private fun NormalHero(
    modifier: Modifier = Modifier,
    calculationResult: Calculate.Result,
) {
    val colors = MeadowTheme.colors
    val accent = MeadowTheme.accent

    MeadowCard(
        modifier = modifier.fillMaxWidth(),
        radius = MeadowRadius.hero,
        contentPadding = androidx.compose.foundation.layout.PaddingValues(
            horizontal = 20.dp, vertical = 18.dp
        ),
    ) {
        when (calculationResult) {
            is Calculate.Result.Failed -> {
                val context = LocalContext.current
                calculationResult.message?.asString(context)?.let {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.bodyLarge,
                        color = colors.inkMuted,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 16.dp),
                    )
                }
            }

            is Calculate.Result.Success -> {
                Box {
                    // Decorative sparks (from design 1a) — kept clear of the rings
                    SparkDot(color = colors.marks.accent, size = 5.dp, x = (-156).dp, y = 14.dp, alpha = 0.7f)
                    SparkDot(color = accent.accent, size = 3.dp, x = (-142).dp, y = 30.dp, alpha = 0.6f)
                    SparkDot(color = colors.quick.accent, size = 4.dp, x = (-162).dp, y = 76.dp, alpha = 0.5f)

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                            ) {
                                CapsLabel(text = stringResource(R.string.cumulative_gpa))
                                Text(
                                    text = calculationResult.cumulative.percentage.asPercent(),
                                    style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp),
                                    color = colors.inkFaint,
                                )
                            }

                            Spacer(modifier = Modifier.height(2.dp))

                            Row(
                                verticalAlignment = Alignment.Bottom,
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                            ) {
                                val cumulativeGpa = calculationResult.cumulative.gpa.toFloat()

                                // Count-up (design: "GPA counts up alongside the ring")
                                val animatedCumulative by animateFloatAsState(
                                    targetValue = cumulativeGpa,
                                    animationSpec = spring(stiffness = Spring.StiffnessLow),
                                    label = "cumulativeGpa",
                                )

                                // Jump on change: dip then spring back with overshoot
                                val bounce = remember { Animatable(1f) }
                                var previousGpa by remember { mutableFloatStateOf(cumulativeGpa) }
                                LaunchedEffect(cumulativeGpa) {
                                    if (previousGpa != cumulativeGpa) {
                                        previousGpa = cumulativeGpa
                                        bounce.snapTo(0.9f)
                                        bounce.animateTo(
                                            targetValue = 1f,
                                            animationSpec = spring(
                                                dampingRatio = Spring.DampingRatioMediumBouncy,
                                                stiffness = Spring.StiffnessMedium,
                                            ),
                                        )
                                    }
                                }

                                Text(
                                    text = animatedCumulative.asGpa(),
                                    style = MaterialTheme.typography.displayLarge,
                                    color = colors.ink,
                                    modifier = Modifier.scale(bounce.value),
                                )
                                Text(
                                    text = calculationResult.cumulative.grade.symbol,
                                    style = MaterialTheme.typography.titleMedium,
                                    color = accent.deep,
                                    modifier = Modifier
                                        .padding(bottom = 4.dp)
                                        .clip(CircleShape)
                                        .background(accent.container)
                                        .padding(horizontal = 10.dp, vertical = 3.dp),
                                )
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                            ) {
                                CapsLabel(text = stringResource(R.string.semester))
                                val animatedSemester by animateFloatAsState(
                                    targetValue = calculationResult.semester.gpa.toFloat(),
                                    animationSpec = spring(stiffness = Spring.StiffnessLow),
                                    label = "semesterGpa",
                                )
                                Text(
                                    text = animatedSemester.asGpa(),
                                    style = MaterialTheme.typography.headlineLarge,
                                    color = accent.accent,
                                )
                                Text(
                                    text = "${calculationResult.semester.grade.symbol} · ${calculationResult.semester.percentage.asPercent()}",
                                    style = MaterialTheme.typography.labelMedium.copy(fontSize = 12.sp),
                                    color = colors.inkGhost,
                                )
                            }
                        }

                        GpaRings(
                            outerProgress = calculationResult.cumulative.percentage / 100f,
                            innerProgress = calculationResult.semester.percentage / 100f,
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun PredictHero(
    modifier: Modifier = Modifier,
    modeResult: ModeResult,
    onTargetGPAChange: (targetGPA: String, reverseOrder: Boolean) -> Unit,
) {
    val colors = MeadowTheme.colors
    val accent = MeadowTheme.accent
    val context = LocalContext.current

    var targetGPA by rememberSaveable { mutableStateOf("") }
    var preferLowHours by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(targetGPA, preferLowHours) {
        onTargetGPAChange(targetGPA, preferLowHours)
    }

    val target = targetGPA.toDoubleOrNull()
    // When the target is unreachable the planner has assigned the best grades it
    // can, so the predicted cumulative IS the max cumulative achievable.
    val predictedCumulative =
        (modeResult.calculationResult as? Calculate.Result.Success)?.cumulative?.gpa
    // Reachability comes from the planner's own verdict — it is computed in the
    // same pass as the prediction, so the chip doesn't flicker while the stale
    // calculation result catches up and the number animates.
    val predictResult = (modeResult as? ModeResult.Predict)?.predictGradesResult
    val reachable = predictResult is PredictGrades.Result.TargetAchieved

    MeadowCard(
        modifier = modifier.fillMaxWidth(),
        radius = MeadowRadius.hero,
        contentPadding = androidx.compose.foundation.layout.PaddingValues(
            horizontal = 18.dp, vertical = 14.dp
        ),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp),
        ) {
            Column(modifier = Modifier.weight(1f)) {
                // Target row
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    CapsLabel(text = stringResource(R.string.target))

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        modifier = Modifier
                            .clip(RoundedCornerShape(MeadowRadius.pill))
                            .background(colors.card)
                            .border(2.dp, accent.accent, RoundedCornerShape(MeadowRadius.pill))
                            .padding(horizontal = 12.dp, vertical = 4.dp),
                    ) {
                        BasicTextField(
                            value = targetGPA,
                            onValueChange = { targetGPA = it },
                            textStyle = MaterialTheme.typography.headlineSmall.copy(
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Black,
                                color = colors.ink,
                            ),
                            cursorBrush = SolidColor(accent.accent),
                            keyboardOptions = KeyboardOptions(
                                keyboardType = androidx.compose.ui.text.input.KeyboardType.Number
                            ),
                            singleLine = true,
                            modifier = Modifier.width(44.dp),
                        )
                        Text(
                            text = "✎",
                            style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                            color = accent.soft,
                        )
                    }

                    when {
                        reachable -> MeadowChip(
                            text = "${stringResource(R.string.reachable)} ✓",
                            style = MeadowChipStyle.Accent,
                        )

                        predictResult is PredictGrades.Result.TargetNotAchieved ->
                            MeadowChip(
                                text = if (predictedCumulative != null) stringResource(
                                    R.string.out_of_reach_max,
                                    predictedCumulative.toFloat().asGpa(),
                                ) else stringResource(R.string.out_of_reach),
                                style = MeadowChipStyle.Warn,
                            )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Needed semester GPA
                when (val calculationResult = modeResult.calculationResult) {
                    is Calculate.Result.Success -> Column {
                        // SEMESTER stands out — this number is the semester GPA,
                        // not the cumulative shown in Normal mode.
                        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                            Text(
                                text = stringResource(R.string.semester).uppercase(),
                                style = CapsLabelStyle.copy(fontWeight = FontWeight.Black),
                                color = accent.deep,
                                maxLines = 1,
                            )
                            Text(
                                text = stringResource(R.string.gpa_needed).uppercase(),
                                style = CapsLabelStyle,
                                color = colors.inkFaint,
                                maxLines = 1,
                            )
                        }

                        Spacer(modifier = Modifier.height(2.dp))

                        Row(
                            verticalAlignment = Alignment.Bottom,
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                        ) {
                            val animatedNeeded by animateFloatAsState(
                                targetValue = calculationResult.semester.gpa.toFloat(),
                                animationSpec = spring(stiffness = Spring.StiffnessLow),
                                label = "neededGpa",
                            )
                            Text(
                                text = animatedNeeded.asGpa(),
                                style = MaterialTheme.typography.displayMedium,
                                color = colors.ink,
                            )
                            Text(
                                text = calculationResult.semester.grade.symbol,
                                style = MaterialTheme.typography.titleSmall,
                                color = accent.deep,
                                modifier = Modifier
                                    .padding(bottom = 4.dp)
                                    .clip(RoundedCornerShape(MeadowRadius.pill))
                                    .dashedBorder(color = accent.soft, radius = MeadowRadius.pill)
                                    .padding(horizontal = 9.dp, vertical = 2.dp),
                            )
                            Text(
                                text = stringResource(R.string.needed_this_semester),
                                style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                                color = colors.inkFaint,
                                modifier = Modifier.padding(bottom = 6.dp),
                            )
                        }

                        if (reachable && target != null) {
                            Spacer(modifier = Modifier.height(2.dp))

                            // "Cumulative 3.54 → 3.40" (design 3a); when out of
                            // reach the chip already shows the max instead.
                            Text(
                                text = "${stringResource(R.string.cumulative)} " +
                                        "${calculationResult.cumulative.gpa.toFloat().asGpa()} → ${targetGPA.trim()}",
                                style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp),
                                color = colors.inkFaint,
                            )
                        }
                    }

                    is Calculate.Result.Failed -> Text(
                        text = calculationResult.message?.asString(context) ?: "",
                        style = MaterialTheme.typography.bodyMedium,
                        color = colors.inkMuted,
                    )
                }
            }

            when (val calculationResult = modeResult.calculationResult) {
                is Calculate.Result.Success -> GpaRings(
                    outerProgress = calculationResult.semester.percentage / 100f,
                    innerProgress = 1f,
                    size = 76.dp,
                    outerStroke = 7.dp,
                    innerStroke = 5.dp,
                    innerDashed = true,
                )

                else -> Unit
            }
        }

        Spacer(modifier = Modifier.height(10.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(colors.divider)
        )
        Spacer(modifier = Modifier.height(10.dp))

        // Low credit-hours preference
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
            ) { preferLowHours = !preferLowHours },
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(18.dp)
                    .clip(RoundedCornerShape(6.dp))
                    .background(if (preferLowHours) accent.accent else Color.Transparent)
                    .then(
                        if (!preferLowHours) Modifier.border(
                            2.dp, colors.inkDisabled, RoundedCornerShape(6.dp)
                        ) else Modifier
                    ),
            ) {
                if (preferLowHours) {
                    Text(
                        text = "✓",
                        style = MaterialTheme.typography.labelSmall,
                        color = accent.onAccent,
                    )
                }
            }
            Column {
                Text(
                    text = stringResource(R.string.prefer_high_grades_low_hours),
                    style = MaterialTheme.typography.bodySmall,
                    color = colors.inkMuted,
                )
                Text(
                    text = stringResource(R.string.prefer_high_grades_low_hours_hint),
                    style = MaterialTheme.typography.bodySmall.copy(fontSize = 10.5.sp),
                    color = colors.inkFaint,
                )
            }
        }
    }
}

@Composable
private fun androidx.compose.foundation.layout.BoxScope.SparkDot(
    color: Color,
    size: Dp,
    x: Dp,
    y: Dp,
    alpha: Float,
) {
    Box(
        modifier = Modifier
            .align(Alignment.TopEnd)
            .offset(x = x, y = y)
            .size(size)
            .alpha(alpha)
            .clip(CircleShape)
            .background(color)
    )
}

private fun Float.asPercent(): String = String.format(Locale.getDefault(), "%.1f%%", this)

private fun Float.asGpa(): String = String.format(Locale.getDefault(), "%.2f", this)

private val previewSuccess = Calculate.Result.Success(
    semester = Calculate.Result.Success.Data(gpa = 3.35, grade = GradeName.BPlus, percentage = 83.8f),
    cumulative = Calculate.Result.Success.Data(gpa = 3.44, grade = GradeName.BPlus, percentage = 86.0f),
)

@Preview(name = "Normal hero · light", showBackground = true)
@Composable
private fun NormalHeroLightPreview() {
    MeadowTheme(darkTheme = false) {
        ResultCard(
            modeResult = ModeResult.Normal(previewSuccess),
            onTargetGPAChange = { _, _ -> },
            mode = Mode.Normal,
        )
    }
}

@Preview(name = "Normal hero · dark", showBackground = true)
@Composable
private fun NormalHeroDarkPreview() {
    MeadowTheme(darkTheme = true) {
        ResultCard(
            modeResult = ModeResult.Normal(previewSuccess),
            onTargetGPAChange = { _, _ -> },
            mode = Mode.Normal,
        )
    }
}

@Preview(name = "Predict hero · light", showBackground = true)
@Composable
private fun PredictHeroLightPreview() {
    MeadowTheme(darkTheme = false) {
        ResultCard(
            modeResult = ModeResult.Predict(
                result = previewSuccess,
                predictGradesResult = PredictGrades.Result.TargetAchieved,
            ),
            onTargetGPAChange = { _, _ -> },
            mode = Mode.Predict(targetCumulativeGPA = "3.60", reverseSubjects = false),
        )
    }
}

@Preview(name = "Predict hero · dark", showBackground = true)
@Composable
private fun PredictHeroDarkPreview() {
    MeadowTheme(darkTheme = true) {
        ResultCard(
            modeResult = ModeResult.Predict(
                result = previewSuccess,
                predictGradesResult = PredictGrades.Result.TargetNotAchieved,
            ),
            onTargetGPAChange = { _, _ -> },
            mode = Mode.Predict(targetCumulativeGPA = "3.90", reverseSubjects = true),
        )
    }
}
