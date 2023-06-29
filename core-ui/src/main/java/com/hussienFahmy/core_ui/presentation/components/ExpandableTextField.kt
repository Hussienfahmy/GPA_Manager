package com.hussienFahmy.core_ui.presentation.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Done
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hussienFahmy.core_ui.LocalSpacing
import com.hussienFahmy.myGpaManager.core.R

@Composable
fun ExpandableTextField(
    title: String,
    value: String,
    onNewValueSubmitted: (String) -> Unit,
    keyboardType: KeyboardType = KeyboardType.Text,
    enabled: Boolean = true,
    imageVector: ImageVector? = null,
) {
    val spacing = LocalSpacing.current

    var editMode by remember {
        mutableStateOf(false)
    }

    var valueState by remember {
        mutableStateOf(value)
    }

    val isError = valueState.isBlank()

    val alpha by animateFloatAsState(targetValue = if (enabled) 1f else 0.5f)

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
            .padding(spacing.medium)
            .alpha(alpha)
            .animateContentSize()
    ) {
        if (!editMode) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                if (imageVector != null) {
                    Icon(
                        imageVector = imageVector,
                        contentDescription = "",
                    )

                    Spacer(modifier = Modifier.width(3.dp))
                }

                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary
                )

                Spacer(modifier = Modifier.width(3.dp))

                AnimatedVisibility(visible = enabled) {
                    Icon(
                        imageVector = Icons.Outlined.Edit,
                        contentDescription = stringResource(R.string.edit),
                        modifier = Modifier
                            .scale(0.75f)
                            .clickable { editMode = true }
                    )
                }
            }

            Text(text = value, style = MaterialTheme.typography.bodyLarge)
        }

        AnimatedVisibility(visible = editMode) {
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = valueState,
                onValueChange = { valueState = it },
                keyboardOptions = KeyboardOptions(
                    keyboardType = keyboardType
                ),
                isError = isError,
                label = { Text(text = title) },
                trailingIcon = {
                    Icon(
                        imageVector = Icons.Outlined.Done,
                        contentDescription = stringResource(id = R.string.save),
                        modifier = Modifier.clickable {
                            if (isError) return@clickable

                            onNewValueSubmitted(valueState)
                            editMode = false
                        }
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Outlined.Close,
                        contentDescription = stringResource(R.string.cancel),
                        modifier = Modifier.clickable {
                            editMode = false
                            // reset to the original value
                            valueState = value
                        }
                    )
                }
            )
        }
    }
}

@Preview
@Composable
fun ExpandableTextFieldPreview() {
    ExpandableTextField(title = "Name", value = "Hussien Ahmed", onNewValueSubmitted = {})
}