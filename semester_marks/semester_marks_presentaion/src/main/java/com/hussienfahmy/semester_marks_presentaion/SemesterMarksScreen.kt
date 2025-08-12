package com.hussienfahmy.semester_marks_presentaion

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.hussienFahmy.core_ui.LocalSpacing
import com.hussienFahmy.core_ui.presentation.components.AddSubjectsHint
import com.hussienFahmy.core_ui.presentation.util.UiEventHandler
import com.hussienfahmy.semester_marks_domain.model.Grade
import com.hussienfahmy.semester_marks_domain.model.Subject
import com.hussienfahmy.semester_marks_presentaion.components.SemesterMarksItem

@Composable
fun SemesterMarksScreen(
    modifier: Modifier = Modifier,
    viewModel: SemesterMarksViewModel = hiltViewModel(),
) {
    UiEventHandler(uiEvent = viewModel.uiEvent)

    val state by viewModel.state

    when (state) {
        is SemesterMarksState.Loading -> Box(modifier = Modifier.fillMaxSize()) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        }

        is SemesterMarksState.Calculated -> SemesterMarksScreenContent(
            modifier = modifier,
            (state as SemesterMarksState.Calculated).subjects,
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
}

@Composable
fun SemesterMarksScreenContent(
    modifier: Modifier = Modifier,
    subjects: List<Subject>,
    onMidTermMarksChange: (subjectId: Long, newMarks: String) -> Unit,
    onOralMarksChange: (subjectId: Long, newMarks: String) -> Unit,
    onPracticalMarksChange: (subjectId: Long, newMarks: String) -> Unit,
    onProjectMarksChange: (subjectId: Long, newMarks: String) -> Unit,
    onResetClick: (subjectId: Long) -> Unit,
    onMidtermAvailabilityCheckChanges: (subjectId: Long, newAvailability: Boolean) -> Unit,
    onPracticalAvailabilityCheckChanges: (subjectId: Long, newAvailability: Boolean) -> Unit,
    onOralAvailabilityCheckChanges: (subjectId: Long, newAvailability: Boolean) -> Unit,
    onProjectAvailabilityCheckChanges: (subjectId: Long, newAvailability: Boolean) -> Unit,
) {
    val spacing = LocalSpacing.current

    if (subjects.isNotEmpty()) {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(spacing.small),
            modifier = modifier
                .fillMaxSize()
                .padding(horizontal = spacing.small),
            contentPadding = PaddingValues(vertical = spacing.small)
        ) {
            itemsIndexed(subjects, key = { _, subject -> subject.id }) { index, subject ->
                SemesterMarksItem(
                    subject = subject,
                    onMidTermMarksChange = { onMidTermMarksChange(subject.id, it) },
                    onOralMarksChange = { onOralMarksChange(subject.id, it) },
                    onPracticalMarksChange = { onPracticalMarksChange(subject.id, it) },
                    onProjectMarksChange = { onProjectMarksChange(subject.id, it) },
                    onResetClick = { onResetClick(subject.id) },
                    showHint = index == 0,
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
        onResetClick = { },
        onMidtermAvailabilityCheckChanges = { _, _ -> },
        onPracticalAvailabilityCheckChanges = { _, _ -> },
        onOralAvailabilityCheckChanges = { _, _ -> },
        onProjectAvailabilityCheckChanges = { _, _ -> }
    )
}