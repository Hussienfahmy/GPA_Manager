package com.hussienfahmy.semester_history_presentation.export

import com.hussienfahmy.core.domain.report.ReportTemplate

sealed class ExportReportEvent {
    data class SelectTemplate(val template: ReportTemplate) : ExportReportEvent()
    object Export : ExportReportEvent()
}
