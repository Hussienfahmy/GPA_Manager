package com.hussienfahmy.core.domain.report.templates

import com.hussienfahmy.core.domain.report.AcademicReportData
import com.hussienfahmy.core.domain.report.ReportCommon
import com.hussienfahmy.core.domain.report.ReportTemplate
import com.hussienfahmy.core.domain.report.ReportTemplateRenderer
import com.hussienfahmy.core.domain.report.SubjectRow
import com.hussienfahmy.core.domain.report.escapeHtml
import com.hussienfahmy.core.domain.report.formatShortDate
import com.hussienfahmy.core.util.toFixedString

class DashboardRenderer : ReportTemplateRenderer {
    override val template = ReportTemplate.DASHBOARD

    override fun render(data: AcademicReportData): String {
        val html = StringBuilder()
        val standing = ReportCommon.resolveStanding(data.cumulativeGPA, data.maxGPA, data.grades)
        val allGpas = data.history.map { it.label to it.semesterGPA }

        html.append(
            """
            <!DOCTYPE html>
            <html lang="en">
            <head>
            <meta charset="UTF-8">
            <title>Academic Dashboard — ${data.studentName.escapeHtml()}</title>
            <style>
              @page { size: A4; margin: 12mm; }
              * { box-sizing: border-box; }
              html,body{margin:0;padding:0;background:#fafbfc;color:#0b1020;font-family:Helvetica,Arial,sans-serif;font-size:9pt;line-height:1.45;-webkit-print-color-adjust:exact;print-color-adjust:exact}
              .page-frame{padding:0}

              .hdr{display:table;width:100%;padding-bottom:8pt;margin-bottom:10pt;border-bottom:0.6pt solid #e5e8f0}
              .hdr .l{display:table-cell;vertical-align:middle}
              .hdr .r{display:table-cell;vertical-align:middle;text-align:right;font-size:7.5pt;color:#6b7280;letter-spacing:1pt;text-transform:uppercase}
              .logo{display:inline-block;width:22pt;height:22pt;background:#0b1020;border-radius:5pt;vertical-align:middle;margin-right:8pt;text-align:center;color:#fff;font-family:Georgia,serif;font-size:13pt;line-height:22pt;font-weight:bold}
              .app{display:inline-block;vertical-align:middle;font-family:Georgia,serif;font-size:13pt;font-weight:bold}
              .app small{display:block;font-family:Helvetica,sans-serif;font-size:7pt;color:#6b7280;letter-spacing:2pt;text-transform:uppercase;margin-top:2pt;font-weight:normal}

              .hero{margin-bottom:12pt}
              .hero .name{font-family:Georgia,serif;font-size:22pt;margin:0;font-weight:normal;letter-spacing:0.2pt}
              .hero .affil{color:#6b7280;font-size:8.5pt;margin-top:3pt}
              .hero .affil span+span::before{content:' · ';margin:0 4pt;color:#c6cadb}

              .kpis{display:table;width:100%;margin-top:10pt;border-collapse:separate;border-spacing:6pt 0;table-layout:fixed}
              .kpi{display:table-cell;background:#fff;border:0.4pt solid #e5e8f0;border-radius:6pt;padding:9pt 11pt;vertical-align:top;width:33.33%}
              .kpi .k{font-size:6.8pt;letter-spacing:1.5pt;text-transform:uppercase;color:#6b7280;margin-bottom:4pt}
              .kpi .v{font-family:Georgia,serif;font-size:20pt;line-height:1;color:#0b1020}
              .kpi .v .suf{font-size:10pt;color:#9ca3af;margin-left:2pt}
              .kpi .sub{font-size:7pt;color:#6b7280;margin-top:4pt}
              .kpi.accent{background:#0b1020;color:#fff;border-color:#0b1020}
              .kpi.accent .k{color:#8b90a6}
              .kpi.accent .v{color:#fff}
              .kpi.accent .v .suf{color:#fbbf24}
              .kpi.accent .sub{color:#c6cadb}

              .chart-card{background:#fff;border:0.4pt solid #e5e8f0;border-radius:6pt;padding:10pt 12pt;margin-bottom:12pt}
              .chart-card .title{font-family:Georgia,serif;font-size:11pt;font-weight:bold;margin:0 0 2pt 0}
              .chart-card .sub{font-size:7.5pt;color:#6b7280;margin-bottom:8pt}
              .chart-wrap{position:relative;height:90pt}
              .chart-wrap svg{width:100%;height:100%;display:block;overflow:visible}
              .legend{font-size:7pt;color:#6b7280;letter-spacing:1pt;text-transform:uppercase;margin-top:6pt;display:table;width:100%}
              .legend .l{display:table-cell}
              .legend .r{display:table-cell;text-align:right}

              .row2{display:table;width:100%;border-collapse:separate;border-spacing:6pt 0;table-layout:fixed;margin-bottom:12pt}
              .row2 .col{display:table-cell;vertical-align:top;width:50%}

              .sect{font-family:Georgia,serif;font-size:12pt;font-weight:bold;margin:0 0 6pt 0;letter-spacing:0.2pt;page-break-after:avoid}
              .sect::before{content:'';display:inline-block;width:3pt;height:10pt;background:#6366f1;margin-right:6pt;vertical-align:middle;margin-bottom:2pt}

              .sem{background:#fff;border:0.4pt solid #e5e8f0;border-radius:6pt;margin-bottom:8pt;overflow:hidden;page-break-inside:avoid}
              .sem-head{padding:7pt 12pt;display:table;width:100%;border-bottom:0.4pt solid #e5e8f0;background:#fbfbff}
              .sem-head .lbl{display:table-cell;font-family:Georgia,serif;font-size:10pt;font-weight:bold}
              .sem-head .chips{display:table-cell;text-align:right;font-size:7.5pt}
              .sem-head .chips .chip{display:inline-block;border:0.4pt solid #e5e8f0;background:#fff;padding:2pt 8pt;border-radius:12pt;margin-left:4pt;color:#4b5563}
              .sem-head .chips .chip b{font-family:Georgia,serif;color:#6366f1;font-weight:normal;margin-left:3pt}
              .sem-placeholder{padding:8pt 12pt;text-align:center;color:#9ca3af;font-style:italic;font-size:8pt}

              table.subjects{width:100%;border-collapse:collapse;font-size:8.2pt;table-layout:fixed}
              table.subjects col.c-subject{width:auto}
              table.subjects col.c-hrs{width:7%}
              table.subjects col.c-grade{width:10%}
              table.subjects col.c-pts{width:7%}
              table.subjects col.c-mid{width:8%}
              table.subjects col.c-prac{width:8%}
              table.subjects col.c-oral{width:6%}
              table.subjects col.c-proj{width:7%}
              table.subjects col.c-final{width:7%}
              table.subjects col.c-total{width:12%}
              table.subjects thead th{text-align:center;font-weight:normal;font-size:6.5pt;letter-spacing:1.3pt;text-transform:uppercase;color:#6b7280;padding:6pt 4pt;border-bottom:0.3pt solid #e5e8f0}
              table.subjects thead th:first-child{text-align:left;padding-left:12pt}
              table.subjects tbody td{text-align:center;padding:5pt 4pt;border-bottom:0.3pt solid #f0f2f8;font-variant-numeric:tabular-nums;vertical-align:middle}
              table.subjects tbody tr:last-child td{border-bottom:0}
              table.subjects tbody td.subject-name{text-align:left;padding-left:12pt;font-weight:bold;font-size:8.6pt;word-wrap:break-word}
              table.subjects tbody tr.no-grade td.subject-name{color:#9ca3af;font-style:italic;font-weight:normal}
              table.subjects .muted{color:#c6cadb}
              .grade-pill{display:inline-block;min-width:22pt;padding:2pt 6pt;border-radius:3pt;font-family:Georgia,serif;font-weight:bold;font-size:9pt;background:#eef0fb;color:#0b1020;border:0.4pt solid #dfe3f2}
              .grade-pill.gA{background:#10b981;color:#fff;border-color:#10b981}
              .grade-pill.gB{background:#f59e0b;color:#fff;border-color:#f59e0b}

              ${
                ReportCommon.pageFooterCss(
                    accentHex = "#6366f1",
                    bgHex = "#fafbfc",
                    borderHex = "#e5e8f0"
                )
            }
            </style>
            </head>
            <body>
            <div class="page-frame">

            <div class="hdr">
              <div class="l">${
                ReportCommon.logoImg(
                    data.logoBase64Png,
                    sizePt = 22.0
                )
            }<span class="app" style="margin-left:8pt">GPA Manager<small>Academic Dashboard</small></span></div>
              <div class="r">Generated · ${formatShortDate()}</div>
            </div>

            <div class="hero">
              <h1 class="name">${data.studentName.escapeHtml()}</h1>
              <div class="affil">
                <span>${data.university.escapeHtml()}</span>
                <span>${data.faculty.escapeHtml()}</span>
                <span>${data.department.escapeHtml()}</span>
                <span>Level ${data.level}</span>
              </div>
              <div class="kpis">
                <div class="kpi accent"><div class="k">Cumulative GPA</div><div class="v">${
                data.cumulativeGPA.toFixedString(2)
            }<span class="suf">/ ${data.maxGPA}</span></div></div>
                <div class="kpi"><div class="k">Credits Earned</div><div class="v">${data.totalCreditHours}<span class="suf">hrs</span></div><div class="sub">across ${data.history.size} semesters</div></div>
                <div class="kpi"><div class="k">Academic Standing</div><div class="v" style="font-size:14pt;">${standing.symbol}</div><div class="sub">${standing.band}</div></div>
              </div>
            </div>

            <div class="chart-card">
              <div class="title">GPA Trajectory</div>
              <div class="sub">Per-semester GPA · trailing</div>
              <div class="chart-wrap">
                ${sparklineChart(allGpas, data.maxGPA)}
              </div>
              <div class="legend"><span class="l">GPA across all semesters</span><span class="r">Current: ${
                data.cumulativeGPA.toFixedString(2)
            }</span></div>
            </div>
        """.trimIndent()
        )

        if (data.currentSemester != null) {
            html.append(
                """
            <h2 class="sect">Current Semester</h2>
            <div class="sem">
              <div class="sem-head">
                <div class="lbl">Current Semester</div>
                <div class="chips">
                  <span class="chip">GPA <b>${data.currentSemester.semesterGPA.toFixedString(2)}</b></span><span class="chip">Hrs <b>${data.currentSemester.totalCreditHours}</b></span>
                </div>
              </div>
              ${subjectTable(data.currentSemester.subjects)}
            </div>
            """.trimIndent()
            )
        }

        if (data.history.isNotEmpty()) {
            data.history.forEach { sem ->
                val gradeSym =
                    ReportCommon.gradeSymbolForGpa(sem.semesterGPA, data.maxGPA, data.grades)
                html.append(
                    """
                <div class="sem">
                  <div class="sem-head">
                    <div class="lbl">${sem.label.escapeHtml()}</div>
                    <div class="chips">
                      <span class="chip">GPA <b>${sem.semesterGPA.toFixedString(2)}</b></span><span class="chip">Hrs <b>${sem.totalCreditHours}</b></span><span class="chip">Grade <b>$gradeSym</b></span>
                    </div>
                  </div>
                """.trimIndent()
                )
                if (sem.subjects.isEmpty()) {
                    html.append("""<div class="sem-placeholder">Summary semester — no subject detail available.</div>""")
                } else {
                    html.append(subjectTable(sem.subjects))
                }
                html.append("""</div>""")
            }
        }

        html.append(
            """
            </div>
            ${ReportCommon.pageFooterHtml(data.logoBase64Png, data.qrBase64Png)}
            </body>
            </html>
        """.trimIndent()
        )

        return html.toString()
    }

