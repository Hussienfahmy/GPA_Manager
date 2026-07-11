package com.hussienfahmy.semester_history_presentation

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.rounded.PriorityHigh
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextDirection
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.LifecycleResumeEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.hussienfahmy.core.R
import com.hussienfahmy.core.data.local.entity.Subject
import com.hussienfahmy.core_ui.presentation.components.meadow.CapsLabel
import com.hussienfahmy.core_ui.presentation.components.meadow.MeadowCard
import com.hussienfahmy.core_ui.presentation.components.meadow.MeadowConfirmationSheet
import com.hussienfahmy.core_ui.presentation.components.meadow.PillButton
import com.hussienfahmy.core_ui.presentation.components.meadow.PillButtonStyle
import com.hussienfahmy.core_ui.presentation.util.toStringWithOptionalDecimals
import com.hussienfahmy.core_ui.theme.MeadowAccentProvider
import com.hussienfahmy.core_ui.theme.MeadowRadius
import com.hussienfahmy.core_ui.theme.MeadowTheme
import com.hussienfahmy.semester_history_domain.model.Semester
import com.hussienfahmy.semester_history_domain.model.SemesterDetail
import com.hussienfahmy.semester_history_presentation.components.AddSubjectSheet
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf
import java.util.Locale

@Composable
fun SemesterDetailRoot(
    semesterId: Long,
    // Koin/AndroidX cache a ViewModel by class name by default — parametersOf
    // alone won't give a different semesterId a fresh instance if the same
    // ViewModelStoreOwner is reused (e.g. embedded inline without a new nav
    // back-stack entry, as onboarding does). Keying by semesterId fixes that
    // and is a no-op for normal navigation, where each entry is already
    // distinct.
    viewModel: SemesterDetailViewModel = koinViewModel(
        key = "semester_detail_$semesterId",
    ) { parametersOf(semesterId) },
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                is SemesterDetailEvent.ShowError -> snackbarHostState.showSnackbar(
                    event.message.asString(context)
                )
            }
        }
    }

    LifecycleResumeEffect(Unit) {
        onPauseOrDispose {
            viewModel.onAction(SemesterDetailAction.OnScreenExit)
        }
    }

    SemesterDetailScreen(
        state = state,
        onAction = viewModel::onAction,
        snackbarHostState = snackbarHostState,
    )
}

@Composable
fun SemesterDetailScreen(
    state: SemesterDetailState,
    onAction: (SemesterDetailAction) -> Unit,
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
) {
    var showAddSubjectSheet by remember { mutableStateOf(false) }
    var editingSubject by remember { mutableStateOf<Subject?>(null) }

    MeadowAccentProvider(MeadowTheme.colors.history) {
        Scaffold(
            containerColor = MeadowTheme.colors.paper,
            // The app's root Scaffold already accounts for the status bar —
            // without this, this nested Scaffold reserves it a second time,
            // showing up as a big blank gap above the header card.
            contentWindowInsets = WindowInsets(0, 0, 0, 0),
            snackbarHost = { SnackbarHost(snackbarHostState) },
            floatingActionButton = {
                AnimatedContent(targetState = state.isSubmitting, label = "fab_content") { submitting ->
                    if (submitting) {
                        MeadowCard(contentPadding = PaddingValues(14.dp)) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(20.dp),
                                strokeWidth = 2.dp,
                                color = MeadowTheme.accent.accent,
                            )
                        }
                    } else {
                        PillButton(
                            text = stringResource(R.string.detail_add_subject),
                            onClick = { showAddSubjectSheet = true },
                            style = PillButtonStyle.Primary,
                        )
                    }
                }
            }
        ) { padding ->
            when {
                state.detail == null -> Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    contentAlignment = Alignment.Center,
                ) {
                    CircularProgressIndicator(color = MeadowTheme.accent.accent)
                }

                else -> SemesterDetailContent(
                    detail = state.detail,
                    onEditSubject = { editingSubject = it },
                    onDeleteSubject = { onAction(SemesterDetailAction.OnDeleteSubject(it)) },
                    modifier = Modifier.padding(padding),
                )
            }
        }
    }

    if (showAddSubjectSheet) {
        AddSubjectSheet(
            availableGrades = state.availableGrades,
            subjectSettings = state.subjectSettings,
            onDismiss = { showAddSubjectSheet = false },
            onAdd = { name, creditHours, gradeName, totalMarks, semesterMarks, metadata ->
                onAction(
                    SemesterDetailAction.OnAddSubject(
                        name, creditHours, gradeName, totalMarks, semesterMarks, metadata
                    )
                )
            },
        )
    }

    editingSubject?.let { subject ->
        AddSubjectSheet(
            availableGrades = state.availableGrades,
            subjectSettings = state.subjectSettings,
            initialSubject = subject,
            onDismiss = { editingSubject = null },
            onAdd = { name, creditHours, gradeName, totalMarks, semesterMarks, metadata ->
                onAction(
                    SemesterDetailAction.OnEditSubject(
                        subject, name, creditHours, gradeName, totalMarks, semesterMarks, metadata
                    )
                )
            },
        )
    }
}

