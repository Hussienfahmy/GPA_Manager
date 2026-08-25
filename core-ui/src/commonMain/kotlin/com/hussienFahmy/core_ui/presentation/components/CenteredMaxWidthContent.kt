package com.hussienfahmy.core_ui.presentation.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.widthIn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

// Caps content width and centers it on wide/landscape windows - full-bleed list/settings screens
// otherwise stretch edge to edge on a tablet instead of reading like a phone screen just wider.
@Composable
fun CenteredMaxWidthContent(
    modifier: Modifier = Modifier,
    maxWidth: Dp = 720.dp,
    content: @Composable () -> Unit,
) {
    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.TopCenter) {
        Box(Modifier.widthIn(max = maxWidth).fillMaxWidth()) {
            content()
        }
    }
}