    private fun sparklineChart(gpas: List<Pair<String, Double>>, maxGpa: Double): String {
        if (gpas.isEmpty()) return """<svg viewBox="0 0 300 110" preserveAspectRatio="none"><text x="10" y="50" font-size="8" fill="#999">No data</text></svg>"""

        val minGpa = 2.5
        val range = maxGpa - minGpa
        val width = 300.0
        val height = 110.0
        val step = width / (gpas.size - 1).coerceAtLeast(1)

        val points = gpas.mapIndexed { i, (_, gpa) ->
            val x = i * step
            val y = height - ((gpa - minGpa) / range * (height - 20)).coerceIn(0.0, height)
            "$x,$y"
        }.joinToString(" ")

        return """
            <svg viewBox="0 0 300 110" preserveAspectRatio="none">
              <line x1="0" y1="10"  x2="300" y2="10"  stroke="#eef0fb" stroke-width="0.5"/>
              <line x1="0" y1="55" x2="300" y2="55" stroke="#eef0fb" stroke-width="0.5"/>
              <polyline fill="none" stroke="#6366f1" stroke-width="1.8" stroke-linejoin="round" points="$points"/>
              <g fill="#6366f1">
                ${
            gpas.mapIndexed { i, _ ->
                "<circle cx=\"${i * step}\" cy=\"${
                    height - ((gpas[i].second - minGpa) / range * (height - 20)).coerceIn(
                        0.0,
                        height
                    )
                }\" r=\"2\"/>"
            }.joinToString("")
        }
              </g>
              ${
            gpas.mapIndexed { i, (_, gpa) ->
                if (i == 0 || i == gpas.lastIndex) {
                    val x = i * step
                    val y = height - ((gpa - minGpa) / range * (height - 20)).coerceIn(0.0, height)
                    val anchor = if (i == 0) "start" else "end"
                    val dx = if (i == 0) 4.0 else -4.0
                    """<text x="${x + dx}" y="${y - 6}" font-size="9" font-weight="bold" fill="#0f1222" text-anchor="$anchor" font-family="Georgia,serif">${
                        gpa.toFixedString(2)
                    }</text>"""
                } else ""
            }.joinToString("")
        }
            </svg>
        """.trimIndent()
    }

