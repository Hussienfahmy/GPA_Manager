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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.hussienfahmy.core.R
import com.hussienfahmy.core_ui.R as CoreUiR

@Composable
fun UserPhoto(
    modifier: Modifier = Modifier,
    photoUrl: String?
) {
    AsyncImage(
        model = ImageRequest.Builder(LocalContext.current)
            .data(photoUrl)
            .crossfade(true)
            .build(),
        contentDescription = stringResource(R.string.user_photo),
        error = painterResource(id = CoreUiR.drawable.baseline_person_24),
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