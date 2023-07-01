package com.hussienfahmy.gpa_system_sittings_presentaion

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.hussienFahmy.core.domain.gpa_settings.model.GPA
import com.hussienFahmy.core_ui.LocalSpacing
import com.hussienfahmy.gpa_system_sittings_presentaion.components.GPASystemItem

@Composable
fun GPASettingsScreen(
    modifier: Modifier,
    viewModel: GPASettingsViewModel = hiltViewModel(),
) {
    val state by viewModel.state

    when (state) {
        is GPAState.Loading -> {
            Box(modifier = modifier.fillMaxSize()) {
                CircularProgressIndicator()
            }
        }

        is GPAState.Loaded -> {
            val gpa = (state as GPAState.Loaded).gpa
            GPASettingsContent(
                modifier = modifier,
                gpa = gpa,
                onGPASystemChanged = { viewModel.onEvent(GPAEvent.UpdateGPASystem(it)) }
            )
        }
    }
}

@Composable
fun GPASettingsContent(
    modifier: Modifier,
    gpa: GPA,
    onGPASystemChanged: (GPA.System) -> Unit
) {
    val spacing = LocalSpacing.current

    Column(modifier.padding(spacing.extraSmall)) {
        Card {
            Column(modifier = Modifier.padding(spacing.small)) {
                GPASystemItem(
                    currentGPASystem = gpa.system,
                    onGPASystemChanged = onGPASystemChanged
                )
            }
        }
    }
}

@Preview
@Composable
fun GPASettingsContentPreview() {
    GPASettingsContent(
        modifier = Modifier.fillMaxSize(),
        gpa = GPA(system = GPA.System.FOUR),
        onGPASystemChanged = {}
    )
}