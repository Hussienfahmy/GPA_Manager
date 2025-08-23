package com.hussienfahmy.onboarding_presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.hussienfahmy.core.domain.auth.service.AuthService
import com.hussienfahmy.core_ui.LocalSpacing
import com.hussienfahmy.core_ui.presentation.util.UiEventHandler
import com.hussienfahmy.onboarding_presentation.sign_in.AuthEvent
import com.hussienfahmy.onboarding_presentation.sign_in.SignInState
import com.hussienfahmy.onboarding_presentation.sign_in.SignInViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.koinInject

@Composable
fun OnBoardingScreen(
    viewModel: SignInViewModel = koinViewModel(),
    onSignInSuccess: () -> Unit
) {
    val authService = koinInject<AuthService>()
    val scope = rememberCoroutineScope()

    val state by viewModel.state

    LaunchedEffect(key1 = state) {
        when (state) {
            SignInState.Success -> onSignInSuccess()
            SignInState.Initial -> {}
        }
    }

    UiEventHandler(uiEvent = viewModel.uiEvent)

    val spacing = LocalSpacing.current

    Box(modifier = Modifier.fillMaxSize()) {
        Text(
            text = "Let's make your GPA great again!",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.align(Alignment.Center)
        )

        OutlinedButton(
            onClick = {
                scope.launch {
                    val signInResult = authService.signIn()

                    viewModel.onEvent(AuthEvent.OnSignInResult(signInResult))
                }
            },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = spacing.medium)
        ) {
            Text(text = "Sign in")
        }
    }
}

@Preview
@Composable
fun OnBoardingScreenPreview() {
    OnBoardingScreen(
        onSignInSuccess = {}
    )
}