package com.hussienfahmy.semester_history_presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.KeyboardArrowDown
import androidx.compose.material.icons.outlined.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hussienfahmy.core.R
import com.hussienfahmy.core_ui.LocalSpacing
import com.hussienfahmy.semester_history_domain.model.Semester

@OptIn(ExperimentalMaterial3Api::class)
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
    var showDeleteSheet by remember { mutableStateOf(false) }

    if (showDeleteSheet) {
        ModalBottomSheet(onDismissRequest = { showDeleteSheet = false }) {
            val spacing = LocalSpacing.current
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(spacing.medium),
                verticalArrangement = Arrangement.spacedBy(spacing.small),
            ) {
                Text(
                    text = stringResource(R.string.delete),
                    style = MaterialTheme.typography.titleLarge,
                )
                Text(
                    text = stringResource(R.string.history_delete_semester_message, semester.label),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                Spacer(modifier = Modifier.height(spacing.small))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(spacing.small),
                ) {
                    OutlinedButton(
                        onClick = { showDeleteSheet = false },
                        modifier = Modifier.weight(1f),
                    ) {
                        Text(stringResource(R.string.cancel))
                    }
                    Button(
                        onClick = { showDeleteSheet = false; onDeleteClick() },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.error,
                        ),
                    ) {
                        Text(stringResource(R.string.delete))
                    }
                }
                Spacer(modifier = Modifier.height(spacing.medium))
            }
        }
    }

    val spacing = LocalSpacing.current
    val isDetailed = semester.type == Semester.Type.DETAILED

    val cardContent: @Composable () -> Unit = {
        Row(
            modifier = Modifier.padding(spacing.medium),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                IconButton(onClick = onMoveUp, enabled = canMoveUp) {
                    Icon(
                        Icons.Outlined.KeyboardArrowUp,
                        contentDescription = stringResource(R.string.history_move_up),
                        tint = if (canMoveUp) MaterialTheme.colorScheme.primary
                        else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f),
                    )
                }
                IconButton(onClick = onMoveDown, enabled = canMoveDown) {
                    Icon(
                        Icons.Outlined.KeyboardArrowDown,
                        contentDescription = stringResource(R.string.history_move_down),
                        tint = if (canMoveDown) MaterialTheme.colorScheme.primary
                        else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f),
                    )
                }
            }

            Spacer(modifier = Modifier.width(spacing.small))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = semester.label,
                    style = MaterialTheme.typography.titleMedium,
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    SemesterTypeBadge(type = semester.type)
                    Spacer(modifier = Modifier.width(spacing.small))
                    Text(
                        text = stringResource(R.string.history_gpa_value, semester.semesterGPA),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.primary,
                    )
                    Spacer(modifier = Modifier.width(spacing.small))
                    Text(
                        text = stringResource(
                            R.string.history_hours_value,
                            semester.totalCreditHours
                        ),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
                Text(
                    text = stringResource(
                        R.string.history_points_value,
                        semester.semesterGPA * semester.totalCreditHours
                    ),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                )
            }
            IconButton(onClick = onEditClick) {
                Icon(Icons.Outlined.Edit, contentDescription = stringResource(R.string.edit))
            }
            IconButton(onClick = { showDeleteSheet = true }) {
                Icon(
                    Icons.Outlined.Delete,
                    contentDescription = stringResource(R.string.delete),
                    tint = MaterialTheme.colorScheme.error,
                )
            }
        }
    }

    if (isDetailed) {
        Card(
            shape = RoundedCornerShape(12.dp),
            modifier = modifier.fillMaxWidth(),
            onClick = onClick,
        ) { cardContent() }
    } else {
        Card(
            shape = RoundedCornerShape(12.dp),
            modifier = modifier.fillMaxWidth(),
        ) { cardContent() }
    }
}

@Composable
fun SemesterTypeBadge(type: Semester.Type) {
    val label = when (type) {
        Semester.Type.SUMMARY -> stringResource(R.string.history_type_summary)
        Semester.Type.DETAILED -> stringResource(R.string.history_type_detailed)
    }
    val containerColor = when (type) {
        Semester.Type.SUMMARY -> MaterialTheme.colorScheme.secondaryContainer
        Semester.Type.DETAILED -> MaterialTheme.colorScheme.tertiaryContainer
    }
    val contentColor = when (type) {
        Semester.Type.SUMMARY -> MaterialTheme.colorScheme.onSecondaryContainer
        Semester.Type.DETAILED -> MaterialTheme.colorScheme.onTertiaryContainer
    }
    Surface(
        color = containerColor,
        contentColor = contentColor,
        shape = RoundedCornerShape(4.dp),
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            modifier = Modifier.padding(top = 2.dp, bottom = 2.dp, end = 6.dp),
        )
    }
}

@Preview
@Composable
private fun SemesterCardPreview() {
    SemesterCard(
        semester = Semester(
            id = 0,
            label = "Label",
            level = 1,
            type = Semester.Type.SUMMARY,
            semesterGPA = 3.18,
            totalCreditHours = 19,
            status = Semester.Status.ARCHIVED,
            order = 1,
            createdAt = 1,
            archivedAt = 1
        ),
        onClick = {},
        onEditClick = {},
        onDeleteClick = {},
        onMoveUp = {},
        onMoveDown = {},
        canMoveUp = true,
        canMoveDown = true
    )
}