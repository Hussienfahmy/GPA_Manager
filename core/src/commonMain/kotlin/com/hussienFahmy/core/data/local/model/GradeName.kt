package com.hussienfahmy.core.data.local.model

import kotlinx.serialization.Serializable

// @Serializable needed for sync_domain's Firestore (kotlinx.serialization) round-trip.
@Serializable
enum class GradeName(val symbol: String) {
    F("F"),
    DMinus("D-"),
    D("D"),
    DPlus("D+"),
    CMinus("C-"),
    C("C"),
    CPlus("C+"),
    BMinus("B-"),
    B("B"),
    BPlus("B+"),
    AMinus("A-"),
    A("A"),
    APlus("A+"),

    // History-only pass/fail markers - never seeded as a `Grade` row, so they never appear in
    // Grade Settings or the live Semester tab's grade picker (both are DB-driven off that
    // table), and carry no points/percentage - CalculateSemesterGPA already excludes any
    // subject whose grade has no matching `Grade` row from the GPA sum, exactly like this.
    NP("NP"),
    NF("NF"),
}