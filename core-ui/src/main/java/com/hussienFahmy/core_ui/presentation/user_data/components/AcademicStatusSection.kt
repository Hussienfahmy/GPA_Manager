package com.hussienfahmy.core_ui.presentation.user_data.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import com.hussienfahmy.core.R
import com.hussienfahmy.core.domain.user_data.model.UserData
import com.hussienfahmy.core_ui.LocalSpacing
import com.hussienfahmy.core_ui.presentation.components.ExpandableTextField

@Composable
fun AcademicStatusSection(
    level: Int,
    semester: UserData.AcademicInfo.Semester,
    onLevelChange: (String) -> Unit,
    onSemesterChange: (UserData.AcademicInfo.Semester) -> Unit,
    modifier: Modifier = Modifier
) {
    val spacing = LocalSpacing.current

    Card(modifier = modifier) {
        Column(
            modifier = Modifier.padding(spacing.medium),
            verticalArrangement = Arrangement.spacedBy(spacing.medium)
        ) {
            Text(
                text = stringResource(R.string.onboarding_academic_status_title),
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.fillMaxWidth()
            )

            Text(
                text = stringResource(R.string.onboarding_academic_status_subtitle),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.fillMaxWidth()
            )

            ExpandableTextField(
                title = stringResource(R.string.level),
                value = level.toString(),
                onNewValueSubmitted = onLevelChange,
                keyboardType = KeyboardType.Number
            )

            SemesterSelection(
                semester = semester,
                onSemesterClick = onSemesterChange
            )
        }
    }
}