@Composable
private fun SemesterDetailContent(
    detail: SemesterDetail,
    onEditSubject: (Subject) -> Unit,
    onDeleteSubject: (Long) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier.padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(11.dp),
        contentPadding = PaddingValues(top = 4.dp, bottom = 96.dp),
    ) {
        item {
            SemesterHeaderCard(semester = detail.semester, subjectCount = detail.subjectCount)
        }

        if (detail.subjects.isEmpty()) {
            item {
                Text(
                    text = stringResource(R.string.history_no_subjects_hint),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MeadowTheme.colors.inkMuted,
                    modifier = Modifier.padding(16.dp),
                )
            }
        } else {
            items(detail.subjects, key = { it.id }) { subject ->
                SubjectRow(
                    subject = subject,
                    onEdit = { onEditSubject(subject) },
                    onDelete = { onDeleteSubject(subject.id) },
                    modifier = Modifier.animateItem(),
                )
            }
        }
    }
}

@Composable
private fun SemesterHeaderCard(
    semester: Semester,
    subjectCount: Int,
    modifier: Modifier = Modifier,
) {
    val colors = MeadowTheme.colors
    val accent = MeadowTheme.accent

    MeadowCard(
        modifier = modifier.fillMaxWidth(),
        radius = MeadowRadius.hero,
        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 15.dp),
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Column(modifier = Modifier.weight(1f)) {
                CapsLabel(text = stringResource(R.string.history_semester_gpa))
                Text(
                    text = String.format(Locale.getDefault(), "%.2f", semester.semesterGPA),
                    style = MaterialTheme.typography.displayMedium.copy(fontSize = 34.sp),
                    color = accent.ink,
                )
            }
            Column(horizontalAlignment = Alignment.End) {
                CapsLabel(text = stringResource(R.string.credit_hours))
                Text(
                    text = semester.totalCreditHours.toString(),
                    style = MaterialTheme.typography.displaySmall.copy(fontSize = 22.sp),
                    color = accent.ink,
                )
                Text(
                    text = stringResource(R.string.detail_subjects_count, subjectCount),
                    style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                    color = colors.inkFaint,
                )
            }
        }
    }
}

@Composable
private fun SubjectRow(
    subject: Subject,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = MeadowTheme.colors
    val accent = MeadowTheme.accent

    var showDeleteConfirmation by remember { mutableStateOf(false) }

    if (showDeleteConfirmation) MeadowConfirmationSheet(
        title = stringResource(R.string.history_delete_subject_title),
        body = stringResource(R.string.history_delete_subject_message, subject.name),
        confirmText = stringResource(R.string.delete),
        onConfirm = onDelete,
        onDismiss = { showDeleteConfirmation = false },
    )

    val noGrade = subject.gradeName == null

    MeadowCard(
        modifier = modifier.fillMaxWidth(),
        radius = 18.dp,
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
        border = if (noGrade) androidx.compose.foundation.BorderStroke(2.dp, colors.marks.soft)
        else null,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            // Grade tile — violet when graded, amber "!" warning when not
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(if (noGrade) colors.marks.container else accent.container),
            ) {
                if (noGrade) {
                    Icon(
                        imageVector = Icons.Rounded.PriorityHigh,
                        contentDescription = null,
                        tint = colors.marks.deep,
                    )
                } else {
                    Text(
                        text = subject.gradeName?.symbol.orEmpty(),
                        style = MaterialTheme.typography.headlineSmall.copy(
                            textDirection = TextDirection.Ltr
                        ),
                        color = accent.deep,
                    )
                }
            }

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = subject.name,
                    style = MaterialTheme.typography.titleLarge.copy(fontSize = 15.sp),
                    color = accent.ink,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                if (noGrade) {
                    Text(
                        text = stringResource(R.string.history_no_grade_excluded_hint),
                        style = MaterialTheme.typography.titleSmall.copy(fontSize = 11.5.sp),
                        color = colors.marks.deep,
                    )
                } else {
                    val hrs = subject.creditHours.toStringWithOptionalDecimals()
                    val marks = subject.semesterMarks
                    Text(
                        text = if (marks != null && subject.totalMarks > 0) {
                            stringResource(
                                R.string.detail_hrs_marks,
                                hrs,
                                marks.value.toStringWithOptionalDecimals(),
                                subject.totalMarks.toStringWithOptionalDecimals(),
                            )
                        } else {
                            stringResource(R.string.detail_hrs_only, hrs)
                        },
                        style = MaterialTheme.typography.bodySmall,
                        color = colors.inkFaint,
                    )
                }
            }

            Icon(
                imageVector = Icons.Outlined.Edit,
                contentDescription = stringResource(R.string.edit),
                tint = colors.navItemIcon,
                modifier = Modifier
                    .size(18.dp)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = onEdit,
                    ),
            )

            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(26.dp)
                    .clip(CircleShape)
                    .background(colors.dangerContainer)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                    ) { showDeleteConfirmation = true },
            ) {
                Icon(
                    imageVector = Icons.Outlined.Close,
                    contentDescription = stringResource(R.string.delete),
                    tint = colors.onDangerContainer,
                    modifier = Modifier.size(14.dp),
                )
            }
        }
    }
}
