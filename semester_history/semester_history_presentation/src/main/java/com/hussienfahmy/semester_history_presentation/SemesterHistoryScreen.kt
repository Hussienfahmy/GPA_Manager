package com.hussienfahmy.semester_history_presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Done
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.LifecycleResumeEffect
import com.hussienfahmy.core.R
import com.hussienfahmy.core_ui.LocalSpacing
import com.hussienfahmy.core_ui.presentation.util.UiEventHandler
import com.hussienfahmy.semester_history_domain.model.Semester
import com.hussienfahmy.semester_history_presentation.components.AddPastSemesterSheet
import com.hussienfahmy.semester_history_presentation.components.CumulativeGpaCard
import com.hussienfahmy.semester_history_presentation.components.EditSemesterSheet
import com.hussienfahmy.semester_history_presentation.components.FinishSemesterDialog
import com.hussienfahmy.semester_history_presentation.components.SemesterCard
import com.hussienfahmy.semester_history_presentation.export.ExportReportSheetContent
import com.hussienfahmy.semester_history_presentation.export.ExportReportViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SemesterHistoryScreen(
    modifier: Modifier = Modifier,
    snackBarHostState: SnackbarHostState,
    onSemesterClick: (semesterId: Long) -> Unit,
    onExportHtml: (String) -> Unit,
    viewModel: SemesterHistoryViewModel = koinViewModel(),
    exportViewModel: ExportReportViewModel = koinViewModel()
) {
    UiEventHandler(uiEvent = viewModel.uiEvent, snackBarHostState = snackBarHostState)

    val scope = rememberCoroutineScope()
    val addSubjectsFirstMsg = stringResource(R.string.history_add_subjects_first)

    LaunchedEffect(Unit) {
        viewModel.navigateToDetail.collect { semesterId ->
            onSemesterClick(semesterId)
        }
    }

    LifecycleResumeEffect(Unit) {
        onPauseOrDispose {
            viewModel.onEvent(SemesterHistoryEvent.OnScreenExit)
        }
    }

    val state by viewModel.state

    var showFinishDialog by remember { mutableStateOf(false) }
    var showAddSheet by remember { mutableStateOf(false) }
    var showExportSheet by remember { mutableStateOf(false) }
    var editingSemester by remember { mutableStateOf<Semester?>(null) }

    val exportSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val exportState by exportViewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        exportViewModel.exportHtml.collect { html ->
            scope.launch { exportSheetState.hide() }.invokeOnCompletion {
                showExportSheet = false
            }
            onExportHtml(html)
        }
    }

    LaunchedEffect(exportState.error) {
        exportState.error?.let { snackBarHostState.showSnackbar(it) }
    }

    if (showExportSheet) {
        ModalBottomSheet(
            onDismissRequest = { showExportSheet = false },
            sheetState = exportSheetState,
        ) {
            ExportReportSheetContent(
                state = exportState,
                onEvent = exportViewModel::onEvent,
            )
        }
    }

    if (showFinishDialog && state is SemesterHistoryState.Loaded) {
        val loadedState = state as SemesterHistoryState.Loaded
        FinishSemesterDialog(
            currentLevel = loadedState.currentLevel,
            currentSemesterNum = loadedState.currentSemesterNum,
            onConfirm = {
                showFinishDialog = false
                viewModel.onEvent(SemesterHistoryEvent.FinishSemester)
            },
            onDismiss = { showFinishDialog = false },
        )
    }

    if (showAddSheet) {
        AddPastSemesterSheet(
            onDismiss = { showAddSheet = false },
            onAddSummary = { label, gpa, hours, level ->
                viewModel.onEvent(
                    SemesterHistoryEvent.AddSummarySemester(
                        label = label,
                        semesterGPA = gpa,
                        totalCreditHours = hours,
                        level = level,
                    )
                )
            },
            onAddDetailed = { label, level ->
                viewModel.onEvent(
                    SemesterHistoryEvent.AddDetailedSemester(
                        label = label,
                        level = level,
                    )
                )
            },
        )
    }

    editingSemester?.let { semester ->
        EditSemesterSheet(
            semester = semester,
            onDismiss = { editingSemester = null },
            onSaveLabel = { id, label ->
                viewModel.onEvent(SemesterHistoryEvent.EditSemesterLabel(id, label))
            },
            onSaveSummary = { id, label, gpa, hours ->
                viewModel.onEvent(SemesterHistoryEvent.EditSummarySemester(id, label, gpa, hours))
            },
        )
    }

    when (state) {
        is SemesterHistoryState.Loading -> Box(modifier = modifier.fillMaxSize()) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        }

        is SemesterHistoryState.Loaded -> {
            val loadedState = state as SemesterHistoryState.Loaded
            SemesterHistoryContent(
                modifier = modifier,
                state = loadedState,
                onFinishSemesterClick = {
                    if (loadedState.hasWorkspaceSubjects) {
                        showFinishDialog = true
                    } else {
                        scope.launch { snackBarHostState.showSnackbar(addSubjectsFirstMsg) }
                    }
                },
                onAddPastSemesterClick = { showAddSheet = true },
                onSemesterClick = onSemesterClick,
                onEditClick = { editingSemester = it },
                onDeleteClick = { viewModel.onEvent(SemesterHistoryEvent.DeleteSemester(it)) },
                onMoveUp = { viewModel.onEvent(SemesterHistoryEvent.MoveSemesterUp(it)) },
                onMoveDown = { viewModel.onEvent(SemesterHistoryEvent.MoveSemesterDown(it)) },
                isExporting = exportState.isExporting,
                onExportClick = { showExportSheet = true },
            )
        }
    }
}

