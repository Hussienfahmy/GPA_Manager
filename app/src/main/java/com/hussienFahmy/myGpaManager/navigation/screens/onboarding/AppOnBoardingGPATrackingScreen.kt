package com.hussienfahmy.myGpaManager.navigation.screens.onboarding

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.hussienfahmy.core.R
import com.hussienfahmy.core.domain.analytics.AnalyticsLogger
import com.hussienfahmy.core_ui.presentation.components.OnboardingConstants
import com.hussienfahmy.core_ui.presentation.components.OnboardingLayout
import com.hussienfahmy.core_ui.presentation.user_data.UserDataEvent
import com.hussienfahmy.core_ui.presentation.user_data.UserDataState
import com.hussienfahmy.core_ui.presentation.user_data.UserDataViewModel
import com.hussienfahmy.core_ui.presentation.user_data.components.GPATrackingSection
import com.hussienfahmy.core_ui.presentation.util.UiEventHandler
import com.hussienfahmy.myGpaManager.navigation.SlideTransitions
import com.hussienfahmy.myGpaManager.navigation.graphs.OnBoardingNavGraph
import com.ramcosta.composedestinations.annotation.Destination
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.koinInject

@Destination<OnBoardingNavGraph>(style = SlideTransitions::class)
@Composable
fun AppOnBoardingGPATrackingScreen(
    onNextClick: () -> Unit,
    snackBarHostState: SnackbarHostState,
    onBackClick: (() -> Unit)?,
    viewModel: UserDataViewModel = koinViewModel()
) {
    val analyticsLogger: AnalyticsLogger = koinInject()

    UiEventHandler(
        uiEvent = viewModel.uiEvent,
        snackBarHostState = snackBarHostState,
    )

    val state by viewModel.customState.collectAsStateWithLifecycle()

    OnboardingLayout(
        title = stringResource(R.string.onboarding_gpa_tracking_title),
        subtitle = stringResource(R.string.onboarding_gpa_tracking_subtitle),
        currentStep = OnboardingConstants.Steps.GPA_TRACKING,
        onNextClick = {
            analyticsLogger.logProfileSetupCompleted(completionPercentage = 70)
            onNextClick()
        },
        onBackClick = onBackClick,
        onSkipClick = {
            analyticsLogger.logProfileSetupCompleted(completionPercentage = 60)
            onNextClick()
        },
        showSkip = true
    ) {
        when (val s = state) {
            is UserDataState.Loading -> {
                // Loading handled by layout
            }

            is UserDataState.Loaded -> {
                val userData = s.userData

                GPATrackingSection(
                    cumulativeGPA = userData.academicProgress.cumulativeGPA,
                    creditHours = userData.academicProgress.creditHours,
                    onCumulativeGPAChange = { viewModel.onEvent(UserDataEvent.UpdateCumulativeGPA(it)) },
                    onCreditHoursChange = { viewModel.onEvent(UserDataEvent.UpdateCreditHours(it)) }
                )
            }
        }
    }
}