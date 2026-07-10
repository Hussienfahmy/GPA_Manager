package com.hussienfahmy.core_ui.presentation.components.meadow

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.PriorityHigh
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.hussienfahmy.core.R
import com.hussienfahmy.core_ui.theme.MeadowRadius
import com.hussienfahmy.core_ui.theme.MeadowTheme

/**
 * The reference confirmation dialog — all dialogs derive from this.
 * Anatomy: icon tile (danger red / tab hue) · title 17/900 · body 12.5/600 ·
 * right-aligned text-cancel + filled-confirm. Destructive verbs stay literal.
 */
@Composable
fun MeadowConfirmationDialog(
    title: String,
    body: String,
    confirmText: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    cancelText: String = stringResource(R.string.cancel),
    destructive: Boolean = true,
    icon: String = if (destructive) "!" else "✓",
) {
    Dialog(onDismissRequest = onDismiss) {
        MeadowConfirmationDialogContent(
            title = title,
            body = body,
            confirmText = confirmText,
            onConfirm = onConfirm,
            onDismiss = onDismiss,
            cancelText = cancelText,
            destructive = destructive,
            icon = icon,
        )
    }
}

@Composable
fun MeadowConfirmationDialogContent(
    title: String,
    body: String,
    confirmText: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    cancelText: String,
    destructive: Boolean,
    icon: String,
    modifier: Modifier = Modifier,
) {
    val colors = MeadowTheme.colors
    val accent = MeadowTheme.accent

    MeadowCard(
        modifier = modifier.fillMaxWidth(),
        radius = MeadowRadius.hero,
        contentPadding = PaddingValues(start = 22.dp, end = 22.dp, top = 22.dp, bottom = 18.dp),
        border = if (colors.isLight) BorderStroke(1.dp, colors.segmentedBg) else null,
        elevated = true,
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(40.dp)
                .clip(RoundedCornerShape(MeadowRadius.tile))
                .background(if (destructive) colors.dangerContainer else accent.container),
        ) {
            Icon(
                imageVector = if (destructive) Icons.Rounded.PriorityHigh else Icons.Rounded.Check,
                contentDescription = null,
                tint = if (destructive) colors.onDangerContainer else accent.deep,
                modifier = Modifier.size(22.dp),
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = title,
            style = MaterialTheme.typography.headlineMedium,
            color = colors.ink,
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = body,
            style = MaterialTheme.typography.bodyMedium,
            color = colors.chipText,
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.End),
        ) {
            PillButton(
                text = cancelText,
                onClick = onDismiss,
                style = PillButtonStyle.Text,
                compact = true,
            )
            PillButton(
                text = confirmText,
                onClick = {
                    onConfirm()
                    onDismiss()
                },
                style = if (destructive) PillButtonStyle.Danger else PillButtonStyle.Primary,
                compact = true,
            )
        }
    }
}

@Composable
private fun MeadowConfirmationDialogShowcase() {
    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier
            .background(MeadowTheme.colors.paper)
            .padding(12.dp),
    ) {
        MeadowConfirmationDialogContent(
            title = "Delete Machine Learning?",
            body = "The subject and its marks are permanently removed from this semester.",
            confirmText = "Delete",
            onConfirm = {},
            onDismiss = {},
            cancelText = "Cancel",
            destructive = true,
            icon = "!",
        )
        MeadowConfirmationDialogContent(
            title = "Finish this semester?",
            body = "Semester 3.35 GPA joins your history and a fresh semester begins.",
            confirmText = "Finish ✓",
            onConfirm = {},
            onDismiss = {},
            cancelText = "Not yet",
            destructive = false,
            icon = "✓",
        )
    }
}

@Preview(name = "MeadowConfirmationDialog · light")
@Composable
private fun MeadowConfirmationDialogLightPreview() {
    MeadowTheme(darkTheme = false) { MeadowConfirmationDialogShowcase() }
}

@Preview(name = "MeadowConfirmationDialog · dark")
@Composable
private fun MeadowConfirmationDialogDarkPreview() {
    MeadowTheme(darkTheme = true) { MeadowConfirmationDialogShowcase() }
}
