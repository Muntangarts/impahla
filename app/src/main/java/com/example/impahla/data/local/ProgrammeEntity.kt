package com.example.impahla.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "programmes")
data class ProgrammeEntity(
    @PrimaryKey(autoGenerate = true)
    val programmeId: Long = 0,
    val eventId: Long,
    val activityName: String,
    val description: String = "",
    val startTime: String,
    val endTime: String = "",
    val displayOrder: Int = 0
)
