package com.hussienfahmy.semester_marks_presentaion

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hussienfahmy.core.R
import com.hussienfahmy.core_ui.LocalSpacing
import com.hussienfahmy.core_ui.presentation.components.AddSubjectsHint
import com.hussienfahmy.core_ui.presentation.components.meadow.PillButton
import com.hussienfahmy.core_ui.presentation.components.meadow.PillButtonStyle
import com.hussienfahmy.core_ui.presentation.util.UiEventHandler
import com.hussienfahmy.core_ui.theme.MeadowAccentProvider
import com.hussienfahmy.core_ui.theme.MeadowTheme
import com.hussienfahmy.semester_marks_domain.model.Grade
import com.hussienfahmy.semester_marks_domain.model.Subject
import com.hussienfahmy.semester_marks_presentaion.components.SemesterMarksItem
import org.koin.androidx.compose.koinViewModel

@Composable
fun SemesterMarksScreen(
    modifier: Modifier = Modifier,
    viewModel: SemesterMarksViewModel = koinViewModel(),
) {
    val snackBarHostState = remember { SnackbarHostState() }
    UiEventHandler(uiEvent = viewModel.uiEvent, snackBarHostState = snackBarHostState)

    val state by viewModel.state

    // Handle screen disposal with sync
    DisposableEffect(Unit) {
        onDispose {
            viewModel.onEvent(SemesterMarksEvent.OnScreenExit)
        }
    }

    MeadowAccentProvider(MeadowTheme.colors.marks) {
        Box(modifier = Modifier.fillMaxSize()) {
        when (val s = state) {
            is SemesterMarksState.Loading ->
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))

            is SemesterMarksState.Calculated -> SemesterMarksScreenContent(
                modifier = modifier,
            subjects = s.subjects,
            onMidTermMarksChange = { subjectId, marks ->
                viewModel.onEvent(
                    SemesterMarksEvent.ChangeMidtermMark(subjectId, marks)
                )
            },
            onOralMarksChange = { subjectId, marks ->
                viewModel.onEvent(
                    SemesterMarksEvent.ChangeOralMark(subjectId, marks)
                )
            },
            onPracticalMarksChange = { subjectId, marks ->
                viewModel.onEvent(
                    SemesterMarksEvent.ChangePracticalMark(subjectId, marks)
                )
            },
            onProjectMarksChange = { subjectId, marks ->
                viewModel.onEvent(
                    SemesterMarksEvent.ChangeProjectMark(subjectId, marks)
                )
            },
            onFinalExamMaxMarksChange = { subjectId, marks ->
                viewModel.onEvent(
                    SemesterMarksEvent.ChangeFinalExamMaxMarks(subjectId, marks)
                )
            },
            onResetClick = { subjectId ->
                viewModel.onEvent(
                    SemesterMarksEvent.ResetMarks(subjectId)
                )
            },
            onMidtermAvailabilityCheckChanges = { subjectId, newAvailability ->
                viewModel.onEvent(
                    SemesterMarksEvent.SetMidtermAvailability(subjectId, newAvailability)
                )
            },
            onPracticalAvailabilityCheckChanges = { subjectId, newAvailability ->
                viewModel.onEvent(
                    SemesterMarksEvent.SetPracticalAvailability(subjectId, newAvailability)
                )
            },
            onOralAvailabilityCheckChanges = { subjectId, newAvailability ->
                viewModel.onEvent(
                    SemesterMarksEvent.SetOralAvailability(subjectId, newAvailability)
                )
            },
            onProjectAvailabilityCheckChanges = { subjectId, newAvailability ->
                viewModel.onEvent(
                    SemesterMarksEvent.SetProjectAvailability(subjectId, newAvailability)
                )
            },
            )
        }

        SnackbarHost(
            hostState = snackBarHostState,
            modifier = Modifier.align(Alignment.BottomCenter),
        )
        }
    }
}

