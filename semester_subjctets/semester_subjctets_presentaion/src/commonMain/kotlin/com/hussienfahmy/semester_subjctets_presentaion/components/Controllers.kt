package com.hussienfahmy.semester_subjctets_presentaion.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import org.jetbrains.compose.resources.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hussienfahmy.core.generated.resources.*
import com.hussienfahmy.core_ui.presentation.components.meadow.PillButton
import com.hussienfahmy.core_ui.presentation.components.meadow.PillButtonStyle
import com.hussienfahmy.core_ui.presentation.components.meadow.SegmentedToggle
import com.hussienfahmy.semester_subjctets_presentaion.model.Mode

/**
 * Screen toolbar: Normal/Predict segmented toggle · "+ Add" tonal pill · "Reset" text pill.
 */
@Composable
fun Controllers(
    onChangeModeClick: () -> Unit,
    onAddClick: () -> Unit,
    onResetClick: () -> Unit,
    mode: Mode,
    modifier: Modifier = Modifier,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.fillMaxWidth(),
    ) {
        val selectedIndex = when (mode) {
            Mode.Normal -> 0
            is Mode.Predict -> 1
        }

        SegmentedToggle(
            options = listOf(
                stringResource(Res.string.mode_normal),
                stringResource(Res.string.mode_predictive),
            ),
            selectedIndex = selectedIndex,
            onSelect = { index -> if (index != selectedIndex) onChangeModeClick() },
        )

        Spacer(modifier = Modifier.weight(1f))

        PillButton(
            text = stringResource(Res.string.add),
            onClick = onAddClick,
            style = PillButtonStyle.Tonal,
            icon = Icons.Rounded.Add,
            compact = true,
        )

        Spacer(modifier = Modifier.width(4.dp))

        PillButton(
            text = stringResource(Res.string.reset),
            onClick = onResetClick,
            style = PillButtonStyle.Text,
            compact = true,
        )
    }
}

@Preview
@Composable
fun ControllersPreview() {
    var mode: Mode by remember {
        mutableStateOf(Mode.Normal)
    }

    Controllers(
        onChangeModeClick = {
            mode = when (mode) {
                Mode.Normal -> Mode.Predict("3.25", true)
                is Mode.Predict -> Mode.Normal
            }
        },
        onAddClick = { },
        onResetClick = { },
        mode = mode
    )
}
