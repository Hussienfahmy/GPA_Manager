package com.hussienfahmy.core_ui.presentation.user_data.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.hussienfahmy.core.R
import com.hussienfahmy.core_ui.presentation.components.ExpandableTextField
import com.hussienfahmy.core_ui.presentation.components.meadow.CapsLabel
import com.hussienfahmy.core_ui.presentation.components.meadow.MeadowCard
import com.hussienfahmy.core_ui.presentation.components.meadow.MeadowRowDivider
import com.hussienfahmy.core_ui.theme.MeadowTheme

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
    MeadowCard(
        modifier = modifier,
        contentPadding = PaddingValues(vertical = 16.dp),
    ) {
        Column(modifier = Modifier.padding(horizontal = 18.dp)) {
            CapsLabel(text = stringResource(R.string.onboarding_institution_info_title))
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = stringResource(R.string.onboarding_institution_info_subtitle),
                style = MaterialTheme.typography.bodySmall,
                color = MeadowTheme.colors.inkFaint,
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        ExpandableTextField(
            title = stringResource(R.string.university),
            value = university,
            onNewValueSubmitted = onUniversityChange,
        )
        MeadowRowDivider()
        ExpandableTextField(
            title = stringResource(R.string.faculty),
            value = faculty,
            onNewValueSubmitted = onFacultyChange,
        )
        MeadowRowDivider()
        ExpandableTextField(
            title = stringResource(R.string.department),
            value = department,
            onNewValueSubmitted = onDepartmentChange,
        )
    }
}
