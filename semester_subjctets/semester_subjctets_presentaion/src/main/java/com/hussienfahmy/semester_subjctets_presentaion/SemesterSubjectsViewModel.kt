package com.hussienfahmy.semester_subjctets_presentaion

import androidx.lifecycle.viewModelScope
import com.hussienfahmy.core.data.local.util.UpdateResult
import com.hussienfahmy.core.domain.analytics.AnalyticsLogger
import com.hussienfahmy.core.domain.analytics.AnalyticsValues
import com.hussienfahmy.core.domain.grades.use_case.GetActiveGradeNames
import com.hussienfahmy.core_ui.presentation.model.UiEvent
import com.hussienfahmy.core_ui.presentation.viewmodel.UiViewModel
import com.hussienfahmy.semester_subjctets_domain.use_case.CalculationUseCases
import com.hussienfahmy.semester_subjctets_domain.use_case.ClearGrade
import com.hussienfahmy.semester_subjctets_domain.use_case.PredictGrades
import com.hussienfahmy.semester_subjctets_domain.use_case.SubjectUseCases
import com.hussienfahmy.semester_subjctets_presentaion.model.Mode
import com.hussienfahmy.semester_subjctets_presentaion.model.ModeResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class SemesterSubjectsViewModel(
    private val calculationUseCases: CalculationUseCases,
    private val subjectUseCases: SubjectUseCases,
    private val getActiveGradeNames: GetActiveGradeNames,
    private val analyticsLogger: AnalyticsLogger,
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
            getActiveGradeNames()
        ) { subjectsWithGrade, mode, fixedSubjectsIds, activeGradeNames ->
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
                        grades = activeGradeNames,
                        mode = mode,
                        modeResult = ModeResult.Normal(calculationResult)
                    )
                }

                is Mode.Predict -> {
                    val predictionResult = calculationUseCases.predictGrades(
                        subjectWithAssignedGrades = subjects,
                        targetCumulativeGPA = mode.targetCumulativeGPA,
                        reverseSubjects = mode.reverseSubjects
                    )

                    // Log prediction calculation
                    analyticsLogger.logPredictionCalculated(
                        isAchievable = predictionResult is PredictGrades.Result.TargetAchieved,
                        fixedSubjectsCount = fixedSubjectsIds.size
                    )

                    if (predictionResult is PredictGrades.Result.Failed) predictionResult.message?.let {
                        _uiEvent.send(UiEvent.ShowSnackBar(it))
                    }

                    SemesterSubjectsState.Loaded(
                        subjects = subjects,
                        grades = activeGradeNames,
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
                is SemesterSubjectsEvent.AddSubject -> {
                    analyticsLogger.logSubjectAdded(
                        creditHours = event.creditHours.toDoubleOrNull() ?: 0.0,
                        hasAssessments = mapOf(
                            AnalyticsValues.ASSESSMENT_MIDTERM to event.midtermAvailable,
                            AnalyticsValues.ASSESSMENT_PRACTICAL to event.practicalAvailable,
                            AnalyticsValues.ASSESSMENT_ORAL to event.oralAvailable
                        )
                    )
                    subjectUseCases.addSubject(
                        event.subjectName,
                        event.creditHours,
                        event.midtermAvailable,
                        event.practicalAvailable,
                        event.oralAvailable,
                        event.projectAvailable
                    )
                }

                is SemesterSubjectsEvent.CLearAll -> {
                    val currentSubjects = (state.value as? SemesterSubjectsState.Loaded)?.subjects ?: emptyList()
                    analyticsLogger.logBulkAction(
                        actionType = AnalyticsValues.BULK_ACTION_CLEAR_ALL,
                        affectedCount = currentSubjects.size
                    )
                    mode.value = Mode.Normal
                    subjectUseCases.clearGrade(ClearGrade.Request.All)
                }

                is SemesterSubjectsEvent.ClearGrade -> subjectUseCases.clearGrade(
                    ClearGrade.Request.ById(
                        event.subjectId
                    )
                )

                is SemesterSubjectsEvent.DeleteSubject -> {
                    analyticsLogger.logSubjectDeleted(event.subjectId)
                    subjectUseCases.deleteSubject(event.subjectId)
                }
                is SemesterSubjectsEvent.SetGrade -> {
                    analyticsLogger.logGradeAssigned(
                        subjectId = event.subjectId,
                        gradeName = event.gradeName.name,
                    )
                    subjectUseCases.setGrade(
                        event.subjectId,
                        event.gradeName
                    )
                }

                is SemesterSubjectsEvent.UpdateName -> subjectUseCases.updateName(
                    event.subjectId,
                    event.subjectName
                )

                is SemesterSubjectsEvent.ChangeMode -> {
                    val currentSubjects = (state.value as? SemesterSubjectsState.Loaded)?.subjects ?: emptyList()
                    val currentSubjectsCount = currentSubjects.size
                    val fromMode = when (mode.value) {
                        Mode.Normal -> AnalyticsValues.MODE_NORMAL
                        is Mode.Predict -> AnalyticsValues.MODE_PREDICTIVE
                    }

                    mode.value = when (mode.value) {
                        Mode.Normal -> {
                            analyticsLogger.logCalculationModeSwitched(
                                fromMode = fromMode,
                                toMode = AnalyticsValues.MODE_PREDICTIVE,
                                subjectsCount = currentSubjectsCount
                            )
                            Mode.Predict()
                        }
                        is Mode.Predict -> {
                            analyticsLogger.logCalculationModeSwitched(
                                fromMode = fromMode,
                                toMode = AnalyticsValues.MODE_NORMAL,
                                subjectsCount = currentSubjectsCount
                            )
                            Mode.Normal
                        }
                    }
                }

                is SemesterSubjectsEvent.SubmitPredictiveData -> {
                    val currentSubjects = (state.value as? SemesterSubjectsState.Loaded)?.subjects ?: emptyList()
                    analyticsLogger.logPredictiveModeEnabled(
                        targetGpa = event.targetCumulativeGPA.toDoubleOrNull() ?: 0.0,
                        subjectsCount = currentSubjects.size,
                        reverseCalculation = event.reserveSubjects
                    )

                    analyticsLogger.logTargetGpaSet(
                        targetGpa = event.targetCumulativeGPA.toDoubleOrNull() ?: 0.0,
                        currentGpa = 0.0 // Will be available from calculation result
                    )

                    mode.value = Mode.Predict(
                        targetCumulativeGPA = event.targetCumulativeGPA,
                        reverseSubjects = event.reserveSubjects
                    )
                }

                is SemesterSubjectsEvent.FixGrade -> {
                    analyticsLogger.logGradeFixed(
                        isFixed = event.fixed
                    )

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