package com.hussienfahmy.onboarding_presentation

import android.app.Activity
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AutoAwesome
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.History
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import org.jetbrains.compose.resources.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hussienfahmy.core.generated.resources.Res
import com.hussienfahmy.core.domain.auth.service.AuthService
import com.hussienfahmy.core_ui.LocalSpacing
import com.hussienfahmy.core_ui.presentation.components.OnboardingConstants
import com.hussienfahmy.core_ui.presentation.components.OnboardingLayout
import com.hussienfahmy.core_ui.presentation.components.meadow.MeadowCard
import com.hussienfahmy.core_ui.presentation.util.UiEventHandler
import com.hussienfahmy.core_ui.theme.MeadowTheme
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
                val signInResult = authService.signIn(context as Activity)
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
            // Animated Hero Section
            AnimatedHeroSection()

            // Welcome message with personality
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

            // Feature highlights with icons
            FeatureHighlights()
        }
    }
}

/** Brand ring mark (design 2d): a 3/4-swept green ring with "4.0" centered,
 *  a filled inner disc, and three slowly-orbiting spark dots in the tab hues. */
@Composable
private fun AnimatedHeroSection() {
    val colors = MeadowTheme.colors
    val accent = MeadowTheme.accent

    val infiniteTransition = rememberInfiniteTransition(label = "hero_animation")
    val orbit by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(60_000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart,
        ),
        label = "orbit",
    )

    Box(contentAlignment = Alignment.Center, modifier = Modifier.size(120.dp)) {
        Canvas(modifier = Modifier.size(120.dp)) {
            val stroke = 10.dp.toPx()
            val radius = (size.minDimension - stroke) / 2f
            drawCircle(color = colors.ringTrack, radius = radius, style = Stroke(width = stroke))
            drawArc(
                color = accent.accent,
                startAngle = -90f,
                sweepAngle = 360f * 0.75f,
                useCenter = false,
                topLeft = Offset(center.x - radius, center.y - radius),
                size = Size(radius * 2, radius * 2),
                style = Stroke(width = stroke, cap = StrokeCap.Round),
            )
            drawCircle(color = accent.container, radius = size.minDimension * (32f / 120f))
        }

        Text(
            text = "4.0",
            style = MaterialTheme.typography.displaySmall.copy(fontSize = 26.sp),
            color = accent.deep,
        )

        SparkDot(colors.marks.accent, angle = orbit, distance = 58.dp, size = 6.dp)
        SparkDot(colors.history.accent, angle = orbit + 140f, distance = 60.dp, size = 5.dp)
        SparkDot(colors.quick.accent, angle = orbit + 250f, distance = 62.dp, size = 4.dp)
    }
}

@Composable
private fun SparkDot(
    color: androidx.compose.ui.graphics.Color,
    angle: Float,
    distance: androidx.compose.ui.unit.Dp,
    size: androidx.compose.ui.unit.Dp,
) {
    val rad = Math.toRadians(angle.toDouble())
    Box(
        modifier = Modifier
            .offset(
                x = (distance.value * kotlin.math.cos(rad)).dp,
                y = (distance.value * kotlin.math.sin(rad)).dp,
            )
            .size(size)
            .clip(CircleShape)
            .background(color)
    )
}

private data class Feature(
    val icon: ImageVector,
    val title: String,
    val description: String,
    val accent: com.hussienfahmy.core_ui.theme.MeadowAccent,
)

@Composable
private fun FeatureHighlights() {
    val colors = MeadowTheme.colors
    val features = listOf(
        Feature(
            Icons.Outlined.CalendarMonth,
            stringResource(Res.string.onboarding_feature_semester_title),
            stringResource(Res.string.onboarding_feature_semester_desc),
            colors.semester,
        ),
        Feature(
            Icons.Outlined.Check,
            stringResource(Res.string.onboarding_feature_marks_title),
            stringResource(Res.string.onboarding_feature_marks_desc),
            colors.marks,
        ),
        Feature(
            Icons.Outlined.History,
            stringResource(Res.string.onboarding_feature_more_title),
            stringResource(Res.string.onboarding_feature_more_desc),
            colors.history,
        ),
        Feature(
            Icons.Outlined.AutoAwesome,
            stringResource(Res.string.onboarding_feature_quick_title),
            stringResource(Res.string.onboarding_feature_quick_desc),
            colors.quick,
        ),
    )

    Column(
        verticalArrangement = Arrangement.spacedBy(10.dp),
        modifier = Modifier.fillMaxWidth(),
    ) {
        features.chunked(2).forEach { rowFeatures ->
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                rowFeatures.forEach { feature ->
                    FeatureCard(feature = feature, modifier = Modifier.weight(1f))
                }
            }
        }
    }
}

@Composable
private fun FeatureCard(feature: Feature, modifier: Modifier = Modifier) {
    val colors = MeadowTheme.colors

    MeadowCard(
        modifier = modifier,
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 14.dp),
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(34.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(feature.accent.container),
        ) {
            Icon(
                imageVector = feature.icon,
                contentDescription = null,
                tint = feature.accent.deep,
                modifier = Modifier.size(18.dp),
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = feature.title,
            style = MaterialTheme.typography.titleSmall.copy(fontSize = 13.5.sp),
            color = colors.ink,
        )
        Text(
            text = feature.description,
            style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.5.sp, lineHeight = 15.sp),
            color = colors.inkFaint,
        )
    }
}

@Preview
@Composable
fun OnBoardingScreenPreview() {
    OnBoardingScreen(
        onSignInSuccess = {}
    )
}