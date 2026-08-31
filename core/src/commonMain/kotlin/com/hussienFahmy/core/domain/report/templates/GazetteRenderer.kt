package com.hussienfahmy.core.domain.report.templates

import com.hussienfahmy.core.domain.report.AcademicReportData
import com.hussienfahmy.core.domain.report.ReportCommon
import com.hussienfahmy.core.domain.report.ReportTemplate
import com.hussienfahmy.core.domain.report.ReportTemplateRenderer
import com.hussienfahmy.core.domain.report.SubjectRow
import com.hussienfahmy.core.domain.report.escapeHtml
import com.hussienfahmy.core.domain.report.formatDate
import com.hussienfahmy.core.util.toFixedString

class GazetteRenderer : ReportTemplateRenderer {
    override val template = ReportTemplate.GAZETTE

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
              @page { size: A4; margin: 14mm 14mm 14mm 14mm; }
              * { box-sizing: border-box; }

              html, body {
                margin: 0; padding: 0;
                background: #ffffff;
                color: #000000;
                font-family: Georgia, 'Times New Roman', serif;
                font-size: 9.5pt;
                line-height: 1.5;
                -webkit-print-color-adjust: exact;
                print-color-adjust: exact;
              }

              .sans { font-family: Helvetica, Arial, sans-serif; }
              .page-frame { padding: 0; }

              .masthead {
                text-align: center;
                border-top: 3pt solid #000;
                border-bottom: 1pt solid #000;
                padding: 8pt 0 6pt 0;
                margin-bottom: 4pt;
              }
              .masthead .eyebrow {
                font-family: Helvetica, Arial, sans-serif;
                font-size: 7pt;
                letter-spacing: 4pt;
                text-transform: uppercase;
                color: #000;
                margin-bottom: 4pt;
              }
              .masthead .title {
                font-family: Georgia, serif;
                font-size: 28pt;
                font-weight: bold;
                letter-spacing: 1pt;
                line-height: 1;
                margin: 0;
              }
              .masthead .sub {
                font-family: Helvetica, Arial, sans-serif;
                font-size: 7pt;
                letter-spacing: 3pt;
                text-transform: uppercase;
                margin-top: 4pt;
              }

              .dateline {
                display: table;
                width: 100%;
                padding: 3pt 0;
                border-bottom: 0.5pt solid #000;
                font-family: Helvetica, Arial, sans-serif;
                font-size: 7.5pt;
                letter-spacing: 1.5pt;
                text-transform: uppercase;
                margin-bottom: 12pt;
              }
              .dateline .l { display: table-cell; text-align: left; }
              .dateline .c { display: table-cell; text-align: center; }
              .dateline .r { display: table-cell; text-align: right; }

