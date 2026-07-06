package com.hussienfahmy.semester_history_presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.hussienfahmy.core.R
import com.hussienfahmy.core_ui.presentation.components.meadow.MeadowBottomSheet
import com.hussienfahmy.core_ui.presentation.components.meadow.PillButton
import com.hussienfahmy.core_ui.presentation.components.meadow.PillButtonStyle
import com.hussienfahmy.core_ui.theme.MeadowRadius
import com.hussienfahmy.core_ui.theme.MeadowTheme

/**
 * "Finish this semester?" — positive confirmation (design 3f/2b): the semester
 * GPA joins history and a fresh semester begins. Never rendered as destructive.
 */
@Composable
fun FinishSemesterSheet(
    currentLevel: Int,
    currentSemesterNum: Int,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
) {
    val colors = MeadowTheme.colors
    val accent = MeadowTheme.accent

    val nextSemesterNum = if (currentSemesterNum == 1) 2 else 1
    val nextLevel = if (currentSemesterNum == 2) currentLevel + 1 else currentLevel
    val archiveLabel = "Year $currentLevel - Semester $currentSemesterNum"

    MeadowBottomSheet(onDismiss = onDismiss) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(40.dp)
                .clip(RoundedCornerShape(MeadowRadius.tile))
                .background(accent.container),
        ) {
            Text(
                text = "✓",
                style = MaterialTheme.typography.headlineLarge,
                color = accent.deep,
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = stringResource(R.string.history_finish_semester_title),
            style = MaterialTheme.typography.headlineMedium,
            color = colors.ink,
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = stringResource(R.string.history_finish_semester_will_be_saved, archiveLabel),
            style = MaterialTheme.typography.labelMedium,
            color = accent.deep,
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = stringResource(
                R.string.history_finish_semester_message,
                nextLevel,
                nextSemesterNum,
            ),
            style = MaterialTheme.typography.bodyMedium,
            color = colors.chipText,
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.End),
        ) {
            PillButton(
                text = stringResource(R.string.cancel),
                onClick = onDismiss,
                style = PillButtonStyle.Text,
                compact = true,
            )
            PillButton(
                text = "${stringResource(R.string.history_finish_semester)} ✓",
                onClick = onConfirm,
                style = PillButtonStyle.Primary,
                compact = true,
            )
        }
    }
}
