package com.hussienfahmy.myGpaManager.navigation.screens.onboarding

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.hussienfahmy.core.R
import com.hussienfahmy.core_ui.LocalSpacing
import com.hussienfahmy.core_ui.presentation.components.OnboardingConstants
import com.hussienfahmy.core_ui.presentation.components.OnboardingLayout
import com.hussienfahmy.core_ui.presentation.components.meadow.MeadowChip
import com.hussienfahmy.core_ui.presentation.components.meadow.MeadowChipStyle
import com.hussienfahmy.core_ui.presentation.util.UiEventHandler
import com.hussienfahmy.core_ui.theme.MeadowTheme
import com.hussienfahmy.myGpaManager.navigation.SlideTransitions
import com.hussienfahmy.myGpaManager.navigation.graphs.OnBoardingNavGraph
import com.hussienfahmy.myGpaManager.navigation.screens.onboarding.models.AppOnBoardingGPATrackingEvent
import com.hussienfahmy.myGpaManager.navigation.screens.onboarding.models.AppOnBoardingGPATrackingState
import com.hussienfahmy.semester_history_domain.model.Semester
import com.hussienfahmy.semester_history_presentation.SemesterDetailRoot
import com.hussienfahmy.semester_history_presentation.components.AddPastSemesterSheet
import com.hussienfahmy.semester_history_presentation.components.CumulativeGpaCard
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

    UiEventHandler(
        uiEvent = viewModel.uiEvent,
        snackBarHostState = snackBarHostState,
    )

    AppOnBoardingGPATrackingContent(
        uiState = uiState,
        semesters = semesters,
        cumulativeGPA = cumulative.cumulativeGPA,
        totalCreditHours = cumulative.creditHours,
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
    onEvent: (AppOnBoardingGPATrackingEvent) -> Unit,
    onNextClick: () -> Unit,
    onBackClick: (() -> Unit)?,
) {
    val viewingSemesterId = uiState.viewingSemesterDetailId

    if (viewingSemesterId != null) {
        // Same subject list/add/edit/delete experience as History -> Semester
        // Detail — no separate single-subject flow to keep in sync.
        OnboardingSemesterDetailView(
            semesterId = viewingSemesterId,
            semesterLabel = semesters.firstOrNull { it.id == viewingSemesterId }?.label.orEmpty(),
            onBack = { onEvent(AppOnBoardingGPATrackingEvent.ViewSemesterDetail(null)) },
        )
        return
    }

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
                semestersCount = semesters.size,
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
                    onOpenDetail = if (semester.type == Semester.Type.DETAILED) {
                        { onEvent(AppOnBoardingGPATrackingEvent.ViewSemesterDetail(semester.id)) }
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
}

/**
 * The real Semester Detail screen, embedded in-flow with a minimal back
 * header since onboarding has no bottom nav / system app bar to pop from.
 */
@Composable
private fun OnboardingSemesterDetailView(
    semesterId: Long,
    semesterLabel: String,
    onBack: () -> Unit,
) {
    val colors = MeadowTheme.colors

    BackHandler(onBack = onBack)

    Column(modifier = Modifier.fillMaxSize()) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .background(colors.paper)
                .padding(horizontal = 16.dp, vertical = 12.dp),
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(36.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(colors.card)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = onBack,
                    ),
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                    contentDescription = stringResource(R.string.onboarding_back),
                    tint = colors.ink,
                    modifier = Modifier.size(18.dp),
                )
            }
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                text = semesterLabel,
                style = MaterialTheme.typography.headlineSmall,
                color = colors.ink,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }

        androidx.compose.foundation.layout.Box(modifier = Modifier.weight(1f)) {
            SemesterDetailRoot(semesterId = semesterId)
        }
    }
}

@Composable
private fun OnboardingSemesterItem(
    semester: Semester,
    onDelete: () -> Unit,
    onOpenDetail: (() -> Unit)?,
    modifier: Modifier = Modifier,
) {
    val spacing = LocalSpacing.current

    Card(
        shape = RoundedCornerShape(12.dp),
        modifier = modifier
            .fillMaxWidth()
            .then(
                if (onOpenDetail != null) Modifier.clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = onOpenDetail,
                ) else Modifier
            ),
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
                    if (semester.type == Semester.Type.DETAILED) {
                        MeadowChip(
                            text = "${stringResource(R.string.history_type_detailed)} ›",
                            style = MeadowChipStyle.Accent,
                        )
                    } else {
                        MeadowChip(text = stringResource(R.string.history_type_summary))
                    }
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
        onEvent = {},
        onNextClick = {},
        onBackClick = {},
    )
}
