package com.example.impahla.data.local

import androidx.room.TypeConverter
import com.example.impahla.model.ContributionStatus
import com.example.impahla.model.ContributionType

class Converters {
    @TypeConverter
    fun fromStatus(value: ContributionStatus): String = value.name

    @TypeConverter
    fun toStatus(value: String): ContributionStatus = try {
        ContributionStatus.valueOf(value)
    } catch (e: Exception) {
        ContributionStatus.PROMISED
    }

    @TypeConverter
    fun fromType(value: ContributionType): String = value.name

    @TypeConverter
    fun toType(value: String): ContributionType = try {
        ContributionType.valueOf(value)
    } catch (e: Exception) {
        ContributionType.CASH
    }
}
