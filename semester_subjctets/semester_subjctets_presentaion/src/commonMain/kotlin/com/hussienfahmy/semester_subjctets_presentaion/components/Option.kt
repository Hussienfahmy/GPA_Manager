package com.hussienfahmy.semester_subjctets_presentaion.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import com.hussienfahmy.core_ui.LocalSpacing

enum class OptionType { Horizontal, Vertical }

@Composable
fun Option(
    text: String,
    onClick: () -> Unit,
    icon: ImageVector,
    type: OptionType,
    modifier: Modifier = Modifier,
) {
    val spacing = LocalSpacing.current

    when (type) {
        OptionType.Horizontal -> {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = modifier
                    .clip(RoundedCornerShape(spacing.small))
                    .clickable { onClick() }
            ) {
                Icon(imageVector = icon, contentDescription = "")
                Text(text = text, color = MaterialTheme.colorScheme.primary)
            }
        }

        OptionType.Vertical -> {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = modifier
                    .clip(RoundedCornerShape(spacing.small))
                    .clickable { onClick() }
            ) {
                Icon(imageVector = icon, contentDescription = "")
                Text(text = text, color = MaterialTheme.colorScheme.primary)
            }
        }
    }
}

@Preview
@Composable
fun OptionPreview() {
    Column {
        Option(
            text = "Horizontal Option",
            onClick = { },
            icon = Icons.Default.Add,
            type = OptionType.Horizontal
        )
        Option(
            text = "Vertical Option",
            onClick = { },
            icon = Icons.Default.Add,
            type = OptionType.Vertical
        )
    }
}