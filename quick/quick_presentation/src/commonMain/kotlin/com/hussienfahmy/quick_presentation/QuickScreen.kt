package com.hussienfahmy.quick_presentation

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.hussienfahmy.core.domain.user_data.model.UserData
import com.hussienfahmy.core_ui.LocalScaffoldContentPadding
import com.hussienfahmy.core_ui.LocalSpacing
import com.hussienfahmy.core_ui.presentation.util.UiEventHandler
import com.hussienfahmy.core_ui.theme.MeadowAccentProvider
import com.hussienfahmy.core_ui.theme.MeadowTheme
import com.hussienfahmy.quick_domain.model.QuickCalculationRequest
import com.hussienfahmy.quick_presentation.components.InputCard
import com.hussienfahmy.quick_presentation.components.QuickResultCard
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun QuickScreen(
    modifier: Modifier = Modifier,
    viewModel: QuickViewModel = koinViewModel(),
    snackBarHostState: SnackbarHostState,
) {
    UiEventHandler(uiEvent = viewModel.uiEvent, snackBarHostState = snackBarHostState)

    DisposableEffect(Unit) {
        onDispose {
            viewModel.onEvent(QuickEvent.OnScreenExit)
        }
    }

    val state by viewModel.state

    Crossfade(targetState = state.isLoading, label = "quickScreenLoading") { loading ->
        if (loading) {
            // Spinner anchored top (not center) so content doesn't jump up on load.
            Box(modifier = Modifier.fillMaxSize()) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .padding(top = 40.dp)
                )
            }
        } else MeadowAccentProvider(MeadowTheme.colors.quick) {
            QuickScreenContent(
                modifier = modifier,
                academicProgress = state.academicProgress,
                invalidCumulativeGPAInput = state.invalidCumulativeGPAInput,
                invalidSemesterGPAInput = state.invalidSemesterGPAInput,
                invalidCumulativeGPAAboveMax = state.invalidCumulativeGPAAboveMax,
                invalidSemesterGPAAboveMax = state.invalidSemesterGPAAboveMax,
                invalidTotalHoursInput = state.invalidTotalHoursInput,
                invalidSemesterHoursInput = state.invalidSemesterHoursInput,
                cumulativeGPA = state.cumulativeGPA,
                cumulativeGPAPercentage = state.cumulativeGPAPercentage,
                onCalculate = { viewModel.onEvent(QuickEvent.Calculate(it)) }
            )
        }
    }
}

@Composable
private fun QuickScreenContent(
    modifier: Modifier = Modifier,
    academicProgress: UserData.AcademicProgress,
    invalidCumulativeGPAInput: Boolean,
    invalidSemesterGPAInput: Boolean,
    invalidCumulativeGPAAboveMax: Boolean,
    invalidSemesterGPAAboveMax: Boolean,
    invalidTotalHoursInput: Boolean,
    invalidSemesterHoursInput: Boolean,
    cumulativeGPA: Float,
    cumulativeGPAPercentage: Float,
    onCalculate: (QuickCalculationRequest) -> Unit,
) {
    val spacing = LocalSpacing.current
    val scaffoldPadding = LocalScaffoldContentPadding.current

    val inputsValid = !invalidCumulativeGPAInput && !invalidSemesterGPAInput &&
            !invalidCumulativeGPAAboveMax && !invalidSemesterGPAAboveMax &&
            !invalidTotalHoursInput && !invalidSemesterHoursInput

    // Result first — the screen reads like an instrument (design 2c).
    // fillMaxSize keeps the same height as the loading state → no vertical jump.
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(spacing.small)
            .padding(bottom = scaffoldPadding.calculateBottomPadding())
    ) {
        QuickResultCard(
            modifier = Modifier.fillMaxWidth(),
            cumulativeGPA = cumulativeGPA,
            cumulativeGPAPercentage = cumulativeGPAPercentage,
            inputsValid = inputsValid,
        )

        Spacer(modifier = Modifier.height(spacing.small))

        InputCard(
            modifier = Modifier.fillMaxWidth(),
            academicProgress = academicProgress,
            invalidCumulativeGPAInput = invalidCumulativeGPAInput,
            invalidSemesterGPAInput = invalidSemesterGPAInput,
            invalidTotalHoursInput = invalidTotalHoursInput,
            invalidSemesterHoursInput = invalidSemesterHoursInput,
            invalidCumulativeGPAAboveMax = invalidCumulativeGPAAboveMax,
            invalidSemesterGPAAboveMax = invalidSemesterGPAAboveMax,
            onCalculate = onCalculate
        )
    }
}

@Preview(showBackground = true)
@Composable
fun QuickScreenContentPreview() {
    QuickScreenContent(
        academicProgress = UserData.AcademicProgress(
            cumulativeGPA = 3.5,
            creditHours = 100,
        ),
        invalidCumulativeGPAInput = true,
        invalidSemesterGPAInput = false,
        invalidTotalHoursInput = false,
        invalidSemesterHoursInput = false,
        invalidCumulativeGPAAboveMax = true,
        invalidSemesterGPAAboveMax = false,
        cumulativeGPA = 3.5f,
        cumulativeGPAPercentage = 87.5f,
        onCalculate = {}
    )
}
