package com.hussienfahmy.semester_subjctets_presentaion.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import org.jetbrains.compose.resources.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hussienfahmy.core.generated.resources.*
import com.hussienfahmy.core.data.local.model.GradeName
import com.hussienfahmy.core_ui.LocalScaffoldContentPadding
import com.hussienfahmy.core_ui.presentation.components.meadow.CardAction
import com.hussienfahmy.core_ui.presentation.components.meadow.CardActionStyle
import com.hussienfahmy.core_ui.presentation.components.meadow.GradePill
import com.hussienfahmy.core_ui.presentation.components.meadow.GradePillState
import com.hussienfahmy.core_ui.presentation.components.meadow.MeadowCard
import com.hussienfahmy.core_ui.presentation.components.meadow.MeadowChip
import com.hussienfahmy.core_ui.presentation.components.meadow.MeadowChipStyle
import com.hussienfahmy.core_ui.presentation.components.meadow.MeadowConfirmationSheet
import com.hussienfahmy.core_ui.theme.MeadowTheme
import com.hussienfahmy.semester_subjctets_domain.model.Grade
import com.hussienfahmy.semester_subjctets_domain.model.Subject
import com.hussienfahmy.semester_subjctets_presentaion.model.Mode

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
    columns: Int,
    modifier: Modifier = Modifier,
) {
    val scaffoldPadding = LocalScaffoldContentPadding.current

    LazyVerticalGrid(
        columns = GridCells.Fixed(columns),
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(11.dp),
        horizontalArrangement = Arrangement.spacedBy(11.dp),
        contentPadding = PaddingValues(bottom = 8.dp + scaffoldPadding.calculateBottomPadding()),
    ) {
        items(items = subjects, key = { it.id }) { subject ->
            SubjectCard(
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
fun SubjectCard(
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
    val colors = MeadowTheme.colors
    val accent = MeadowTheme.accent

    var showConfirmDeleteDialog by remember { mutableStateOf(false) }

    if (showConfirmDeleteDialog) MeadowConfirmationSheet(
        title = stringResource(Res.string.delete_subject_title, subject.name),
        body = stringResource(Res.string.delete_subject_message),
        confirmText = stringResource(Res.string.delete),
        onConfirm = { onDeleteSubject(subject.id) },
        onDismiss = { showConfirmDeleteDialog = false },
    )

    var isExpanded by remember { mutableStateOf(false) }

    MeadowCard(
        modifier = modifier.fillMaxWidth(),
        border = if (isExpanded) BorderStroke(2.dp, accent.container) else null,
        elevated = isExpanded,
        animateSize = true,
    ) {
        // Header: name + chip (+ predict-mode grade pill), tap to expand
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                ) { isExpanded = !isExpanded },
        ) {
            Text(
                text = subject.name,
                style = MaterialTheme.typography.titleLarge,
                color = colors.ink,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(1f),
            )

            val isPredict = mode is Mode.Predict

            // Capped by semester marks — the best grade the marks still allow
            val capped = availableGrades.any { it > subject.maxGradeNameCanBeAssigned }
            if (isPredict && capped) {
                MeadowChip(
                    text = stringResource(
                        Res.string.capped_max, subject.maxGradeNameCanBeAssigned.symbol
                    ),
                    style = MeadowChipStyle.Warn,
                )
            }

            MeadowChip(
                text = stringResource(Res.string.hrs_value, subject.creditHours.toString()),
            )

            if (isPredict) {
                val symbol = subject.assignedGrade?.name?.symbol ?: "—"
                GradePill(
                    text = symbol,
                    state = if (subject.fixedGrade) GradePillState.Fixed
                    else GradePillState.Suggested,
                    modifier = Modifier.width(52.dp),
                )
            }
        }

        // Grade ladder — Normal mode; in Predict mode only for a fixed subject,
        // where "Fix grade" means picking the letter yourself.
        if (mode == Mode.Normal || (mode is Mode.Predict && subject.fixedGrade && isExpanded)) {
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp),
            ) {
                availableGrades.forEach { availableGrade ->
                    val selected = availableGrade == subject.selectedGradeName
                    val locked = availableGrade > subject.maxGradeNameCanBeAssigned
                    GradePill(
                        text = availableGrade.symbol,
                        state = when {
                            selected -> GradePillState.Selected
                            locked -> GradePillState.Locked
                            else -> GradePillState.Rest
                        },
                        onClick = { onGradeClick(subject.id, availableGrade) },
                        modifier = Modifier.weight(1f),
                    )
                }
            }
        }

        // Expanded footer actions
        if (isExpanded) {
            Spacer(modifier = Modifier.height(12.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(colors.divider)
            )
            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                when (mode) {
                    Mode.Normal -> {
                        CardAction(
                            text = stringResource(Res.string.rename),
                            onClick = { onRenameClick(subject) },
                            modifier = Modifier.weight(1f),
                        )
                        CardAction(
                            text = stringResource(Res.string.delete),
                            onClick = { showConfirmDeleteDialog = true },
                            style = CardActionStyle.Danger,
                            modifier = Modifier.weight(1f),
                        )
                        CardAction(
                            text = stringResource(Res.string.reset),
                            onClick = { onResetClick(subject.id) },
                            modifier = Modifier.weight(1f),
                        )
                    }

                    is Mode.Predict -> {
                        // One action at a time: lock it, or hand it back to the planner
                        if (!subject.fixedGrade) {
                            CardAction(
                                text = stringResource(Res.string.fix_grade),
                                onClick = { onFixGradeClick(subject.id, true) },
                                style = CardActionStyle.Accent,
                                modifier = Modifier.weight(1f),
                            )
                        } else {
                            CardAction(
                                text = stringResource(Res.string.let_app_predict),
                                onClick = { onFixGradeClick(subject.id, false) },
                                style = CardActionStyle.Accent,
                                modifier = Modifier.weight(1f),
                            )
                        }
                    }
                }
            }

            if (mode is Mode.Predict) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = if (subject.fixedGrade) stringResource(Res.string.locked_prediction_hint)
                    else stringResource(Res.string.lock_grade_hint),
                    style = MaterialTheme.typography.bodySmall,
                    color = colors.inkFaint,
                )
            }
        }
    }
}

