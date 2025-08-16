package com.hussienfahmy.subject_settings_presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.hussienFahmy.core.domain.subject_settings.model.SubjectSettings
import com.hussienFahmy.core_ui.LocalSpacing
import com.hussienFahmy.myGpaManager.core.R


@Composable
fun SubjectMarksDependsOnItem(
    modifier: Modifier = Modifier,
    currentSubjectsMarksDependsOn: SubjectSettings.SubjectsMarksDependsOn,
    onNewSubjectsMarksDependsOnSelected: (SubjectSettings.SubjectsMarksDependsOn) -> Unit
) {
    val spacing = LocalSpacing.current

    FlowRow(
        modifier = modifier
            .fillMaxWidth()
            .padding(spacing.small),
        itemVerticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        Text(
            text = stringResource(id = R.string.subject_marks_depends_on),
            style = MaterialTheme.typography.bodyMedium
        )

        Row {
            val isCreditSelected =
                currentSubjectsMarksDependsOn == SubjectSettings.SubjectsMarksDependsOn.CREDIT
            FilterChip(
                selected = isCreditSelected,
                onClick = { onNewSubjectsMarksDependsOnSelected(SubjectSettings.SubjectsMarksDependsOn.CREDIT) },
                label = { Text(text = stringResource(id = R.string.credit_hours)) },
                leadingIcon = if (isCreditSelected) {
                    {
                        Icon(
                            imageVector = Icons.Filled.Done,
                            contentDescription = "Done icon",
                            modifier = Modifier.size(FilterChipDefaults.IconSize)
                        )
                    }
                } else {
                    null
                }
            )

            Spacer(modifier = Modifier.width(spacing.extraSmall))

            val isConstantSelected =
                currentSubjectsMarksDependsOn == SubjectSettings.SubjectsMarksDependsOn.CONSTANT
            FilterChip(
                selected = isConstantSelected,
                onClick = { onNewSubjectsMarksDependsOnSelected(SubjectSettings.SubjectsMarksDependsOn.CONSTANT) },
                label = { Text(text = stringResource(id = R.string.constant)) },
                leadingIcon = if (isConstantSelected) {
                    {
                        Icon(
                            imageVector = Icons.Filled.Done,
                            contentDescription = "Done icon",
                            modifier = Modifier.size(FilterChipDefaults.IconSize)
                        )
                    }
                } else {
                    null
                }
            )
        }
    }
}