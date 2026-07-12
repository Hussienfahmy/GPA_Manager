package com.hussienfahmy.core_ui.presentation.components

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Done
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import org.jetbrains.compose.resources.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hussienfahmy.core.generated.resources.Res
import com.hussienfahmy.core_ui.presentation.components.meadow.MeadowTextField
import com.hussienfahmy.core_ui.theme.MeadowTheme

/**
 * Inline-edit field (design 3f, rightmost): rest shows the label + value + a
 * pencil; tapping the pencil swaps in a focused [MeadowTextField] with ✓/✕.
 * Disabled fields read as auto/derived with faded value + supporting text.
 */
@Composable
fun ExpandableTextField(
    title: String,
    value: String,
    onNewValueSubmitted: (String) -> Unit,
    keyboardType: KeyboardType = KeyboardType.Text,
    enabled: Boolean = true,
    singleLine: Boolean = true,
    imageVector: ImageVector? = null,
    supportingText: String? = null,
) {
    val colors = MeadowTheme.colors
    val accent = MeadowTheme.accent

    var editMode by remember { mutableStateOf(false) }
    var valueState by remember(value) { mutableStateOf(value) }

    val isError = valueState.isBlank()

    val commit = {
        if (!isError) {
            onNewValueSubmitted(valueState)
            editMode = false
        }
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp)
            .animateContentSize(),
    ) {
        if (!editMode) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .alpha(if (enabled) 1f else 0.55f),
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f),
                ) {
                    if (imageVector != null) {
                        Icon(
                            imageVector = imageVector,
                            contentDescription = null,
                            tint = accent.deep,
                            modifier = Modifier.size(18.dp),
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                    }
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleMedium,
                        color = accent.deep,
                    )
                    if (enabled) {
                        Spacer(modifier = Modifier.width(6.dp))
                        Icon(
                            imageVector = Icons.Outlined.Edit,
                            contentDescription = stringResource(Res.string.edit),
                            tint = colors.navItemIcon,
                            modifier = Modifier
                                .size(15.dp)
                                .clickable(
                                    interactionSource = remember { MutableInteractionSource() },
                                    indication = null,
                                ) { editMode = true },
                        )
                    }
                }

                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = value,
                        style = MaterialTheme.typography.bodyLarge,
                        color = colors.ink,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                    if (!enabled && supportingText != null) {
                        Text(
                            text = supportingText,
                            style = MaterialTheme.typography.labelSmall,
                            color = colors.inkFaint,
                        )
                    }
                }
            }
        } else {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                MeadowTextField(
                    value = valueState,
                    onValueChange = { valueState = it },
                    label = title,
                    isError = isError,
                    singleLine = singleLine,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = keyboardType,
                        imeAction = ImeAction.Done,
                    ),
                    keyboardActions = KeyboardActions(onDone = { commit() }),
                    modifier = Modifier.weight(1f),
                )
                ActionTile(
                    icon = Icons.Outlined.Done,
                    description = stringResource(Res.string.save),
                    background = accent.container,
                    tint = accent.deep,
                    onClick = commit,
                )
                ActionTile(
                    icon = Icons.Outlined.Close,
                    description = stringResource(Res.string.cancel),
                    background = null,
                    tint = colors.inkFaint,
                    onClick = {
                        valueState = value
                        editMode = false
                    },
                )
            }
        }
    }
}

@Composable
private fun ActionTile(
    icon: ImageVector,
    description: String,
    background: androidx.compose.ui.graphics.Color?,
    tint: androidx.compose.ui.graphics.Color,
    onClick: () -> Unit,
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .size(34.dp)
            .clip(RoundedCornerShape(12.dp))
            .then(if (background != null) Modifier.background(background) else Modifier)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick,
            ),
    ) {
        Icon(
            imageVector = icon,
            contentDescription = description,
            tint = tint,
            modifier = Modifier.size(18.dp),
        )
    }
}

@Preview
@Composable
private fun ExpandableTextFieldPreview() {
    MeadowTheme(darkTheme = false) {
        Box(modifier = Modifier.background(MeadowTheme.colors.card)) {
            ExpandableTextField(title = "Name", value = "Hussien Fahmy", onNewValueSubmitted = {})
        }
    }
}
