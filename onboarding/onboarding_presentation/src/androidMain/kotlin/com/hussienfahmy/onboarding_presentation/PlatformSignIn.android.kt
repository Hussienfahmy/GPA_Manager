package com.hussienfahmy.onboarding_presentation

import androidx.activity.compose.LocalActivity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.hussienfahmy.core.domain.auth.GoogleAuthUiClient
import com.hussienfahmy.core.generated.resources.Res
import com.hussienfahmy.core.generated.resources.onboarding_sign_in_with_google
import com.hussienfahmy.core_ui.presentation.components.meadow.PillButton
import com.hussienfahmy.core_ui.presentation.components.meadow.PillButtonStyle
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject

@Composable
actual fun rememberPlatformSignIn(): PlatformSignIn {
    val googleAuthUiClient = koinInject<GoogleAuthUiClient>()
    // CredentialManager needs an Activity context to present its UI - onboarding is always hosted
    // in one by the time it's visible, so a null here only happens in previews/tests.
    val activity = LocalActivity.current
    return remember(googleAuthUiClient, activity) {
        PlatformSignIn { activity?.let { googleAuthUiClient.signIn(it) } }
    }
}

@Composable
actual fun PlatformSignInButton(
    onClick: () -> Unit,
    enabled: Boolean,
    modifier: Modifier,
) {
    PillButton(
        text = stringResource(Res.string.onboarding_sign_in_with_google),
        onClick = onClick,
        modifier = modifier,
        style = PillButtonStyle.Primary,
        enabled = enabled,
    )
}