@Composable
fun SemesterHistoryContent(
    modifier: Modifier = Modifier,
    state: SemesterHistoryState.Loaded,
    isExporting: Boolean,
    onFinishSemesterClick: () -> Unit,
    onAddPastSemesterClick: () -> Unit,
    onSemesterClick: (Long) -> Unit,
    onEditClick: (Semester) -> Unit,
    onDeleteClick: (Long) -> Unit,
    onMoveUp: (Long) -> Unit,
    onMoveDown: (Long) -> Unit,
    onExportClick: () -> Unit,
) {
    val spacing = LocalSpacing.current

    Scaffold(
        modifier = modifier.fillMaxSize(),
        contentWindowInsets = WindowInsets(0),
        floatingActionButton = {
            Column(horizontalAlignment = Alignment.End) {
                ExtendedFloatingActionButton(
                    onClick = onFinishSemesterClick,
                    icon = { Icon(Icons.Outlined.Done, contentDescription = null) },
                    text = { Text(stringResource(R.string.history_finish_semester)) },
                    containerColor = if (state.hasWorkspaceSubjects)
                        MaterialTheme.colorScheme.secondaryContainer
                    else
                        MaterialTheme.colorScheme.surfaceVariant,
                    contentColor = if (state.hasWorkspaceSubjects)
                        MaterialTheme.colorScheme.onSecondaryContainer
                    else
                        MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f),
                )
                Spacer(modifier = Modifier.height(spacing.small))
                FloatingActionButton(onClick = onAddPastSemesterClick) {
                    Icon(Icons.Outlined.Add, contentDescription = stringResource(R.string.add))
                }
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .padding(paddingValues)
                .padding(horizontal = spacing.small),
            verticalArrangement = Arrangement.spacedBy(spacing.small),
            contentPadding = PaddingValues(vertical = spacing.small),
        ) {
            item {
                CumulativeGpaCard(
                    cumulativeGPA = state.cumulativeGPA,
                    totalCreditHours = state.totalCreditHours,
                )
            }

            item {
                OutlinedButton(
                    onClick = onExportClick,
                    enabled = !isExporting,
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    if (isExporting) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(16.dp),
                            strokeWidth = 2.dp,
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Outlined.Share,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp),
                        )
                    }
                    Spacer(modifier = Modifier.size(spacing.small))
                    Text(stringResource(R.string.export_pdf))
                }
            }

            item {
                Text(
                    text = stringResource(
                        R.string.history_you_are_in,
                        state.currentLevel,
                        state.currentSemesterNum
                    ),
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(top = spacing.small),
                )
            }

            if (state.semesters.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(spacing.large),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(
                            text = stringResource(R.string.history_empty_state),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = TextAlign.Center,
                        )
                    }
                }
            } else {
                itemsIndexed(
                    items = state.semesters,
                    key = { _, semester -> semester.id },
                ) { index, semester ->
                    SemesterCard(
                        semester = semester,
                        onClick = {
                            if (semester.type == Semester.Type.DETAILED) onSemesterClick(semester.id)
                        },
                        onEditClick = { onEditClick(semester) },
                        onDeleteClick = { onDeleteClick(semester.id) },
                        onMoveUp = { onMoveUp(semester.id) },
                        onMoveDown = { onMoveDown(semester.id) },
                        canMoveUp = index > 0,
                        canMoveDown = index < state.semesters.size - 1,
                        modifier = Modifier.animateItem(),
                    )
                }
            }
        }
    }
}
