package com.example.impahla.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.impahla.model.ContributionStatus
import com.example.impahla.model.ContributionType

@Entity(tableName = "contributions")
data class ContributionEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val eventId: Long = 0,
    val contributorId: Long = 0,
    val contributorName: String,
    val type: ContributionType,
    val cashAmount: Double = 0.0,
    val giftDescription: String = "",
    val giftCategory: String = "",
    val status: ContributionStatus,
    val photoPath: String? = null,
    val notes: String? = null,
    val timestamp: Long = System.currentTimeMillis()
)
