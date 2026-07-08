package com.hussienfahmy.core_ui.presentation.user_data.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.hussienfahmy.core.R
import com.hussienfahmy.core_ui.presentation.components.ExpandableTextField
import com.hussienfahmy.core_ui.presentation.components.meadow.CapsLabel
import com.hussienfahmy.core_ui.presentation.components.meadow.MeadowCard
import com.hussienfahmy.core_ui.presentation.components.meadow.MeadowRowDivider
import com.hussienfahmy.core_ui.theme.MeadowTheme

@Composable
fun PersonalInfoSection(
    name: String,
    photoUrl: String?,
    uploadingPhoto: Boolean,
    onNameChange: (String) -> Unit,
    onChangePhotoClick: () -> Unit,
    modifier: Modifier = Modifier,
    enablePhotoEditing: Boolean = true
) {
    val colors = MeadowTheme.colors

    MeadowCard(
        modifier = modifier.fillMaxWidth(),
        contentPadding = PaddingValues(vertical = 16.dp),
    ) {
        Column(modifier = Modifier.padding(horizontal = 18.dp)) {
            CapsLabel(text = stringResource(R.string.onboarding_personal_info_title))
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = stringResource(R.string.onboarding_personal_info_subtitle),
                style = androidx.compose.material3.MaterialTheme.typography.bodySmall,
                color = colors.inkFaint,
            )
            Spacer(modifier = Modifier.height(14.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
            ) {
                ProfilePhotoEditable(
                    photoUrl = photoUrl,
                    uploadingPhoto = uploadingPhoto,
                    enablePhotoEditing = enablePhotoEditing,
                    onChangePhotoClick = onChangePhotoClick,
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))
        MeadowRowDivider()

        ExpandableTextField(
            title = stringResource(R.string.name),
            value = name,
            onNewValueSubmitted = onNameChange,
        )
    }
}

/** Circular avatar with a camera badge overlay (profile / personal info). */
@Composable
fun ProfilePhotoEditable(
    photoUrl: String?,
    uploadingPhoto: Boolean,
    enablePhotoEditing: Boolean,
    onChangePhotoClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = MeadowTheme.colors
    val accent = MeadowTheme.accent

    Box(modifier = modifier.size(112.dp)) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .matchParentSize()
                .clip(CircleShape)
                .background(accent.container),
        ) {
            if (uploadingPhoto) {
                CircularProgressIndicator(color = accent.accent)
            } else {
                UserPhoto(
                    photoUrl = photoUrl,
                    modifier = Modifier.matchParentSize(),
                )
            }
        }
        if (enablePhotoEditing) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .size(34.dp)
                    .clip(CircleShape)
                    .background(accent.accent)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = onChangePhotoClick,
                    ),
            ) {
                Icon(
                    imageVector = Icons.Filled.PhotoCamera,
                    contentDescription = stringResource(R.string.change_photo),
                    tint = accent.onAccent,
                    modifier = Modifier.size(18.dp),
                )
            }
        }
    }
}
