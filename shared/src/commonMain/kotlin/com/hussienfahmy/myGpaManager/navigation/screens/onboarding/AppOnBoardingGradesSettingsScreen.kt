package com.hussienfahmy.myGpaManager.navigation.screens.onboarding

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import org.jetbrains.compose.resources.stringResource
import com.hussienfahmy.core.generated.resources.*
import com.hussienfahmy.core_ui.presentation.components.OnboardingConstants
import com.hussienfahmy.core_ui.presentation.components.OnboardingLayout
import com.hussienfahmy.grades_setting_presentation.GradeSettingsScreen

@Composable
fun AppOnBoardingGradesSettingsScreen(
    onNextClick: () -> Unit,
    snackBarHostState: SnackbarHostState,
    onBackClick: (() -> Unit)?
) {
    OnboardingLayout(
        title = stringResource(Res.string.onboarding_grades_title),
        subtitle = stringResource(Res.string.onboarding_grades_subtitle),
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