package com.hussienfahmy.semester_history_presentation

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.LifecycleResumeEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.hussienfahmy.core.R
import com.hussienfahmy.core.data.local.entity.Subject
import com.hussienfahmy.core_ui.LocalSpacing
import com.hussienfahmy.core_ui.presentation.util.toStringWithOptionalDecimals
import com.hussienfahmy.semester_history_domain.model.Semester
import com.hussienfahmy.semester_history_domain.model.SemesterDetail
import com.hussienfahmy.semester_history_presentation.components.AddSubjectSheet
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun SemesterDetailRoot(
    semesterId: Long,
    viewModel: SemesterDetailViewModel = koinViewModel { parametersOf(semesterId) },
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                is SemesterDetailEvent.ShowError -> snackbarHostState.showSnackbar(
                    event.message.asString(
                        context
                    )
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SemesterDetailScreen(
    state: SemesterDetailState,
    onAction: (SemesterDetailAction) -> Unit,
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
) {
    var showAddSubjectSheet by remember { mutableStateOf(false) }
    var editingSubject by remember { mutableStateOf<Subject?>(null) }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                if (!state.isSubmitting) showAddSubjectSheet = true
            }) {
                AnimatedContent(
                    targetState = state.isSubmitting,
                    label = "fab_content"
                ) { submitting ->
                    if (submitting) {
                        CircularProgressIndicator(
                            modifier = Modifier.padding(8.dp),
                            strokeWidth = 2.dp,
                        )
                    } else {
                        Icon(
                            Icons.Outlined.Add,
                            contentDescription = stringResource(R.string.add_subject),
                        )
                    }
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
                CircularProgressIndicator()
            }

            else -> SemesterDetailContent(
                detail = state.detail,
                onEditSubject = { editingSubject = it },
                onDeleteSubject = { onAction(SemesterDetailAction.OnDeleteSubject(it)) },
                modifier = Modifier.padding(padding),
            )
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
                        name,
                        creditHours,
                        gradeName,
                        totalMarks,
                        semesterMarks,
                        metadata
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
                        subject,
                        name,
                        creditHours,
                        gradeName,
                        totalMarks,
                        semesterMarks,
                        metadata
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
    val spacing = LocalSpacing.current

    LazyColumn(
        modifier = modifier.padding(horizontal = spacing.small),
        verticalArrangement = Arrangement.spacedBy(spacing.small),
        contentPadding = androidx.compose.foundation.layout.PaddingValues(bottom = 80.dp),
    ) {
        item {
            SemesterHeaderCard(semester = detail.semester)
        }

        if (detail.subjects.isEmpty()) {
            item {
                Text(
                    text = stringResource(R.string.history_no_subjects_hint),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(spacing.medium),
                )
            }
        } else {
            item {
                Text(
                    text = stringResource(R.string.history_subjects_header, detail.subjectCount),
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(top = spacing.small),
                )
            }
            items(detail.subjects, key = { it.id }) { subject ->
                SubjectCard(
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
private fun SemesterHeaderCard(semester: Semester, modifier: Modifier = Modifier) {
    val spacing = LocalSpacing.current

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
        ),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(spacing.medium),
            horizontalArrangement = Arrangement.SpaceEvenly,
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = stringResource(R.string.history_semester_gpa),
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                )
                Text(
                    text = "%.2f".format(semester.semesterGPA),
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                )
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = stringResource(R.string.credit_hours),
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                )
                Text(
                    text = semester.totalCreditHours.toString(),
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SubjectCard(
    subject: Subject,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val spacing = LocalSpacing.current
    var showDeleteSheet by remember { mutableStateOf(false) }

    if (showDeleteSheet) {
        ModalBottomSheet(onDismissRequest = { showDeleteSheet = false }) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(spacing.medium),
                verticalArrangement = Arrangement.spacedBy(spacing.small),
            ) {
                Text(
                    text = stringResource(R.string.history_delete_subject_title),
                    style = MaterialTheme.typography.titleLarge,
                )
                Text(
                    text = stringResource(R.string.history_delete_subject_message, subject.name),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                Spacer(Modifier.height(spacing.small))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(spacing.small),
                ) {
                    OutlinedButton(
                        onClick = { showDeleteSheet = false },
                        modifier = Modifier.weight(1f),
                    ) { Text(stringResource(R.string.cancel)) }
                    Button(
                        onClick = { showDeleteSheet = false; onDelete() },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.error,
                        ),
                    ) { Text(stringResource(R.string.delete)) }
                }
                Spacer(Modifier.height(spacing.medium))
            }
        }
    }

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(10.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = spacing.medium, vertical = spacing.small),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            GradeBadge(symbol = subject.gradeName?.symbol)
            Spacer(Modifier.width(spacing.small))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = subject.name,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium,
                )
                Text(
                    text = stringResource(
                        R.string.credit_hours_value_in_history,
                        if (subject.creditHours % 1.0 == 0.0) subject.creditHours.toInt() else subject.creditHours,
                    ),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                subject.semesterMarks?.let { marks ->
                    val scoreText = if (subject.totalMarks > 0) {
                        stringResource(
                            R.string.history_score_with_total,
                            marks.value.toStringWithOptionalDecimals(),
                            subject.totalMarks.toStringWithOptionalDecimals(),
                        )
                    } else {
                        stringResource(
                            R.string.history_score_no_total,
                            marks.value.toStringWithOptionalDecimals()
                        )
                    }
                    Text(
                        text = scoreText,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
                if (subject.gradeName == null) {
                    Text(
                        text = stringResource(R.string.history_no_grade_excluded),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.error.copy(alpha = 0.8f),
                    )
                }
            }
            IconButton(onClick = onEdit) {
                Icon(Icons.Outlined.Edit, contentDescription = stringResource(R.string.edit))
            }
            IconButton(onClick = { showDeleteSheet = true }) {
                Icon(
                    Icons.Outlined.Delete,
                    contentDescription = stringResource(R.string.delete),
                    tint = MaterialTheme.colorScheme.error,
                )
            }
        }
    }
}

@Composable
private fun GradeBadge(symbol: String?) {
    val containerColor = if (symbol != null)
        MaterialTheme.colorScheme.tertiaryContainer
    else
        MaterialTheme.colorScheme.surfaceVariant

    val contentColor = if (symbol != null)
        MaterialTheme.colorScheme.onTertiaryContainer
    else
        MaterialTheme.colorScheme.onSurfaceVariant

    Surface(
        color = containerColor,
        contentColor = contentColor,
        shape = RoundedCornerShape(8.dp),
    ) {
        Text(
            text = symbol ?: "–",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
        )
    }
}
