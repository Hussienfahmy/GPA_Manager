package com.hussienfahmy.myGpaManager.navigation.screens.onboarding

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import org.jetbrains.compose.resources.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.hussienfahmy.core.generated.resources.Res
import com.hussienfahmy.core_ui.theme.MeadowTheme
import com.hussienfahmy.semester_history_presentation.SemesterDetailRoot

/**
 * Real nested nav destination for a DETAILED semester's subject list —
 * same subject list/add/edit/delete experience as History -> Semester
 * Detail, reached via the real back stack instead of an in-place content
 * swap. Keeps its own minimal back header since onboarding has no bottom
 * nav / system app bar to provide one.
 */
@Composable
fun AppOnBoardingSemesterDetailScreen(
    semesterId: Long,
    semesterLabel: String,
    onBackClick: () -> Unit,
) {
    val colors = MeadowTheme.colors

    Column(modifier = Modifier.fillMaxSize()) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .background(colors.paper)
                .padding(horizontal = 16.dp, vertical = 12.dp),
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(36.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(colors.card)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = onBackClick,
                    ),
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                    contentDescription = stringResource(Res.string.onboarding_back),
                    tint = colors.ink,
                    modifier = Modifier.size(18.dp),
                )
            }
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                text = semesterLabel,
                style = MaterialTheme.typography.headlineSmall,
                color = colors.ink,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }

        Box(modifier = Modifier.weight(1f)) {
            SemesterDetailRoot(semesterId = semesterId)
        }
    }
}
