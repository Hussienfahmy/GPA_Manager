package com.hussienfahmy.core.domain.report.templates

import com.hussienfahmy.core.domain.report.AcademicReportData
import com.hussienfahmy.core.domain.report.ReportCommon
import com.hussienfahmy.core.domain.report.ReportTemplate
import com.hussienfahmy.core.domain.report.ReportTemplateRenderer
import com.hussienfahmy.core.domain.report.SubjectRow
import com.hussienfahmy.core.domain.report.escapeHtml
import com.hussienfahmy.core.domain.report.formatDate
import com.hussienfahmy.core.util.toFixedString

class MinimalRenderer : ReportTemplateRenderer {
    override val template = ReportTemplate.MINIMAL

    override fun render(data: AcademicReportData): String {
        val standing = ReportCommon.resolveStanding(data.cumulativeGPA, data.maxGPA, data.grades)
        val sb = StringBuilder()

        sb.append(
            """
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<title>Academic Report — ${data.studentName.escapeHtml()}</title>
<style>
@page { size: A4; margin: 22mm 0 0 0; }
* { box-sizing: border-box; }
html, body {
  margin: 0; padding: 0;
  background: #ffffff;
  color: #2d3142;
  font-family: 'Helvetica Neue', Helvetica, Arial, sans-serif;
  font-size: 9.5pt;
  line-height: 1.55;
  -webkit-print-color-adjust: exact;
  print-color-adjust: exact;
}
.page { padding: 0 20mm; }

.kicker {
  font-size: 7pt;
  letter-spacing: 4pt;
  text-transform: uppercase;
  color: #8a8d99;
  margin-bottom: 6pt;
}
.student-name {
  font-family: Georgia, 'Times New Roman', serif;
  font-size: 32pt;
  line-height: 1;
  letter-spacing: -0.5pt;
  font-weight: normal;
  margin: 0 0 4pt 0;
}
.subline {
  font-size: 9pt;
  color: #5b5f70;
  margin-bottom: 28pt;
}
.subline b { color: #2d3142; font-weight: 600; }

.hero {
  display: table;
  width: 100%;
  margin-bottom: 26pt;
  padding-bottom: 18pt;
  border-bottom: 0.5pt solid #d8dae3;
}
.hero .gpa-block {
  display: table-cell;
  vertical-align: bottom;
  width: 55%;
}
.hero .gpa-block .label {
  font-size: 7pt;
  letter-spacing: 2.5pt;
  text-transform: uppercase;
  color: #8a8d99;
  margin-bottom: 2pt;
}
.hero .gpa-block .value {
  font-family: Georgia, serif;
  font-size: 72pt;
  line-height: 1;
  letter-spacing: -2pt;
  color: #2d3142;
  font-weight: normal;
}
.hero .gpa-block .value .denom {
  font-size: 18pt;
  color: #b0b3bf;
  letter-spacing: 0;
  margin-left: 4pt;
}
.hero .stats {
  display: table-cell;
  vertical-align: bottom;
  text-align: right;
  width: 45%;
}
.hero .stats .stat {
  display: inline-block;
  margin-left: 18pt;
  text-align: right;
}
.hero .stats .stat .l {
  font-size: 6.8pt;
  letter-spacing: 2pt;
  text-transform: uppercase;
  color: #8a8d99;
  display: block;
  margin-bottom: 2pt;
}
.hero .stats .stat .v {
  font-family: Georgia, serif;
  font-size: 22pt;
  line-height: 1;
  color: #2d3142;
}
.hero .stats .stat .v .suf {
  font-size: 9pt;
  color: #b0b3bf;
  margin-left: 2pt;
}

.section-label {
  font-size: 7pt;
  letter-spacing: 3pt;
  text-transform: uppercase;
  color: #8a8d99;
  margin-bottom: 10pt;
  padding-bottom: 4pt;
  border-bottom: 0.5pt solid #d8dae3;
}

.sem {
  margin-bottom: 18pt;
  page-break-inside: avoid;
}
.sem .head {
  display: table;
  width: 100%;
  margin-bottom: 8pt;
}
.sem .head .ttl {
  display: table-cell;
  vertical-align: baseline;
  font-family: Georgia, serif;
  font-size: 14pt;
  color: #2d3142;
}
.sem .head .meta {
  display: table-cell;
  vertical-align: baseline;
  text-align: right;
  font-size: 9pt;
  color: #5b5f70;
}
.sem .head .meta b {
  font-family: Georgia, serif;
  color: #2d3142;
  font-weight: normal;
  margin-left: 2pt;
}
.sem .head .meta .sep {
  color: #d8dae3;
  margin: 0 6pt;
}

.placeholder {
  font-size: 8.5pt;
  color: #b0b3bf;
  font-style: italic;
  padding: 4pt 0;
}

table.subj {
  width: 100%;
  border-collapse: collapse;
  font-size: 8.5pt;
  table-layout: fixed;
}
table.subj thead th {
  font-size: 6.5pt;
  letter-spacing: 1.5pt;
  text-transform: uppercase;
  color: #8a8d99;
  font-weight: normal;
  text-align: right;
  padding: 4pt 3pt;
  border-bottom: 0.4pt solid #d8dae3;
}
table.subj thead th:first-child { text-align: left; }
table.subj tbody td {
  padding: 5pt 3pt;
  text-align: right;
  border-bottom: 0.3pt solid #eef0f4;
  font-variant-numeric: tabular-nums;
  vertical-align: middle;
}
table.subj tbody td.name {
  text-align: left;
  color: #2d3142;
  font-weight: 600;
  word-wrap: break-word;
}
table.subj tbody tr.no-grade td.name {
  color: #b0b3bf;
  font-style: italic;
  font-weight: normal;
}
table.subj tbody tr:last-child td { border-bottom: 0; }
table.subj .muted { color: #d8dae3; }
table.subj col.c-name { width: auto; }
table.subj col.c-hrs { width: 7%; }
table.subj col.c-grade { width: 9%; }
table.subj col.c-pts { width: 7%; }
table.subj col.c-mid { width: 7%; }
table.subj col.c-prac { width: 7%; }
table.subj col.c-oral { width: 7%; }
table.subj col.c-proj { width: 7%; }
table.subj col.c-final { width: 7%; }
table.subj col.c-total { width: 13%; }
.grade {
  display: inline-block;
  font-family: Georgia, serif;
  font-size: 9pt;
  color: #2d3142;
  letter-spacing: 0.3pt;
}

${ReportCommon.pageFooterCss(accentHex = "#2d3142", bgHex = "#ffffff", borderHex = "#d8dae3")}
</style>
</head>
<body>
<div class="page">

<div class="kicker">Academic Report · ${formatDate()}</div>
<h1 class="student-name">${data.studentName.escapeHtml()}</h1>
<div class="subline"><b>${data.university.escapeHtml()}</b> &nbsp;·&nbsp; ${data.faculty.escapeHtml()} &nbsp;·&nbsp; ${data.department.escapeHtml()} &nbsp;·&nbsp; Level ${data.level}</div>

<div class="hero">
  <div class="gpa-block">
    <div class="label">Cumulative GPA</div>
    <div class="value">${data.cumulativeGPA.toFixedString(2)}<span class="denom">/ ${
                data.maxGPA.toFixedString(2)
            }</span></div>
  </div>
  <div class="stats">
    <div class="stat"><span class="l">Standing</span><span class="v">${standing.symbol}</span></div>
    <div class="stat"><span class="l">Credits</span><span class="v">${data.totalCreditHours}<span class="suf">hrs</span></span></div>
    <div class="stat"><span class="l">Terms</span><span class="v">${data.history.size}</span></div>
  </div>
</div>


"""
        )

        if (data.history.isNotEmpty()) {
            sb.append("""<div class="section-label">Archived Semesters</div>""")
            data.history.forEach { sem ->
                val gradeSym =
                    ReportCommon.gradeSymbolForGpa(sem.semesterGPA, data.maxGPA, data.grades)
                sb.append(
                    """
                <div class="sem">
                  <div class="head">
                    <div class="ttl">${sem.label.escapeHtml()}</div>
                    <div class="meta">GPA <b>${sem.semesterGPA.toFixedString(2)}</b><span class="sep">·</span>Credits <b>${sem.totalCreditHours}</b><span class="sep">·</span>Grade <b>$gradeSym</b></div>
                  </div>
                """.trimIndent()
                )
                if (sem.subjects.isEmpty()) {
                    sb.append("""<div class="placeholder">Summary semester — no subject detail recorded</div>""")
                } else {
                    sb.append(subjectTable(sem.subjects))
                }
                sb.append("""</div>""")
            }
        }

        sb.append(
            """
</div>
${ReportCommon.pageFooterHtml(data.logoBase64Png, data.qrBase64Png)}
</body>
</html>
        """.trimIndent()
        )

        return sb.toString()
    }

