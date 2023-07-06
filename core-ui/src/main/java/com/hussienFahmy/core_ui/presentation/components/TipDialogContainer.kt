package com.hussienFahmy.core_ui.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.QuestionMark
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.hussienFahmy.core_ui.LocalSpacing
import com.hussienFahmy.myGpaManager.core.R

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
        AlertDialog(onDismissRequest = { showDialog = false }) {
            Surface(
                modifier = Modifier.clip(RoundedCornerShape(spacing.medium))
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(spacing.medium)
                ) {
                    Text(
                        text = tipText,
                        style = MaterialTheme.typography.bodyLarge,
                    )

                    Spacer(modifier = Modifier.height(spacing.small))

                    Button(onClick = { showDialog = false }) {
                        Text(text = stringResource(R.string.thanks), textAlign = TextAlign.Center)
                    }
                }
            }
        }
    }
}