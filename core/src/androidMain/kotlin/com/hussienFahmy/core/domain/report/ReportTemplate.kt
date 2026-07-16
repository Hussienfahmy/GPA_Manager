package com.hussienfahmy.core.domain.report

import androidx.annotation.StringRes
import com.hussienfahmy.core.R

enum class ReportTemplate(
    val id: String,
    @param:StringRes val displayNameRes: Int,
    @param:StringRes val taglineRes: Int,
    @param:StringRes val descriptionRes: Int,
    val accentColorHex: Long,
    val backgroundColorHex: Long,
    val isDark: Boolean,
) {
    LEDGER(
        id = "ledger",
        displayNameRes = R.string.tpl_ledger_name,
        taglineRes = R.string.tpl_ledger_tagline,
        descriptionRes = R.string.tpl_ledger_desc,
        accentColorHex = 0xFF1A2A4A,
        backgroundColorHex = 0xFFFDFCF8,
        isDark = false,
    ),
    MODERN(
        id = "modern",
        displayNameRes = R.string.tpl_modern_name,
        taglineRes = R.string.tpl_modern_tagline,
        descriptionRes = R.string.tpl_modern_desc,
        accentColorHex = 0xFF6366F1,
        backgroundColorHex = 0xFFF4F5F9,
        isDark = false,
    ),
    GAZETTE(
        id = "gazette",
        displayNameRes = R.string.tpl_gazette_name,
        taglineRes = R.string.tpl_gazette_tagline,
        descriptionRes = R.string.tpl_gazette_desc,
        accentColorHex = 0xFF000000,
        backgroundColorHex = 0xFFFFFFFF,
        isDark = false,
    ),
    DASHBOARD(
        id = "dashboard",
        displayNameRes = R.string.tpl_dashboard_name,
        taglineRes = R.string.tpl_dashboard_tagline,
        descriptionRes = R.string.tpl_dashboard_desc,
        accentColorHex = 0xFF6366F1,
        backgroundColorHex = 0xFFFAFBFC,
        isDark = false,
    ),
    TIMELINE(
        id = "timeline",
        displayNameRes = R.string.tpl_timeline_name,
        taglineRes = R.string.tpl_timeline_tagline,
        descriptionRes = R.string.tpl_timeline_desc,
        accentColorHex = 0xFFEC4899,
        backgroundColorHex = 0xFFF7F7F5,
        isDark = false,
    ),
    MINIMAL(
        id = "minimal",
        displayNameRes = R.string.tpl_minimal_name,
        taglineRes = R.string.tpl_minimal_tagline,
        descriptionRes = R.string.tpl_minimal_desc,
        accentColorHex = 0xFF2D3142,
        backgroundColorHex = 0xFFFFFFFF,
        isDark = false,
    );

    companion object {
        val DEFAULT = LEDGER
    }
}
