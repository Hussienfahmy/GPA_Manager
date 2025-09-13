package com.hussienfahmy.semester_marks_presentaion.components

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.ParagraphStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDirection
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hussienfahmy.core.R
import com.hussienfahmy.core_ui.LocalSpacing
import com.hussienfahmy.core_ui.presentation.components.ConfirmationDialog
import com.hussienfahmy.core_ui.presentation.components.TipDialogContainer
import com.hussienfahmy.core_ui.presentation.util.toStringWithOptionalDecimals
import com.hussienfahmy.semester_marks_domain.model.Grade
import com.hussienfahmy.semester_marks_domain.model.Subject

@Composable
fun SemesterMarksItem(
    subject: Subject,
    onMidTermMarksChange: (String) -> Unit,
    onOralMarksChange: (String) -> Unit,
    onPracticalMarksChange: (String) -> Unit,
    onProjectMarksChange: (String) -> Unit,
    onResetClick: () -> Unit,
    showHint: Boolean,
    onMidtermAvailabilityCheckChanges: (Boolean) -> Unit,
    onPracticalAvailabilityCheckChanges: (Boolean) -> Unit,
    onOralAvailabilityCheckChanges: (Boolean) -> Unit,
    onProjectAvailabilityCheckChanges: (Boolean) -> Unit,
) {
    val spacing = LocalSpacing.current

    var midtermInput by remember { mutableStateOf(subject.midtermMarks?.toString() ?: "") }

    var practicalInput by remember { mutableStateOf(subject.practicalMarks?.toString() ?: "") }

    var oralInput by remember { mutableStateOf(subject.oralMarks?.toString() ?: "") }

    var projectInput by remember { mutableStateOf(subject.projectMarks?.toString() ?: "") }

    var showResetConfirmationDialog by remember { mutableStateOf(false) }

    var showSemesterWorkAvailabilityDialog by remember { mutableStateOf(false) }

    if (showResetConfirmationDialog) ConfirmationDialog(onDismiss = {
        showResetConfirmationDialog = false
    }, onConfirm = {
        onResetClick()
        midtermInput = ""
        practicalInput = ""
        oralInput = ""
    })

    if (showSemesterWorkAvailabilityDialog) SemesterWorkAvailabilityDialog(
        onDismiss = { showSemesterWorkAvailabilityDialog = false },
        midtermAvailable = subject.midtermAvailable,
        practicalAvailable = subject.practicalAvailable,
        oralAvailable = subject.oralAvailable,
        projectAvailable = subject.projectAvailable,
        onMidtermCheckChanges = { enabled ->
            if (!enabled) midtermInput = ""
            onMidtermAvailabilityCheckChanges(enabled)
        },
        onPracticalCheckChanges = { enabled ->
            if (!enabled) practicalInput = ""
            onPracticalAvailabilityCheckChanges(enabled)
        },
        onOralCheckChanges = { enabled ->
            if (!enabled) oralInput = ""
            onOralAvailabilityCheckChanges(enabled)
        },
        onProjectCheckChanges = { enabled ->
            if (!enabled) projectInput = ""
            onProjectAvailabilityCheckChanges(enabled)
        }
    )


    Card(shape = RoundedCornerShape(spacing.medium)) {
        Column(
            modifier = Modifier.padding(spacing.small),
            verticalArrangement = Arrangement.spacedBy(spacing.small)
        ) {
            SubjectTitle(
                subject = subject,
                onEditCLick = { showSemesterWorkAvailabilityDialog = true },
                onResetClick = { showResetConfirmationDialog = true },
            )

            LinearProgressIndicator(
                modifier = Modifier.fillMaxWidth(),
                progress = { subject.courseMarksPercentage },
                drawStopIndicator = {}
            )

            HorizontalDivider()

            SemesterWork(
                midtermAvailable = subject.midtermAvailable,
                practicalAvailable = subject.practicalAvailable,
                oralAvailable = subject.oralAvailable,
                projectAvailable = subject.projectAvailable,
                midtermInput = midtermInput,
                onMidTermMarksChange = {
                    midtermInput = it
                    onMidTermMarksChange(it)
                },
                practicalInput = practicalInput,
                onPracticalMarksChange = {
                    practicalInput = it
                    onPracticalMarksChange(it)
                },
                oralInput = oralInput,
                onOralMarksChange = {
                    oralInput = it
                    onOralMarksChange(it)
                },
                projectInput = projectInput,
                onProjectMarksChange = {
                    projectInput = it
                    onProjectMarksChange(it)
                }
            )

            HorizontalDivider()

            Column {
                if (showHint) {
                    TipDialogContainer(tipText = stringResource(R.string.tip_required_marks)) {
                        Text(
                            text = "The required marks you need to get to achieve the grade",
                            maxLines = 1,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                    Spacer(modifier = Modifier.height(spacing.small))
                }

                AvailableGrades(subject.grades)
            }
        }
    }
}

@Composable
private fun SemesterWork(
    midtermAvailable: Boolean,
    practicalAvailable: Boolean,
    oralAvailable: Boolean,
    projectAvailable: Boolean,
    midtermInput: String,
    onMidTermMarksChange: (String) -> Unit,
    practicalInput: String,
    onPracticalMarksChange: (String) -> Unit,
    oralInput: String,
    onOralMarksChange: (String) -> Unit,
    projectInput: String,
    onProjectMarksChange: (String) -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        if (midtermAvailable) SemesterMarkTextField(
            modifier = Modifier.weight(1f),
            title = stringResource(R.string.midterm),
            value = midtermInput,
            onValueChanged = onMidTermMarksChange,
        )

        if (practicalAvailable) SemesterMarkTextField(
            modifier = Modifier.weight(1f),
            title = stringResource(R.string.practical),
            value = practicalInput,
            onValueChanged = onPracticalMarksChange,
        )

        if (oralAvailable) SemesterMarkTextField(
            modifier = Modifier.weight(1f),
            title = stringResource(R.string.oral),
            value = oralInput,
            onValueChanged = onOralMarksChange,
        )

        if (projectAvailable) SemesterMarkTextField(
            modifier = Modifier.weight(1f),
            title = stringResource(R.string.project),
            value = projectInput,
            onValueChanged = onProjectMarksChange,
        )
    }
}

@Composable
private fun SubjectTitle(
    subject: Subject,
    onResetClick: () -> Unit,
    onEditCLick: () -> Unit,
) {
    val spacing = LocalSpacing.current

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(text = subject.name, style = MaterialTheme.typography.titleLarge)

        Spacer(modifier = Modifier.width(spacing.medium))

        Text(
            text = "${subject.courseMarks.toStringWithOptionalDecimals()}/${subject.courseTotalMarks.toStringWithOptionalDecimals()}",
            style = MaterialTheme.typography.bodyLarge
        )

        Spacer(modifier = Modifier.weight(1f))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(spacing.small)
        ) {
            Icon(
                imageVector = Icons.Default.Refresh,
                contentDescription = stringResource(id = R.string.reset),
                modifier = Modifier.clickable(onClick = onResetClick)
            )

            Icon(
                imageVector = Icons.Default.Edit,
                contentDescription = stringResource(id = R.string.edit),
                modifier = Modifier.clickable(onClick = onEditCLick)
            )
        }
    }
}

