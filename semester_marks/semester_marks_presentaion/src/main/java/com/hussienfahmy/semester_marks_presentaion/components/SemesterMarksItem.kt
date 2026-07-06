package com.hussienfahmy.semester_marks_presentaion.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextDirection
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hussienfahmy.core.R
import com.hussienfahmy.core_ui.presentation.components.meadow.MeadowCard
import com.hussienfahmy.core_ui.presentation.components.meadow.MeadowConfirmationSheet
import com.hussienfahmy.core_ui.presentation.util.toStringWithOptionalDecimals
import com.hussienfahmy.core_ui.theme.LocalMeadowAccent
import com.hussienfahmy.core_ui.theme.MeadowRadius
import com.hussienfahmy.core_ui.theme.MeadowTheme
import com.hussienfahmy.semester_marks_domain.model.Grade
import com.hussienfahmy.semester_marks_domain.model.Subject

@Composable
fun SemesterMarksItem(
    subject: Subject,
    onMidTermMarksChange: (String) -> Unit,
    onOralMarksChange: (String) -> Unit,
    onPracticalMarksChange: (String) -> Unit,
    onProjectMarksChange: (String) -> Unit,
    onFinalExamMaxMarksChange: (String) -> Unit,
    onResetClick: () -> Unit,
    showHint: Boolean,
    onMidtermAvailabilityCheckChanges: (Boolean) -> Unit,
    onPracticalAvailabilityCheckChanges: (Boolean) -> Unit,
    onOralAvailabilityCheckChanges: (Boolean) -> Unit,
    onProjectAvailabilityCheckChanges: (Boolean) -> Unit,
) {
    val colors = MeadowTheme.colors
    val accent = MeadowTheme.accent

    var midtermInput by remember { mutableStateOf(subject.midtermMarks?.toStringWithOptionalDecimals() ?: "") }
    var practicalInput by remember { mutableStateOf(subject.practicalMarks?.toStringWithOptionalDecimals() ?: "") }
    var oralInput by remember { mutableStateOf(subject.oralMarks?.toStringWithOptionalDecimals() ?: "") }
    var projectInput by remember { mutableStateOf(subject.projectMarks?.toStringWithOptionalDecimals() ?: "") }

    var showResetConfirmation by remember { mutableStateOf(false) }
    var showSettingsSheet by remember { mutableStateOf(false) }
    var isExpanded by remember { mutableStateOf(showHint) }

    if (showResetConfirmation) MeadowConfirmationSheet(
        title = stringResource(R.string.reset_marks_title, subject.name),
        body = stringResource(R.string.reset_marks_message),
        confirmText = stringResource(R.string.reset),
        onConfirm = {
            onResetClick()
            midtermInput = ""
            practicalInput = ""
            oralInput = ""
            projectInput = ""
        },
        onDismiss = { showResetConfirmation = false },
    )

    if (showSettingsSheet) SubjectSettingsSheet(
        subjectName = subject.name,
        onDismiss = { showSettingsSheet = false },
        midtermAvailable = subject.midtermAvailable,
        practicalAvailable = subject.practicalAvailable,
        oralAvailable = subject.oralAvailable,
        projectAvailable = subject.projectAvailable,
        finalExamMaxMarks = subject.finalExamMaxMarks,
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
        },
        onFinalExamMaxMarksSave = onFinalExamMaxMarksChange,
    )

    MeadowCard(
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize(
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioNoBouncy,
                    stiffness = Spring.StiffnessMedium,
                )
            ),
        radius = if (isExpanded) MeadowRadius.hero else MeadowRadius.card,
        contentPadding = PaddingValues(
            horizontal = 18.dp, vertical = if (isExpanded) 16.dp else 14.dp
        ),
        elevated = isExpanded,
    ) {
        // Header: name · (compact: total) · reset · setup — tap to expand
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp),
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
                color = accent.ink,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(1f),
            )

            if (!isExpanded) {
                TotalMarks(subject = subject, empty = subject.courseMarks == 0.0)
            }

            if (isExpanded) {
                Icon(
                    imageVector = Icons.Default.Refresh,
                    contentDescription = stringResource(R.string.reset),
                    tint = colors.navItemIcon,
                    modifier = Modifier
                        .size(18.dp)
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null,
                        ) { showResetConfirmation = true },
                )
            }
            Icon(
                imageVector = Icons.Default.Edit,
                contentDescription = stringResource(R.string.edit),
                tint = colors.navItemIcon,
                modifier = Modifier
                    .size(18.dp)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                    ) { showSettingsSheet = true },
            )
        }

        if (isExpanded) {
            Spacer(modifier = Modifier.height(8.dp))

            // Progress bar + total
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                val progress by animateFloatAsState(
                    targetValue = subject.courseMarksPercentage.coerceIn(0f, 1f),
                    label = "marksProgress",
                )
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(8.dp)
                        .clip(CircleShape)
                        .background(colors.chipBg),
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(progress)
                            .fillMaxHeight()
                            .clip(CircleShape)
                            .background(accent.accent),
                    )
                }
                TotalMarks(subject = subject, empty = false)
            }
        }

        // Component tiles
        val hasComponents = subject.midtermAvailable || subject.practicalAvailable ||
                subject.oralAvailable || subject.projectAvailable
        if (hasComponents) {
            Spacer(modifier = Modifier.height(if (isExpanded) 14.dp else 12.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                if (subject.midtermAvailable) MarkTile(
                    title = stringResource(R.string.midterm),
                    value = midtermInput,
                    onValueChange = {
                        midtermInput = it
                        onMidTermMarksChange(it)
                    },
                    editable = isExpanded,
                    modifier = Modifier.weight(1f),
                )
                if (subject.practicalAvailable) MarkTile(
                    title = stringResource(R.string.practical),
                    value = practicalInput,
                    onValueChange = {
                        practicalInput = it
                        onPracticalMarksChange(it)
                    },
                    editable = isExpanded,
                    modifier = Modifier.weight(1f),
                )
                if (subject.oralAvailable) MarkTile(
                    title = stringResource(R.string.oral),
                    value = oralInput,
                    onValueChange = {
                        oralInput = it
                        onOralMarksChange(it)
                    },
                    editable = isExpanded,
                    modifier = Modifier.weight(1f),
                )
                if (subject.projectAvailable) MarkTile(
                    title = stringResource(R.string.project),
                    value = projectInput,
                    onValueChange = {
                        projectInput = it
                        onProjectMarksChange(it)
                    },
                    editable = isExpanded,
                    modifier = Modifier.weight(1f),
                )
            }
        }

        // Grade ladder — expanded only
        AnimatedVisibility(visible = isExpanded) {
            Column {
                Spacer(modifier = Modifier.height(14.dp))
                Text(
                    text = stringResource(R.string.final_exam_marks_needed),
                    style = MaterialTheme.typography.bodySmall,
                    color = colors.navItemText,
                )
                Spacer(modifier = Modifier.height(8.dp))
                GradeLadder(grades = subject.grades)
            }
        }
    }
}

