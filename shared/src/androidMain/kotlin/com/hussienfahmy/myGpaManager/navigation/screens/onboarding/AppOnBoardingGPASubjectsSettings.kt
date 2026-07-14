package com.hussienfahmy.myGpaManager.navigation.screens.onboarding

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import org.jetbrains.compose.resources.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.hussienfahmy.core.generated.resources.*
import com.hussienfahmy.core_ui.LocalSpacing
import com.hussienfahmy.core_ui.presentation.components.OnboardingConstants
import com.hussienfahmy.core_ui.presentation.components.OnboardingLayout
import com.hussienfahmy.gpa_system_sittings_presentaion.GPASettingsScreen
import com.hussienfahmy.subject_settings_presentation.SubjectsSettingsScreen

@Composable
fun AppOnBoardingGPASubjectsSettings(
    onStartClick: () -> Unit,
    onBackClick: () -> Unit,
    snackBarHostState: SnackbarHostState
) {
    val spacing = LocalSpacing.current

    OnboardingLayout(
        title = stringResource(Res.string.onboarding_final_title),
        subtitle = stringResource(Res.string.onboarding_final_subtitle),
        currentStep = OnboardingConstants.Steps.FINAL_SETUP,
        onNextClick = onStartClick,
        nextButtonText = stringResource(Res.string.onboarding_start_using_app),
        onSkipClick = onStartClick,
        onBackClick = onBackClick
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(spacing.large)
        ) {
            Text(
                text = stringResource(Res.string.onboarding_gpa_calculation_settings),
                style = MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.Center
            )

            GPASettingsScreen()

            Text(
                text = stringResource(Res.string.onboarding_subject_configuration),
                style = MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.Center
            )

            SubjectsSettingsScreen(
                snackBarHostState = snackBarHostState
            )
        }
    }
}