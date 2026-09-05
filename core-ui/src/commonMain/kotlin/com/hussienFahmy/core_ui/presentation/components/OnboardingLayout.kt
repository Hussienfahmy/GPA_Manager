package com.hussienfahmy.core_ui.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import org.jetbrains.compose.resources.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.dropUnlessResumed
import com.hussienfahmy.core.generated.resources.*
import com.hussienfahmy.core_ui.presentation.components.meadow.PillButton
import com.hussienfahmy.core_ui.presentation.components.meadow.PillButtonStyle
import com.hussienfahmy.core_ui.theme.MeadowTheme

@Composable
fun OnboardingLayout(
    title: String,
    subtitle: String? = null,
    currentStep: Int,
    onBackClick: (() -> Unit)? = null,
    onNextClick: (() -> Unit)? = null,
    onSkipClick: (() -> Unit)? = null,
    nextButtonText: String? = null,
    showSkip: Boolean = false,
    nextButtonEnabled: Boolean = true,
    nextButtonLoading: Boolean = false,
    enableScrolling: Boolean = true,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    val colors = MeadowTheme.colors

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(colors.paper)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.displaySmall.copy(fontSize = 24.sp),
            color = colors.ink,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = if (subtitle != null) 6.dp else 14.dp)
        )

        subtitle?.let {
            Text(
                text = it,
                style = MaterialTheme.typography.bodyMedium,
                color = colors.inkMuted,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 14.dp)
            )
        }

        Box(
            modifier = Modifier
                .weight(1f)
                .then(
                    if (enableScrolling) Modifier.verticalScroll(rememberScrollState())
                    else Modifier
                )
        ) {
            content()
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (currentStep > 1 && onBackClick != null) {
                PillButton(
                    text = stringResource(Res.string.onboarding_back),
                    onClick = dropUnlessResumed { onBackClick() },
                    style = PillButtonStyle.Text,
                    compact = true,
                )
            } else {
                Spacer(modifier = Modifier.weight(0.3f))
            }

            if (showSkip && onSkipClick != null) {
                PillButton(
                    text = stringResource(Res.string.onboarding_skip_for_now),
                    onClick = dropUnlessResumed { onSkipClick() },
                    style = PillButtonStyle.Text,
                    compact = true,
                )
            } else {
                Spacer(modifier = Modifier.weight(0.4f))
            }

            if (onNextClick != null) {
                if (nextButtonLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(28.dp),
                        strokeWidth = 3.dp,
                        color = MeadowTheme.accent.accent,
                    )
                } else {
                    PillButton(
                        text = nextButtonText ?: stringResource(Res.string.onboarding_next),
                        onClick = dropUnlessResumed { onNextClick() },
                        style = PillButtonStyle.Primary,
                        enabled = nextButtonEnabled,
                    )
                }
            } else {
                Spacer(modifier = Modifier.weight(0.3f))
            }
        }
    }
}
