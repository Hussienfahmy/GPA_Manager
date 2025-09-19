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
import com.hussienfahmy.core_ui.presentation.user_data.components.InstitutionInfoSection
import com.hussienfahmy.core_ui.presentation.util.UiEventHandler
import com.hussienfahmy.myGpaManager.navigation.SlideTransitions
import com.hussienfahmy.myGpaManager.navigation.graphs.OnBoardingNavGraph
import com.ramcosta.composedestinations.annotation.Destination
import org.koin.androidx.compose.koinViewModel

@Destination<OnBoardingNavGraph>(style = SlideTransitions::class)
@Composable
fun AppOnBoardingInstitutionInfoScreen(
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
        title = stringResource(R.string.onboarding_institution_info_title),
        subtitle = stringResource(R.string.onboarding_institution_info_subtitle),
        currentStep = OnboardingConstants.Steps.INSTITUTION_INFO,
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

                InstitutionInfoSection(
                    university = userData.academicInfo.university,
                    faculty = userData.academicInfo.faculty,
                    department = userData.academicInfo.department,
                    onUniversityChange = { viewModel.onEvent(UserDataEvent.UpdateUniversity(it)) },
                    onFacultyChange = { viewModel.onEvent(UserDataEvent.UpdateFaculty(it)) },
                    onDepartmentChange = { viewModel.onEvent(UserDataEvent.UpdateDepartment(it)) }
                )
            }
        }
    }
}