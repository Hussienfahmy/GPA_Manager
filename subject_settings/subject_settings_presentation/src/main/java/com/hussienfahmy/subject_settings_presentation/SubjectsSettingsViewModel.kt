package com.hussienfahmy.subject_settings_presentation

import androidx.lifecycle.viewModelScope
import com.hussienFahmy.core.data.local.util.UpdateResult
import com.hussienFahmy.core_ui.presentation.model.UiEvent
import com.hussienFahmy.core_ui.presentation.viewmodel.UiViewModel
import com.hussienfahmy.subject_settings_domain.use_case.SubjectSettingsUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SubjectsSettingsViewModel @Inject constructor(
    private val useCases: SubjectSettingsUseCases,
) : UiViewModel<SubjectSettingsEvent, SubjectSettingsState>(initialState = {
    SubjectSettingsState.Loading
}) {
    init {
        useCases.observeSubjectSettings()
            .onEach {
                state.value = SubjectSettingsState.Success(it)
            }.launchIn(viewModelScope)
    }

    override fun onEvent(event: SubjectSettingsEvent) {
        viewModelScope.launch {
            val result = when (event) {
                is SubjectSettingsEvent.UpdateConstantMarks -> useCases.updateConstantMarks(event.constantMarks)
                is SubjectSettingsEvent.UpdateMarksPerCreditHour -> useCases.updateMarksPerCreditHour(
                    event.marksPerCreditHour
                )

                is SubjectSettingsEvent.UpdateSubjectMarksDependsOn -> useCases.updateSubjectsDependsOn(
                    event.dependsOn
                )
            }

            if (result is UpdateResult.Failed) {
                _uiEvent.send(
                    UiEvent.ShowSnackBar(result.message)
                )
            }
        }
    }
}