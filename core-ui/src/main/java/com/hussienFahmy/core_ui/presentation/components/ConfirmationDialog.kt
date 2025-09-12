package com.hussienfahmy.core_ui.presentation.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.hussienfahmy.core.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConfirmationDialog(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            OutlinedButton(
                onClick = {
                    onConfirm()
                    onDismiss()
                }) {
                Text(
                    text = stringResource(R.string.yes),
                    color = MaterialTheme.colorScheme.error
                )
            }
        },
        dismissButton = {
            OutlinedButton(
                onClick = {
                    onDismiss()
                }) {
                Text(text = stringResource(id = R.string.cancel))
            }
        },
        text = {
            Text(
                text = stringResource(R.string.are_you_sure),
                style = MaterialTheme.typography.titleLarge
            )
        }
    )
}