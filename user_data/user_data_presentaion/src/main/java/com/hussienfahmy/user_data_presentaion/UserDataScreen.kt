package com.hussienfahmy.user_data_presentaion

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.hussienFahmy.core.domain.user_data.model.UserData
import com.hussienFahmy.core_ui.LocalSpacing
import com.hussienFahmy.core_ui.presentation.util.UiEventHandler
import com.hussienFahmy.myGpaManager.core.R
import com.hussienfahmy.user_data_presentaion.components.SemesterSelection
import com.hussienfahmy.user_data_presentaion.components.UserDataItem
import com.hussienfahmy.user_data_presentaion.components.UserPhoto

@Composable
fun UserDataScreen(
    modifier: Modifier = Modifier,
    viewModel: UserDataViewModel = hiltViewModel(),
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

    val state by viewModel.state

    when (state) {
        is UserDataState.Loading -> {
            Box(modifier = Modifier.fillMaxSize()) {
                CircularProgressIndicator(modifier.align(Alignment.Center))
            }
        }

        is UserDataState.Loaded -> {
            UserDataScreenContent(
                modifier = modifier,
                userData = (state as UserDataState.Loaded).userData,
                onChangePhotoClick = { galleryLauncher.launch("image/*") },
                onUpdateName = { viewModel.onEvent(UserDataEvent.UpdateName(it)) },
                onUpdateCumulativeGPA = { viewModel.onEvent(UserDataEvent.UpdateCumulativeGPA(it)) },
                onUpdateCreditHours = { viewModel.onEvent(UserDataEvent.UpdateCreditHours(it)) },
                onUpdateUniversity = { viewModel.onEvent(UserDataEvent.UpdateUniversity(it)) },
                onUpdateFaculty = { viewModel.onEvent(UserDataEvent.UpdateFaculty(it)) },
                onUpdateDepartment = { viewModel.onEvent(UserDataEvent.UpdateDepartment(it)) },
                onUpdateLevel = { viewModel.onEvent(UserDataEvent.UpdateLevel(it)) },
                onUpdateSemester = { viewModel.onEvent(UserDataEvent.UpdateSemester(it)) },
            )
        }
    }
}

@Composable
fun UserDataScreenContent(
    modifier: Modifier,
    userData: UserData,
    onChangePhotoClick: () -> Unit,
    onUpdateName: (String) -> Unit,
    onUpdateCumulativeGPA: (String) -> Unit,
    onUpdateCreditHours: (String) -> Unit,
    onUpdateUniversity: (String) -> Unit,
    onUpdateFaculty: (String) -> Unit,
    onUpdateDepartment: (String) -> Unit,
    onUpdateLevel: (String) -> Unit,
    onUpdateSemester: (UserData.AcademicInfo.Semester) -> Unit,
) {
    val spacing = LocalSpacing.current

    Column(
        verticalArrangement = Arrangement.spacedBy(spacing.small),
        modifier = modifier.scrollable(rememberScrollState(), Orientation.Vertical)
    ) {
        Spacer(modifier = Modifier.height(spacing.extraSmall))

        Card {
            Column {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = spacing.small),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Box(contentAlignment = Alignment.BottomEnd) {
                        UserPhoto(photoUrl = userData.photoUrl, modifier = Modifier.size(120.dp))
                        Icon(
                            imageVector = Icons.Filled.PhotoCamera,
                            contentDescription = stringResource(R.string.change_photo),
                            modifier = Modifier
                                .scale(1.5f)
                                .clickable { onChangePhotoClick() }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(spacing.medium))

                UserDataItem(
                    title = stringResource(R.string.name),
                    value = userData.name,
                    onNewValueSubmitted = { onUpdateName(it) }
                )
            }
        }

        Card {
            Column {
                UserDataItem(
                    title = stringResource(R.string.university),
                    value = userData.academicInfo.university,
                    onNewValueSubmitted = { onUpdateUniversity(it) }
                )

                UserDataItem(
                    title = stringResource(R.string.faculty),
                    value = userData.academicInfo.faculty,
                    onNewValueSubmitted = { onUpdateFaculty(it) }
                )

                UserDataItem(
                    title = stringResource(R.string.department),
                    value = userData.academicInfo.department,
                    onNewValueSubmitted = { onUpdateDepartment(it) }
                )
            }
        }

        Card {
            Column {
                UserDataItem(
                    title = stringResource(R.string.level),
                    value = userData.academicInfo.level.toString(),
                    onNewValueSubmitted = { onUpdateLevel(it) },
                    keyboardType = KeyboardType.Number
                )

                SemesterSelection(
                    semester = userData.academicInfo.semester,
                    onSemesterClick = onUpdateSemester
                )

                UserDataItem(
                    title = stringResource(R.string.cumulative_gpa),
                    value = userData.academicProgress.cumulativeGPA.toString(),
                    onNewValueSubmitted = { onUpdateCumulativeGPA(it) },
                    keyboardType = KeyboardType.Number
                )

                UserDataItem(
                    title = stringResource(R.string.total_hours),
                    value = userData.academicProgress.creditHours.toString(),
                    onNewValueSubmitted = { onUpdateCreditHours(it) },
                    keyboardType = KeyboardType.Number
                )
            }
        }
    }
}
