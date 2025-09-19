package com.hussienfahmy.myGpaManager.navigation.screens.onboarding

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import com.hussienfahmy.core_ui.presentation.user_data.components.PersonalInfoSection
import com.hussienfahmy.core_ui.presentation.util.UiEventHandler
import com.hussienfahmy.myGpaManager.navigation.SlideTransitions
import com.hussienfahmy.myGpaManager.navigation.graphs.OnBoardingNavGraph
import com.ramcosta.composedestinations.annotation.Destination
import org.koin.androidx.compose.koinViewModel

@Destination<OnBoardingNavGraph>(style = SlideTransitions::class)
@Composable
fun AppOnBoardingPersonalInfoScreen(
    onNextClick: () -> Unit,
    snackBarHostState: SnackbarHostState,
    viewModel: UserDataViewModel = koinViewModel()
) {
    UiEventHandler(
        uiEvent = viewModel.uiEvent,
        snackBarHostState = snackBarHostState,
    )

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri ->
            uri?.let {
                viewModel.onEvent(UserDataEvent.UploadPhoto(it))
            }
        }
    )

    val state by viewModel.customState.collectAsStateWithLifecycle()

    OnboardingLayout(
        title = stringResource(R.string.onboarding_personal_info_title),
        subtitle = stringResource(R.string.onboarding_personal_info_subtitle),
        currentStep = OnboardingConstants.Steps.PERSONAL_INFO,
        onNextClick = onNextClick,
        onSkipClick = onNextClick,
        showSkip = true,
        nextButtonEnabled = true
    ) {
        when (val s = state) {
            is UserDataState.Loading -> {
                // Loading handled by layout
            }

            is UserDataState.Loaded -> {
                val userData = s.userData

                PersonalInfoSection(
                    name = userData.name,
                    photoUrl = userData.photoUrl,
                    uploadingPhoto = viewModel.uploadingPhoto,
                    onNameChange = { viewModel.onEvent(UserDataEvent.UpdateName(it)) },
                    onChangePhotoClick = { galleryLauncher.launch("image/*") }
                )
            }
        }
    }
}