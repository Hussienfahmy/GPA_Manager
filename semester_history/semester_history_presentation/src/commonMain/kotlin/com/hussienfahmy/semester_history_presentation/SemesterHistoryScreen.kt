package com.hussienfahmy.semester_history_presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import com.hussienfahmy.core_ui.LocalScaffoldContentPadding
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
import org.jetbrains.compose.resources.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.hussienfahmy.core.generated.resources.*
import com.hussienfahmy.core_ui.LocalSpacing
import com.hussienfahmy.core_ui.presentation.components.meadow.MeadowBottomSheet
import com.hussienfahmy.core_ui.presentation.components.meadow.MeadowChip
import com.hussienfahmy.core_ui.presentation.components.meadow.MeadowChipStyle
import com.hussienfahmy.core_ui.presentation.components.meadow.PillButton
import com.hussienfahmy.core_ui.presentation.components.meadow.PillButtonStyle
import com.hussienfahmy.core_ui.presentation.util.UiEventHandler
import com.hussienfahmy.core_ui.theme.MeadowAccentProvider
import com.hussienfahmy.core_ui.theme.MeadowTheme
import com.hussienfahmy.semester_history_domain.model.Semester
import com.hussienfahmy.semester_history_presentation.components.AddPastSemesterSheet
import com.hussienfahmy.semester_history_presentation.components.CumulativeGpaCard
import com.hussienfahmy.semester_history_presentation.components.EditSemesterSheet
import com.hussienfahmy.semester_history_presentation.components.FinishSemesterSheet
import com.hussienfahmy.semester_history_presentation.components.SemesterCard
import com.hussienfahmy.semester_history_presentation.export.ExportReportSheetContent
import com.hussienfahmy.semester_history_presentation.export.ExportReportViewModel
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun SemesterHistoryScreen(
    modifier: Modifier = Modifier,
    snackBarHostState: SnackbarHostState,
    onSemesterClick: (semesterId: Long, label: String) -> Unit,
    onExportHtml: (String) -> Unit,
    viewModel: SemesterHistoryViewModel = koinViewModel(),
    exportViewModel: ExportReportViewModel = koinViewModel()
) {
    UiEventHandler(uiEvent = viewModel.uiEvent, snackBarHostState = snackBarHostState)

    val scope = rememberCoroutineScope()
    val addSubjectsFirstMsg = stringResource(Res.string.history_add_subjects_first)

    LaunchedEffect(Unit) {
        viewModel.navigateToDetail.collect { (semesterId, label) ->
            onSemesterClick(semesterId, label)
        }
    }

    val state by viewModel.state

    var showFinishDialog by remember { mutableStateOf(false) }
    var showAddSheet by remember { mutableStateOf(false) }
    var showExportSheet by remember { mutableStateOf(false) }
    var editingSemester by remember { mutableStateOf<Semester?>(null) }

    val exportState by exportViewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        exportViewModel.exportHtml.collect { html ->
            showExportSheet = false
            onExportHtml(html)
        }
    }

    LaunchedEffect(exportState.error) {
        exportState.error?.let { snackBarHostState.showSnackbar(it) }
    }

    MeadowAccentProvider(MeadowTheme.colors.history) {
        if (showExportSheet) {
            MeadowBottomSheet(onDismiss = { showExportSheet = false }) {
                ExportReportSheetContent(
                    state = exportState,
                    onEvent = exportViewModel::onEvent,
                )
            }
        }

        if (showFinishDialog && state is SemesterHistoryState.Loaded) {
            val loadedState = state as SemesterHistoryState.Loaded
            FinishSemesterSheet(
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
                    viewModel.onEvent(
                        SemesterHistoryEvent.EditSummarySemester(
                            id,
                            label,
                            gpa,
                            hours
                        )
                    )
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
}

@Composable
fun SemesterHistoryContent(
    modifier: Modifier = Modifier,
    state: SemesterHistoryState.Loaded,
    isExporting: Boolean,
    onFinishSemesterClick: () -> Unit,
    onAddPastSemesterClick: () -> Unit,
    onSemesterClick: (semesterId: Long, label: String) -> Unit,
    onEditClick: (Semester) -> Unit,
    onDeleteClick: (Long) -> Unit,
    onMoveUp: (Long) -> Unit,
    onMoveDown: (Long) -> Unit,
    onExportClick: () -> Unit,
) {
    val spacing = LocalSpacing.current
    val scaffoldPadding = LocalScaffoldContentPadding.current

    val colors = MeadowTheme.colors

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = colors.paper,
        contentWindowInsets = WindowInsets(0),
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .padding(paddingValues)
                .padding(horizontal = spacing.small),
            verticalArrangement = Arrangement.spacedBy(11.dp),
            contentPadding = PaddingValues(
                top = spacing.small,
                bottom = spacing.small + scaffoldPadding.calculateBottomPadding(),
            ),
        ) {
            item {
                // Same trailing-pill convention as the Semester tab's Controllers row.
                Row(modifier = Modifier.fillMaxWidth()) {
                    Spacer(modifier = Modifier.weight(1f))
                    PillButton(
                        text = stringResource(Res.string.add),
                        onClick = onAddPastSemesterClick,
                        style = PillButtonStyle.Tonal,
                        icon = Icons.Rounded.Add,
                        compact = true,
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    PillButton(
                        text = stringResource(Res.string.history_finish_semester),
                        onClick = onFinishSemesterClick,
                        style = PillButtonStyle.Primary,
                        icon = Icons.Rounded.Check,
                        enabled = state.hasWorkspaceSubjects,
                        compact = true,
                    )
                }
            }

            item {
                CumulativeGpaCard(
                    cumulativeGPA = state.cumulativeGPA,
                    totalCreditHours = state.totalCreditHours,
                    semestersCount = state.semesters.size,
                    onExportClick = onExportClick,
                    isExporting = isExporting,
                )
            }

            item {
                MeadowChip(
                    text = stringResource(
                        Res.string.history_you_are_in,
                        state.currentLevel,
                        state.currentSemesterNum
                    ),
                    style = MeadowChipStyle.Accent,
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
                            text = stringResource(Res.string.history_empty_state),
                            style = MaterialTheme.typography.bodyMedium,
                            color = colors.inkMuted,
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
                            if (semester.type == Semester.Type.DETAILED) {
                                onSemesterClick(semester.id, semester.label)
                            }
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
