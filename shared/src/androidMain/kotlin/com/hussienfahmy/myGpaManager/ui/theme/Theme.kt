package com.hussienfahmy.myGpaManager.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import com.hussienfahmy.core_ui.theme.MeadowTheme

@Composable
fun GPAManagerTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MeadowTheme(darkTheme = darkTheme, content = content)
}
