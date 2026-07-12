package com.hussienfahmy.core_ui.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import org.jetbrains.compose.resources.stringResource
import com.hussienfahmy.core.generated.resources.Res

@Composable
fun AddSubjectsHint(
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.fillMaxSize()
    ) {
        Text(
            text = stringResource(Res.string.no_subjects),
            style = MaterialTheme.typography.titleLarge
        )
        Text(
            text = stringResource(Res.string.add_subjects_hint),
            style = MaterialTheme.typography.bodyLarge
        )
    }
}