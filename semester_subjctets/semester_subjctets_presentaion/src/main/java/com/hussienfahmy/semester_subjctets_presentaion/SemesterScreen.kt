package com.hussienfahmy.semester_subjctets_presentaion

import android.content.res.Configuration
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.hussienFahmy.core.data.local.model.GradeName
import com.hussienFahmy.core_ui.LocalSpacing
import com.hussienFahmy.core_ui.presentation.components.AddSubjectsHint
import com.hussienFahmy.core_ui.presentation.util.UiEventHandler
import com.hussienfahmy.semester_subjctets_domain.model.Grade
import com.hussienfahmy.semester_subjctets_domain.model.Subject
import com.hussienfahmy.semester_subjctets_domain.use_case.Calculate
import com.hussienfahmy.semester_subjctets_presentaion.components.AddSubjectDialog
import com.hussienfahmy.semester_subjctets_presentaion.components.Controllers
import com.hussienfahmy.semester_subjctets_presentaion.components.RenameDialog
import com.hussienfahmy.semester_subjctets_presentaion.components.ResultCard
import com.hussienfahmy.semester_subjctets_presentaion.components.SubjectsColumn
import com.hussienfahmy.semester_subjctets_presentaion.model.Mode
import com.hussienfahmy.semester_subjctets_presentaion.model.ModeResult


@Composable
fun SemesterScreen(
    modifier: Modifier = Modifier,
    snackBarHostState: SnackbarHostState,
    viewModel: SemesterSubjectsViewModel = hiltViewModel(),
) {
    UiEventHandler(uiEvent = viewModel.uiEvent, snackBarHostState = snackBarHostState)

    var showAddDialog by remember { mutableStateOf(false) }

    if (showAddDialog) {
        AddSubjectDialog(
            onDismissClick = { showAddDialog = false },
            onAddSubject = { name, creditHours, midtermAvailable, practicalAvailable, oralAvailable, projectAvailable ->
                viewModel.onEvent(
                    SemesterSubjectsEvent.AddSubject(
                        name,
                        creditHours,
                        midtermAvailable,
                        practicalAvailable,
                        oralAvailable,
                        projectAvailable
                    )
                )
            }
        )
    }

    var showRenameDialog: Subject? by remember { mutableStateOf(null) }

    if (showRenameDialog != null) {
        RenameDialog(
            onDismiss = { showRenameDialog = null },
            onSaveClick = { newName ->
                viewModel.onEvent(
                    SemesterSubjectsEvent.UpdateName(showRenameDialog!!.id, newName)
                )
            },
            subjectCurrentName = showRenameDialog!!.name
        )
    }

    val state by viewModel.state

    val spacing = LocalSpacing.current

    when (state) {
        is SemesterSubjectsState.Loading -> Box(modifier = Modifier.fillMaxSize()) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        }

        is SemesterSubjectsState.Loaded -> {
            val loadedState = state as SemesterSubjectsState.Loaded
            SemesterScreenContent(
                modifier = modifier.padding(spacing.small),
                state = loadedState,
                onAddClick = { showAddDialog = true },
                onChangeModeClick = { viewModel.onEvent(SemesterSubjectsEvent.ChangeMode) },
                onResetClick = { viewModel.onEvent(SemesterSubjectsEvent.CLearAll) },
                onPredictDataChange = { (targetCumulativeGpa, reverseSubjects) ->
                    viewModel.onEvent(
                        SemesterSubjectsEvent.SubmitPredictiveData(
                            targetCumulativeGpa,
                            reverseSubjects
                        )
                    )
                },
                onGradeClick = { subjectId, grade ->
                    viewModel.onEvent(
                        SemesterSubjectsEvent.SetGrade(subjectId, grade)
                    )
                },
                onResetSubjectClick = { subjectId ->
                    viewModel.onEvent(
                        SemesterSubjectsEvent.ClearGrade(subjectId)
                    )
                },
                onDeleteSubject = { subjectId ->
                    viewModel.onEvent(
                        SemesterSubjectsEvent.DeleteSubject(subjectId)
                    )
                },
                onRenameClick = { showRenameDialog = it },
                onFixGradeClick = { subjectId, fixed ->
                    viewModel.onEvent(
                        SemesterSubjectsEvent.FixGrade(subjectId, fixed)
                    )
                }
            )
        }
    }
}

