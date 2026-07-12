package com.hussienfahmy.grades_setting_presentation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import org.jetbrains.compose.resources.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hussienfahmy.core.generated.resources.Res
import com.hussienfahmy.core.data.local.model.GradeName
import com.hussienfahmy.core_ui.presentation.components.meadow.MeadowRowDivider
import com.hussienfahmy.core_ui.presentation.components.meadow.MeadowSettingsGroup
import com.hussienfahmy.core_ui.presentation.components.meadow.SegmentedToggle
import com.hussienfahmy.core_ui.presentation.util.UiEventHandler
import com.hussienfahmy.core_ui.theme.MeadowAccentProvider
import com.hussienfahmy.core_ui.theme.MeadowTheme
import com.hussienfahmy.grades_setting_domain.model.GradeSetting
import com.hussienfahmy.grades_setting_presentation.components.GradeItem
import com.hussienfahmy.grades_setting_presentation.model.Mode
import org.koin.compose.viewmodel.koinViewModel


@Composable
fun GradeSettingsScreen(
    modifier: Modifier = Modifier,
    displayFilterChips: Boolean = true,
    viewModel: GradeSettingsViewModel = koinViewModel(),
    snackBarHostState: SnackbarHostState,
) {
    UiEventHandler(
        uiEvent = viewModel.uiEvent,
        snackBarHostState = snackBarHostState,
    )

    val state by viewModel.state

    MeadowAccentProvider(MeadowTheme.colors.marks) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
        ) {
            Spacer(modifier = Modifier.height(12.dp))

            if (displayFilterChips) {
                SegmentedToggle(
                    options = listOf(
                        stringResource(Res.string.active_grades_only),
                        stringResource(Res.string.all_grades),
                    ),
                    selectedIndex = if (state.mode == Mode.ONLY_ACTIVE) 0 else 1,
                    onSelect = { index ->
                        viewModel.onModeChange(if (index == 0) Mode.ONLY_ACTIVE else Mode.ALL)
                    },
                )
                Spacer(modifier = Modifier.height(12.dp))
            }

            if (state.isLoading) {
                Box(modifier = Modifier.fillMaxSize()) {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
            } else {
                GradeSettingsScreenContent(
                    state = state,
                    onGradeActiveChange = { grade, newActive ->
                        viewModel.onEvent(GradeEvent.ActivateGrade(grade, newActive))
                    },
                    onSavePoint = { grade, newPoints ->
                        viewModel.onEvent(GradeEvent.UpdatePoints(grade, newPoints))
                    },
                    onSavePercentage = { grade, newPercentage ->
                        viewModel.onEvent(GradeEvent.UpdatePercentage(grade, newPercentage))
                    }
                )
            }
        }
    }
}

@Composable
private fun GradeSettingsScreenContent(
    modifier: Modifier = Modifier,
    state: GradeSettingsState,
    onGradeActiveChange: (grade: GradeSetting, newActive: Boolean) -> Unit,
    onSavePoint: (grade: GradeSetting, newPoints: String) -> Unit,
    onSavePercentage: (grade: GradeSetting, newPercentage: String) -> Unit,
) {
    val visibleGrades = state.gradesSetting.filter { it.active || state.mode == Mode.ALL }

    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .padding(bottom = 12.dp),
    ) {
        MeadowSettingsGroup {
            state.gradesSetting.forEachIndexed { index, grade ->
                if (index > 0 && grade in visibleGrades) MeadowRowDivider()
                AnimatedVisibility(
                    visible = grade.active || state.mode == Mode.ALL,
                    enter = fadeIn() + expandVertically(),
                    exit = shrinkVertically() + fadeOut(),
                ) {
                    GradeItem(
                        gradeSetting = grade,
                        onGradeActiveChange = { onGradeActiveChange(grade, it) },
                        onSavePoint = { onSavePoint(grade, it) },
                        onSavePercentage = { onSavePercentage(grade, it) },
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun GradeSettingsScreenContentPreview() {
    MeadowTheme(darkTheme = false) {
        MeadowAccentProvider(MeadowTheme.colors.marks) {
            GradeSettingsScreenContent(
                modifier = Modifier.padding(16.dp),
                state = GradeSettingsState(
                    listOf(
                        GradeSetting(name = GradeName.APlus, points = 4.0, percentage = 97.0, active = true),
                        GradeSetting(name = GradeName.A, points = 4.0, percentage = 93.0, active = true),
                        GradeSetting(name = GradeName.BPlus, points = 3.3, percentage = 84.0, active = true),
                        GradeSetting(name = GradeName.DMinus, points = 1.3, percentage = 62.0, active = false),
                        GradeSetting(name = GradeName.F, points = 0.0, percentage = 0.0, active = true),
                    ),
                    isLoading = false,
                ),
                onGradeActiveChange = { _, _ -> },
                onSavePoint = { _, _ -> },
                onSavePercentage = { _, _ -> },
            )
        }
    }
}
