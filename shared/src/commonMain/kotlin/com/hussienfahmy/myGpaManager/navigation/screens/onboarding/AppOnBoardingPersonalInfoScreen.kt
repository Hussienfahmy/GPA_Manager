package com.hussienfahmy.myGpaManager.navigation.screens.onboarding

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import org.jetbrains.compose.resources.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.hussienfahmy.core.generated.resources.*
import com.hussienfahmy.core_ui.presentation.components.OnboardingConstants
import com.hussienfahmy.core_ui.presentation.components.OnboardingLayout
import com.hussienfahmy.core_ui.presentation.user_data.UserDataEvent
import com.hussienfahmy.core_ui.presentation.user_data.UserDataState
import com.hussienfahmy.core_ui.presentation.user_data.UserDataViewModel
import com.hussienfahmy.core_ui.presentation.user_data.components.PersonalInfoSection
import com.hussienfahmy.core_ui.presentation.user_data.rememberImagePickerLauncher
import com.hussienfahmy.core_ui.presentation.util.UiEventHandler
import org.koin.compose.viewmodel.koinViewModel

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

    val pickImage = rememberImagePickerLauncher(
        onImagePicked = { viewModel.onEvent(UserDataEvent.UploadPhoto(it)) },
    )

    val state by viewModel.customState.collectAsStateWithLifecycle()

    OnboardingLayout(
        title = stringResource(Res.string.onboarding_personal_info_title),
        subtitle = stringResource(Res.string.onboarding_personal_info_subtitle),
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
                    onChangePhotoClick = pickImage
                )
            }
        }
    }
}