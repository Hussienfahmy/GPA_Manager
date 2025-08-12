package com.hussienfahmy.semester_subjctets_presentaion.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import com.hussienFahmy.core_ui.LocalSpacing
import com.hussienFahmy.core_ui.presentation.components.SemesterMarkChooser
import com.hussienFahmy.myGpaManager.core.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddSubjectDialog(
    onAddSubject: (
        subjectName: String,
        creditHours: String,
        midtermAvailable: Boolean,
        practicalAvailable: Boolean,
        oralAvailable: Boolean,
        projectAvailable: Boolean,
    ) -> Unit,
    onDismissClick: () -> Unit,
) {
    var subjectName by remember { mutableStateOf("") }
    var creditHours by remember { mutableStateOf("") }
    var midtermAvailable by remember { mutableStateOf(true) }
    var practicalAvailable by remember { mutableStateOf(false) }
    var oralAvailable by remember { mutableStateOf(true) }
    var projectAvailable by remember { mutableStateOf(false) }

    val validSubjectName = subjectName.isNotBlank()
    val validCreditHours = creditHours.isNotBlank()

    val saveSubject = {
        onAddSubject(
            subjectName,
            creditHours,
            midtermAvailable,
            practicalAvailable,
            oralAvailable,
            projectAvailable
        )
    }

    val subjectNameFocusRequester = FocusRequester()
    val creditHoursFocusRequester = FocusRequester()

    val resetInputs = {
        subjectName = ""
        creditHours = ""
        // reset the focus to the subject name
        subjectNameFocusRequester.requestFocus()
    }

    val spacing = LocalSpacing.current

    BasicAlertDialog(
        onDismissRequest = { onDismissClick() },
    ) {
        Surface(
            modifier = Modifier
                .clip(RoundedCornerShape(spacing.medium))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(spacing.medium)
            ) {
                Text(
                    text = stringResource(R.string.add_subject),
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.secondary
                )

                Spacer(modifier = Modifier.height(spacing.medium))

                Row {
                    OutlinedTextField(
                        value = subjectName,
                        onValueChange = { subjectName = it },
                        label = {
                            Text(text = stringResource(R.string.subject_name))
                        },
                        singleLine = true,
                        modifier = Modifier
                            .weight(2.5f)
                            .focusRequester(subjectNameFocusRequester),
                        isError = !validSubjectName,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Next
                        ),
                        keyboardActions = KeyboardActions(
                            onNext = {
                                creditHoursFocusRequester.requestFocus()
                            }
                        )
                    )

                    Spacer(modifier = Modifier.width(spacing.small))

                    OutlinedTextField(
                        value = creditHours,
                        onValueChange = { creditHours = it },
                        label = {
                            Text(text = stringResource(R.string.hours))
                        },
                        singleLine = true,
                        modifier = Modifier
                            .weight(1f)
                            .focusRequester(creditHoursFocusRequester),
                        isError = !validCreditHours,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number,
                            imeAction = ImeAction.Done
                        ),
                        keyboardActions = KeyboardActions(
                            onDone = {
                                if (validSubjectName && validCreditHours) {
                                    saveSubject()
                                    onDismissClick()
                                }
                            }
                        )
                    )
                }

                Spacer(modifier = Modifier.height(spacing.medium))

                Column(verticalArrangement = Arrangement.spacedBy(spacing.small)) {
                    SemesterMarkChooser(
                        title = stringResource(R.string.midterm),
                        available = midtermAvailable,
                        onCheckChanges = { midtermAvailable = it }
                    )
                    SemesterMarkChooser(
                        title = stringResource(R.string.oral),
                        available = oralAvailable,
                        onCheckChanges = { oralAvailable = it }
                    )
                    SemesterMarkChooser(
                        title = stringResource(R.string.practical),
                        available = practicalAvailable,
                        onCheckChanges = { practicalAvailable = it }
                    )
                    SemesterMarkChooser(
                        title = stringResource(R.string.project),
                        available = projectAvailable,
                        onCheckChanges = { projectAvailable = it }
                    )
                }

                Spacer(modifier = Modifier.height(spacing.medium))

                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        if (validSubjectName && validCreditHours) {
                            saveSubject()
                            resetInputs()
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.tertiaryContainer)
                ) {
                    Text(
                        text = stringResource(R.string.add_another_subject),
                        color = MaterialTheme.colorScheme.onTertiaryContainer,
                    )
                }

                Button(
                    onClick = {
                        if (validSubjectName && validCreditHours) {
                            saveSubject()
                            onDismissClick()
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondaryContainer),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = stringResource(R.string.add_and_close),
                        color = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun AddSubjectDialogPreview() {
    AddSubjectDialog(onAddSubject = { _, _, _, _, _, _ -> }, onDismissClick = {})
}