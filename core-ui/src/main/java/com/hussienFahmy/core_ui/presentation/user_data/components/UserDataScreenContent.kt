package com.hussienfahmy.core_ui.presentation.user_data.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.jetbrains.compose.resources.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.hussienfahmy.core.generated.resources.Res
import com.hussienfahmy.core.domain.user_data.model.UserData
import com.hussienfahmy.core_ui.presentation.components.ExpandableTextField
import com.hussienfahmy.core_ui.presentation.components.meadow.CapsLabel
import com.hussienfahmy.core_ui.presentation.components.meadow.MeadowCard
import com.hussienfahmy.core_ui.presentation.components.meadow.MeadowRowDivider
import com.hussienfahmy.core_ui.presentation.user_data.UserDataState
import com.hussienfahmy.core_ui.theme.MeadowAccentProvider
import com.hussienfahmy.core_ui.theme.MeadowTheme
import kotlin.math.floor

@Composable
fun UserDataScreenContent(
    modifier: Modifier,
    state: UserDataState.Loaded,
    uploadingPhoto: Boolean,
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
    val colors = MeadowTheme.colors
    val userData = state.userData

    // Profile is reached from the More hub — wear its slate accent.
    MeadowAccentProvider(colors.more) {
        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = modifier
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 14.dp)
        ) {
            PersonalInfoSection(
                name = userData.name,
                photoUrl = userData.photoUrl,
                uploadingPhoto = uploadingPhoto,
                onNameChange = onUpdateName,
                onChangePhotoClick = onChangePhotoClick,
                enablePhotoEditing = enablePhotoEditing,
            )

            InstitutionInfoSection(
                university = userData.academicInfo.university,
                faculty = userData.academicInfo.faculty,
                department = userData.academicInfo.department,
                onUniversityChange = onUpdateUniversity,
                onFacultyChange = onUpdateFaculty,
                onDepartmentChange = onUpdateDepartment,
            )

            AcademicStatusSection(
                level = userData.academicInfo.level,
                semester = userData.academicInfo.semester,
                onLevelChange = onUpdateLevel,
                onSemesterChange = onUpdateSemester,
            )

            // Academic progress — derived, read-only
            MeadowCard(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(vertical = 6.dp),
            ) {
                ExpandableTextField(
                    title = stringResource(Res.string.cumulative_gpa),
                    value = "%.4f".format(
                        floor(userData.academicProgress.cumulativeGPA * 10000) / 10000.0
                    ),
                    onNewValueSubmitted = onUpdateCumulativeGPA,
                    keyboardType = KeyboardType.Number,
                    enabled = false,
                    supportingText = stringResource(Res.string.calculated_from_history),
                )
                MeadowRowDivider()
                ExpandableTextField(
                    title = stringResource(Res.string.total_hours),
                    value = userData.academicProgress.creditHours.toString(),
                    onNewValueSubmitted = onUpdateCreditHours,
                    keyboardType = KeyboardType.Number,
                    enabled = false,
                    supportingText = stringResource(Res.string.calculated_from_history),
                )
            }
        }
    }
}
