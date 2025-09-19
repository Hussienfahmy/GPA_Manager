package com.hussienfahmy.grades_setting_presentation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.hussienfahmy.core.R
import com.hussienfahmy.core.data.local.model.GradeName
import com.hussienfahmy.core_ui.LocalSpacing
import com.hussienfahmy.core_ui.presentation.util.UiEventHandler
import com.hussienfahmy.grades_setting_domain.model.GradeSetting
import com.hussienfahmy.grades_setting_presentation.components.GradeItem
import com.hussienfahmy.grades_setting_presentation.model.Mode
import org.koin.androidx.compose.koinViewModel


@Composable
fun GradeSettingsScreen(
    modifier: Modifier = Modifier,
    displayFilterChips: Boolean = true,
    viewModel: GradeSettingsViewModel = koinViewModel(),
    snackBarHostState: SnackbarHostState,
) {
    val spacing = LocalSpacing.current

    UiEventHandler(
        uiEvent = viewModel.uiEvent,
        snackBarHostState = snackBarHostState,
    )

    val state by viewModel.state

    Column(
        modifier = modifier.padding(horizontal = spacing.small)
    ) {
        Spacer(modifier = Modifier.height(spacing.small))

        if (displayFilterChips) {
            FilterChipsRow(
                state = state,
                onModeChange = viewModel::onModeChange
            )
        }

        Spacer(modifier = Modifier.padding(spacing.small))

        if (state.isLoading) {
            Box(modifier = Modifier.fillMaxSize()) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
        } else {
            GradeSettingsScreenContent(
                modifier = modifier,
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

@Composable
private fun FilterChipsRow(
    state: GradeSettingsState,
    onModeChange: (Mode) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {
        val isOnlyActiveSelected = state.mode == Mode.ONLY_ACTIVE
        FilterChip(
            selected = isOnlyActiveSelected,
            onClick = { onModeChange(Mode.ONLY_ACTIVE) },
            label = {
                Text(
                    text = stringResource(R.string.active_grades_only),
                    style = MaterialTheme.typography.titleMedium,
                    textAlign = TextAlign.Center,
                )
            },
            leadingIcon = if (isOnlyActiveSelected) {
                {
                    Icon(
                        imageVector = Icons.Filled.Done,
                        contentDescription = "Done icon",
                        modifier = Modifier.size(FilterChipDefaults.IconSize)
                    )
                }
            } else {
                null
            }
        )

        val isAllSelected = state.mode == Mode.ALL
        FilterChip(
            selected = isAllSelected,
            onClick = { onModeChange(Mode.ALL) },
            label = {
                Text(
                    text = stringResource(R.string.all_grades),
                    style = MaterialTheme.typography.titleMedium,
                    textAlign = TextAlign.Center,
                )
            },
            leadingIcon = if (isAllSelected) {
                {
                    Icon(
                        imageVector = Icons.Filled.Done,
                        contentDescription = "Done icon",
                        modifier = Modifier.size(FilterChipDefaults.IconSize)
                    )
                }
            } else {
                null
            }
        )
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
    val spacing = LocalSpacing.current

    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(bottom = spacing.small),
        verticalArrangement = Arrangement.spacedBy(spacing.small),
    ) {
        items(state.gradesSetting, key = { it.id }) { grade ->
            AnimatedVisibility(
                visible = grade.active || state.mode == Mode.ALL,
                enter = fadeIn() + expandVertically(),
                exit = shrinkVertically() + fadeOut()
            ) {
                GradeItem(
                    gradeSetting = grade,
                    onGradeActiveChange = { onGradeActiveChange(grade, it) },
                    onSavePoint = { onSavePoint(grade, it) },
                    onSavePercentage = { onSavePercentage(grade, it) }
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GradeSettingsScreenContentPreview() {
    GradeSettingsScreenContent(
        modifier = Modifier.fillMaxSize(),
        state = GradeSettingsState(
            listOf(
                GradeSetting(
                    name = GradeName.A,
                    points = 3.67,
                    percentage = 85.0,
                    active = true
                ),
                GradeSetting(
                    name = GradeName.B,
                    points = 3.0,
                    percentage = 75.0,
                    active = false
                ),
            ),
            isLoading = false
        ),
        onGradeActiveChange = { _, _ -> },
        onSavePoint = { _, _ -> },
        onSavePercentage = { _, _ -> }
    )
}