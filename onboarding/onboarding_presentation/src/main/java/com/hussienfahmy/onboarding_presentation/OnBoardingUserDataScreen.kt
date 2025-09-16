package com.hussienfahmy.onboarding_presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.hussienfahmy.core.domain.analytics.AnalyticsLogger
import com.hussienfahmy.core.domain.analytics.AnalyticsValues
import com.hussienfahmy.core_ui.LocalSpacing
import com.hussienfahmy.core_ui.presentation.user_data.UserDataScreen
import org.koin.compose.koinInject

@Composable
fun OnBoardingUserDataScreen(
    snackBarHostState: SnackbarHostState,
    onNextClick: () -> Unit,
) {
    val spacing = LocalSpacing.current
    val analyticsLogger: AnalyticsLogger = koinInject()

    Column(modifier = Modifier.fillMaxSize()) {

        Text(
            text = "Let's Complete your data",
            modifier = Modifier
                .padding(top = spacing.small)
                .align(Alignment.CenterHorizontally),
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(modifier = Modifier.weight(1f))

        UserDataScreen(snackBarHostState = snackBarHostState)

        Spacer(modifier = Modifier.weight(1f))

        OutlinedButton(
            onClick = {
                // Log profile setup completion
                analyticsLogger.logProfileSetupCompleted(completionPercentage = 100)
                onNextClick()
            },
            modifier = Modifier
                .padding(bottom = spacing.medium)
                .align(Alignment.CenterHorizontally)
        ) {
            Text(text = "Next")
        }
    }
}