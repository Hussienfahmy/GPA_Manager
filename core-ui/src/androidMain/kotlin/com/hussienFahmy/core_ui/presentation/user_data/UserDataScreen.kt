package com.hussienfahmy.core_ui.presentation.user_data

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import org.koin.androidx.compose.koinViewModel

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

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri ->
            uri?.let {
                viewModel.onEvent(UserDataEvent.UploadPhoto(it))
            }
        }
    )

    val state by viewModel.customState.collectAsStateWithLifecycle()

    when (state) {
        is UserDataState.Loading -> {
            Box(modifier = Modifier.fillMaxSize()) {
                CircularProgressIndicator(modifier.align(Alignment.Center))
            }
        }

        is UserDataState.Loaded -> {
            UserDataScreenContent(
                modifier = modifier,
                state = (state as UserDataState.Loaded),
                uploadingPhoto = viewModel.uploadingPhoto,
                onChangePhotoClick = { galleryLauncher.launch("image/*") },
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
