package com.hussienfahmy.onboarding_presentation

import android.app.Activity
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hussienfahmy.core.R
import com.hussienfahmy.core.domain.auth.service.AuthService
import com.hussienfahmy.core_ui.LocalSpacing
import com.hussienfahmy.core_ui.presentation.components.OnboardingConstants
import com.hussienfahmy.core_ui.presentation.components.OnboardingLayout
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
    val spacing = LocalSpacing.current
    val context = LocalContext.current

    val state by viewModel.state

    LaunchedEffect(key1 = state) {
        when (state) {
            SignInState.Success -> onSignInSuccess()
            SignInState.Initial,
            SignInState.Loading,
            SignInState.Error -> {
            }
        }
    }

    UiEventHandler(uiEvent = viewModel.uiEvent)

    OnboardingLayout(
        title = stringResource(R.string.onboarding_welcome_title),
        subtitle = stringResource(R.string.onboarding_welcome_subtitle),
        currentStep = OnboardingConstants.Steps.WELCOME,
        onNextClick = {
            scope.launch {
                viewModel.setLoadingState()
                val signInResult = authService.signIn(context as Activity)
                viewModel.onEvent(AuthEvent.OnSignInResult(signInResult))
            }
        },
        nextButtonText = stringResource(R.string.onboarding_welcome_get_started),
        nextButtonEnabled = state != SignInState.Loading,
        nextButtonLoading = state == SignInState.Loading
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(spacing.large),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(spacing.large)
        ) {
            // Animated Hero Section
            AnimatedHeroSection()

            // Welcome message with personality
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(spacing.small)
            ) {
                Text(
                    text = stringResource(R.string.onboarding_welcome_tagline),
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.headlineSmall,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.primary
                )

                Text(
                    text = stringResource(R.string.onboarding_welcome_subtitle_motivational),
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            // Feature highlights with icons
            FeatureHighlights()
        }
    }
}

@Composable
private fun AnimatedHeroSection() {
    val infiniteTransition = rememberInfiniteTransition(label = "hero_animation")

    val scale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.1f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "scale_animation"
    )

    val alpha by infiniteTransition.animateFloat(
        initialValue = 0.7f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "alpha_animation"
    )

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.size(120.dp)
    ) {
        // Background circle with gradient effect
        Box(
            modifier = Modifier
                .size(100.dp)
                .scale(scale)
                .alpha(alpha * 0.3f)
                .background(
                    MaterialTheme.colorScheme.primary,
                    CircleShape
                )
        )

        // Main graduation cap emoji
        Text(
            text = "🎓",
            style = MaterialTheme.typography.displayLarge.copy(
                fontSize = MaterialTheme.typography.displayLarge.fontSize * 1.2f
            ),
            modifier = Modifier.scale(scale)
        )
    }
}

@Composable
private fun FeatureHighlights() {
    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        FeatureCard(
            icon = Icons.Outlined.DateRange,
            title = stringResource(R.string.onboarding_feature_semester_title),
            description = stringResource(R.string.onboarding_feature_semester_desc)
        )

        FeatureCard(
            icon = Icons.Outlined.Check,
            title = stringResource(R.string.onboarding_feature_marks_title),
            description = stringResource(R.string.onboarding_feature_marks_desc)
        )

        FeatureCard(
            icon = Icons.Outlined.Star,
            title = stringResource(R.string.onboarding_feature_quick_title),
            description = stringResource(R.string.onboarding_feature_quick_desc)
        )

        FeatureCard(
            icon = Icons.Outlined.MoreVert,
            title = stringResource(R.string.onboarding_feature_more_title),
            description = stringResource(R.string.onboarding_feature_more_desc)
        )
    }
}

@Composable
private fun FeatureCard(
    icon: ImageVector,
    title: String,
    description: String
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(40.dp)
                    .background(
                        MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                        CircleShape
                    )
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(24.dp)
                )
            }

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleSmall.copy(
                        fontWeight = FontWeight.SemiBold
                    ),
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
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