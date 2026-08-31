package com.hussienfahmy.core_ui.presentation.user_data

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.hussienfahmy.core_ui.presentation.user_data.components.UserDataScreenContent
import com.hussienfahmy.core_ui.presentation.util.UiEventHandler
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun UserDataScreen(
    modifier: Modifier = Modifier,
    viewModel: UserDataViewModel = koinViewModel(),
    snackBarHostState: SnackbarHostState,
) {
    UiEventHandler(
        uiEvent = viewModel.uiEvent,
        snackBarHostState = snackBarHostState,
    )

    val pickImage = rememberImagePickerLauncher(
        onImagePicked = { viewModel.onEvent(UserDataEvent.UploadPhoto(it)) },
    )

    val state by viewModel.customState.collectAsStateWithLifecycle()

    Crossfade(targetState = state is UserDataState.Loading, label = "userDataLoading") { loading ->
        if (loading) {
            Box(modifier = modifier) {
                CircularProgressIndicator(Modifier.align(Alignment.Center))
            }
        } else {
            UserDataScreenContent(
                modifier = modifier,
                state = (state as UserDataState.Loaded),
                uploadingPhoto = viewModel.uploadingPhoto,
                onChangePhotoClick = pickImage,
                onUpdateName = { viewModel.onEvent(UserDataEvent.UpdateName(it)) },
                onUpdateCumulativeGPA = { viewModel.onEvent(UserDataEvent.UpdateCumulativeGPA(it)) },
                onUpdateCreditHours = { viewModel.onEvent(UserDataEvent.UpdateCreditHours(it)) },
                onUpdateUniversity = { viewModel.onEvent(UserDataEvent.UpdateUniversity(it)) },
                onUpdateFaculty = { viewModel.onEvent(UserDataEvent.UpdateFaculty(it)) },
                onUpdateDepartment = { viewModel.onEvent(UserDataEvent.UpdateDepartment(it)) },
                onUpdateLevel = { viewModel.onEvent(UserDataEvent.UpdateLevel(it)) },
                onUpdateSemester = { viewModel.onEvent(UserDataEvent.UpdateSemester(it)) },
                enablePhotoEditing = true,
            )
        }
    }
}
