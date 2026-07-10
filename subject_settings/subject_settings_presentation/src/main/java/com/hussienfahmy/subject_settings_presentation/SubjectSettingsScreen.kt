package com.hussienfahmy.subject_settings_presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import com.hussienfahmy.core.R
import com.hussienfahmy.core.domain.subject_settings.model.SubjectSettings
import com.hussienfahmy.core_ui.presentation.util.toStringWithOptionalDecimals
import com.hussienfahmy.core_ui.presentation.components.meadow.CapsLabel
import com.hussienfahmy.core_ui.presentation.components.meadow.MeadowCard
import com.hussienfahmy.core_ui.presentation.components.meadow.MeadowTextField
import com.hussienfahmy.core_ui.presentation.components.meadow.SegmentedToggle
import com.hussienfahmy.core_ui.theme.CapsLabelStyle
import com.hussienfahmy.core_ui.theme.MeadowAccentProvider
import com.hussienfahmy.core_ui.theme.MeadowTheme
import org.koin.androidx.compose.koinViewModel

@Composable
fun SubjectsSettingsScreen(
    modifier: Modifier = Modifier,
    viewModel: SubjectsSettingsViewModel = koinViewModel(),
    snackBarHostState: SnackbarHostState
) {
    UiEventHandlerBridge(viewModel, snackBarHostState)

    val state by viewModel.state

    MeadowAccentProvider(MeadowTheme.colors.history) {
        when (state) {
            SubjectSettingsState.Loading -> Box(modifier = modifier.fillMaxSize()) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }

            is SubjectSettingsState.Success -> SubjectsSettingsScreenContent(
                modifier = modifier.fillMaxSize(),
                subjectsSettings = (state as SubjectSettingsState.Success).subjectSettings,
                onSubjectsDependsOnChanged = {
                    viewModel.onEvent(SubjectSettingsEvent.UpdateSubjectMarksDependsOn(it))
                },
                onMarksPerCreditHourChanged = {
                    viewModel.onEvent(SubjectSettingsEvent.UpdateMarksPerCreditHour(it))
                },
                onConstantMarksChanged = {
                    viewModel.onEvent(SubjectSettingsEvent.UpdateConstantMarks(it))
                },
            )
        }
    }
}

@Composable
private fun UiEventHandlerBridge(
    viewModel: SubjectsSettingsViewModel,
    snackBarHostState: SnackbarHostState,
) {
    com.hussienfahmy.core_ui.presentation.util.UiEventHandler(
        uiEvent = viewModel.uiEvent,
        snackBarHostState = snackBarHostState,
    )
}

@Composable
fun SubjectsSettingsScreenContent(
    modifier: Modifier = Modifier,
    subjectsSettings: SubjectSettings,
    onSubjectsDependsOnChanged: (SubjectSettings.SubjectsMarksDependsOn) -> Unit,
    onMarksPerCreditHourChanged: (String) -> Unit,
    onConstantMarksChanged: (String) -> Unit
) {
    val colors = MeadowTheme.colors

    val dependsOnCredit =
        subjectsSettings.subjectsMarksDependsOn == SubjectSettings.SubjectsMarksDependsOn.CREDIT

    var marksPerCredit by remember(subjectsSettings.marksPerCreditHour) {
        mutableStateOf(subjectsSettings.marksPerCreditHour.toStringWithOptionalDecimals())
    }
    var constantMarks by remember(subjectsSettings.constantMarks) {
        mutableStateOf(subjectsSettings.constantMarks.toStringWithOptionalDecimals())
    }

    val exampleTotal = if (dependsOnCredit) {
        (subjectsSettings.marksPerCreditHour * 3).toStringWithOptionalDecimals()
    } else {
        subjectsSettings.constantMarks.toStringWithOptionalDecimals()
    }

    Column(modifier = modifier.padding(16.dp)) {
        MeadowCard(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(horizontal = 18.dp, vertical = 16.dp),
        ) {
            CapsLabel(text = stringResource(R.string.subject_marks_depend_on))

            Spacer(modifier = Modifier.height(10.dp))

            SegmentedToggle(
                options = listOf(
                    stringResource(R.string.subject_depends_credit_hours),
                    stringResource(R.string.subject_depends_constant),
                ),
                selectedIndex = if (dependsOnCredit) 0 else 1,
                onSelect = { index ->
                    onSubjectsDependsOnChanged(
                        if (index == 0) SubjectSettings.SubjectsMarksDependsOn.CREDIT
                        else SubjectSettings.SubjectsMarksDependsOn.CONSTANT
                    )
                },
                modifier = Modifier.fillMaxWidth(),
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Only the field for the selected mode is shown.
            if (dependsOnCredit) {
                ValueField(
                    label = stringResource(R.string.marks_per_credit_hour),
                    value = marksPerCredit,
                    enabled = true,
                    onValueChange = {
                        marksPerCredit = it
                        onMarksPerCreditHourChanged(it)
                    },
                )
            } else {
                ValueField(
                    label = stringResource(R.string.constant_subject_marks_label),
                    value = constantMarks,
                    enabled = true,
                    onValueChange = {
                        constantMarks = it
                        onConstantMarksChanged(it)
                    },
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = stringResource(R.string.subject_marks_example, exampleTotal),
                style = MaterialTheme.typography.bodySmall,
                color = colors.inkFaint,
            )
        }
    }
}

/**
 * A settings value field: the active one gets the violet outline, the inactive
 * one is a faded sunken tile (design 4f).
 */
@Composable
private fun ValueField(
    label: String,
    value: String,
    enabled: Boolean,
    onValueChange: (String) -> Unit,
) {
    if (enabled) {
        MeadowTextField(
            value = value,
            onValueChange = onValueChange,
            label = label,
            outlined = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth(),
        )
    } else {
        val colors = MeadowTheme.colors
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .alpha(0.5f)
                .clip(RoundedCornerShape(16.dp))
                .background(colors.surfaceSunken)
                .padding(horizontal = 16.dp, vertical = 12.dp),
        ) {
            Text(
                text = label.uppercase(),
                style = CapsLabelStyle.copy(fontSize = 10.5.sp, letterSpacing = 0.06.em),
                color = colors.inkFaint,
                maxLines = 1,
            )
            Text(
                text = value,
                style = MaterialTheme.typography.headlineLarge.copy(
                    fontSize = 18.sp, fontWeight = FontWeight.Black
                ),
                color = colors.inkFaint,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SubjectsSettingsScreenContentPreview() {
    MeadowTheme(darkTheme = false) {
        MeadowAccentProvider(MeadowTheme.colors.history) {
            SubjectsSettingsScreenContent(
                subjectsSettings = SubjectSettings(
                    subjectsMarksDependsOn = SubjectSettings.SubjectsMarksDependsOn.CREDIT,
                    marksPerCreditHour = 50.0,
                    constantMarks = 150.0,
                ),
                onSubjectsDependsOnChanged = {},
                onMarksPerCreditHourChanged = {},
                onConstantMarksChanged = {},
            )
        }
    }
}
