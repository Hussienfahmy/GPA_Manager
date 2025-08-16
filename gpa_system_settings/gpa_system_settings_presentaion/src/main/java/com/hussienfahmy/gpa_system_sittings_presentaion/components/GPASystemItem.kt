package com.hussienfahmy.gpa_system_sittings_presentaion.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.tooling.preview.Preview
import com.hussienfahmy.core.domain.gpa_settings.model.GPA
import com.hussienfahmy.core_ui.LocalSpacing
import com.hussienfahmy.myGpaManager.core.R

@Composable
fun GPASystemItem(
    currentGPASystem: GPA.System,
    onGPASystemChanged: (GPA.System) -> Unit
) {
    val spacing = LocalSpacing.current

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = stringResource(id = R.string.gpa_system),
            style = MaterialTheme.typography.titleMedium
        )

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            val isFourSelected = currentGPASystem == GPA.System.FOUR
            FilterChip(
                selected = isFourSelected,
                onClick = { onGPASystemChanged(GPA.System.FOUR) },
                label = { Text(text = stringResource(id = R.string.gpa_system_four)) },
                leadingIcon = if (isFourSelected) {
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

            Spacer(modifier = Modifier.width(spacing.small))

            val isFiveSelected = currentGPASystem == GPA.System.FIVE
            FilterChip(
                selected = isFiveSelected,
                onClick = { onGPASystemChanged(GPA.System.FIVE) },
                label = { Text(text = stringResource(id = R.string.gpa_system_five)) },
                leadingIcon = if (isFiveSelected) {
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

@Preview
@Composable
fun GPASystemPreview() {
    GPASystemItem(currentGPASystem = GPA.System.FOUR, onGPASystemChanged = {})
}