              .student {
                text-align: center;
                margin-bottom: 14pt;
              }
              .student .name {
                font-family: Georgia, serif;
                font-size: 26pt;
                font-weight: normal;
                letter-spacing: 0.3pt;
                margin: 0;
                line-height: 1.1;
              }
              .student .affil {
                font-family: Helvetica, Arial, sans-serif;
                font-size: 8pt;
                letter-spacing: 2.5pt;
                text-transform: uppercase;
                margin-top: 6pt;
              }
              .student .affil span + span::before { content: ' — '; color: #000; margin: 0 3pt; }

              .summary-row {
                display: table;
                width: 100%;
                border-top: 1pt solid #000;
                border-bottom: 1pt solid #000;
                margin-bottom: 14pt;
              }
              .summary-row .stat {
                display: table-cell;
                width: 25%;
                text-align: center;
                padding: 9pt 6pt;
                border-right: 0.4pt solid #000;
                vertical-align: middle;
              }
              .summary-row .stat:last-child { border-right: 0; }
              .summary-row .stat .k {
                font-family: Helvetica, Arial, sans-serif;
                font-size: 6.5pt;
                letter-spacing: 2pt;
                text-transform: uppercase;
                display: block;
                margin-bottom: 4pt;
              }
              .summary-row .stat .v {
                font-family: Georgia, serif;
                font-size: 18pt;
                line-height: 1;
              }
              .summary-row .stat .v .suffix {
                font-size: 10pt;
                color: #666;
                margin-left: 2pt;
              }

              .section-title {
                font-family: Helvetica, Arial, sans-serif;
                font-size: 9pt;
                letter-spacing: 4pt;
                text-transform: uppercase;
                font-weight: bold;
                text-align: center;
                margin: 12pt 0 8pt 0;
                padding: 3pt 0;
                border-top: 1.5pt solid #000;
                border-bottom: 0.4pt solid #000;
                page-break-after: avoid;
              }

              .semester {
                margin-bottom: 12pt;
                page-break-inside: avoid;
              }
              .semester-head {
                display: table;
                width: 100%;
                margin-bottom: 4pt;
              }
              .semester-head .label {
                display: table-cell;
                font-family: Georgia, serif;
                font-size: 12pt;
                font-weight: bold;
                font-style: italic;
                vertical-align: baseline;
              }
              .semester-head .meta {
                display: table-cell;
                text-align: right;
                vertical-align: baseline;
                font-family: Helvetica, Arial, sans-serif;
                font-size: 7.5pt;
                letter-spacing: 1.8pt;
                text-transform: uppercase;
              }
              .semester-head .meta span + span::before {
                content: ' · ';
                margin: 0 3pt;
              }
              .semester-head .meta b {
                font-family: Georgia, serif;
                font-size: 9pt;
                letter-spacing: 0;
                text-transform: none;
                font-weight: normal;
                margin-left: 2pt;
              }

              .semester-placeholder {
                font-style: italic;
                text-align: center;
                font-size: 8.5pt;
                padding: 6pt 0;
                border-top: 0.5pt solid #000;
                border-bottom: 0.5pt solid #000;
                color: #555;
              }

              table.subjects {
                width: 100%;
                border-collapse: collapse;
                font-size: 8.5pt;
                table-layout: fixed;
                border-top: 1pt solid #000;
                border-bottom: 1pt solid #000;
              }
              table.subjects col.c-subject { width: auto; }
              table.subjects col.c-hrs     { width: 7%; }
              table.subjects col.c-grade   { width: 8%; }
              table.subjects col.c-pts     { width: 7%; }
              table.subjects col.c-mid     { width: 9%; }
              table.subjects col.c-prac    { width: 9%; }
              table.subjects col.c-oral    { width: 7%; }
              table.subjects col.c-proj    { width: 8%; }
              table.subjects col.c-final   { width: 8%; }
              table.subjects col.c-total   { width: 13%; }

              table.subjects thead th {
                font-family: Helvetica, Arial, sans-serif;
                font-weight: normal;
                font-size: 6.5pt;
                letter-spacing: 1.8pt;
                text-transform: uppercase;
                color: #000;
                padding: 5pt 4pt;
                border-bottom: 0.4pt solid #000;
                text-align: center;
                vertical-align: bottom;
              }
              table.subjects thead th:first-child { text-align: left; }
              table.subjects tbody td {
                padding: 5pt 4pt;
                border-bottom: 0.3pt dotted #999;
                text-align: center;
                vertical-align: middle;
                font-variant-numeric: tabular-nums;
              }
              table.subjects tbody tr:last-child td { border-bottom: 0; }
              table.subjects tbody td.subject-name {
                text-align: left;
                font-family: Georgia, serif;
                font-weight: bold;
                font-size: 9pt;
                word-wrap: break-word;
                overflow-wrap: break-word;
              }
              table.subjects .grade-pill {
                font-family: Georgia, serif;
                font-weight: bold;
                font-size: 10pt;
                letter-spacing: 0.5pt;
              }
              table.subjects .muted { color: #bbb; }
              table.subjects tbody tr.no-grade td.subject-name {
                color: #777;
                font-style: italic;
                font-weight: normal;
              }

              ${
                ReportCommon.pageFooterCss(
                    accentHex = "#000000",
                    bgHex = "#ffffff",
                    borderHex = "#999999"
                )
            }
            </style>
            </head>
            <body>
            <div class="page-frame">

              <div class="masthead">
                <div class="eyebrow">GPA Manager</div>
                <h1 class="title">Academic Report</h1>
                <div class="sub">Volume 1 · Gazette Edition</div>
              </div>

              <div class="dateline">
                <div class="l">${data.studentName.escapeHtml()}</div>
                <div class="c">${formatDate()}</div>
                <div class="r">GPA Manager</div>
              </div>

              <div class="student">
                <h2 class="name">${data.studentName.escapeHtml()}</h2>
                <div class="affil">
                  <span>${data.university.escapeHtml()}</span>
                  <span>${data.faculty.escapeHtml()}</span>
                  <span>${data.department.escapeHtml()}</span>
                  <span>Level ${data.level}</span>
                </div>
              </div>

              <div class="summary-row">
                <div class="stat">
                  <div class="k">Cumulative GPA</div>
                  <div class="v">${data.cumulativeGPA.toFixedString(2)}<span class="suffix">/ ${data.maxGPA}</span></div>
                </div>
                <div class="stat">
                  <div class="k">Credits Earned</div>
                  <div class="v">${data.totalCreditHours}</div>
                </div>
                <div class="stat">
                  <div class="k">Standing</div>
                  <div class="v">${standing.symbol}</div>
                </div>
                <div class="stat">
                  <div class="k">Semesters</div>
                  <div class="v">${data.history.size}</div>
                </div>
              </div>
        """.trimIndent()
        )

        if (data.currentSemester != null) {
            html.append(
                """
              <h2 class="section-title">Current Semester</h2>
              <div class="semester">
                <div class="semester-head">
                  <div class="label">Current Semester</div>
                  <div class="meta">
                    <span>GPA <b>${data.currentSemester.semesterGPA.toFixedString(2)}</b></span>
                    <span>Hrs <b>${data.currentSemester.totalCreditHours}</b></span>
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
                  <div class="semester">
                    <div class="semester-head">
                      <div class="label">${sem.label.escapeHtml()}</div>
                      <div class="meta">
                        <span>GPA <b>${sem.semesterGPA.toFixedString(2)}</b></span>
                        <span>Hrs <b>${sem.totalCreditHours}</b></span>
                        <span>Grade <b>$gradeSym</b></span>
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
            sb.append("""<tr class="$rowClass">""")
            sb.append("""<td class="subject-name">${row.name.escapeHtml()}</td>""")
            sb.append("""<td>${row.creditHours.toFixedString(1)}</td>""")
            sb.append("""<td><span class="grade-pill">${if (row.gradeName != null) row.gradeName.escapeHtml() else "—"}</span></td>""")
            sb.append("""<td>${if (row.gradePoints != null) row.gradePoints.toFixedString(2) else "—"}</td>""")
            sb.append("""<td>${if (row.midterm != null) row.midterm.toFixedString(1) else "—"}</td>""")
            sb.append("""<td>${if (row.practical != null) row.practical.toFixedString(1) else "—"}</td>""")
            sb.append("""<td>${if (row.oral != null) row.oral.toFixedString(1) else "—"}</td>""")
            sb.append("""<td>${if (row.project != null) row.project.toFixedString(1) else "—"}</td>""")
            sb.append("""<td>${if (row.finalExam != null) row.finalExam.toFixedString(1) else "—"}</td>""")
            val marks = ReportCommon.totalMarks(row)
            sb.append(
                """<td>${
                    if (marks != null) "${marks.first.toFixedString(1)} / ${marks.second.toFixedString(1)}" else "—"
                }</td>"""
            )
            sb.append("""</tr>""")
        }

        sb.append("""</tbody></table>""")
        return sb.toString()
    }
}
