package com.hussienfahmy.core_ui.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.hussienfahmy.core.R

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
            text = stringResource(R.string.no_subjects),
            style = MaterialTheme.typography.titleLarge
        )
        Text(
            text = stringResource(R.string.add_subjects_hint),
            style = MaterialTheme.typography.bodyLarge
        )
    }
}