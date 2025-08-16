package com.hussienfahmy.core.data.local.model

import androidx.annotation.Keep

@Keep
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