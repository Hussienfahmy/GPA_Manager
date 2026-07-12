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
}