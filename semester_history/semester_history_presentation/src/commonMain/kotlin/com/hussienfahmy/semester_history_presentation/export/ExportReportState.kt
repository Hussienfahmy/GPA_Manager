package com.hussienfahmy.semester_history_presentation.export

import com.hussienfahmy.core.domain.report.ReportTemplate

data class ExportReportState(
    val selectedTemplate: ReportTemplate = ReportTemplate.LEDGER,
    val isExporting: Boolean = false,
    val error: String? = null,
)
