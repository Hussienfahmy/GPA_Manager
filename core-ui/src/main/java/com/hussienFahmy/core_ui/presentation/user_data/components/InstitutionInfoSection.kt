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
import com.hussienfahmy.core.R
import com.hussienfahmy.core_ui.LocalSpacing
import com.hussienfahmy.core_ui.presentation.components.ExpandableTextField

@Composable
fun InstitutionInfoSection(
    university: String,
    faculty: String,
    department: String,
    onUniversityChange: (String) -> Unit,
    onFacultyChange: (String) -> Unit,
    onDepartmentChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val spacing = LocalSpacing.current

    Card(modifier = modifier) {
        Column(
            modifier = Modifier.padding(spacing.medium),
            verticalArrangement = Arrangement.spacedBy(spacing.medium)
        ) {
            Text(
                text = stringResource(R.string.onboarding_institution_info_title),
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.fillMaxWidth()
            )

            Text(
                text = stringResource(R.string.onboarding_institution_info_subtitle),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.fillMaxWidth()
            )

            ExpandableTextField(
                title = stringResource(R.string.university),
                value = university,
                onNewValueSubmitted = onUniversityChange
            )

            ExpandableTextField(
                title = stringResource(R.string.faculty),
                value = faculty,
                onNewValueSubmitted = onFacultyChange
            )

            ExpandableTextField(
                title = stringResource(R.string.department),
                value = department,
                onNewValueSubmitted = onDepartmentChange
            )
        }
    }
}