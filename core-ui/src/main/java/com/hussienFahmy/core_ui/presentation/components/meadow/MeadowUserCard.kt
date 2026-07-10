package com.hussienfahmy.core_ui.presentation.components.meadow

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.hussienfahmy.core_ui.theme.MeadowAccentProvider
import com.hussienfahmy.core_ui.theme.MeadowRadius
import com.hussienfahmy.core_ui.theme.MeadowTheme

/**
 * Profile hero (design 3b/3c): avatar (photo or initials), name, institution
 * line, and GPA + year chips. Tapping opens the profile screen.
 */
@Composable
fun MeadowUserCard(
    name: String,
    institutionLine: String,
    gpaChip: String,
    yearChip: String,
    photoUrl: String?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = MeadowTheme.colors
    val more = colors.more

    MeadowCard(
        modifier = modifier
            .clickable(
                interactionSource = androidx.compose.runtime.remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick,
            ),
        radius = MeadowRadius.hero,
        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 18.dp),
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(14.dp)) {
            // Avatar + GPA below it, freeing the row for the (possibly wrapping)
            // institution line and year chip.
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(60.dp)
                        .clip(CircleShape)
                        .background(more.container),
                ) {
                    if (photoUrl.isNullOrBlank()) {
                        Text(
                            text = name.initials(),
                            style = MaterialTheme.typography.headlineLarge,
                            color = more.deep,
                        )
                    } else {
                        AsyncImage(
                            model = ImageRequest.Builder(LocalContext.current)
                                .data(photoUrl)
                                .crossfade(true)
                                .build(),
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(CircleShape),
                        )
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                MeadowChip(text = gpaChip, style = MeadowChipStyle.Accent)
            }

            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.Top) {
                    Text(
                        text = name,
                        style = MaterialTheme.typography.headlineMedium,
                        color = more.ink,
                        modifier = Modifier.weight(1f),
                    )
                    Icon(
                        imageVector = Icons.AutoMirrored.Outlined.KeyboardArrowRight,
                        contentDescription = null,
                        tint = colors.inkGhost,
                        modifier = Modifier.size(18.dp),
                    )
                }
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = institutionLine,
                    style = MaterialTheme.typography.bodySmall,
                    color = more.soft,
                )
                Spacer(modifier = Modifier.height(8.dp))
                MeadowChip(text = yearChip)
            }
        }
    }
}

private fun String.initials(): String {
    val parts = trim().split(Regex("\\s+")).filter { it.isNotBlank() }
    return when {
        parts.isEmpty() -> "?"
        parts.size == 1 -> parts[0].take(1).uppercase()
        else -> (parts.first().take(1) + parts.last().take(1)).uppercase()
    }
}

@Preview(name = "UserCard · light")
@Composable
private fun MeadowUserCardLightPreview() {
    MeadowTheme(darkTheme = false) {
        MeadowAccentProvider(MeadowTheme.colors.more) {
            Box(modifier = Modifier.background(MeadowTheme.colors.paper).padding(12.dp)) {
                MeadowUserCard(
                    name = "Hussien Fahmy",
                    institutionLine = "Cairo University · CS",
                    gpaChip = "GPA 3.53",
                    yearChip = "Year 3 · Sem 1",
                    photoUrl = null,
                    onClick = {},
                )
            }
        }
    }
}

@Preview(name = "UserCard · dark")
@Composable
private fun MeadowUserCardDarkPreview() {
    MeadowTheme(darkTheme = true) {
        MeadowAccentProvider(MeadowTheme.colors.more) {
            Box(modifier = Modifier.background(MeadowTheme.colors.paper).padding(12.dp)) {
                MeadowUserCard(
                    name = "Hussien Fahmy",
                    institutionLine = "Cairo University · CS",
                    gpaChip = "GPA 3.53",
                    yearChip = "Year 3 · Sem 1",
                    photoUrl = null,
                    onClick = {},
                )
            }
        }
    }
}
