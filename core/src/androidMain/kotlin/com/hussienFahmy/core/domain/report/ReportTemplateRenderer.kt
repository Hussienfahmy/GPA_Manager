package com.hussienfahmy.core.domain.report

interface ReportTemplateRenderer {
    val template: ReportTemplate
    fun render(data: AcademicReportData): String
}
