package com.hussienfahmy.semester_marks_presentaion.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.hussienFahmy.core_ui.LocalSpacing
import com.hussienFahmy.myGpaManager.core.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SemesterWorkAvailabilityDialog(
    onDismiss: () -> Unit,
    midtermAvailable: Boolean,
    oralAvailable: Boolean,
    practicalAvailable: Boolean,
    projectAvailable: Boolean,
    onMidtermCheckChanges: (Boolean) -> Unit,
    onPracticalCheckChanges: (Boolean) -> Unit,
    onOralCheckChanges: (Boolean) -> Unit,
    onProjectCheckChanges: (Boolean) -> Unit
) {
    val spacing = LocalSpacing.current

    AlertDialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier.clip(RoundedCornerShape(spacing.medium))
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(spacing.small),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(spacing.medium)
            ) {
                SemesterMarkChooser(
                    title = stringResource(id = R.string.midterm),
                    available = midtermAvailable,
                    onCheckChanges = onMidtermCheckChanges
                )
                SemesterMarkChooser(
                    title = stringResource(id = R.string.practical),
                    available = practicalAvailable,
                    onCheckChanges = onPracticalCheckChanges
                )
                SemesterMarkChooser(
                    title = stringResource(id = R.string.oral),
                    available = oralAvailable,
                    onCheckChanges = onOralCheckChanges
                )
                SemesterMarkChooser(
                    title = stringResource(id = R.string.project),
                    available = projectAvailable,
                    onCheckChanges = onProjectCheckChanges
                )
            }
        }
    }
}

@Composable
private fun SemesterMarkChooser(
    title: String,
    available: Boolean,
    onCheckChanges: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = title)

        Switch(checked = available, onCheckedChange = { onCheckChanges(it) })
    }
}

@Preview
@Composable
fun SemesterWorkAvailabilityChooserPreview() {
    SemesterWorkAvailabilityDialog(
        practicalAvailable = true,
        oralAvailable = true,
        midtermAvailable = false,
        projectAvailable = true,
        onDismiss = {},
        onMidtermCheckChanges = {},
        onPracticalCheckChanges = {},
        onOralCheckChanges = {},
        onProjectCheckChanges = {}
    )
}