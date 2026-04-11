package com.hussienfahmy.myGpaManager.navigation.screens.onboarding

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.hussienfahmy.core.R
import com.hussienfahmy.core_ui.LocalSpacing
import com.hussienfahmy.core_ui.presentation.components.OnboardingConstants
import com.hussienfahmy.core_ui.presentation.components.OnboardingLayout
import com.hussienfahmy.core_ui.presentation.util.UiEventHandler
import com.hussienfahmy.myGpaManager.navigation.SlideTransitions
import com.hussienfahmy.myGpaManager.navigation.graphs.OnBoardingNavGraph
import com.hussienfahmy.myGpaManager.navigation.screens.onboarding.models.AppOnBoardingGPATrackingEvent
import com.hussienfahmy.myGpaManager.navigation.screens.onboarding.models.AppOnBoardingGPATrackingState
import com.hussienfahmy.semester_history_domain.model.Semester
import com.hussienfahmy.semester_history_presentation.components.AddPastSemesterSheet
import com.hussienfahmy.semester_history_presentation.components.AddSubjectSheet
import com.hussienfahmy.semester_history_presentation.components.CumulativeGpaCard
import com.hussienfahmy.semester_history_presentation.components.SemesterTypeBadge
import com.ramcosta.composedestinations.annotation.Destination
import org.koin.androidx.compose.koinViewModel

@Destination<OnBoardingNavGraph>(style = SlideTransitions::class)
@Composable
fun AppOnBoardingGPATrackingScreen(
    onNextClick: () -> Unit,
    snackBarHostState: SnackbarHostState,
    onBackClick: (() -> Unit)?,
) {
    val viewModel: AppOnBoardingGPATrackingViewModel = koinViewModel()
    val uiState = viewModel.state.value
    val semesters by viewModel.semesters.collectAsStateWithLifecycle()
    val cumulative by viewModel.cumulative.collectAsStateWithLifecycle()
    val grades by viewModel.grades.collectAsStateWithLifecycle()

    UiEventHandler(
        uiEvent = viewModel.uiEvent,
        snackBarHostState = snackBarHostState,
    )

    AppOnBoardingGPATrackingContent(
        uiState = uiState,
        semesters = semesters,
        cumulativeGPA = cumulative.cumulativeGPA,
        totalCreditHours = cumulative.creditHours,
        grades = grades,
        onEvent = viewModel::onEvent,
        onNextClick = onNextClick,
        onBackClick = onBackClick,
    )
}

