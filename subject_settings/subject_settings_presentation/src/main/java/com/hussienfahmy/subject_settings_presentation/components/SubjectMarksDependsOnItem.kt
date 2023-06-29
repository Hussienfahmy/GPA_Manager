package com.hussienfahmy.subject_settings_presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.hussienFahmy.core_ui.LocalSpacing
import com.hussienFahmy.myGpaManager.core.R
import com.hussienfahmy.subject_settings_domain.model.SubjectSettings


@Composable
fun SubjectMarksDependsOnItem(
    modifier: Modifier = Modifier,
    currentSubjectsMarksDependsOn: SubjectSettings.SubjectsMarksDependsOn,
    onNewSubjectsMarksDependsOnSelected: (SubjectSettings.SubjectsMarksDependsOn) -> Unit
) {
    val spacing = LocalSpacing.current

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(spacing.small),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        Text(
            text = stringResource(id = R.string.subject_marks_depends_on),
            style = MaterialTheme.typography.bodyMedium
        )

        Row {
            TextButton(
                onClick = { onNewSubjectsMarksDependsOnSelected(SubjectSettings.SubjectsMarksDependsOn.CREDIT) },
                enabled = currentSubjectsMarksDependsOn != SubjectSettings.SubjectsMarksDependsOn.CREDIT
            ) {
                Text(text = stringResource(id = R.string.credit_hours))
            }

            Spacer(modifier = Modifier.width(spacing.extraSmall))

            TextButton(
                onClick = { onNewSubjectsMarksDependsOnSelected(SubjectSettings.SubjectsMarksDependsOn.CONSTANT) },
                enabled = currentSubjectsMarksDependsOn != SubjectSettings.SubjectsMarksDependsOn.CONSTANT
            ) {
                Text(text = stringResource(id = R.string.constant))
            }
        }
    }
}