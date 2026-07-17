package com.hussienfahmy.core.domain.report

class ReportTemplateRegistry(renderers: List<ReportTemplateRenderer>) {
    private val byTemplate: Map<ReportTemplate, ReportTemplateRenderer> =
        renderers.associateBy { it.template }

    fun render(template: ReportTemplate, data: AcademicReportData): String {
        val renderer = byTemplate[template]
            ?: byTemplate[ReportTemplate.DEFAULT]
            ?: error("No report template renderer registered")
        return renderer.render(data)
    }
}
