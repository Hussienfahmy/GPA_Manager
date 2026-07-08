package com.hussienfahmy.core_ui.presentation.user_data.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hussienfahmy.core.R
import com.hussienfahmy.core.domain.user_data.model.UserData
import com.hussienfahmy.core_ui.presentation.components.meadow.SegmentedToggle
import com.hussienfahmy.core_ui.theme.MeadowTheme

@Composable
fun SemesterSelection(
    onSemesterClick: (UserData.AcademicInfo.Semester) -> Unit,
    semester: UserData.AcademicInfo.Semester
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
    ) {
        Text(
            text = stringResource(id = R.string.semester),
            style = MaterialTheme.typography.titleMedium,
            color = MeadowTheme.accent.deep,
        )

        SegmentedToggle(
            options = listOf(
                stringResource(R.string.first),
                stringResource(R.string.second),
            ),
            selectedIndex = if (semester == UserData.AcademicInfo.Semester.First) 0 else 1,
            onSelect = { index ->
                onSemesterClick(
                    if (index == 0) UserData.AcademicInfo.Semester.First
                    else UserData.AcademicInfo.Semester.Second
                )
            },
        )
    }
}

@Preview
@Composable
private fun SemesterSelectionPreview() {
    MeadowTheme(darkTheme = false) {
        SemesterSelection(
            onSemesterClick = {},
            semester = UserData.AcademicInfo.Semester.First
        )
    }
}
