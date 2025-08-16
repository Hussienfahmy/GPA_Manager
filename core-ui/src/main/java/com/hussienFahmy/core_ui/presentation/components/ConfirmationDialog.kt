package com.hussienfahmy.core_ui.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import com.hussienfahmy.core_ui.LocalSpacing
import com.hussienfahmy.myGpaManager.core.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConfirmationDialog(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
) {
    val spacing = LocalSpacing.current

    AlertDialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier.clip(RoundedCornerShape(spacing.medium))
        ) {
            Column(
                verticalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(spacing.medium)
            ) {
                Text(
                    text = stringResource(R.string.are_you_sure),
                    style = MaterialTheme.typography.titleLarge
                )

                Spacer(modifier = Modifier.height(spacing.medium))

                Row(modifier = Modifier.fillMaxWidth()) {
                    OutlinedButton(modifier = Modifier.weight(1f),
                        onClick = {
                            onDismiss()
                        }) {
                        Text(text = stringResource(id = R.string.cancel))
                    }

                    Spacer(modifier = Modifier.width(spacing.medium))

                    OutlinedButton(modifier = Modifier.weight(1f),
                        onClick = {
                            onConfirm()
                            onDismiss()
                        }) {
                        Text(
                            text = stringResource(R.string.yes),
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }
            }
        }
    }
}