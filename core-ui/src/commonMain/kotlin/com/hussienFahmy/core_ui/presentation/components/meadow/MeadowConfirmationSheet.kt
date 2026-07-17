package com.hussienfahmy.core_ui.presentation.components.meadow

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import org.jetbrains.compose.resources.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hussienfahmy.core.generated.resources.*
import com.hussienfahmy.core_ui.theme.MeadowRadius
import com.hussienfahmy.core_ui.theme.MeadowTheme

/**
 * Confirmation as a bottom sheet — same anatomy as the reference dialog
 * (icon tile · title 17/900 · body 12.5/600 · text-cancel + filled-confirm).
 */
@Composable
fun MeadowConfirmationSheet(
    title: String,
    body: String,
    confirmText: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    cancelText: String = stringResource(Res.string.cancel),
    destructive: Boolean = true,
) {
    MeadowBottomSheet(onDismiss = onDismiss) {
        MeadowConfirmationSheetContent(
            title = title,
            body = body,
            confirmText = confirmText,
            onConfirm = onConfirm,
            onDismiss = onDismiss,
            cancelText = cancelText,
            destructive = destructive,
        )
    }
}

@Composable
fun MeadowConfirmationSheetContent(
    title: String,
    body: String,
    confirmText: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    cancelText: String,
    destructive: Boolean,
    modifier: Modifier = Modifier,
) {
    val colors = MeadowTheme.colors
    val accent = MeadowTheme.accent

    Column(modifier = modifier.fillMaxWidth()) {
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
private fun MeadowConfirmationSheetShowcase() {
    Column(
        modifier = Modifier
            .background(MeadowTheme.colors.card)
            .padding(start = 20.dp, end = 20.dp, top = 20.dp, bottom = 22.dp),
    ) {
        MeadowConfirmationSheetContent(
            title = "Reset all grades?",
            body = "This permanently clears every assigned grade in the current semester. Your subjects stay in place.",
            confirmText = "Reset all",
            onConfirm = {},
            onDismiss = {},
            cancelText = "Cancel",
            destructive = true,
        )
    }
}

@Preview(name = "MeadowConfirmationSheet · light")
@Composable
private fun MeadowConfirmationSheetLightPreview() {
    MeadowTheme(darkTheme = false) { MeadowConfirmationSheetShowcase() }
}

@Preview(name = "MeadowConfirmationSheet · dark")
@Composable
private fun MeadowConfirmationSheetDarkPreview() {
    MeadowTheme(darkTheme = true) { MeadowConfirmationSheetShowcase() }
}
