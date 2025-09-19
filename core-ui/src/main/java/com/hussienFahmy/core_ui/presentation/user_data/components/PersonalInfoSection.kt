package com.hussienfahmy.core_ui.presentation.user_data.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.hussienfahmy.core.R
import com.hussienfahmy.core_ui.LocalSpacing
import com.hussienfahmy.core_ui.presentation.components.ExpandableTextField

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
    val spacing = LocalSpacing.current

    Card(modifier = modifier) {
        Column(
            modifier = Modifier.padding(spacing.medium),
            verticalArrangement = Arrangement.spacedBy(spacing.medium)
        ) {
            Text(
                text = stringResource(R.string.onboarding_personal_info_title),
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.fillMaxWidth()
            )

            Text(
                text = stringResource(R.string.onboarding_personal_info_subtitle),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(spacing.small))

            // Photo section
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Box(
                    modifier = Modifier.size(120.dp)
                ) {
                    if (uploadingPhoto) {
                        CircularProgressIndicator(
                            modifier = Modifier.align(Alignment.Center)
                        )
                    } else {
                        UserPhoto(
                            photoUrl = photoUrl,
                            modifier = Modifier.matchParentSize()
                        )
                    }
                    if (enablePhotoEditing) {
                        Icon(
                            imageVector = Icons.Filled.PhotoCamera,
                            contentDescription = stringResource(R.string.change_photo),
                            modifier = Modifier
                                .align(Alignment.BottomEnd)
                                .scale(1.5f)
                                .clickable { onChangePhotoClick() }
                        )
                    }
                }
            }

            // Name field
            ExpandableTextField(
                title = stringResource(R.string.name),
                value = name,
                onNewValueSubmitted = onNameChange
            )
        }
    }
}