package com.hussienfahmy.myGpaManager.navigation.screens.onboarding

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.hussienfahmy.core.R
import com.hussienfahmy.core_ui.presentation.components.OnboardingConstants
import com.hussienfahmy.core_ui.presentation.components.OnboardingLayout
import com.hussienfahmy.core_ui.presentation.user_data.UserDataEvent
import com.hussienfahmy.core_ui.presentation.user_data.UserDataState
import com.hussienfahmy.core_ui.presentation.user_data.UserDataViewModel
import com.hussienfahmy.core_ui.presentation.user_data.components.AcademicStatusSection
import com.hussienfahmy.core_ui.presentation.util.UiEventHandler
import com.hussienfahmy.myGpaManager.navigation.SlideTransitions
import com.hussienfahmy.myGpaManager.navigation.graphs.OnBoardingNavGraph
import com.ramcosta.composedestinations.annotation.Destination
import org.koin.androidx.compose.koinViewModel

@Destination<OnBoardingNavGraph>(style = SlideTransitions::class)
@Composable
fun AppOnBoardingAcademicStatusScreen(
    onNextClick: () -> Unit,
    snackBarHostState: SnackbarHostState,
    onBackClick: (() -> Unit)?,
    viewModel: UserDataViewModel = koinViewModel()
) {
    UiEventHandler(
        uiEvent = viewModel.uiEvent,
        snackBarHostState = snackBarHostState,
    )

    val state by viewModel.customState.collectAsStateWithLifecycle()

    OnboardingLayout(
        title = stringResource(R.string.onboarding_academic_status_title),
        subtitle = stringResource(R.string.onboarding_academic_status_subtitle),
        currentStep = OnboardingConstants.Steps.ACADEMIC_STATUS,
        onNextClick = onNextClick,
        onBackClick = onBackClick,
        onSkipClick = onNextClick,
        showSkip = true
    ) {
        when (val s = state) {
            is UserDataState.Loading -> {
                // Loading handled by layout
            }

            is UserDataState.Loaded -> {
                val userData = s.userData

                AcademicStatusSection(
                    level = userData.academicInfo.level,
                    semester = userData.academicInfo.semester,
                    onLevelChange = { viewModel.onEvent(UserDataEvent.UpdateLevel(it)) },
                    onSemesterChange = { viewModel.onEvent(UserDataEvent.UpdateSemester(it)) }
                )
            }
        }
    }
}