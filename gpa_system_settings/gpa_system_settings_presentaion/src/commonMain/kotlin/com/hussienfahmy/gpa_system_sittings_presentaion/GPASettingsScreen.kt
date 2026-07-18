package com.hussienfahmy.gpa_system_sittings_presentaion

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hussienfahmy.core.domain.gpa_settings.model.GPA
import com.hussienfahmy.core_ui.theme.MeadowAccentProvider
import com.hussienfahmy.core_ui.theme.MeadowTheme
import com.hussienfahmy.gpa_system_sittings_presentaion.components.GPASystemItem
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun GPASettingsScreen(
    modifier: Modifier = Modifier,
    viewModel: GPASettingsViewModel = koinViewModel(),
) {
    val state by viewModel.state

    MeadowAccentProvider(MeadowTheme.colors.semester) {
        when (state) {
            is GPAState.Loading -> {
                Box(modifier = modifier.fillMaxSize()) {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
            }

            is GPAState.Loaded -> {
                val gpa = (state as GPAState.Loaded).gpa
                GPASettingsContent(
                    modifier = modifier.fillMaxSize(),
                    gpa = gpa,
                    onGPASystemChanged = { viewModel.onEvent(GPAEvent.UpdateGPASystem(it)) }
                )
            }
        }
    }
}

@Composable
fun GPASettingsContent(
    modifier: Modifier,
    gpa: GPA,
    onGPASystemChanged: (GPA.System) -> Unit
) {
    Column(modifier.padding(16.dp)) {
        GPASystemItem(
            currentGPASystem = gpa.system,
            onGPASystemChanged = onGPASystemChanged,
        )
    }
}

@Preview
@Composable
fun GPASettingsContentPreview() {
    MeadowTheme(darkTheme = false) {
        MeadowAccentProvider(MeadowTheme.colors.semester) {
            GPASettingsContent(
                modifier = Modifier.fillMaxSize(),
                gpa = GPA(system = GPA.System.FOUR),
                onGPASystemChanged = {}
            )
        }
    }
}