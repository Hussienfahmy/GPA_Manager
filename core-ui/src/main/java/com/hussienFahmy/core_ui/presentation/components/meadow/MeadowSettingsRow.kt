package com.hussienfahmy.core_ui.presentation.components.meadow

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.KeyboardArrowRight
import androidx.compose.material.icons.automirrored.outlined.Logout
import androidx.compose.material.icons.outlined.DonutLarge
import androidx.compose.material.icons.outlined.GridView
import androidx.compose.material.icons.outlined.North
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hussienfahmy.core_ui.theme.MeadowAccent
import com.hussienfahmy.core_ui.theme.MeadowTheme

/** What the trailing affordance of a settings row shows. */
enum class SettingsRowTrailing { Chevron, External, None }

/**
 * Settings-hub row (design 3b): a hue-tinted icon tile teaches the destination's
 * color, then a title + optional summary, then a chevron or ↗.
 * [danger] = true renders the whole row in the danger hue (Sign out).
 */
@Composable
fun MeadowSettingsRow(
    icon: ImageVector,
    title: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    summary: String? = null,
    tileAccent: MeadowAccent? = null,
    trailing: SettingsRowTrailing = SettingsRowTrailing.Chevron,
    danger: Boolean = false,
) {
    val colors = MeadowTheme.colors

    val tileBg = when {
        danger -> colors.dangerContainer
        tileAccent != null -> tileAccent.container
        else -> colors.more.container
    }
    val tileFg = when {
        danger -> colors.onDangerContainer
        tileAccent != null -> tileAccent.deep
        else -> colors.more.deep
    }
    val titleColor = if (danger) colors.danger else colors.more.ink

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick,
            )
            .padding(horizontal = 18.dp, vertical = 14.dp),
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(36.dp)
                .clip(RoundedCornerShape(13.dp))
                .background(tileBg),
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = tileFg,
                modifier = Modifier.size(19.dp),
            )
        }

        Spacer(modifier = Modifier.size(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                color = titleColor,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            if (summary != null) {
                Text(
                    text = summary,
                    style = MaterialTheme.typography.bodySmall,
                    color = colors.more.soft,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }

        when (trailing) {
            SettingsRowTrailing.Chevron -> Icon(
                imageVector = Icons.AutoMirrored.Outlined.KeyboardArrowRight,
                contentDescription = null,
                tint = colors.inkGhost,
                modifier = Modifier.size(18.dp),
            )

            SettingsRowTrailing.External -> Icon(
                imageVector = Icons.Outlined.North,
                contentDescription = null,
                tint = colors.inkGhost,
                modifier = Modifier
                    .size(16.dp)
                    .rotate(45f),
            )

            SettingsRowTrailing.None -> Unit
        }
    }
}

/** Uppercased group heading over a settings card ("SETTINGS", "APP"). */
@Composable
fun SettingsGroupLabel(text: String, modifier: Modifier = Modifier) {
    CapsLabel(
        text = text,
        color = MeadowTheme.colors.inkFaint,
        modifier = modifier.padding(start = 6.dp, top = 4.dp),
    )
}

/**
 * A card grouping settings rows. Place [MeadowSettingsRow]s inside, separated by
 * [MeadowRowDivider] where a hairline is wanted.
 */
@Composable
fun MeadowSettingsGroup(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit,
) {
    MeadowCard(
        modifier = modifier,
        contentPadding = PaddingValues(0.dp),
        content = content,
    )
}

/** Inset hairline between two settings rows. */
@Composable
fun MeadowRowDivider(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 18.dp)
            .height(1.dp)
            .background(MeadowTheme.colors.divider),
    )
}

@Composable
private fun MeadowSettingsShowcase() {
    val colors = MeadowTheme.colors
    Column(
        modifier = Modifier
            .background(colors.paper)
            .padding(12.dp),
    ) {
        SettingsGroupLabel(text = "Settings")
        Spacer(modifier = Modifier.height(8.dp))
        MeadowSettingsGroup {
            MeadowSettingsRow(
                icon = Icons.Outlined.DonutLarge,
                title = "GPA system",
                summary = "4.0 scale",
                tileAccent = colors.semester,
                onClick = {},
            )
            MeadowRowDivider()
            MeadowSettingsRow(
                icon = Icons.Outlined.Star,
                title = "Grades",
                summary = "11 active · A+ to F",
                tileAccent = colors.marks,
                onClick = {},
            )
            MeadowRowDivider()
            MeadowSettingsRow(
                icon = Icons.Outlined.GridView,
                title = "Subjects",
                summary = "Marks follow credit hours",
                tileAccent = colors.history,
                onClick = {},
            )
        }
        Spacer(modifier = Modifier.height(12.dp))
        MeadowSettingsGroup {
            MeadowSettingsRow(
                icon = Icons.AutoMirrored.Outlined.Logout,
                title = "Sign out",
                onClick = {},
                trailing = SettingsRowTrailing.None,
                danger = true,
            )
        }
    }
}

@Preview(name = "SettingsRow · light")
@Composable
private fun MeadowSettingsRowLightPreview() {
    MeadowTheme(darkTheme = false) { MeadowSettingsShowcase() }
}

@Preview(name = "SettingsRow · dark")
@Composable
private fun MeadowSettingsRowDarkPreview() {
    MeadowTheme(darkTheme = true) { MeadowSettingsShowcase() }
}