@Composable
fun SemesterMarksScreenContent(
    modifier: Modifier = Modifier,
    subjects: List<Subject>,
    onMidTermMarksChange: (subjectId: Long, newMarks: String) -> Unit,
    onOralMarksChange: (subjectId: Long, newMarks: String) -> Unit,
    onPracticalMarksChange: (subjectId: Long, newMarks: String) -> Unit,
    onProjectMarksChange: (subjectId: Long, newMarks: String) -> Unit,
    onFinalExamMaxMarksChange: (subjectId: Long, newMarks: String) -> Unit,
    onResetClick: (subjectId: Long) -> Unit,
    onMidtermAvailabilityCheckChanges: (subjectId: Long, newAvailability: Boolean) -> Unit,
    onPracticalAvailabilityCheckChanges: (subjectId: Long, newAvailability: Boolean) -> Unit,
    onOralAvailabilityCheckChanges: (subjectId: Long, newAvailability: Boolean) -> Unit,
    onProjectAvailabilityCheckChanges: (subjectId: Long, newAvailability: Boolean) -> Unit,
) {
    val spacing = LocalSpacing.current

    // Expansion owned here so the toolbar always reflects real state.
    // Seed with the first subject expanded (the ladder hint).
    var expandedIds by remember(subjects.firstOrNull()?.id) {
        mutableStateOf(subjects.firstOrNull()?.let { setOf(it.id) } ?: emptySet())
    }
    val anyExpanded = expandedIds.isNotEmpty()

    if (subjects.isNotEmpty()) {
        Column(modifier = modifier.fillMaxSize()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = spacing.small, vertical = 8.dp),
                horizontalArrangement = Arrangement.End,
            ) {
                PillButton(
                    text = if (anyExpanded) stringResource(R.string.collapse_all)
                    else stringResource(R.string.expand_all),
                    onClick = {
                        expandedIds = if (anyExpanded) emptySet()
                        else subjects.map { it.id }.toSet()
                    },
                    style = PillButtonStyle.Tonal,
                    compact = true,
                )
            }

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(spacing.small),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = spacing.small),
                contentPadding = PaddingValues(bottom = spacing.small)
            ) {
                itemsIndexed(subjects, key = { _, subject -> subject.id }) { index, subject ->
                    SemesterMarksItem(
                        subject = subject,
                        isExpanded = subject.id in expandedIds,
                        onToggleExpand = {
                            expandedIds = if (subject.id in expandedIds) {
                                expandedIds - subject.id
                            } else {
                                expandedIds + subject.id
                            }
                        },
                    onMidTermMarksChange = { onMidTermMarksChange(subject.id, it) },
                    onOralMarksChange = { onOralMarksChange(subject.id, it) },
                    onPracticalMarksChange = { onPracticalMarksChange(subject.id, it) },
                    onProjectMarksChange = { onProjectMarksChange(subject.id, it) },
                    onFinalExamMaxMarksChange = { onFinalExamMaxMarksChange(subject.id, it) },
                    onResetClick = { onResetClick(subject.id) },
                    onMidtermAvailabilityCheckChanges = {
                        onMidtermAvailabilityCheckChanges(
                            subject.id,
                            it
                        )
                    },
                    onPracticalAvailabilityCheckChanges = {
                        onPracticalAvailabilityCheckChanges(
                            subject.id,
                            it
                        )
                    },
                    onOralAvailabilityCheckChanges = {
                        onOralAvailabilityCheckChanges(
                            subject.id,
                            it
                        )
                    },
                    onProjectAvailabilityCheckChanges = {
                        onProjectAvailabilityCheckChanges(
                            subject.id,
                            it
                        )
                    },
                )
                }
            }
        }
    } else {
        AddSubjectsHint()
    }
}

@Preview
@Composable
fun SemesterMarksScreenContentPreview() {
    SemesterMarksScreenContent(
        subjects = listOf(
            Subject(
                id = 1L,
                name = "Math",
                practicalAvailable = false,
                projectAvailable = true,
                midtermAvailable = true,
                oralAvailable = true,
                practicalMarks = 0.0,
                projectMarks = 0.0,
                midtermMarks = 10.0,
                oralMarks = 15.0,
                courseTotalMarks = 25.0,
                finalExamMaxMarks = 60.0,
                grades = listOf(
                    Grade(
                        symbol = "A",
                        achievable = Grade.Achievable.No
                    ),
                    Grade(
                        symbol = "B",
                        achievable = Grade.Achievable.Yes(
                            neededMarks = 15.0
                        )
                    ),
                    Grade(
                        symbol = "C",
                        achievable = Grade.Achievable.Yes(
                            neededMarks = 10.0
                        )
                    ),
                    Grade(
                        symbol = "D",
                        achievable = Grade.Achievable.Yes(
                            neededMarks = 5.0
                        )
                    ),
                )
            )
        ),
        onMidTermMarksChange = { _, _ -> },
        onOralMarksChange = { _, _ -> },
        onPracticalMarksChange = { _, _ -> },
        onProjectMarksChange = { _, _ -> },
        onFinalExamMaxMarksChange = { _, _ -> },
        onResetClick = { },
        onMidtermAvailabilityCheckChanges = { _, _ -> },
        onPracticalAvailabilityCheckChanges = { _, _ -> },
        onOralAvailabilityCheckChanges = { _, _ -> },
        onProjectAvailabilityCheckChanges = { _, _ -> }
    )
}