package com.hussienfahmy.semester_subjctets_presentaion.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.LockOpen
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.ParagraphStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDirection
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.hussienFahmy.core.data.local.model.GradeName
import com.hussienFahmy.core_ui.LocalSpacing
import com.hussienFahmy.core_ui.presentation.components.ConfirmationDialog
import com.hussienFahmy.myGpaManager.core.R
import com.hussienfahmy.semester_subjctets_domain.model.Subject
import com.hussienfahmy.semester_subjctets_presentaion.model.Mode

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SubjectsColumn(
    subjects: List<Subject>,
    activeGrades: List<GradeName>,
    onRenameClick: (subject: Subject) -> Unit,
    onDeleteSubject: (subjectId: Long) -> Unit,
    onFixGradeClick: (subjectId: Long, fixed: Boolean) -> Unit,
    onResetClick: (subjectId: Long) -> Unit,
    onGradeClick: (subjectId: Long, grade: GradeName) -> Unit,
    mode: Mode,
) {
    LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        items(items = subjects, key = { it.id }) { subject ->
            Subject(
                modifier = Modifier.animateItem(),
                subject = subject,
                availableGrades = activeGrades,
                mode = mode,
                onGradeClick = onGradeClick,
                onDeleteSubject = onDeleteSubject,
                onFixGradeClick = onFixGradeClick,
                onRenameClick = onRenameClick,
                onResetClick = onResetClick
            )
        }
    }
}

@Composable
fun Subject(
    modifier: Modifier = Modifier,
    subject: Subject,
    availableGrades: List<GradeName>,
    onRenameClick: (subject: Subject) -> Unit,
    onDeleteSubject: (subjectId: Long) -> Unit,
    onFixGradeClick: (subjectId: Long, fixed: Boolean) -> Unit,
    onResetClick: (subjectId: Long) -> Unit,
    onGradeClick: (subjectId: Long, clickedGrade: GradeName) -> Unit,
    mode: Mode,
) {
    val spacing = LocalSpacing.current

    var showConfirmDeleteDialog by remember { mutableStateOf(false) }

    if (showConfirmDeleteDialog) ConfirmationDialog(
        onDismiss = { showConfirmDeleteDialog = !showConfirmDeleteDialog },
        onConfirm = { onDeleteSubject(subject.id) }
    )

    var isExpanded by remember { mutableStateOf(false) }

    Card(
        shape = RoundedCornerShape(spacing.small),
        modifier = modifier
    ) {
        Column(
            Modifier
                .padding(spacing.medium)
                .animateContentSize(
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioMediumBouncy,
                        stiffness = Spring.StiffnessMedium
                    )
                )
        ) {
            // subject name and expand mark
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .pointerInput(Unit) {
                        detectTapGestures {
                            isExpanded = !isExpanded
                        }
                    }
            ) {
                Text(
                    text = subject.name,
                    color = MaterialTheme.colorScheme.secondary,
                    style = MaterialTheme.typography.titleLarge,
                )

                AnimatedVisibility(visible = mode is Mode.Predict && !isExpanded && subject.fixedGrade) {
                    Spacer(modifier = Modifier.width(spacing.small))
                    Icon(imageVector = Icons.Default.Lock, contentDescription = "")
                }

                Spacer(modifier = Modifier.weight(1f))

                AnimatedVisibility(mode is Mode.Predict) {
                    Text(
                        text = "C.H: ${subject.creditHours}",
                        color = MaterialTheme.colorScheme.primary,
                    )
                }

                val expandIcon = if (isExpanded) Icons.Default.KeyboardArrowLeft
                else Icons.Default.KeyboardArrowDown
                Icon(imageVector = expandIcon, contentDescription = "")
            }

            Spacer(modifier = Modifier.height(spacing.small))
            // horizontal rule
            Divider()
            Spacer(modifier = Modifier.height(spacing.small))

            Grades(
                availableGrades = availableGrades,
                subject = subject,
                onGradeClick = onGradeClick,
                mode = mode
            )

            if (isExpanded) Spacer(modifier = Modifier.height(spacing.small))

            if (isExpanded)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Option(
                        text = stringResource(R.string.rename),
                        onClick = { onRenameClick(subject) },
                        icon = Icons.Default.Edit,
                        type = OptionType.Vertical
                    )
                    Option(
                        text = stringResource(R.string.delete),
                        onClick = { showConfirmDeleteDialog = true },
                        icon = Icons.Default.Delete,
                        type = OptionType.Vertical
                    )

                    if (mode == Mode.Normal) Option(
                        text = stringResource(R.string.reset),
                        onClick = { onResetClick(subject.id) },
                        icon = Icons.Default.Refresh,
                        type = OptionType.Vertical
                    )

                    if (mode is Mode.Predict) Option(
                        text = if (subject.fixedGrade) stringResource(R.string.predict_grade)
                        else stringResource(R.string.fix_grade),
                        onClick = { onFixGradeClick(subject.id, !subject.fixedGrade) },
                        icon = if (subject.fixedGrade) Icons.Default.Lock else Icons.Default.LockOpen,
                        type = OptionType.Vertical
                    )
                }
        }
    }
}

/**
 * Grades.
 *
 * @param onGradeClick On grade click lambda deliver the subject with a grade the user clicked to assign to it
 * @param availableGrades all the grades available to the user on the screen to each subject
 * @param subject to bind the data on the composable
 */
@Composable
fun Grades(
    onGradeClick: (subjectId: Long, clickedGrade: GradeName) -> Unit,
    availableGrades: List<GradeName>,
    subject: Subject,
    mode: Mode
) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        availableGrades.forEach { availableGrade ->
            // disable if the grade is not fixed in predictive mode OR th student can't achieve it
            GradeButton(
                grade = availableGrade,
                selected = availableGrade == subject.selectedGradeName,
                disabled = availableGrade > subject.maxGradeNameCanBeAssigned
                        || (mode is Mode.Predict && !subject.fixedGrade),
                onGradeClick = { onGradeClick(subject.id, availableGrade) }
            )
        }
    }
}

@Composable
fun GradeButton(
    grade: GradeName,
    selected: Boolean,
    disabled: Boolean,
    onGradeClick: () -> Unit,
) {
    val spacing = LocalSpacing.current

    val buttonSize = spacing.large
    val disabledAlpha by animateFloatAsState(targetValue = if (disabled) 0.5f else 1f)

    val selectedAlpha by animateFloatAsState(targetValue = if (selected) 1f else 0.7f)

    val textColor = MaterialTheme.colorScheme.secondary.copy(
        alpha = selectedAlpha
    )

    Box(
        modifier = Modifier
            .size(buttonSize)
            .clip(CircleShape)
            .then(
                if (!disabled) Modifier.clickable { onGradeClick() }
                else Modifier
            )
            .then(
                if (selected) Modifier.border(
                    width = 2.dp,
                    color = textColor,
                    shape = CircleShape
                ) else Modifier
            )
            .padding(spacing.extraSmall),
    ) {
        Text(
            text = buildAnnotatedString {
                withStyle(ParagraphStyle(textDirection = TextDirection.Ltr)) {
                    append(grade.symbol)
                }
            }, color = textColor,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .align(Alignment.Center)
                .size(buttonSize)
                .alpha(disabledAlpha)
        )
    }
}