@Composable
internal fun AppOnBoardingGPATrackingContent(
    uiState: AppOnBoardingGPATrackingState,
    semesters: List<Semester>,
    cumulativeGPA: Double,
    totalCreditHours: Int,
    grades: List<com.hussienfahmy.core.data.local.entity.Grade>,
    onEvent: (AppOnBoardingGPATrackingEvent) -> Unit,
    onNextClick: () -> Unit,
    onBackClick: (() -> Unit)?,
) {
    val spacing = LocalSpacing.current

    OnboardingLayout(
        title = stringResource(R.string.onboarding_gpa_tracking_title),
        subtitle = stringResource(R.string.onboarding_gpa_tracking_subtitle),
        currentStep = OnboardingConstants.Steps.GPA_TRACKING,
        onNextClick = onNextClick,
        onBackClick = onBackClick,
        onSkipClick = onNextClick,
        showSkip = true,
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(spacing.small)) {
            CumulativeGpaCard(
                cumulativeGPA = cumulativeGPA,
                totalCreditHours = totalCreditHours,
            )

            OutlinedButton(
                onClick = { onEvent(AppOnBoardingGPATrackingEvent.ShowAddSheet) },
                modifier = Modifier.fillMaxWidth(),
            ) {
                Icon(Icons.Outlined.Add, contentDescription = null)
                Spacer(modifier = Modifier.width(spacing.small))
                Text(stringResource(R.string.history_add_past_data))
            }

            semesters.forEach { semester ->
                OnboardingSemesterItem(
                    semester = semester,
                    onDelete = { onEvent(AppOnBoardingGPATrackingEvent.DeleteSemesterEvent(semester.id)) },
                    onAddSubjects = if (semester.type == Semester.Type.DETAILED) {
                        { onEvent(AppOnBoardingGPATrackingEvent.SetAddingSubjectsSemester(semester.id)) }
                    } else null,
                )
            }
        }
    }

    if (uiState.showAddSheet) {
        AddPastSemesterSheet(
            onDismiss = { onEvent(AppOnBoardingGPATrackingEvent.HideAddSheet) },
            onAddSummary = { label, gpa, hours, level ->
                onEvent(AppOnBoardingGPATrackingEvent.AddSummarySemester(label, gpa, hours, level))
            },
            onAddDetailed = { label, level ->
                onEvent(AppOnBoardingGPATrackingEvent.AddDetailedSemester(label, level))
            },
        )
    }

    uiState.addingSubjectsToSemesterId?.let { semesterId ->
        AddSubjectSheet(
            availableGrades = grades,
            onDismiss = { onEvent(AppOnBoardingGPATrackingEvent.SetAddingSubjectsSemester(null)) },
            onAdd = { name, creditHours, gradeName ->
                onEvent(
                    AppOnBoardingGPATrackingEvent.AddSubject(
                        semesterId,
                        name,
                        creditHours,
                        gradeName
                    )
                )
            },
        )
    }
}


@Composable
private fun OnboardingSemesterItem(
    semester: Semester,
    onDelete: () -> Unit,
    onAddSubjects: (() -> Unit)?,
    modifier: Modifier = Modifier,
) {
    val spacing = LocalSpacing.current

    Card(
        shape = RoundedCornerShape(12.dp),
        modifier = modifier.fillMaxWidth(),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(spacing.medium),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = semester.label,
                    style = MaterialTheme.typography.titleMedium,
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(spacing.small),
                ) {
                    SemesterTypeBadge(type = semester.type)
                    Text(
                        text = stringResource(R.string.history_gpa_value, semester.semesterGPA),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.primary,
                    )
                    Text(
                        text = stringResource(
                            R.string.history_hours_value,
                            semester.totalCreditHours
                        ),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }
            if (onAddSubjects != null) {
                IconButton(onClick = onAddSubjects) {
                    Icon(
                        Icons.Outlined.Add,
                        contentDescription = stringResource(R.string.history_add_subjects),
                        tint = MaterialTheme.colorScheme.primary,
                    )
                }
            }
            IconButton(onClick = onDelete) {
                Icon(
                    Icons.Outlined.Delete,
                    contentDescription = stringResource(R.string.delete),
                    tint = MaterialTheme.colorScheme.error,
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun AppOnBoardingGPATrackingContentPreview() {
    AppOnBoardingGPATrackingContent(
        uiState = AppOnBoardingGPATrackingState(),
        semesters = listOf(
            Semester(
                id = 1L,
                label = "Year 1 - Semester 1",
                level = 1,
                type = Semester.Type.SUMMARY,
                semesterGPA = 3.5,
                totalCreditHours = 18,
                status = Semester.Status.ARCHIVED,
                order = 0,
                createdAt = 0L,
                archivedAt = null,
            ),
            Semester(
                id = 2L,
                label = "Year 1 - Semester 2",
                level = 1,
                type = Semester.Type.DETAILED,
                semesterGPA = 3.8,
                totalCreditHours = 20,
                status = Semester.Status.ARCHIVED,
                order = 1,
                createdAt = 0L,
                archivedAt = null,
            ),
        ),
        cumulativeGPA = 3.65,
        totalCreditHours = 38,
        grades = emptyList(),
        onEvent = {},
        onNextClick = {},
        onBackClick = {},
    )
}
