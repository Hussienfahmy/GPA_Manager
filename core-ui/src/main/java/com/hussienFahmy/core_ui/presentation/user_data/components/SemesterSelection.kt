package com.hussienfahmy.core_ui.presentation.user_data.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hussienfahmy.core.domain.user_data.model.UserData
import com.hussienfahmy.core_ui.LocalSpacing
import com.hussienfahmy.myGpaManager.core.R

@Composable
fun SemesterSelection(
    onSemesterClick: (UserData.AcademicInfo.Semester) -> Unit,
    semester: UserData.AcademicInfo.Semester
) {
    val spacing = LocalSpacing.current

    Card(shape = RoundedCornerShape(spacing.medium)) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .padding(spacing.medium)
        ) {
            Text(
                text = stringResource(id = R.string.semester),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
            )

            Row(horizontalArrangement = Arrangement.spacedBy(2.dp)) {
                val firstSemesterColor =
                    if (semester == UserData.AcademicInfo.Semester.First)
                        MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
                    else Color.Unspecified

                Text(
                    text = stringResource(R.string.first),
                    modifier = Modifier
                        .clip(RoundedCornerShape(spacing.medium))
                        .clickable { onSemesterClick(UserData.AcademicInfo.Semester.First) }
                        .background(firstSemesterColor)
                        .padding(horizontal = spacing.medium, vertical = spacing.small)
                )

                val secondSemesterColor =
                    if (semester == UserData.AcademicInfo.Semester.Second)
                        MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
                    else Color.Unspecified

                Text(
                    text = stringResource(R.string.second),
                    modifier = Modifier
                        .clip(RoundedCornerShape(spacing.medium))
                        .clickable { onSemesterClick(UserData.AcademicInfo.Semester.Second) }
                        .background(secondSemesterColor)
                        .padding(horizontal = spacing.medium, vertical = spacing.small)
                )
            }
        }
    }
}

@Preview
@Composable
fun SemesterSelectionPreview() {
    SemesterSelection(
        onSemesterClick = {},
        semester = UserData.AcademicInfo.Semester.First
    )
}