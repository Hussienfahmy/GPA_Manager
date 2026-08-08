package com.hussienfahmy.onboarding_presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import com.hussienfahmy.core.domain.auth.service.AppleSignIn
import com.hussienfahmy.core.generated.resources.*
import com.hussienfahmy.core_ui.LocalSpacing
import com.hussienfahmy.core_ui.presentation.components.OnboardingConstants
import com.hussienfahmy.core_ui.presentation.components.OnboardingLayout
import com.hussienfahmy.core_ui.presentation.util.UiEventHandler
import com.hussienfahmy.onboarding_presentation.sign_in.AuthEvent
import com.hussienfahmy.onboarding_presentation.sign_in.SignInState
import com.hussienfahmy.onboarding_presentation.sign_in.SignInViewModel
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject

@Composable
actual fun OnBoardingScreen(
    viewModel: SignInViewModel,
    onSignInSuccess: () -> Unit,
) {
    val appleSignIn = koinInject<AppleSignIn>()
    val scope = rememberCoroutineScope()
    val spacing = LocalSpacing.current

    val state by viewModel.state

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
                val signInResult = appleSignIn.signIn()
                viewModel.onEvent(AuthEvent.OnSignInResult(signInResult))
            }
        },
        nextButtonText = stringResource(Res.string.onboarding_welcome_get_started),
        nextButtonEnabled = state != SignInState.Loading && state != SignInState.Syncing,
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

            FeatureHighlights()
        }
    }
}