@Composable
fun SemesterScreenContent(
    modifier: Modifier = Modifier,
    state: SemesterSubjectsState.Loaded,
    onAddClick: () -> Unit,
    onChangeModeClick: () -> Unit,
    onResetClick: () -> Unit,
    onPredictDataChange: (Pair<String, Boolean>) -> Unit,
    onRenameClick: (subject: Subject) -> Unit,
    onDeleteSubject: (subjectId: Long) -> Unit,
    onFixGradeClick: (subjectId: Long, fixed: Boolean) -> Unit,
    onResetSubjectClick: (subjectId: Long) -> Unit,
    onGradeClick: (subjectId: Long, grade: GradeName) -> Unit,
) {
    val configuration = LocalConfiguration.current
    when (configuration.orientation) {
        Configuration.ORIENTATION_LANDSCAPE -> SemesterContentLandscape(
            modifier = modifier,
            state = state,
            onAddClick = onAddClick,
            onChangeModeClick = onChangeModeClick,
            onResetClick = onResetClick,
            onPredictDataChange = onPredictDataChange,
            onRenameClick = onRenameClick,
            onDeleteSubject = onDeleteSubject,
            onFixGradeClick = onFixGradeClick,
            onResetSubjectClick = onResetSubjectClick,
            onGradeClick = onGradeClick
        )

        else -> SemesterContentPortrait(
            modifier = modifier,
            state = state,
            onAddClick = onAddClick,
            onChangeModeClick = onChangeModeClick,
            onResetClick = onResetClick,
            onPredictDataChange = onPredictDataChange,
            onRenameClick = onRenameClick,
            onDeleteSubject = onDeleteSubject,
            onFixGradeClick = onFixGradeClick,
            onResetSubjectClick = onResetSubjectClick,
            onGradeClick = onGradeClick
        )
    }
}

@Composable
fun SemesterContentPortrait(
    modifier: Modifier = Modifier,
    state: SemesterSubjectsState.Loaded,
    onAddClick: () -> Unit,
    onChangeModeClick: () -> Unit,
    onResetClick: () -> Unit,
    onPredictDataChange: (Pair<String, Boolean>) -> Unit,
    onRenameClick: (subject: Subject) -> Unit,
    onDeleteSubject: (subjectId: Long) -> Unit,
    onFixGradeClick: (subjectId: Long, fixed: Boolean) -> Unit,
    onResetSubjectClick: (subjectId: Long) -> Unit,
    onGradeClick: (subjectId: Long, grade: GradeName) -> Unit,
) {
    val spacing = LocalSpacing.current

    Column(modifier = modifier) {
        Controllers(
            mode = state.mode,
            onChangeModeClick = onChangeModeClick,
            onAddClick = onAddClick,
            onResetClick = onResetClick
        )

        Spacer(modifier = Modifier.height(spacing.small))

        ResultCard(
            calculationResult = state.modeResult.calculationResult,
            onTargetGPAChange = { targetGPA, reverseOrder ->
                onPredictDataChange(targetGPA to reverseOrder)
            },
            mode = state.mode,
        )

        Spacer(modifier = Modifier.height(spacing.small))


        if (state.subjects.isEmpty()) {
            AddSubjectsHint()
        } else {
            SubjectsColumn(
                subjects = state.subjects,
                activeGrades = state.grades,
                mode = state.mode,
                onGradeClick = onGradeClick,
                onResetClick = onResetSubjectClick,
                onDeleteSubject = onDeleteSubject,
                onRenameClick = onRenameClick,
                onFixGradeClick = onFixGradeClick
            )
        }
    }
}

