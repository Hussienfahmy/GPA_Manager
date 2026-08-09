package com.hussienfahmy.onboarding_presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.UIKitInteropProperties
import androidx.compose.ui.viewinterop.UIKitView
import com.hussienfahmy.core.domain.auth.service.EmailPasswordSignIn
import com.hussienfahmy.core.generated.resources.*
import com.hussienfahmy.core_ui.LocalSpacing
import com.hussienfahmy.core_ui.presentation.components.OnboardingConstants
import com.hussienfahmy.core_ui.presentation.components.OnboardingLayout
import com.hussienfahmy.core_ui.presentation.util.UiEventHandler
import com.hussienfahmy.onboarding_presentation.sign_in.AuthEvent
import com.hussienfahmy.onboarding_presentation.sign_in.SignInState
import com.hussienfahmy.onboarding_presentation.sign_in.SignInViewModel
import kotlinx.cinterop.BetaInteropApi
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.ObjCAction
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject
import platform.CoreGraphics.CGRectMake
import platform.Foundation.NSSelectorFromString
import platform.UIKit.UIControlEventEditingChanged
import platform.UIKit.UIKeyboardType
import platform.UIKit.UIKeyboardTypeDefault
import platform.UIKit.UIKeyboardTypeEmailAddress
import platform.UIKit.UITextAutocapitalizationType
import platform.UIKit.UITextAutocorrectionType
import platform.UIKit.UITextBorderStyle
import platform.UIKit.UITextField

@Composable
actual fun OnBoardingScreen(
    viewModel: SignInViewModel,
    onSignInSuccess: () -> Unit,
) {
    // Temporary: Sign in with Apple (AppleSignIn.kt) needs a paid Apple Developer account, which
    // this project doesn't have enrolled yet - native UITextFields (not Compose-drawn ones) so
    // this is a clean drop-in swap for AppleSignIn once that's sorted out.
    val emailPasswordSignIn = koinInject<EmailPasswordSignIn>()
    val scope = rememberCoroutineScope()
    val spacing = LocalSpacing.current

    val state by viewModel.state
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    LaunchedEffect(key1 = state) {
        when (state) {
            SignInState.Success -> onSignInSuccess()
            SignInState.Initial,
            SignInState.Loading,
            SignInState.Syncing,
            SignInState.Error -> {
            }
        }
    }

    UiEventHandler(uiEvent = viewModel.uiEvent)

    OnboardingLayout(
        title = stringResource(Res.string.onboarding_welcome_title),
        subtitle = stringResource(Res.string.onboarding_welcome_subtitle),
        currentStep = OnboardingConstants.Steps.WELCOME,
        onNextClick = {
            scope.launch {
                viewModel.setLoadingState()
                val signInResult = emailPasswordSignIn.signIn(email, password)
                viewModel.onEvent(AuthEvent.OnSignInResult(signInResult))
            }
        },
        nextButtonText = stringResource(Res.string.onboarding_welcome_get_started),
        nextButtonEnabled = state != SignInState.Loading && state != SignInState.Syncing &&
                email.isNotBlank() && password.isNotBlank(),
        nextButtonLoading = state == SignInState.Loading || state == SignInState.Syncing
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(spacing.large),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(spacing.large)
        ) {
            AnimatedHeroSection()

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(spacing.small)
            ) {
                Text(
                    text = stringResource(Res.string.onboarding_welcome_tagline),
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.headlineSmall,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.primary
                )

                Text(
                    text = stringResource(Res.string.onboarding_welcome_subtitle_motivational),
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(spacing.small)
            ) {
                NativeTextField(
                    value = email,
                    onValueChange = { email = it },
                    placeholder = "Email",
                    isSecure = false,
                    keyboardType = UIKeyboardTypeEmailAddress,
                )
                NativeTextField(
                    value = password,
                    onValueChange = { password = it },
                    placeholder = "Password",
                    isSecure = true,
                )
            }

            FeatureHighlights()
        }
    }
}

@OptIn(ExperimentalForeignApi::class, BetaInteropApi::class)
@Composable
private fun NativeTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    isSecure: Boolean,
    modifier: Modifier = Modifier,
    keyboardType: UIKeyboardType = UIKeyboardTypeDefault,
) {
    UIKitView(
        factory = {
            val textField = object : UITextField(CGRectMake(0.0, 0.0, 0.0, 0.0)) {
                @ObjCAction
                fun editingChanged() {
                    onValueChange(text ?: "")
                }
            }
            textField.apply {
                this.placeholder = placeholder
                secureTextEntry = isSecure
                this.keyboardType = keyboardType
                autocapitalizationType =
                    UITextAutocapitalizationType.UITextAutocapitalizationTypeNone
                autocorrectionType = UITextAutocorrectionType.UITextAutocorrectionTypeNo
                borderStyle = UITextBorderStyle.UITextBorderStyleRoundedRect
                addTarget(
                    target = textField,
                    action = NSSelectorFromString(textField::editingChanged.name),
                    forControlEvents = UIControlEventEditingChanged,
                )
            }
        },
        modifier = modifier.fillMaxWidth().height(44.dp), update = { textField ->
            if (textField.text != value) textField.text = value
        },
        properties = UIKitInteropProperties(
            isInteractive = true,
            isNativeAccessibilityEnabled = true
        )
    )
}