@Composable
private fun TotalMarks(subject: Subject, empty: Boolean) {
    val colors = MeadowTheme.colors
    val accent = MeadowTheme.accent

    Row(verticalAlignment = Alignment.Bottom) {
        Text(
            text = subject.courseMarks.toStringWithOptionalDecimals(),
            style = MaterialTheme.typography.titleSmall.copy(fontSize = 13.sp),
            color = if (empty) colors.inkDisabled else accent.accent,
        )
        Text(
            text = "/${subject.courseTotalMarks.toStringWithOptionalDecimals()}",
            style = MaterialTheme.typography.bodySmall.copy(fontSize = 13.sp),
            color = colors.inkDisabled,
        )
    }
}

/** The "final-exam marks needed" ladder: amber = reachable, faded = out of reach. */
@Composable
private fun GradeLadder(grades: List<Grade>) {
    val colors = MeadowTheme.colors
    val accent = MeadowTheme.accent

    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
        grades.forEach { grade ->
            val achievable = grade.achievable as? Grade.Achievable.Yes

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(MeadowRadius.tile))
                    .background(if (achievable != null) accent.container else colors.chipBg)
                    .alpha(if (achievable != null) 1f else 0.55f)
                    .padding(top = 7.dp, bottom = 6.dp),
            ) {
                Text(
                    text = grade.symbol,
                    style = MaterialTheme.typography.titleSmall.copy(textDirection = TextDirection.Ltr),
                    color = if (achievable != null) accent.deep else colors.inkDisabled,
                    maxLines = 1,
                )
                Text(
                    text = achievable?.neededMarks?.toStringWithOptionalDecimals() ?: "—",
                    style = MaterialTheme.typography.labelSmall,
                    color = if (achievable != null) accent.soft else colors.inkDisabled,
                    maxLines = 1,
                )
            }
        }
    }
}

@Preview(name = "MarksItem · light", showBackground = true)
@Composable
private fun SemesterMarksItemLightPreview() {
    MeadowTheme(darkTheme = false) {
        PreviewItem()
    }
}

@Preview(name = "MarksItem · dark", showBackground = true)
@Composable
private fun SemesterMarksItemDarkPreview() {
    MeadowTheme(darkTheme = true) {
        PreviewItem()
    }
}

@Composable
private fun PreviewItem() {
    CompositionLocalProvider(
        LocalMeadowAccent provides MeadowTheme.colors.marks
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(11.dp),
            modifier = Modifier
                .background(MeadowTheme.colors.paper)
                .padding(16.dp),
        ) {
            SemesterMarksItem(
                subject = Subject(
                    id = 1,
                    name = "Computer Graphics",
                    practicalMarks = null,
                    midtermMarks = 15.0,
                    oralMarks = 8.0,
                    projectMarks = null,
                    courseTotalMarks = 100.0,
                    finalExamMaxMarks = 60.0,
                    midtermAvailable = true,
                    practicalAvailable = false,
                    oralAvailable = true,
                    projectAvailable = false,
                    grades = listOf(
                        Grade(symbol = "A", achievable = Grade.Achievable.No),
                        Grade(symbol = "A-", achievable = Grade.Achievable.No),
                        Grade(symbol = "B+", achievable = Grade.Achievable.Yes(neededMarks = 57.0)),
                        Grade(symbol = "B", achievable = Grade.Achievable.Yes(neededMarks = 52.0)),
                        Grade(symbol = "C+", achievable = Grade.Achievable.Yes(neededMarks = 47.0)),
                        Grade(symbol = "C", achievable = Grade.Achievable.Yes(neededMarks = 42.0)),
                    )
                ),
                onMidTermMarksChange = {},
                onPracticalMarksChange = {},
                onOralMarksChange = {},
                onProjectMarksChange = {},
                onFinalExamMaxMarksChange = {},
                onMidtermAvailabilityCheckChanges = {},
                onPracticalAvailabilityCheckChanges = {},
                onOralAvailabilityCheckChanges = {},
                onProjectAvailabilityCheckChanges = {},
                onResetClick = {},
                showHint = true,
            )
        }
    }
}