@Composable
fun SemesterContentLandscape(
    modifier: Modifier = Modifier,
    state: SemesterSubjectsState.Loaded,
    onAddClick: () -> Unit,
    onChangeModeClick: () -> Unit,
    onResetClick: () -> Unit,
    onPredictDataChange: (Pair<String, Boolean>) -> Unit,
    onRenameClick: (subject: Subject) -> Unit,
    onDeleteSubject: (subjectId: Long) -> Unit,
    onFixGradeClick: (subjectId: Long, fixed: Boolean) -> Unit,
    onResetSubjectClick: (subjectId: Long) -> Unit,
    onGradeClick: (subjectId: Long, grade: GradeName) -> Unit,
) {
    val spacing = LocalSpacing.current

    Column(modifier = modifier) {
        Controllers(
            mode = state.mode,
            onChangeModeClick = onChangeModeClick,
            onAddClick = onAddClick,
            onResetClick = onResetClick
        )

        Spacer(modifier = Modifier.height(spacing.small))

        Row {
            ResultCard(
                calculationResult = state.modeResult.calculationResult,
                onTargetGPAChange = { targetGPA, reverseOrder ->
                    onPredictDataChange(targetGPA to reverseOrder)
                },
                mode = state.mode,
            )

            Spacer(modifier = Modifier.width(spacing.small))

            if (state.subjects.isEmpty()) {
                AddSubjectsHint()
            } else {
                SubjectsColumn(
                    subjects = state.subjects,
                    activeGrades = state.grades,
                    mode = state.mode,
                    onGradeClick = onGradeClick,
                    onResetClick = onResetSubjectClick,
                    onDeleteSubject = onDeleteSubject,
                    onRenameClick = onRenameClick,
                    onFixGradeClick = onFixGradeClick
                )
            }
        }
    }
}

@Preview
@Composable
fun SemesterScreenContentPreviewPortrait() {
    SemesterContentPortrait(
        state = SemesterSubjectsState.Loaded(
            subjects = listOf(
                Subject(
                    id = 0,
                    name = "Math",
                    creditHours = 3.0,
                    assignedGrade = Grade(
                        name = GradeName.A,
                        points = 4.0,
                        percentage = 75.0
                    ),
                    totalMarks = 150.0,
                    fixedGrade = false,
                    maxGradeCanBeAssigned = Grade(
                        name = GradeName.A,
                        points = 4.0,
                        percentage = 75.0
                    ),
                ),
                Subject(
                    id = 1,
                    name = "Physics",
                    creditHours = 3.0,
                    assignedGrade = Grade(
                        name = GradeName.D,
                        points = 2.0,
                        percentage = 45.0
                    ),
                    totalMarks = 150.0,
                    fixedGrade = true,
                    maxGradeCanBeAssigned = Grade(
                        name = GradeName.C,
                        points = 2.33,
                        percentage = 45.0
                    ),
                ),
            ),
            grades = listOf(
                GradeName.A,
                GradeName.BPlus,
                GradeName.B,
                GradeName.CPlus,
                GradeName.C,
                GradeName.D,
                GradeName.F,
            ),
            mode = Mode.Normal,
            modeResult = ModeResult.Normal(
                result = Calculate.Result.Success(
                    semester = Calculate.Result.Success.Data(
                        gpa = 3.5,
                        grade = GradeName.A,
                        percentage = 90.0f,
                    ),
                    cumulative = Calculate.Result.Success.Data(
                        gpa = 3.4,
                        grade = GradeName.BPlus,
                        percentage = 80.0f,
                    ),
                )
            )
        ),
        onAddClick = { },
        onChangeModeClick = { },
        onResetClick = { },
        onPredictDataChange = {},
        onRenameClick = {},
        onDeleteSubject = {},
        onFixGradeClick = { _, _ -> },
        onResetSubjectClick = {},
        onGradeClick = { _, _ -> }
    )
}