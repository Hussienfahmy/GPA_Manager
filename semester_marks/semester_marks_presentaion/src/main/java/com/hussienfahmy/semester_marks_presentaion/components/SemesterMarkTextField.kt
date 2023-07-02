package com.hussienfahmy.semester_marks_presentaion.components

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun SemesterMarkTextField(
    modifier: Modifier,
    title: String,
    value: String,
    onValueChanged: (String) -> Unit,
    keyboardType: KeyboardType
) {
    OutlinedTextField(
        modifier = modifier,
        value = value,
        onValueChange = onValueChanged,
        label = { Text(text = title) },
        isError = value.isNotBlank() && value.toDoubleOrNull() == null,
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType)
    )
}

@Preview
@Composable
fun SemesterMarkTextFieldPreview() {
    SemesterMarkTextField(
        modifier = Modifier,
        title = "Midterm",
        value = "50",
        onValueChanged = {},
        keyboardType = KeyboardType.Number
    )
}