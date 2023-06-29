package com.hussienfahmy.gpa_system_sittings_presentaion.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.hussienFahmy.myGpaManager.core.R
import com.hussienfahmy.gpa_system_sittings_domain.model.GPA

@Composable
fun GPASystemItem(
    currentGPASystem: GPA.System,
    onGPASystemChanged: (GPA.System) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = stringResource(id = R.string.gpa_system),
            style = MaterialTheme.typography.titleMedium
        )

        Row {
            TextButton(
                onClick = { onGPASystemChanged(GPA.System.FOUR) },
                enabled = currentGPASystem != GPA.System.FOUR
            ) {
                Text(text = stringResource(id = R.string.gpa_system_four))
            }

            TextButton(
                onClick = { onGPASystemChanged(GPA.System.FIVE) },
                enabled = currentGPASystem != GPA.System.FIVE
            ) {
                Text(text = stringResource(id = R.string.gpa_system_five))
            }
        }
    }
}

@Preview
@Composable
fun GPASystemPreview() {
    GPASystemItem(currentGPASystem = GPA.System.FOUR, onGPASystemChanged = {})
}