package com.hussienfahmy.onboarding_presentation

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.UIKitInteropProperties
import androidx.compose.ui.viewinterop.UIKitView
import com.hussienfahmy.core.domain.auth.service.AppleSignIn
import kotlinx.cinterop.BetaInteropApi
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.ObjCAction
import org.koin.compose.koinInject
import platform.AuthenticationServices.ASAuthorizationAppleIDButton
import platform.AuthenticationServices.ASAuthorizationAppleIDButtonStyle
import platform.AuthenticationServices.ASAuthorizationAppleIDButtonTypeSignIn
import platform.Foundation.NSSelectorFromString
import platform.UIKit.UIControlEventTouchUpInside
import platform.darwin.NSObject

@Composable
actual fun rememberPlatformSignIn(): PlatformSignIn {
    val appleSignIn = koinInject<AppleSignIn>()
    return remember(appleSignIn) {
        PlatformSignIn { appleSignIn.signIn() }
    }
}

@OptIn(ExperimentalForeignApi::class, BetaInteropApi::class)
@Composable
actual fun PlatformSignInButton(
    onClick: () -> Unit,
    enabled: Boolean,
    modifier: Modifier,
) {
    val currentOnClick by rememberUpdatedState(onClick)
    // UIControl.addTarget holds its target weakly, so this has to stay alive for the button's
    // lifetime - remember does that. It forwards to whichever onClick is current at tap time.
    val target = remember {
        object : NSObject() {
            @ObjCAction
            fun handleTap() = currentOnClick()
        }
    }

    UIKitView(
        factory = {
            ASAuthorizationAppleIDButton(
                authorizationButtonType = ASAuthorizationAppleIDButtonTypeSignIn,
                authorizationButtonStyle = ASAuthorizationAppleIDButtonStyle.ASAuthorizationAppleIDButtonStyleBlack,
            ).apply {
                addTarget(
                    target = target,
                    action = NSSelectorFromString("handleTap"),
                    forControlEvents = UIControlEventTouchUpInside,
                )
            }
        },
        modifier = modifier.fillMaxWidth().height(50.dp),
        update = { button -> button.enabled = enabled },
        properties = UIKitInteropProperties(
            isInteractive = true,
            isNativeAccessibilityEnabled = true,
        ),
    )
}
