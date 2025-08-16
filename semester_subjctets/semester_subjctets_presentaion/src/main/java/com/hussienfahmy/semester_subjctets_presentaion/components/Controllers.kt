package com.hussienfahmy.semester_subjctets_presentaion.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.SwitchLeft
import androidx.compose.material.icons.filled.SwitchRight
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.hussienfahmy.core_ui.LocalSpacing
import com.hussienfahmy.core_ui.presentation.components.TipDialogContainer
import com.hussienfahmy.myGpaManager.core.R
import com.hussienfahmy.semester_subjctets_presentaion.model.Mode

@Composable
fun Controllers(
    onChangeModeClick: () -> Unit,
    onAddClick: () -> Unit,
    onResetClick: () -> Unit,
    mode: Mode,
) {
    val spacing = LocalSpacing.current

    Card(shape = RoundedCornerShape(spacing.small)) {
        Column {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(spacing.small)
            ) {
                TipDialogContainer(
                    modifier = Modifier.weight(2f),
                    tipText = stringResource(R.string.tip_modes)
                ) {
                    Option(
                        text = when (mode) {
                            Mode.Normal -> stringResource(R.string.mode_normal)
                            is Mode.Predict -> stringResource(R.string.mode_predictive)
                        },
                        onClick = { onChangeModeClick() },
                        icon = when (mode) {
                            Mode.Normal -> Icons.Default.SwitchRight
                            is Mode.Predict -> Icons.Default.SwitchLeft
                        },
                        type = OptionType.Horizontal,
                    )
                }

                Option(
                    text = stringResource(R.string.add),
                    onClick = onAddClick,
                    icon = Icons.Default.Add,
                    type = OptionType.Horizontal,
                    modifier = Modifier.weight(1f)
                )

                Option(
                    text = stringResource(R.string.reset),
                    onClick = onResetClick,
                    icon = Icons.Default.Refresh,
                    type = OptionType.Horizontal,
                    modifier = Modifier.weight(1f)
                )
            }
        }
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