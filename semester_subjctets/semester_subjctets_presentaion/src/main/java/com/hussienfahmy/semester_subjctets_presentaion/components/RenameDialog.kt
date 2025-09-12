package com.hussienfahmy.semester_subjctets_presentaion.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import com.hussienfahmy.core.R
import com.hussienfahmy.core_ui.LocalSpacing

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RenameDialog(
    onDismiss: () -> Unit,
    onSaveClick: (newName: String) -> Unit,
    subjectCurrentName: String,
) {
    var newName by remember { mutableStateOf(subjectCurrentName) }

    val spacing = LocalSpacing.current

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            Button(onClick = {
                if (newName.isNotBlank()) {
                    onSaveClick(newName)
                    onDismiss()
                }
            }, modifier = Modifier.fillMaxWidth()) {
                Text(text = stringResource(R.string.save))
            }
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(spacing.medium)
            ) {
                Text(text = stringResource(R.string.rename_subject))

                Spacer(modifier = Modifier.height(spacing.medium))

                OutlinedTextField(
                    value = newName,
                    onValueChange = { newName = it },
                    label = { Text(text = stringResource(R.string.new_subject_name)) },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                    isError = newName.isBlank()
                )
            }
        }
    )
}