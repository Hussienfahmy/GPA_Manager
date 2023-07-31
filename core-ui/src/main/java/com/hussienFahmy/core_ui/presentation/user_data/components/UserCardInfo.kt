package com.hussienFahmy.core_ui.presentation.user_data.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.hussienFahmy.core.domain.user_data.model.UserData
import com.hussienFahmy.core_ui.LocalSpacing
import com.hussienFahmy.myGpaManager.core.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserInfoCard(onCardClick: () -> Unit, userData: UserData) {
    val spacing = LocalSpacing.current

    Card(shape = RoundedCornerShape(spacing.small), onClick = onCardClick) {
        ConstraintLayout(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
        ) {
            val (editIcon, personalDataRow, academicInfoColumn, academicProgressColumn) = createRefs()

            Icon(
                imageVector = Icons.Outlined.Edit,
                contentDescription = stringResource(id = R.string.edit),
                modifier = Modifier
                    .constrainAs(editIcon) {
                        top.linkTo(parent.top)
                        end.linkTo(parent.end)
                    }
                    .scale(0.7f),
            )

            PersonalDataRow(
                modifier = Modifier.constrainAs(personalDataRow) {
                    top.linkTo(parent.top)
                    linkTo(start = parent.start, end = academicInfoColumn.start, bias = 0f)
                },
                userData = userData,
            )

            AcademicInfoColumn(
                modifier = Modifier.constrainAs(academicInfoColumn) {
                    top.linkTo(editIcon.bottom, spacing.extraSmall)
                    bottom.linkTo(parent.bottom)
                    end.linkTo(parent.end)
                },
                academicInfo = userData.academicInfo
            )

            AcademicProgressColumn(
                modifier = Modifier.constrainAs(academicProgressColumn) {
                    start.linkTo(parent.start)
                    bottom.linkTo(parent.bottom)
                    top.linkTo(personalDataRow.bottom, spacing.small)
                },
                userData = userData
            )
        }
    }
}

@Composable
fun AcademicProgressColumn(
    modifier: Modifier,
    userData: UserData
) {
    Column(modifier = modifier) {
        // Level and semester
        Text(
            text = stringResource(
                id = R.string.level_semester,
                userData.academicInfo.level,
                when (userData.academicInfo.semester) {
                    UserData.AcademicInfo.Semester.First -> stringResource(id = R.string.first)
                    UserData.AcademicInfo.Semester.Second -> stringResource(id = R.string.second)
                }
            )
        )
        // C.GPA
        Text(
            text = stringResource(
                id = R.string.cumulative_gpa_value,
                userData.academicProgress.cumulativeGPA
            )
        )
        // Credit hour
        Text(
            text = stringResource(
                id = R.string.credit_hours_value,
                userData.academicProgress.creditHours
            )
        )
    }
}

@Composable
fun AcademicInfoColumn(
    modifier: Modifier,
    academicInfo: UserData.AcademicInfo,
) {
    Column(modifier = modifier) {
        Text(text = academicInfo.university)
        Text(text = academicInfo.faculty)
        Text(text = academicInfo.department)
    }
}

@Composable
fun PersonalDataRow(
    userData: UserData,
    modifier: Modifier,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // the image
        UserPhoto(photoUrl = userData.photoUrl, modifier = Modifier.size(60.dp))

        Spacer(modifier = Modifier.width(5.dp))

        // name and email
        Column {
            Text(text = userData.name)
            Text(text = userData.email, style = MaterialTheme.typography.bodySmall)
        }
    }
}

@Preview
@Composable
fun UserInfoCardPreview() {
    UserInfoCard(
        onCardClick = { }, userData = UserData(
            name = "Hussien Fahmy",
            email = "contact@h-fahmy.com",
            photoUrl = "",
            academicInfo = UserData.AcademicInfo(
                university = "Ain Shams University",
                faculty = "Faculty of Science",
                department = "Geology",
                level = 4,
                semester = UserData.AcademicInfo.Semester.First
            ),
            academicProgress = UserData.AcademicProgress(
                cumulativeGPA = 3.5,
                creditHours = 120
            ),
            id = ""
        )
    )
}