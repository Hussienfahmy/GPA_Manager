package com.hussienFahmy.grades_setting_presentation.components

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.hussienFahmy.core.data.local.model.GradeName
import com.hussienFahmy.core_ui.LocalSpacing
import com.hussienFahmy.grades_setting_domain.model.GradeSetting
import com.hussienFahmy.myGpaManager.core.R

@Composable
fun GradeItem(
    modifier: Modifier = Modifier,
    onGradeActiveChange: (Boolean) -> Unit,
    gradeSetting: GradeSetting,
    onSavePoint: (newPoints: String) -> Unit,
    onSavePercentage: (newPercentage: String) -> Unit,
) {
    val spacing = LocalSpacing.current

    var showEditPointsDialog by remember {
        mutableStateOf(false)
    }

    if (showEditPointsDialog) EditTextDialog(
        onDismiss = { showEditPointsDialog = false },
        onSaveClick = onSavePoint,
        title = stringResource(R.string.points),
        value = gradeSetting.points?.toString() ?: ""
    )

    var showEditPercentageDialog by remember {
        mutableStateOf(false)
    }

    if (showEditPercentageDialog) EditTextDialog(
        onDismiss = { showEditPercentageDialog = false },
        onSaveClick = onSavePercentage,
        title = stringResource(R.string.Percentage),
        value = gradeSetting.percentage?.toString() ?: ""
    )

    val alpha by animateFloatAsState(targetValue = if (gradeSetting.active) 1f else 0.5f)

    Card(
        shape = RoundedCornerShape(spacing.small),
        modifier = modifier
            .alpha(alpha)
    ) {
        Column(
            modifier = Modifier
                .padding(spacing.small)
                .animateContentSize()
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = gradeSetting.symbol,
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.primary
                )
                Switch(
                    checked = gradeSetting.active,
                    onCheckedChange = { onGradeActiveChange(!gradeSetting.active) })
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = gradeSetting.points?.let {
                        stringResource(
                            id = R.string.points_value,
                            it
                        )
                    }
                        ?: stringResource(
                            id = R.string.not_set
                        ),
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(spacing.medium))
                        .clickable {
                            showEditPointsDialog = true
                        }
                )
                Text(
                    text = gradeSetting.percentage?.let {
                        stringResource(
                            id = R.string.percentage_value,
                            it
                        )
                    }
                        ?: stringResource(
                            id = R.string.not_set
                        ),
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(spacing.medium))
                        .clickable {
                            showEditPercentageDialog = true
                        }
                )
            }
        }
    }
}

@Preview
@Composable
fun GradeItemPreview() {
    var grade by remember {
        mutableStateOf(
            GradeSetting(GradeName.APlus, null, 90.0, true),
        )
    }

    GradeItem(
        gradeSetting = grade,
        onGradeActiveChange = { grade = grade.copy(active = !grade.active) },
        onSavePoint = {},
        onSavePercentage = {}
    )
}