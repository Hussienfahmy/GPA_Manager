package com.hussienfahmy.core.domain.report

import org.jetbrains.compose.resources.StringResource
import com.hussienfahmy.core.generated.resources.*

enum class ReportTemplate(
    val id: String,
    val displayNameRes: StringResource,
    val taglineRes: StringResource,
    val descriptionRes: StringResource,
    val accentColorHex: Long,
    val backgroundColorHex: Long,
    val isDark: Boolean,
) {
    LEDGER(
        id = "ledger",
        displayNameRes = Res.string.tpl_ledger_name,
        taglineRes = Res.string.tpl_ledger_tagline,
        descriptionRes = Res.string.tpl_ledger_desc,
        accentColorHex = 0xFF1A2A4A,
        backgroundColorHex = 0xFFFDFCF8,
        isDark = false,
    ),
    MODERN(
        id = "modern",
        displayNameRes = Res.string.tpl_modern_name,
        taglineRes = Res.string.tpl_modern_tagline,
        descriptionRes = Res.string.tpl_modern_desc,
        accentColorHex = 0xFF6366F1,
        backgroundColorHex = 0xFFF4F5F9,
        isDark = false,
    ),
    GAZETTE(
        id = "gazette",
        displayNameRes = Res.string.tpl_gazette_name,
        taglineRes = Res.string.tpl_gazette_tagline,
        descriptionRes = Res.string.tpl_gazette_desc,
        accentColorHex = 0xFF000000,
        backgroundColorHex = 0xFFFFFFFF,
        isDark = false,
    ),
    DASHBOARD(
        id = "dashboard",
        displayNameRes = Res.string.tpl_dashboard_name,
        taglineRes = Res.string.tpl_dashboard_tagline,
        descriptionRes = Res.string.tpl_dashboard_desc,
        accentColorHex = 0xFF6366F1,
        backgroundColorHex = 0xFFFAFBFC,
        isDark = false,
    ),
    TIMELINE(
        id = "timeline",
        displayNameRes = Res.string.tpl_timeline_name,
        taglineRes = Res.string.tpl_timeline_tagline,
        descriptionRes = Res.string.tpl_timeline_desc,
        accentColorHex = 0xFFEC4899,
        backgroundColorHex = 0xFFF7F7F5,
        isDark = false,
    ),
    MINIMAL(
        id = "minimal",
        displayNameRes = Res.string.tpl_minimal_name,
        taglineRes = Res.string.tpl_minimal_tagline,
        descriptionRes = Res.string.tpl_minimal_desc,
        accentColorHex = 0xFF2D3142,
        backgroundColorHex = 0xFFFFFFFF,
        isDark = false,
    );

    companion object {
        val DEFAULT = LEDGER
    }
}
