package com.hussienfahmy.semester_history_presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hussienfahmy.core.R
import com.hussienfahmy.core.domain.auth.repository.AuthRepository
import com.hussienfahmy.core.domain.grades.use_case.GetActiveGrades
import com.hussienfahmy.core.domain.subject_settings.model.SubjectSettings
import com.hussienfahmy.core.domain.subject_settings.use_case.GetSubjectsSettings
import com.hussienfahmy.core.domain.sync.SemesterDirtyTracker
import com.hussienfahmy.core.model.UiText
import com.hussienfahmy.semester_history_domain.use_case.AddSubjectToSemester
import com.hussienfahmy.semester_history_domain.use_case.DeleteSubjectFromSemester
import com.hussienfahmy.semester_history_domain.use_case.EditSemester
import com.hussienfahmy.semester_history_domain.use_case.EditSubjectInSemester
import com.hussienfahmy.semester_history_domain.use_case.GetSemesterDetail
import com.hussienfahmy.sync_domain.use_case.PushSemesters
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SemesterDetailViewModel(
    getSemesterDetail: GetSemesterDetail,
    getActiveGrades: GetActiveGrades,
    getSubjectsSettings: GetSubjectsSettings,
    private val semesterId: Long,
    private val addSubjectToSemester: AddSubjectToSemester,
    private val editSubjectInSemester: EditSubjectInSemester,
    private val deleteSubjectFromSemester: DeleteSubjectFromSemester,
    private val editSemester: EditSemester,
    private val dirtyTracker: SemesterDirtyTracker,
    private val pushSemesters: PushSemesters,
    private val authRepository: AuthRepository,
    private val applicationScope: CoroutineScope,
) : ViewModel() {

    private val _isSubmitting = MutableStateFlow(false)
    private val _subjectSettings = MutableStateFlow<SubjectSettings?>(null)

    init {
        viewModelScope.launch {
            _subjectSettings.value = getSubjectsSettings()
        }
    }

    val state = combine(
        getSemesterDetail(semesterId),
        getActiveGrades(),
        _isSubmitting,
        _subjectSettings,
    ) { detail, grades, isSubmitting, settings ->
        SemesterDetailState(
            detail = detail,
            availableGrades = grades,
            isSubmitting = isSubmitting,
            subjectSettings = settings
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = SemesterDetailState(),
    )

    private val _events = Channel<SemesterDetailEvent>()
    val events = _events.receiveAsFlow()

    fun onAction(action: SemesterDetailAction) {
        when (action) {
            is SemesterDetailAction.OnAddSubject -> addSubject(action)
            is SemesterDetailAction.OnEditSubject -> editSubject(action)
            is SemesterDetailAction.OnDeleteSubject -> deleteSubject(action.subjectId)
            is SemesterDetailAction.OnScreenExit -> onScreenExit()
        }
    }

    private fun addSubject(action: SemesterDetailAction.OnAddSubject) {
        viewModelScope.launch {
            _isSubmitting.value = true
            try {
                addSubjectToSemester(
                    semesterId, action.name, action.creditHours, action.gradeName,
                    action.totalMarks, action.semesterMarks, action.metadata,
                )
                editSemester(EditSemester.Request.RecalculateDetailed(semesterId))
            } catch (_: Exception) {
                _events.send(SemesterDetailEvent.ShowError(UiText.StringResource(R.string.history_error_add_subject_failed)))
            } finally {
                _isSubmitting.value = false
            }
        }
    }

    private fun editSubject(action: SemesterDetailAction.OnEditSubject) {
        viewModelScope.launch {
            _isSubmitting.value = true
            try {
                editSubjectInSemester(
                    action.subject, action.name, action.creditHours, action.gradeName,
                    action.totalMarks, action.semesterMarks, action.metadata,
                )
                editSemester(EditSemester.Request.RecalculateDetailed(semesterId))
            } catch (_: Exception) {
                _events.send(SemesterDetailEvent.ShowError(UiText.StringResource(R.string.history_error_update_subject_failed)))
            } finally {
                _isSubmitting.value = false
            }
        }
    }

    private fun deleteSubject(subjectId: Long) {
        viewModelScope.launch {
            _isSubmitting.value = true
            try {
                deleteSubjectFromSemester(subjectId)
                editSemester(EditSemester.Request.RecalculateDetailed(semesterId))
            } catch (_: Exception) {
                _events.send(SemesterDetailEvent.ShowError(UiText.StringResource(R.string.history_error_delete_subject_failed)))
            } finally {
                _isSubmitting.value = false
            }
        }
    }

    private fun onScreenExit() {
        if (dirtyTracker.consumeChanges()) {
            applicationScope.launch {
                try {
                    val userId = authRepository.userId.filterNotNull().firstOrNull()
                    if (userId != null) pushSemesters(userId)
                } catch (_: Exception) {
                    // Silent fail — sync will retry on next app lifecycle event
                }
            }
        }
    }
}