    private fun subjectTable(subjects: List<SubjectRow>): String {
        val sb = StringBuilder()
        sb.append("""<table class="subjects"><colgroup><col class="c-subject"><col class="c-hrs"><col class="c-grade"><col class="c-pts"><col class="c-mid"><col class="c-prac"><col class="c-oral"><col class="c-proj"><col class="c-final"><col class="c-total"></colgroup><thead><tr><th>Subject</th><th>Hrs</th><th>Grade</th><th>Pts</th><th>Mid</th><th>Prac</th><th>Oral</th><th>Proj</th><th>Final</th><th>Total</th></tr></thead><tbody>""")
        subjects.forEach { row ->
            val rowClass = if (row.gradeName == null) "no-grade" else ""
            val gradeClass =
                if (row.gradeName != null) ReportCommon.gradePillClass(row.gradeName) else ""
            sb.append(
                """<tr class="$rowClass"><td class="subject-name">${row.name.escapeHtml()}</td><td>${
                    row.creditHours.toFixedString(1)
                }</td><td>${if (row.gradeName != null) """<span class="grade-pill $gradeClass">${row.gradeName.escapeHtml()}</span>""" else """<span class="muted">—</span>"""}</td><td>${
                    if (row.gradePoints != null) row.gradePoints.toFixedString(2) else """<span class="muted">—</span>"""
                }</td><td>${if (row.midterm != null) row.midterm.toFixedString(0) else """<span class="muted">—</span>"""}</td><td>${
                    if (row.practical != null) row.practical.toFixedString(0) else """<span class="muted">—</span>"""
                }</td><td>${if (row.oral != null) row.oral.toFixedString(0) else """<span class="muted">—</span>"""}</td><td>${
                    if (row.project != null) row.project.toFixedString(0) else """<span class="muted">—</span>"""
                }</td><td>${if (row.finalExam != null) row.finalExam.toFixedString(0) else """<span class="muted">—</span>"""}</td><td>${
                    ReportCommon.totalMarks(
                        row
                    )?.let {
                        "${it.first.toFixedString(0)} / ${it.second.toFixedString(0)}"
                    } ?: """<span class="muted">—</span>"""
                }</td></tr>""")
        }
        sb.append("""</tbody></table>""")
        return sb.toString()
    }
}
