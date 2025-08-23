package com.hussienfahmy.quick_presentation

import android.content.res.Configuration
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Preview
import com.hussienfahmy.core.domain.user_data.model.UserData
import com.hussienfahmy.core_ui.LocalSpacing
import com.hussienfahmy.core_ui.presentation.util.UiEventHandler
import com.hussienfahmy.quick_domain.model.QuickCalculationRequest
import com.hussienfahmy.quick_presentation.components.InputCard
import com.hussienfahmy.quick_presentation.components.QuickResultCard
import org.koin.androidx.compose.koinViewModel

@Composable
fun QuickScreen(
    modifier: Modifier = Modifier,
    viewModel: QuickViewModel = koinViewModel(),
    snackBarHostState: SnackbarHostState,
) {
    UiEventHandler(uiEvent = viewModel.uiEvent, snackBarHostState = snackBarHostState)

    val state by viewModel.state

    if (state.isLoading) {
        Box(modifier = Modifier.fillMaxSize()) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center)
            )
        }
    } else {
        val configuration = LocalConfiguration.current

        when (configuration.orientation) {
            Configuration.ORIENTATION_LANDSCAPE -> QuickScreenLandscape(
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

            else -> QuickScreenPortrait(
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
private fun QuickScreenPortrait(
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

    Column {
        InputCard(
            modifier = modifier
                .fillMaxWidth()
                .padding(spacing.small)
                .weight(1f),
            academicProgress = academicProgress,
            invalidCumulativeGPAInput = invalidCumulativeGPAInput,
            invalidSemesterGPAInput = invalidSemesterGPAInput,
            invalidTotalHoursInput = invalidTotalHoursInput,
            invalidSemesterHoursInput = invalidSemesterHoursInput,
            invalidCumulativeGPAAboveMax = invalidCumulativeGPAAboveMax,
            invalidSemesterGPAAboveMax = invalidSemesterGPAAboveMax,
            onCalculate = onCalculate
        )

        Spacer(modifier = Modifier.height(spacing.small))

        QuickResultCard(
            modifier = modifier
                .fillMaxWidth()
                .padding(spacing.small)
                .weight(1f),
            cumulativeGPA = cumulativeGPA,
            cumulativeGPAPercentage = cumulativeGPAPercentage,
        )
    }
}

@Composable
private fun QuickScreenLandscape(
    modifier: Modifier = Modifier,
    academicProgress: UserData.AcademicProgress,
    invalidCumulativeGPAInput: Boolean,
    invalidSemesterGPAInput: Boolean,
    invalidTotalHoursInput: Boolean,
    invalidCumulativeGPAAboveMax: Boolean,
    invalidSemesterGPAAboveMax: Boolean,
    invalidSemesterHoursInput: Boolean,
    cumulativeGPA: Float,
    cumulativeGPAPercentage: Float,
    onCalculate: (QuickCalculationRequest) -> Unit,
) {
    val spacing = LocalSpacing.current

    Row {
        QuickResultCard(
            modifier = modifier
                .fillMaxSize()
                .weight(1f),
            cumulativeGPA = cumulativeGPA,
            cumulativeGPAPercentage = cumulativeGPAPercentage,
        )

        Spacer(modifier = Modifier.width(spacing.small))

        InputCard(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f),
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

@Preview
@Composable
fun QuickScreenPortraitPreview() {
    QuickScreenPortrait(
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

@Preview
@Composable
fun QuickScreenLandscapePreview() {
    QuickScreenLandscape(
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