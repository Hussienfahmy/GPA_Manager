package com.hussienfahmy.myGpaManager.navigation.screens.onboarding

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.hussienfahmy.core.R
import com.hussienfahmy.core_ui.presentation.components.OnboardingConstants
import com.hussienfahmy.core_ui.presentation.components.OnboardingLayout
import com.hussienfahmy.grades_setting_presentation.GradeSettingsScreen
import com.hussienfahmy.myGpaManager.navigation.SlideTransitions
import com.hussienfahmy.myGpaManager.navigation.graphs.OnBoardingNavGraph
import com.ramcosta.composedestinations.annotation.Destination

@Destination<OnBoardingNavGraph>(style = SlideTransitions::class)
@Composable
fun AppOnBoardingGradesSettingsScreen(
    onNextClick: () -> Unit,
    snackBarHostState: SnackbarHostState,
    onBackClick: (() -> Unit)?
) {
    OnboardingLayout(
        title = stringResource(R.string.onboarding_grades_title),
        subtitle = stringResource(R.string.onboarding_grades_subtitle),
        currentStep = OnboardingConstants.Steps.GRADES_SETTINGS,
        onNextClick = onNextClick,
        onBackClick = onBackClick,
        onSkipClick = onNextClick,
        showSkip = true,
        enableScrolling = false
    ) {
        GradeSettingsScreen(snackBarHostState = snackBarHostState, displayFilterChips = false)
    }
}