private fun previewSubject(
    id: Long,
    name: String,
    grade: GradeName?,
    fixed: Boolean = false,
    maxGrade: GradeName = GradeName.A,
) = Subject(
    id = id,
    name = name,
    creditHours = 3.0,
    totalMarks = 150.0,
    fixedGrade = fixed,
    assignedGrade = grade?.let { Grade(name = it, points = 4.0, percentage = 90.0) },
    maxGradeCanBeAssigned = Grade(name = maxGrade, points = 4.0, percentage = 90.0),
)

private val previewGrades = listOf(
    GradeName.A, GradeName.AMinus, GradeName.BPlus, GradeName.B,
    GradeName.CPlus, GradeName.C, GradeName.D, GradeName.F,
)

@Preview(name = "Subjects · normal · light", showBackground = true, heightDp = 500)
@Composable
private fun SubjectsColumnNormalPreview() {
    MeadowTheme(darkTheme = false) {
        SubjectsColumn(
            subjects = listOf(
                previewSubject(0, "Computer Graphics", GradeName.BPlus),
                previewSubject(1, "Mobile Development", GradeName.AMinus),
                previewSubject(2, "Machine Learning", null, maxGrade = GradeName.BPlus),
            ),
            activeGrades = previewGrades,
            onRenameClick = {}, onDeleteSubject = {}, onFixGradeClick = { _, _ -> },
            onResetClick = {}, onGradeClick = { _, _ -> },
            mode = Mode.Normal,
            columns = 1
        )
    }
}

@Preview(name = "Subjects · predict · dark", showBackground = true, heightDp = 500)
@Composable
private fun SubjectsColumnPredictPreview() {
    MeadowTheme(darkTheme = true) {
        SubjectsColumn(
            subjects = listOf(
                previewSubject(0, "Computer Graphics", GradeName.BPlus, fixed = true),
                previewSubject(1, "Mobile Development", GradeName.A),
                previewSubject(2, "Machine Learning", GradeName.AMinus, maxGrade = GradeName.BPlus),
            ),
            activeGrades = previewGrades,
            onRenameClick = {}, onDeleteSubject = {}, onFixGradeClick = { _, _ -> },
            onResetClick = {}, onGradeClick = { _, _ -> },
            mode = Mode.Predict(targetCumulativeGPA = "3.6", reverseSubjects = false),
            columns = 1
        )
    }
}
