package com.hussienfahmy.semester_marks_presentaion.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.hussienFahmy.core_ui.LocalSpacing
import com.hussienFahmy.core_ui.presentation.components.SemesterMarkChooser
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

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {},
        text = {
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
                    title = stringResource(id = R.string.oral),
                    available = oralAvailable,
                    onCheckChanges = onOralCheckChanges
                )
                SemesterMarkChooser(
                    title = stringResource(id = R.string.practical),
                    available = practicalAvailable,
                    onCheckChanges = onPracticalCheckChanges
                )
                SemesterMarkChooser(
                    title = stringResource(id = R.string.project),
                    available = projectAvailable,
                    onCheckChanges = onProjectCheckChanges
                )
            }
        }
    )
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