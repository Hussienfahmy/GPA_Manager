package com.hussienfahmy.core_ui.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.QuestionMark
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.hussienfahmy.core.R
import com.hussienfahmy.core_ui.LocalSpacing

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TipDialogContainer(
    modifier: Modifier = Modifier,
    tipText: String,
    content: @Composable () -> Unit,
) {
    val spacing = LocalSpacing.current

    var showDialog by remember { mutableStateOf(false) }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(spacing.small),
        modifier = modifier
    ) {
        content()

        Icon(
            imageVector = Icons.Outlined.QuestionMark,
            contentDescription = stringResource(id = R.string.more_info),
            tint = MaterialTheme.colorScheme.onPrimary,
            modifier = Modifier
                .scale(0.7f)
                .clickable { showDialog = true }
                .background(MaterialTheme.colorScheme.primary, CircleShape)
        )
    }
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            confirmButton = {
                Button(onClick = { showDialog = false }) {
                    Text(text = stringResource(R.string.thanks), textAlign = TextAlign.Center)
                }
            },
            text = {
                Text(
                    text = tipText,
                    style = MaterialTheme.typography.bodyLarge,
                )

            }
        )
    }
}