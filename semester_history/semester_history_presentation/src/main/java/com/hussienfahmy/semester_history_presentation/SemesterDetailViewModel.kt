package com.hussienfahmy.semester_history_presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hussienfahmy.core.data.local.entity.Grade
import com.hussienfahmy.core.data.local.model.GradeName
import com.hussienfahmy.core.domain.grades.use_case.GetActiveGrades
import com.hussienfahmy.semester_history_domain.use_case.AddSubjectToSemester
import com.hussienfahmy.semester_history_domain.use_case.DeleteSubjectFromSemester
import com.hussienfahmy.semester_history_domain.use_case.EditSemester
import com.hussienfahmy.semester_history_domain.use_case.EditSubjectInSemester
import com.hussienfahmy.semester_history_domain.use_case.GetSemesterDetail
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SemesterDetailViewModel(
    getSemesterDetail: GetSemesterDetail,
    getActiveGrades: GetActiveGrades,
    private val semesterId: Long,
    private val addSubjectToSemester: AddSubjectToSemester,
    private val editSubjectInSemester: EditSubjectInSemester,
    private val deleteSubjectFromSemester: DeleteSubjectFromSemester,
    private val editSemester: EditSemester,
) : ViewModel() {

    val detail = getSemesterDetail(semesterId)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = null,
        )

    val availableGrades = getActiveGrades()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList<Grade>(),
        )

    private val _isSubmitting = MutableStateFlow(false)
    val isSubmitting = _isSubmitting.asStateFlow()

    private val _errorMessage = Channel<String>()
    val errorMessage = _errorMessage.receiveAsFlow()

    fun addSubject(name: String, creditHours: Double, gradeName: GradeName) {
        viewModelScope.launch {
            _isSubmitting.value = true
            try {
                addSubjectToSemester(semesterId, name, creditHours, gradeName)
                editSemester(EditSemester.Request.RecalculateDetailed(semesterId))
            } catch (_: Exception) {
                _errorMessage.send("Failed to add subject. Please try again.")
            } finally {
                _isSubmitting.value = false
            }
        }
    }

    fun editSubject(
        subject: com.hussienfahmy.core.data.local.entity.Subject,
        name: String,
        creditHours: Double,
        gradeName: GradeName
    ) {
        viewModelScope.launch {
            _isSubmitting.value = true
            try {
                editSubjectInSemester(subject, name, creditHours, gradeName)
                editSemester(EditSemester.Request.RecalculateDetailed(semesterId))
            } catch (_: Exception) {
                _errorMessage.send("Failed to update subject. Please try again.")
            } finally {
                _isSubmitting.value = false
            }
        }
    }

    fun deleteSubject(subjectId: Long) {
        viewModelScope.launch {
            _isSubmitting.value = true
            try {
                deleteSubjectFromSemester(subjectId)
                editSemester(EditSemester.Request.RecalculateDetailed(semesterId))
            } catch (_: Exception) {
                _errorMessage.send("Failed to delete subject. Please try again.")
            } finally {
                _isSubmitting.value = false
            }
        }
    }

    fun editSummary(label: String, gpa: Double, hours: Int) {
        viewModelScope.launch {
            _isSubmitting.value = true
            try {
                editSemester(EditSemester.Request.SummaryFields(semesterId, label, gpa, hours))
            } catch (_: Exception) {
                _errorMessage.send("Failed to save changes. Please try again.")
            } finally {
                _isSubmitting.value = false
            }
        }
    }
}
