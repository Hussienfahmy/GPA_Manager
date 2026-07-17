package com.hussienfahmy.grades_setting_presentation.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import org.jetbrains.compose.resources.stringResource
import androidx.compose.ui.text.style.TextDirection
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hussienfahmy.core.generated.resources.*
import com.hussienfahmy.core.data.local.model.GradeName
import com.hussienfahmy.core.util.toTwoDecimals
import com.hussienfahmy.core_ui.presentation.components.meadow.MeadowSwitch
import com.hussienfahmy.core_ui.theme.MeadowTheme
import com.hussienfahmy.grades_setting_domain.model.GradeSetting

/**
 * A grade row (design 3d): symbol tile · pts pill · ≥% pill · active switch.
 * F wears the danger tile. Tapping a value pill opens the numeric edit sheet;
 * the pill takes the focused (accent-border) state while its sheet is open.
 */
@Composable
fun GradeItem(
    modifier: Modifier = Modifier,
    onGradeActiveChange: (Boolean) -> Unit,
    gradeSetting: GradeSetting,
    onSavePoint: (newPoints: String) -> Unit,
    onSavePercentage: (newPercentage: String) -> Unit,
) {
    val colors = MeadowTheme.colors
    val accent = MeadowTheme.accent

    var showEditPointsSheet by remember { mutableStateOf(false) }
    var showEditPercentageSheet by remember { mutableStateOf(false) }

    if (showEditPointsSheet) EditGradeValueSheet(
        symbol = gradeSetting.symbol,
        title = stringResource(Res.string.grade_edit_points_title, gradeSetting.symbol),
        subtitle = stringResource(Res.string.grade_edit_points_subtitle),
        fieldLabel = stringResource(Res.string.grade_edit_points_field),
        value = gradeSetting.points?.toString() ?: "",
        onDismiss = { showEditPointsSheet = false },
        onSaveClick = onSavePoint,
    )

    if (showEditPercentageSheet) EditGradeValueSheet(
        symbol = gradeSetting.symbol,
        title = stringResource(Res.string.grade_edit_percentage_title, gradeSetting.symbol),
        subtitle = stringResource(Res.string.grade_edit_percentage_subtitle),
        fieldLabel = stringResource(Res.string.grade_edit_percentage_field),
        value = gradeSetting.percentage?.toString() ?: "",
        onDismiss = { showEditPercentageSheet = false },
        onSaveClick = onSavePercentage,
    )

    val alpha by animateFloatAsState(
        targetValue = if (gradeSetting.active) 1f else 0.45f,
        label = "gradeRowAlpha",
    )

    val isFail = gradeSetting.name == GradeName.F
    val tileBg = if (isFail) colors.dangerContainer else accent.container
    val tileFg = if (isFail) colors.onDangerContainer else accent.deep

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        modifier = modifier
            .fillMaxWidth()
            .alpha(alpha)
            .padding(horizontal = 16.dp, vertical = 12.dp),
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(40.dp)
                .clip(RoundedCornerShape(14.dp))
                .background(tileBg),
        ) {
            Text(
                text = gradeSetting.symbol,
                style = MaterialTheme.typography.headlineSmall.copy(
                    textDirection = TextDirection.Ltr
                ),
                color = tileFg,
            )
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.weight(1f),
        ) {
            ValuePill(
                text = gradeSetting.points?.let {
                    stringResource(Res.string.grade_pts_pill, it.toFloat().toTwoDecimals().toString())
                } ?: stringResource(Res.string.not_set),
                focused = showEditPointsSheet,
                onClick = { showEditPointsSheet = true },
            )
            ValuePill(
                text = gradeSetting.percentage?.let {
                    stringResource(Res.string.grade_pct_pill, it.toFloat().toTwoDecimals().toString())
                } ?: stringResource(Res.string.not_set),
                focused = showEditPercentageSheet,
                onClick = { showEditPercentageSheet = true },
            )
        }

        MeadowSwitch(
            checked = gradeSetting.active,
            onCheckedChange = { onGradeActiveChange(it) },
        )
    }
}

@Composable
private fun ValuePill(
    text: String,
    focused: Boolean,
    onClick: () -> Unit,
) {
    val colors = MeadowTheme.colors
    val accent = MeadowTheme.accent
    val shape = RoundedCornerShape(999.dp)

    Text(
        text = text,
        style = MaterialTheme.typography.titleSmall.copy(fontSize = 12.sp),
        color = if (focused) accent.deep else colors.inkMuted,
        modifier = Modifier
            .clip(shape)
            .background(if (focused) colors.card else colors.surfaceSunken)
            .then(
                if (focused) Modifier.border(2.dp, accent.accent, shape) else Modifier
            )
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick,
            )
            .padding(horizontal = 12.dp, vertical = 5.dp),
    )
}

@Preview
@Composable
private fun GradeItemPreview() {
    MeadowTheme(darkTheme = false) {
        com.hussienfahmy.core_ui.theme.MeadowAccentProvider(MeadowTheme.colors.marks) {
            var grade by remember {
                mutableStateOf(GradeSetting(GradeName.BPlus, 84.0, 3.30, true))
            }
            GradeItem(
                gradeSetting = grade,
                onGradeActiveChange = { grade = grade.copy(active = it) },
                onSavePoint = {},
                onSavePercentage = {},
            )
        }
    }
}
