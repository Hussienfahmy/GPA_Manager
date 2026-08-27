package com.hussienfahmy.semester_history_presentation.components

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
import com.hussienfahmy.core.domain.user_data.model.UserData
import com.hussienfahmy.core.generated.resources.*
import com.hussienfahmy.core_ui.presentation.components.meadow.MeadowBottomSheet
import com.hussienfahmy.core_ui.presentation.components.meadow.PillButton
import com.hussienfahmy.core_ui.presentation.components.meadow.PillButtonStyle
import com.hussienfahmy.core_ui.theme.MeadowRadius
import com.hussienfahmy.core_ui.theme.MeadowTheme
import com.hussienfahmy.semester_history_domain.use_case.SemesterProgression

/**
 * "Finish this semester?" — positive confirmation (design 3f/2b): the semester
 * GPA joins history and a fresh semester begins. Never rendered as destructive.
 */
@Composable
fun FinishSemesterSheet(
    currentLevel: Int,
    currentSemester: UserData.AcademicInfo.Semester,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
) {
    MeadowBottomSheet(onDismiss = onDismiss) {
        FinishSemesterSheetContent(
            currentLevel = currentLevel,
            currentSemester = currentSemester,
            onConfirm = onConfirm,
            onDismiss = onDismiss,
        )
    }
}

@Composable
fun FinishSemesterSheetContent(
    currentLevel: Int,
    currentSemester: UserData.AcademicInfo.Semester,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = MeadowTheme.colors
    val accent = MeadowTheme.accent

    // "next" only ever resolves to First/Second (see SemesterProgression) - finish-semester never
    // lands on Summer itself, only a user manually setting their profile to it can.
    val (nextSemester, nextLevel) = SemesterProgression.next(currentSemester, currentLevel)
    val nextSemesterNum = if (nextSemester == UserData.AcademicInfo.Semester.First) 1 else 2
    val archiveLabel = SemesterProgression.label(currentLevel, currentSemester)

    Column(modifier = modifier.fillMaxWidth()) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(40.dp)
                .clip(RoundedCornerShape(MeadowRadius.tile))
                .background(accent.container),
        ) {
            Icon(
                imageVector = Icons.Rounded.Check,
                contentDescription = null,
                tint = accent.deep,
                modifier = Modifier.size(22.dp),
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = stringResource(Res.string.history_finish_semester_title),
            style = MaterialTheme.typography.headlineMedium,
            color = colors.ink,
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = stringResource(Res.string.history_finish_semester_will_be_saved, archiveLabel),
            style = MaterialTheme.typography.labelMedium,
            color = accent.deep,
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = stringResource(
                Res.string.history_finish_semester_message,
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
                text = stringResource(Res.string.cancel),
                onClick = onDismiss,
                style = PillButtonStyle.Text,
                compact = true,
            )
            PillButton(
                text = stringResource(Res.string.history_finish_semester),
                onClick = onConfirm,
                style = PillButtonStyle.Primary,
                icon = Icons.Rounded.Check,
                compact = true,
            )
        }
    }
}

@Composable
private fun FinishSemesterSheetShowcase() {
    Column(
        modifier = Modifier
            .background(MeadowTheme.colors.card)
            .padding(start = 20.dp, end = 20.dp, top = 20.dp, bottom = 22.dp),
    ) {
        FinishSemesterSheetContent(
            currentLevel = 2,
            currentSemester = UserData.AcademicInfo.Semester.First,
            onConfirm = {},
            onDismiss = {},
        )
    }
}

@Preview(name = "FinishSemesterSheet · light", showBackground = true)
@Composable
private fun FinishSemesterSheetLightPreview() {
    MeadowTheme(darkTheme = false) { FinishSemesterSheetShowcase() }
}

@Preview(name = "FinishSemesterSheet · dark", showBackground = true)
@Composable
private fun FinishSemesterSheetDarkPreview() {
    MeadowTheme(darkTheme = true) { FinishSemesterSheetShowcase() }
}

@Preview(name = "FinishSemesterSheet · AR", showBackground = true, locale = "ar")
@Composable
private fun FinishSemesterSheetArPreview() {
    MeadowTheme(darkTheme = false) { FinishSemesterSheetShowcase() }
}
