package com.hussienFahmy.core_ui.presentation.user_data.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.hussienFahmy.core.domain.user_data.model.UserData
import com.hussienFahmy.core_ui.LocalSpacing
import com.hussienFahmy.core_ui.presentation.components.ExpandableTextField
import com.hussienFahmy.myGpaManager.core.R

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
    enablePhotoEditing: Boolean = true,
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
                        if (enablePhotoEditing) {
                            Icon(
                                imageVector = Icons.Filled.PhotoCamera,
                                contentDescription = stringResource(R.string.change_photo),
                                modifier = Modifier
                                    .scale(1.5f)
                                    .clickable { onChangePhotoClick() }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(spacing.medium))

                ExpandableTextField(
                    title = stringResource(R.string.name),
                    value = userData.name,
                    onNewValueSubmitted = { onUpdateName(it) }
                )
            }
        }

        Card {
            Column {
                ExpandableTextField(
                    title = stringResource(R.string.university),
                    value = userData.academicInfo.university,
                    onNewValueSubmitted = { onUpdateUniversity(it) }
                )

                ExpandableTextField(
                    title = stringResource(R.string.faculty),
                    value = userData.academicInfo.faculty,
                    onNewValueSubmitted = { onUpdateFaculty(it) }
                )

                ExpandableTextField(
                    title = stringResource(R.string.department),
                    value = userData.academicInfo.department,
                    onNewValueSubmitted = { onUpdateDepartment(it) }
                )
            }
        }

        Card {
            Column {
                ExpandableTextField(
                    title = stringResource(R.string.level),
                    value = userData.academicInfo.level.toString(),
                    onNewValueSubmitted = { onUpdateLevel(it) },
                    keyboardType = KeyboardType.Number
                )

                SemesterSelection(
                    semester = userData.academicInfo.semester,
                    onSemesterClick = onUpdateSemester
                )

                ExpandableTextField(
                    title = stringResource(R.string.cumulative_gpa),
                    value = userData.academicProgress.cumulativeGPA.toString(),
                    onNewValueSubmitted = { onUpdateCumulativeGPA(it) },
                    keyboardType = KeyboardType.Number
                )

                ExpandableTextField(
                    title = stringResource(R.string.total_hours),
                    value = userData.academicProgress.creditHours.toString(),
                    onNewValueSubmitted = { onUpdateCreditHours(it) },
                    keyboardType = KeyboardType.Number
                )
            }
        }
    }
}