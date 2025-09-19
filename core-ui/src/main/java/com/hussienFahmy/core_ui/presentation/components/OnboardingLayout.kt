package com.hussienfahmy.core_ui.presentation.components

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
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.dropUnlessResumed
import com.hussienfahmy.core.R
import com.hussienfahmy.core_ui.LocalSpacing

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
    val spacing = LocalSpacing.current

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(spacing.medium)
    ) {

        // Title and subtitle
        Text(
            text = title,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = if (subtitle != null) spacing.small else spacing.medium)
        )

        subtitle?.let {
            Text(
                text = it,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = spacing.medium)
            )
        }

        // Content
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

        // Navigation buttons
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = spacing.medium),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Back button or spacer (show back button for all steps except the first)
            if (currentStep > 1 && onBackClick != null) {
                TextButton(onClick = dropUnlessResumed {
                    onBackClick()
                }) {
                    Text(stringResource(R.string.onboarding_back))
                }
            } else {
                Spacer(modifier = Modifier.weight(0.3f))
            }

            // Skip button (centered when available)
            if (showSkip && onSkipClick != null) {
                TextButton(onClick = dropUnlessResumed {
                    onSkipClick()
                }) {
                    Text(stringResource(R.string.onboarding_skip_for_now))
                }
            } else {
                Spacer(modifier = Modifier.weight(0.4f))
            }

            // Next button
            if (onNextClick != null) {
                OutlinedButton(
                    onClick = dropUnlessResumed {
                        onNextClick()
                    },
                    enabled = nextButtonEnabled && !nextButtonLoading
                ) {
                    if (nextButtonLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(16.dp),
                            strokeWidth = 2.dp
                        )
                    } else {
                        Text(nextButtonText ?: stringResource(R.string.onboarding_next))
                    }
                }
            } else {
                Spacer(modifier = Modifier.weight(0.3f))
            }
        }
    }
}

@Preview
@Composable
fun OnboardingLayoutPreview() {
    OnboardingLayout(
        title = "Complete Your Profile",
        subtitle = "Help us personalize your experience by sharing some basic information",
        currentStep = 2,
        onBackClick = {},
        onNextClick = {},
        onSkipClick = {},
        showSkip = true
    ) {
        Text(
            text = "Content goes here...",
            modifier = Modifier.padding(16.dp)
        )
    }
}