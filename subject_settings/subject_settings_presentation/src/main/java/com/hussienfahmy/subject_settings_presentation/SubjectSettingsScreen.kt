package com.hussienfahmy.subject_settings_presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CreditScore
import androidx.compose.material.icons.outlined.Pin
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import com.hussienfahmy.core.R
import com.hussienfahmy.core.domain.subject_settings.model.SubjectSettings
import com.hussienfahmy.core_ui.LocalSpacing
import com.hussienfahmy.core_ui.presentation.components.ExpandableTextField
import com.hussienfahmy.core_ui.presentation.util.UiEventHandler
import com.hussienfahmy.subject_settings_presentation.components.SubjectMarksDependsOnItem
import org.koin.androidx.compose.koinViewModel

@Composable
fun SubjectsSettingsScreen(
    modifier: Modifier = Modifier,
    viewModel: SubjectsSettingsViewModel = koinViewModel(),
    snackBarHostState: SnackbarHostState
) {
    UiEventHandler(uiEvent = viewModel.uiEvent, snackBarHostState = snackBarHostState)

    val state by viewModel.state

    when (state) {
        SubjectSettingsState.Loading -> Box(modifier = modifier.fillMaxSize()) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        }

        is SubjectSettingsState.Success -> SubjectsSettingsScreenContent(
            modifier = modifier.fillMaxSize(),
            subjectsSettings = (state as SubjectSettingsState.Success).subjectSettings,
            onSubjectsDependsOnChanged = {
                viewModel.onEvent(
                    SubjectSettingsEvent.UpdateSubjectMarksDependsOn(it)
                )
            },
            onMarksPerCreditHourChanged = {
                viewModel.onEvent(
                    SubjectSettingsEvent.UpdateMarksPerCreditHour(it)
                )
            },
            onConstantMarksChanged = { viewModel.onEvent(SubjectSettingsEvent.UpdateConstantMarks(it)) }
        )
    }
}

@Composable
fun SubjectsSettingsScreenContent(
    modifier: Modifier = Modifier,
    subjectsSettings: SubjectSettings,
    onSubjectsDependsOnChanged: (SubjectSettings.SubjectsMarksDependsOn) -> Unit,
    onMarksPerCreditHourChanged: (String) -> Unit,
    onConstantMarksChanged: (String) -> Unit
) {
    val spacing = LocalSpacing.current

    Box(modifier = modifier.padding(vertical = spacing.small)) {
        Card {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(spacing.small)
            ) {
                SubjectMarksDependsOnItem(
                    currentSubjectsMarksDependsOn = subjectsSettings.subjectsMarksDependsOn,
                    onNewSubjectsMarksDependsOnSelected = onSubjectsDependsOnChanged
                )

                ExpandableTextField(
                    title = stringResource(id = R.string.subject_marks_per_credit),
                    value = subjectsSettings.marksPerCreditHour.toString(),
                    onNewValueSubmitted = onMarksPerCreditHourChanged,
                    keyboardType = KeyboardType.Number,
                    enabled = subjectsSettings.subjectsMarksDependsOn == SubjectSettings.SubjectsMarksDependsOn.CREDIT,
                    imageVector = Icons.Outlined.CreditScore,
                )

                ExpandableTextField(
                    title = stringResource(id = R.string.const_subject_marks),
                    value = subjectsSettings.constantMarks.toString(),
                    onNewValueSubmitted = onConstantMarksChanged,
                    keyboardType = KeyboardType.Number,
                    enabled = subjectsSettings.subjectsMarksDependsOn == SubjectSettings.SubjectsMarksDependsOn.CONSTANT,
                    imageVector = Icons.Outlined.Pin,
                )
            }
        }
    }
}