package com.hussienfahmy.semester_subjctets_presentaion

import androidx.lifecycle.viewModelScope
import com.hussienFahmy.core.data.local.util.UpdateResult
import com.hussienFahmy.core.domain.grades.use_case.GetActiveGradeNames
import com.hussienFahmy.core_ui.presentation.model.UiEvent
import com.hussienFahmy.core_ui.presentation.viewmodel.UiViewModel
import com.hussienfahmy.semester_subjctets_domain.use_case.CalculationUseCases
import com.hussienfahmy.semester_subjctets_domain.use_case.ClearGrade
import com.hussienfahmy.semester_subjctets_domain.use_case.PredictGrades
import com.hussienfahmy.semester_subjctets_domain.use_case.SubjectUseCases
import com.hussienfahmy.semester_subjctets_presentaion.model.Mode
import com.hussienfahmy.semester_subjctets_presentaion.model.ModeResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SemesterSubjectsViewModel @Inject constructor(
    private val calculationUseCases: CalculationUseCases,
    private val subjectUseCases: SubjectUseCases,
    private val getActiveGradeNames: GetActiveGradeNames,
) : UiViewModel<SemesterSubjectsEvent, SemesterSubjectsState>(
    initialState = { SemesterSubjectsState.Loading },
) {

    private val subjectsWithGrade = subjectUseCases.observeSubjectsWithGrades()
    private val mode = MutableStateFlow<Mode>(Mode.Normal)
    private val fixedSubjectsIds = MutableStateFlow<List<Long>>(emptyList())

    init {
        // recalculate when subjects or mode changes
        combine(
            subjectsWithGrade.distinctUntilChanged(),
            mode,
            fixedSubjectsIds,
        ) { subjectsWithGrade, mode, fixedSubjectsIds ->
            val calculationResult =
                calculationUseCases.calculate(subjectsWithGrade)

            val subjects = subjectsWithGrade.map { subject ->
                if (fixedSubjectsIds.contains(subject.id))
                    subject.copy(fixedGrade = true)
                else subject
            }

            when (mode) {
                is Mode.Normal -> {
                    SemesterSubjectsState.Loaded(
                        subjects = subjects,
                        grades = getActiveGradeNames(),
                        mode = mode,
                        modeResult = ModeResult.Normal(calculationResult)
                    )
                }

                is Mode.Predict -> {
                    val predictionResult = calculationUseCases.predictGrades(
                        subjectWithAssignedGrades = subjects,
                        targetCumulativeGPA = mode.targetCumulativeGPA,
                        reverseSubjects = mode.reserveSubjects
                    )

                    if (predictionResult is PredictGrades.Result.Failed) predictionResult.message?.let {
                        _uiEvent.send(UiEvent.ShowSnackBar(it))
                    }

                    SemesterSubjectsState.Loaded(
                        subjects = subjects,
                        grades = getActiveGradeNames(),
                        mode = mode,
                        modeResult = ModeResult.Predict(
                            result = calculationResult,
                            predictGradesResult = predictionResult
                        )
                    )
                }
            }
        }.onEach {
            state.value = it
        }.launchIn(viewModelScope)
    }

    override fun onEvent(event: SemesterSubjectsEvent) {
        viewModelScope.launch {
            val updateResult: Any = when (event) {
                is SemesterSubjectsEvent.AddSubject -> subjectUseCases.addSubject(
                    event.subjectName,
                    event.creditHours
                )

                is SemesterSubjectsEvent.CLearAll -> subjectUseCases.clearGrade(ClearGrade.Request.All)

                is SemesterSubjectsEvent.ClearGrade -> subjectUseCases.clearGrade(
                    ClearGrade.Request.ById(
                        event.subjectId
                    )
                )

                is SemesterSubjectsEvent.DeleteSubject -> subjectUseCases.deleteSubject(event.subjectId)
                is SemesterSubjectsEvent.SetGrade -> subjectUseCases.setGrade(
                    event.subjectId,
                    event.gradeName
                )

                is SemesterSubjectsEvent.UpdateName -> subjectUseCases.updateName(
                    event.subjectId,
                    event.subjectName
                )

                is SemesterSubjectsEvent.ChangeMode -> mode.value = when (mode.value) {
                    Mode.Normal -> Mode.Predict()
                    is Mode.Predict -> Mode.Normal
                }

                is SemesterSubjectsEvent.SubmitPredictiveData -> {
                    mode.value = Mode.Predict(
                        targetCumulativeGPA = event.targetCumulativeGPA,
                        reserveSubjects = event.reserveSubjects
                    )
                }

                is SemesterSubjectsEvent.FixGrade -> {
                    if (event.fixed) {
                        fixedSubjectsIds.value = fixedSubjectsIds.value + event.subjectId
                    } else {
                        fixedSubjectsIds.value = fixedSubjectsIds.value - event.subjectId
                    }
                }
            }

            if (updateResult is UpdateResult.Failed) {
                _uiEvent.send(UiEvent.ShowSnackBar(updateResult.message))
            }
        }
    }
}