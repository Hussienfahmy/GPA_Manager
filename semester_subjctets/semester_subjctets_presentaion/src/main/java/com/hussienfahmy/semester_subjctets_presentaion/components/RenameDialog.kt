package com.hussienfahmy.semester_subjctets_presentaion.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.hussienFahmy.core_ui.LocalSpacing
import com.hussienFahmy.myGpaManager.core.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RenameDialog(
    onDismiss: () -> Unit,
    onSaveClick: (newName: String) -> Unit,
    subjectCurrentName: String,
) {
    var newName by remember { mutableStateOf(subjectCurrentName) }

    val spacing = LocalSpacing.current

    AlertDialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier.clip(RoundedCornerShape(spacing.medium))
        ) {
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

                Spacer(modifier = Modifier.height(20.dp))

                Button(onClick = {
                    if (newName.isNotBlank()) {
                        onSaveClick(newName)
                        onDismiss()
                    }
                }, modifier = Modifier.fillMaxWidth()) {
                    Text(text = stringResource(R.string.save))
                }
            }
        }
    }
}