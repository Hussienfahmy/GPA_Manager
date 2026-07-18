package com.hussienfahmy.core.domain.report.templates

import com.hussienfahmy.core.domain.report.AcademicReportData
import com.hussienfahmy.core.domain.report.ReportCommon
import com.hussienfahmy.core.domain.report.ReportTemplate
import com.hussienfahmy.core.domain.report.ReportTemplateRenderer
import com.hussienfahmy.core.domain.report.SubjectRow
import com.hussienfahmy.core.domain.report.escapeHtml
import com.hussienfahmy.core.domain.report.formatDate

class ModernRenderer : ReportTemplateRenderer {
    override val template = ReportTemplate.MODERN

    override fun render(data: AcademicReportData): String {
        val html = StringBuilder()
        val standing = ReportCommon.resolveStanding(data.cumulativeGPA, data.maxGPA, data.grades)

        html.append(
            """
            <!DOCTYPE html>
            <html lang="en">
            <head>
            <meta charset="UTF-8">
            <title>Academic Report — ${data.studentName.escapeHtml()}</title>
            <style>
              @page { size: A4; margin: 14mm 12mm 14mm 12mm; }
              * { box-sizing: border-box; }

              html, body {
                margin: 0; padding: 0;
                background: #f4f5f9;
                color: #0f1222;
                font-family: Helvetica, Arial, sans-serif;
                font-size: 9.5pt;
                line-height: 1.5;
                -webkit-print-color-adjust: exact;
                print-color-adjust: exact;
              }

              .page-frame { padding: 6mm 4mm; }

              .report-header {
                display: table;
                width: 100%;
                margin-bottom: 12pt;
                padding-bottom: 10pt;
              }
              .brand { display: table-cell; vertical-align: middle; width: 55%; }
              .meta  { display: table-cell; vertical-align: middle; width: 45%; text-align: right; }

              .brand-mark {
                display: inline-block;
                width: 30pt; height: 30pt;
                background: #6366f1;
                border-radius: 4pt;
                vertical-align: middle;
                margin-right: 8pt;
              }
              .brand-mark img {
                width: 100%; height: 100%;
                border-radius: 4pt;
              }
              .brand-title { display: inline-block; vertical-align: middle; }
              .app-name {
                font-family: Georgia, serif;
                font-size: 14pt;
                font-weight: bold;
                color: #0f1222;
                letter-spacing: 0.2pt;
                line-height: 1;
              }
              .doc-kind {
                display: block;
                font-size: 7.5pt;
                color: #6b6f85;
                letter-spacing: 1.8pt;
                text-transform: uppercase;
                margin-top: 3pt;
              }
              .meta .gen-date {
                display: inline-block;
                background: linear-gradient(135deg, #4f46e5 0%, #6366f1 100%);
                color: #ffffff;
                font-family: Georgia, serif;
                font-size: 9pt;
                padding: 5pt 12pt;
                border-radius: 20pt;
              }
              .meta .gen-label {
                display: block;
                font-size: 7pt;
                letter-spacing: 1.5pt;
                text-transform: uppercase;
                color: #6b6f85;
                margin-bottom: 3pt;
              }

              .hero-card {
                background: #0f1222;
                color: #f4f5f9;
                padding: 14pt 16pt;
                border-radius: 6pt;
                margin-bottom: 14pt;
                display: table;
                width: 100%;
              }
              .hero-left  { display: table-cell; vertical-align: middle; width: 55%; padding-right: 16pt; border-right: 0.5pt solid #2b2e42; }
              .hero-right { display: table-cell; vertical-align: middle; width: 45%; padding-left: 16pt; }

              .hero-name {
                font-family: Georgia, serif;
                font-size: 22pt;
                font-weight: normal;
                color: #ffffff;
                margin: 0 0 6pt 0;
                letter-spacing: 0.3pt;
              }
              .hero-sub {
                font-size: 8.5pt;
                color: #c6cadb;
                margin-bottom: 8pt;
              }
              .hero-meta {
                display: table;
                width: 100%;
                font-size: 8pt;
              }
              .hero-meta .h {
                display: table-cell;
                color: #6b6f85;
                letter-spacing: 1.2pt;
                text-transform: uppercase;
                font-size: 6.8pt;
                padding: 2pt 8pt 2pt 0;
                vertical-align: top;
                width: 22%;
              }
              .hero-meta .v {
                display: table-cell;
                color: #f4f5f9;
                padding: 2pt 0;
                vertical-align: top;
              }

              .gpa-big {
                text-align: center;
              }
              .gpa-big .num {
                font-family: Georgia, serif;
                font-size: 36pt;
                color: #f4f5f9;
                line-height: 1;
                font-weight: normal;
              }
              .gpa-big .scale {
                color: #fbbf24;
                font-size: 14pt;
                margin-left: 3pt;
              }
              .gpa-big .lbl {
                font-size: 7pt;
                letter-spacing: 2pt;
                text-transform: uppercase;
                color: #6b6f85;
                margin-top: 4pt;
              }
              .gpa-chips {
                margin-top: 10pt;
                text-align: center;
              }
              .gpa-chips .chip {
                display: inline-block;
                background: #1a1d30;
                border: 0.4pt solid #2b2e42;
                padding: 4pt 10pt;
                margin: 0 2pt;
                font-size: 7.5pt;
                color: #c6cadb;
                border-radius: 20pt;
              }
              .gpa-chips .chip b {
                font-family: Georgia, serif;
                color: #f4f5f9;
                font-weight: normal;
                margin-left: 3pt;
              }

              .section-title {
                font-family: Georgia, serif;
                font-size: 13pt;
                font-weight: bold;
                color: #0f1222;
                margin: 10pt 0 8pt 0;
                letter-spacing: 0.2pt;
                page-break-after: avoid;
              }
              .section-title::before {
                content: '';
                display: inline-block;
                width: 14pt;
                height: 2pt;
                background: #6366f1;
                vertical-align: middle;
                margin-right: 8pt;
                margin-bottom: 2pt;
              }

              .semester-card {
                background: #ffffff;
                border: 0.4pt solid #dfe3f2;
                border-radius: 6pt;
                margin-bottom: 10pt;
                overflow: hidden;
                page-break-inside: avoid;
              }
              .semester-head {
                background: #eef0fb;
                padding: 7pt 12pt;
                display: table;
                width: 100%;
                border-bottom: 0.4pt solid #dfe3f2;
              }
              .semester-head .label {
                display: table-cell;
                font-family: Georgia, serif;
                font-size: 10.5pt;
                color: #0f1222;
                font-weight: bold;
                vertical-align: middle;
              }
              .semester-head .meta {
                display: table-cell;
                text-align: right;
                vertical-align: middle;
                font-size: 8pt;
              }
              .semester-head .chip {
                display: inline-block;
                padding: 2pt 8pt;
                background: #fff;
                border: 0.4pt solid #dfe3f2;
                border-radius: 14pt;
                margin-left: 4pt;
                font-size: 7.5pt;
                color: #4b4f67;
                letter-spacing: 0.3pt;
              }
              .semester-head .chip b {
                font-family: Georgia, serif;
                color: #6366f1;
                font-weight: normal;
                margin-left: 2pt;
              }

              .semester-placeholder {
                font-style: italic;
                color: #6b6f85;
                font-size: 8.5pt;
                padding: 10pt 12pt;
                text-align: center;
              }

              table.subjects {
                width: 100%;
                border-collapse: collapse;
                font-size: 8.5pt;
                table-layout: fixed;
              }
              table.subjects col.c-subject { width: auto; }
              table.subjects col.c-hrs     { width: 7%; }
              table.subjects col.c-grade   { width: 10%; }
              table.subjects col.c-pts     { width: 8%; }
              table.subjects col.c-mid     { width: 9%; }
              table.subjects col.c-prac    { width: 9%; }
              table.subjects col.c-oral    { width: 7%; }
              table.subjects col.c-proj    { width: 8%; }
              table.subjects col.c-final   { width: 8%; }
              table.subjects col.c-total   { width: 13%; }

              table.subjects thead th {
                text-align: center;
                font-weight: normal;
                font-size: 6.5pt;
                letter-spacing: 1.3pt;
                text-transform: uppercase;
                color: #6b6f85;
                padding: 7pt 4pt;
                border-bottom: 0.4pt solid #dfe3f2;
                background: #fbfbfe;
              }
              table.subjects thead th:first-child { text-align: left; padding-left: 12pt; }
              table.subjects tbody td {
                text-align: center;
                padding: 6pt 4pt;
                vertical-align: middle;
                border-bottom: 0.3pt solid #eef0f7;
                color: #0f1222;
                font-variant-numeric: tabular-nums;
              }
              table.subjects tbody td.subject-name {
                text-align: left;
                padding-left: 12pt;
                font-weight: bold;
                font-size: 8.8pt;
                word-wrap: break-word;
                overflow-wrap: break-word;
              }
              table.subjects tbody tr:last-child td { border-bottom: 0; }
              table.subjects tbody tr:nth-child(even) td { background: #fbfbfe; }
              table.subjects .muted { color: #c6cadb; }
              table.subjects tbody tr.no-grade td.subject-name { color: #6b6f85; font-style: italic; font-weight: normal; }

              .grade-pill {
                display: inline-block;
                min-width: 24pt;
                padding: 2pt 6pt;
                border-radius: 3pt;
                font-family: Georgia, serif;
                font-weight: bold;
                font-size: 9pt;
                letter-spacing: 0.3pt;
                background: #eef0fb;
                color: #0f1222;
                border: 0.4pt solid #dfe3f2;
              }
              .grade-pill.gA  { background: #10b981; color: #f4f5f9; border-color: #10b981; }
              .grade-pill.gB  { background: #6366f1; color: #f4f5f9; border-color: #6366f1; }
              .grade-pill.gC  { background: #dfe3f2; color: #0f1222; }
              .grade-pill.gD  { background: #fca5a5; color: #f4f5f9; border-color: #fca5a5; }

              ${
                ReportCommon.pageFooterCss(
                    accentHex = "#6366f1",
                    bgHex = "#f4f5f9",
                    borderHex = "#dfe3f2"
                )
            }
            </style>
            </head>
            <body>
            <div class="page-frame">

              <div class="report-header">
                <div class="brand">
                  <span class="brand-mark">${
                ReportCommon.logoImg(
                    data.logoBase64Png,
                    sizePt = 30.0
                )
            }</span>
                  <span class="brand-title">
                    <span class="app-name">GPA Manager</span>
                    <span class="doc-kind">Academic Record</span>
                  </span>
                </div>
                <div class="meta">
                  <span class="gen-label">Generated</span>
                  <span class="gen-date">${formatDate()}</span>
                </div>
              </div>

              <div class="hero-card">
                <div class="hero-left">
                  <h1 class="hero-name">${data.studentName.escapeHtml()}</h1>
                  <div class="hero-sub">${data.university.escapeHtml()} · ${data.faculty.escapeHtml()}</div>
                  <div class="hero-meta">
                    <div style="display:table-row;"><span class="h">Department</span><span class="v">${data.department.escapeHtml()}</span></div>
                    <div style="display:table-row;"><span class="h">Level</span><span class="v">Level ${data.level}</span></div>
                    <div style="display:table-row;"><span class="h">Standing</span><span class="v">${standing.symbol}</span></div>
                  </div>
                </div>
                <div class="hero-right">
                  <div class="gpa-big">
                    <div><span class="num">${"%.2f".format(data.cumulativeGPA)}</span><span class="scale">/ ${data.maxGPA}</span></div>
                    <div class="lbl">Cumulative GPA</div>
                  </div>
                  <div class="gpa-chips">
                    <span class="chip">Credits <b>${data.totalCreditHours}</b></span>
                    <span class="chip">Semesters <b>${data.history.size}</b></span>
                  </div>
                </div>
              </div>
        """.trimIndent()
        )

        if (data.currentSemester != null) {
            html.append(
                """
              <h2 class="section-title">Current Semester</h2>
              <div class="semester-card">
                <div class="semester-head">
                  <div class="label">Current Semester</div>
                  <div class="meta">
                    <span class="chip">GPA <b>${"%.2f".format(data.currentSemester.semesterGPA)}</b></span>
                    <span class="chip">Credit Hrs <b>${data.currentSemester.totalCreditHours}</b></span>
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
                  <div class="semester-card">
                    <div class="semester-head">
                      <div class="label">${sem.label.escapeHtml()}</div>
                      <div class="meta">
                        <span class="chip">GPA <b>${"%.2f".format(sem.semesterGPA)}</b></span>
                        <span class="chip">Credit Hrs <b>${sem.totalCreditHours}</b></span>
                        <span class="chip">Grade <b>$gradeSym</b></span>
                      </div>
                    </div>
                """.trimIndent()
                )
                if (sem.subjects.isEmpty()) {
                    html.append("""<div class="semester-placeholder">Summary semester — no subject detail available.</div>""")
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


    private fun subjectTable(subjects: List<SubjectRow>): String {
        val sb = StringBuilder()
        sb.append(
            """
            <table class="subjects">
              <colgroup>
                <col class="c-subject"><col class="c-hrs"><col class="c-grade"><col class="c-pts">
                <col class="c-mid"><col class="c-prac"><col class="c-oral"><col class="c-proj"><col class="c-final"><col class="c-total">
              </colgroup>
              <thead>
                <tr>
                  <th>Subject</th><th>Hrs</th><th>Grade</th><th>Pts</th>
                  <th>Mid</th><th>Prac</th><th>Oral</th><th>Proj</th><th>Final</th><th>Total</th>
                </tr>
              </thead>
              <tbody>
        """.trimIndent()
        )

        subjects.forEach { row ->
            val rowClass = if (row.gradeName == null) "no-grade" else ""
            val gradeClass =
                if (row.gradeName != null) ReportCommon.gradePillClass(row.gradeName) else ""
            sb.append("""<tr class="$rowClass">""")
            sb.append("""<td class="subject-name">${row.name.escapeHtml()}</td>""")
            sb.append("""<td>${"%.1f".format(row.creditHours)}</td>""")
            sb.append("""<td>${if (row.gradeName != null) """<span class="grade-pill $gradeClass">${row.gradeName.escapeHtml()}</span>""" else """<span class="muted">—</span>"""}</td>""")
            sb.append("""<td>${if (row.gradePoints != null) "%.2f".format(row.gradePoints) else """<span class="muted">—</span>"""}</td>""")
            sb.append("""<td>${if (row.midterm != null) "%.1f".format(row.midterm) else """<span class="muted">—</span>"""}</td>""")
            sb.append("""<td>${if (row.practical != null) "%.1f".format(row.practical) else """<span class="muted">—</span>"""}</td>""")
            sb.append("""<td>${if (row.oral != null) "%.1f".format(row.oral) else """<span class="muted">—</span>"""}</td>""")
            sb.append("""<td>${if (row.project != null) "%.1f".format(row.project) else """<span class="muted">—</span>"""}</td>""")
            sb.append("""<td>${if (row.finalExam != null) "%.1f".format(row.finalExam) else """<span class="muted">—</span>"""}</td>""")
            val marks = ReportCommon.totalMarks(row)
            sb.append(
                """<td>${
                    if (marks != null) "%.1f&nbsp;/&nbsp;%.1f".format(
                        marks.first,
                        marks.second
                    ) else """<span class="muted">—</span>"""
                }</td>"""
            )
            sb.append("""</tr>""")
        }

        sb.append("""</tbody></table>""")
        return sb.toString()
    }
}
