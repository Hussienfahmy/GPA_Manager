package com.hussienfahmy.semester_history_presentation.export

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hussienfahmy.semester_history_domain.use_case.GenerateAcademicReport
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class ExportReportViewModel(
    private val generateAcademicReport: GenerateAcademicReport,
) : ViewModel() {

    private val _state = MutableStateFlow(ExportReportState())
    val state = _state.asStateFlow()

    private val _exportHtml = Channel<String>()
    val exportHtml = _exportHtml.receiveAsFlow()

    fun onEvent(event: ExportReportEvent) {
        when (event) {
            is ExportReportEvent.SelectTemplate ->
                _state.value = _state.value.copy(selectedTemplate = event.template)

            ExportReportEvent.Export -> exportToPdf()
        }
    }

    private fun exportToPdf() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isExporting = true, error = null)
            try {
                val html = generateAcademicReport(_state.value.selectedTemplate)
                _exportHtml.send(html)
            } catch (e: Exception) {
                _state.value = _state.value.copy(error = e.message ?: "Failed to export PDF")
            } finally {
                _state.value = _state.value.copy(isExporting = false)
            }
        }
    }
}