@Composable
private fun AvailableGrades(grades: List<Grade>) {
    val spacing = LocalSpacing.current

    Card(
        shape = RoundedCornerShape(spacing.small),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primary
        ),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min)
                .padding(horizontal = 3.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            grades.forEachIndexed { index, grade ->
                GradeWithNeededMarksItem(
                    modifier = Modifier.weight(1f),
                    grade = grade,
                )
                // add divider between the grades
                if (index != grades.lastIndex) HorizontalDivider(
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(1.dp)
                )
            }
        }
    }
}

@Composable
private fun GradeWithNeededMarksItem(
    modifier: Modifier = Modifier,
    grade: Grade,
) {
    val animatedAlpha by animateFloatAsState(
        targetValue = if (grade.achievable is Grade.Achievable.Yes) 1f else 0.5f
    )

    Box(
        modifier = modifier
            .heightIn(min = 50.dp)
            .padding(horizontal = 3.dp, vertical = 3.dp)
            .alpha(animatedAlpha),
    ) {
        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .fillMaxWidth()
                .animateContentSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = buildAnnotatedString {
                withStyle(ParagraphStyle(textDirection = TextDirection.Ltr)) {
                    append(grade.symbol)
                }
            })

            val achievable = grade.achievable

            if (achievable is Grade.Achievable.Yes) {
                val neededMarks = achievable.neededMarks

                Text(text = neededMarks.toString())
            }
        }
    }
}

@Preview
@Composable
fun SemesterMarksItemPreview() {
    SemesterMarksItem(
        subject = Subject(
            id = 1,
            name = "Math",
            practicalMarks = 10.0,
            midtermMarks = 10.0,
            oralMarks = 10.0,
            projectMarks = 10.0,
            courseTotalMarks = 100.0,
            midtermAvailable = true,
            practicalAvailable = true,
            oralAvailable = true,
            projectAvailable = true,
            grades = listOf(
                Grade(
                    symbol = "A",
                    achievable = Grade.Achievable.No
                ),
                Grade(
                    symbol = "B",
                    achievable = Grade.Achievable.No
                ),
                Grade(
                    symbol = "C",
                    achievable = Grade.Achievable.Yes(neededMarks = 80.0)
                ),
                Grade(
                    symbol = "D",
                    achievable = Grade.Achievable.Yes(neededMarks = 60.0)
                ),
                Grade(
                    symbol = "E",
                    achievable = Grade.Achievable.Yes(neededMarks = 40.0)
                ),
            )
        ),
        onMidTermMarksChange = {},
        onPracticalMarksChange = {},
        onOralMarksChange = {},
        onProjectMarksChange = {},
        onMidtermAvailabilityCheckChanges = {},
        onPracticalAvailabilityCheckChanges = {},
        onOralAvailabilityCheckChanges = {},
        onProjectAvailabilityCheckChanges = {},
        onResetClick = {},
        showHint = true
    )
}