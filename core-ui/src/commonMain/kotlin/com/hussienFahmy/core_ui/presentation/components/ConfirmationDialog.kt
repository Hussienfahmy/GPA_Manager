package com.hussienfahmy.core_ui.presentation.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import org.jetbrains.compose.resources.stringResource
import com.hussienfahmy.core.generated.resources.*

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
                    text = stringResource(Res.string.yes),
                    color = MaterialTheme.colorScheme.error
                )
            }
        },
        dismissButton = {
            OutlinedButton(
                onClick = {
                    onDismiss()
                }) {
                Text(text = stringResource(resource = Res.string.cancel))
            }
        },
        text = {
            Text(
                text = stringResource(Res.string.are_you_sure),
                style = MaterialTheme.typography.titleLarge
            )
        }
    )
}