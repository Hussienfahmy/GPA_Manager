package com.hussienfahmy.semester_history_presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.KeyboardArrowDown
import androidx.compose.material.icons.outlined.KeyboardArrowUp
import androidx.compose.material.icons.rounded.PriorityHigh
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
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import org.jetbrains.compose.resources.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hussienfahmy.core.generated.resources.*
import com.hussienfahmy.core_ui.presentation.components.meadow.MeadowCard
import com.hussienfahmy.core_ui.presentation.components.meadow.MeadowChip
import com.hussienfahmy.core_ui.presentation.components.meadow.MeadowChipStyle
import com.hussienfahmy.core_ui.presentation.components.meadow.MeadowConfirmationSheet
import com.hussienfahmy.core_ui.theme.MeadowAccentProvider
import com.hussienfahmy.core_ui.theme.MeadowTheme
import com.hussienfahmy.semester_history_domain.model.Semester

/**
 * Journey-list semester card (design 2b): reorder arrows · label · type badge ·
 * GPA · hrs/pts · edit · delete. Detailed cards open the Semester Detail screen.
 */
@Composable
fun SemesterCard(
    semester: Semester,
    onClick: () -> Unit,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit,
    onMoveUp: () -> Unit,
    onMoveDown: () -> Unit,
    canMoveUp: Boolean,
    canMoveDown: Boolean,
    modifier: Modifier = Modifier,
) {
    val colors = MeadowTheme.colors
    val accent = MeadowTheme.accent

    var showDeleteConfirmation by remember { mutableStateOf(false) }

    if (showDeleteConfirmation) MeadowConfirmationSheet(
        title = stringResource(Res.string.delete),
        body = stringResource(Res.string.history_delete_semester_message, semester.label),
        confirmText = stringResource(Res.string.delete),
        onConfirm = onDeleteClick,
        onDismiss = { showDeleteConfirmation = false },
    )

    val isDetailed = semester.type == Semester.Type.DETAILED

    MeadowCard(
        modifier = modifier.fillMaxWidth(),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 13.dp),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .then(
                    if (isDetailed) Modifier.clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = onClick,
                    ) else Modifier
                ),
        ) {
            // Reorder arrows
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(
                    imageVector = Icons.Outlined.KeyboardArrowUp,
                    contentDescription = stringResource(Res.string.history_move_up),
                    tint = colors.inkGhost,
                    modifier = Modifier
                        .size(20.dp)
                        .alpha(if (canMoveUp) 1f else 0.35f)
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null,
                            enabled = canMoveUp,
                            onClick = onMoveUp,
                        ),
                )
                Icon(
                    imageVector = Icons.Outlined.KeyboardArrowDown,
                    contentDescription = stringResource(Res.string.history_move_down),
                    tint = colors.inkGhost,
                    modifier = Modifier
                        .size(20.dp)
                        .alpha(if (canMoveDown) 1f else 0.35f)
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null,
                            enabled = canMoveDown,
                            onClick = onMoveDown,
                        ),
                )
            }

            Column(modifier = Modifier.weight(1f)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                ) {
                    Text(
                        text = semester.label,
                        style = MaterialTheme.typography.titleLarge.copy(fontSize = 15.sp),
                        color = accent.ink,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f, fill = false),
                    )
                    // Amber flag — a subject in this semester has no grade yet.
                    if (semester.hasMissingGrade) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .size(16.dp)
                                .clip(CircleShape)
                                .background(colors.marks.container),
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.PriorityHigh,
                                contentDescription = stringResource(Res.string.history_missing_grade),
                                tint = colors.marks.deep,
                                modifier = Modifier.size(11.dp),
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    if (isDetailed) {
                        MeadowChip(
                            text = "${stringResource(Res.string.history_type_detailed)} ›",
                            style = MeadowChipStyle.Accent,
                        )
                    } else {
                        MeadowChip(text = stringResource(Res.string.history_type_summary))
                    }
                    Text(
                        text = stringResource(Res.string.history_gpa_value, semester.semesterGPA),
                        style = MaterialTheme.typography.labelMedium.copy(fontSize = 12.sp),
                        color = accent.accent,
                    )
                    Text(
                        text = "${
                            stringResource(Res.string.history_hours_value, semester.totalCreditHours)
                        } · ${
                            stringResource(
                                Res.string.history_points_value,
                                semester.semesterGPA * semester.totalCreditHours,
                            )
                        }",
                        style = MaterialTheme.typography.bodySmall,
                        color = colors.inkFaint,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
            }

            Icon(
                imageVector = Icons.Outlined.Edit,
                contentDescription = stringResource(Res.string.edit),
                tint = colors.navItemIcon,
                modifier = Modifier
                    .size(18.dp)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = onEditClick,
                    ),
            )

            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(26.dp)
                    .clip(CircleShape)
                    .background(colors.dangerContainer)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                    ) { showDeleteConfirmation = true },
            ) {
                Icon(
                    imageVector = Icons.Outlined.Close,
                    contentDescription = stringResource(Res.string.delete),
                    tint = colors.onDangerContainer,
                    modifier = Modifier.size(14.dp),
                )
            }
        }
    }
}

@Preview(name = "SemesterCard · light")
@Composable
private fun SemesterCardLightPreview() {
    MeadowTheme(darkTheme = false) { SemesterCardShowcase() }
}

@Preview(name = "SemesterCard · dark")
@Composable
private fun SemesterCardDarkPreview() {
    MeadowTheme(darkTheme = true) { SemesterCardShowcase() }
}

@Composable
private fun SemesterCardShowcase() {
    MeadowAccentProvider(MeadowTheme.colors.history) {
        Column(
            verticalArrangement = Arrangement.spacedBy(11.dp),
            modifier = Modifier
                .background(MeadowTheme.colors.paper)
                .then(Modifier),
        ) {
            SemesterCard(
                semester = Semester(
                    id = 0,
                    label = "Year 1 · Semester 1",
                    level = 1,
                    type = Semester.Type.SUMMARY,
                    semesterGPA = 3.20,
                    totalCreditHours = 17,
                    status = Semester.Status.ARCHIVED,
                    order = 1,
                    createdAt = 1,
                    archivedAt = 1,
                ),
                onClick = {}, onEditClick = {}, onDeleteClick = {},
                onMoveUp = {}, onMoveDown = {},
                canMoveUp = false, canMoveDown = true,
            )
            SemesterCard(
                semester = Semester(
                    id = 1,
                    label = "Year 2 · Semester 1",
                    level = 2,
                    type = Semester.Type.DETAILED,
                    semesterGPA = 3.40,
                    totalCreditHours = 15,
                    status = Semester.Status.ARCHIVED,
                    order = 2,
                    createdAt = 1,
                    archivedAt = 1,
                    hasMissingGrade = true,
                ),
                onClick = {}, onEditClick = {}, onDeleteClick = {},
                onMoveUp = {}, onMoveDown = {},
                canMoveUp = true, canMoveDown = false,
            )
        }
    }
}
