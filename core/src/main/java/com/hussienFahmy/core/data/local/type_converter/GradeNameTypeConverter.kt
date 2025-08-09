package com.hussienFahmy.core.data.local.type_converter

import androidx.room.TypeConverter
import com.hussienFahmy.core.data.local.model.GradeName

class GradeNameTypeConverter {
    @TypeConverter
    fun toGradeName(symbol: String?): GradeName? {
        return GradeName.entries.firstOrNull { it.symbol == symbol }
    }

    @TypeConverter
    fun toGradeSymbol(gradeName: GradeName?): String? {
        return gradeName?.symbol
    }
}