package com.hussienfahmy.core_ui.presentation.user_data.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.compose.LocalPlatformContext
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.hussienfahmy.core.generated.resources.*
import com.hussienfahmy.core_ui.generated.resources.baseline_person_24
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import com.hussienfahmy.core_ui.generated.resources.Res as CoreUiRes

@Composable
fun UserPhoto(
    modifier: Modifier = Modifier,
    photoUrl: String?
) {
    AsyncImage(
        model = ImageRequest.Builder(LocalPlatformContext.current)
            .data(photoUrl)
            .crossfade(true)
            .build(),
        contentDescription = stringResource(Res.string.user_photo),
        error = painterResource(CoreUiRes.drawable.baseline_person_24),
        contentScale = ContentScale.Crop,
        modifier = modifier
            .clip(CircleShape)
            .border(
                BorderStroke(2.dp, MaterialTheme.colorScheme.primary),
                CircleShape
            )
            .padding(2.dp)
    )
}

@Preview
@Composable
fun UserPhotoPreview() {
    UserPhoto(photoUrl = "")
}
