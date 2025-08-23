package com.hussienfahmy.grades_setting_presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import com.hussienfahmy.core_ui.LocalSpacing
import com.hussienfahmy.myGpaManager.core.R
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditTextDialog(
    viewModel: EditTextDialogViewModel = koinViewModel(),
    onDismiss: () -> Unit,
    onSaveClick: (newValue: String) -> Unit,
    title: String,
    value: String,
) {
    LaunchedEffect(value) {
        viewModel.onValueChanged(value)
    }

    val spacing = LocalSpacing.current

    AlertDialog(
        onDismissRequest = onDismiss, modifier = Modifier
            .fillMaxHeight(0.5f)
            .clip(RoundedCornerShape(spacing.medium))
            .background(MaterialTheme.colorScheme.surface)
            .padding(spacing.medium)
    ) {
        Column(
            verticalArrangement = Arrangement.SpaceAround,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OutlinedTextField(
                value = viewModel.value,
                onValueChange = { viewModel.onValueChanged(it) },
                label = { Text(text = title, style = MaterialTheme.typography.labelMedium) },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        onSaveClick(viewModel.value)
                        onDismiss()
                    }
                )
            )

            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    onSaveClick(viewModel.value)
                    onDismiss()
                }
            ) {
                Text(
                    text = stringResource(R.string.save)
                )
            }
        }
    }
}