    private fun subjectTable(subjects: List<SubjectRow>): String {
        val sb = StringBuilder()
        sb.append("""<table class="subj"><colgroup><col class="c-name"><col class="c-hrs"><col class="c-grade"><col class="c-pts"><col class="c-mid"><col class="c-prac"><col class="c-oral"><col class="c-proj"><col class="c-final"><col class="c-total"></colgroup><thead><tr><th>Subject</th><th>Hrs</th><th>Grade</th><th>Pts</th><th>Mid</th><th>Prac</th><th>Oral</th><th>Proj</th><th>Final</th><th>Total</th></tr></thead><tbody>""")
        subjects.forEach { row ->
            val rowClass = if (row.gradeName == null) "no-grade" else ""
            sb.append("""<tr class="$rowClass">""")
            sb.append("""<td class="name">${row.name.escapeHtml()}</td>""")
            sb.append("""<td>${row.creditHours.toFixedString(1)}</td>""")
            sb.append("""<td>${if (row.gradeName != null) """<span class="grade">${row.gradeName.escapeHtml()}</span>""" else """<span class="muted">—</span>"""}</td>""")
            sb.append("""<td>${if (row.gradePoints != null) row.gradePoints.toFixedString(2) else """<span class="muted">—</span>"""}</td>""")
            sb.append("""<td>${if (row.midterm != null) row.midterm.toFixedString(1) else """<span class="muted">—</span>"""}</td>""")
            sb.append("""<td>${if (row.practical != null) row.practical.toFixedString(1) else """<span class="muted">—</span>"""}</td>""")
            sb.append("""<td>${if (row.oral != null) row.oral.toFixedString(1) else """<span class="muted">—</span>"""}</td>""")
            sb.append("""<td>${if (row.project != null) row.project.toFixedString(1) else """<span class="muted">—</span>"""}</td>""")
            sb.append("""<td>${if (row.finalExam != null) row.finalExam.toFixedString(1) else """<span class="muted">—</span>"""}</td>""")
            val marks = ReportCommon.totalMarks(row)
            sb.append(
                """<td>${
                    if (marks != null) "${marks.first.toFixedString(1)} / ${marks.second.toFixedString(1)}" else """<span class="muted">—</span>"""
                }</td>"""
            )
            sb.append("""</tr>""")
        }
        sb.append("""</tbody></table>""")
        return sb.toString()
    }
}
