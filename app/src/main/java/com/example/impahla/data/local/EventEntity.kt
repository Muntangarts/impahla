package com.example.impahla.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.impahla.model.EventCategory
import com.example.impahla.model.EventStatus

@Entity(tableName = "events")
data class EventEntity(
    @PrimaryKey(autoGenerate = true)
    val eventId: Long = 0,
    val managerId: Long = 0,
    val eventName: String,
    val eventCategory: EventCategory,
    val description: String,
    val eventDate: Long,
    val startTime: String,
    val endTime: String,
    val venue: String,
    val location: String,
    val expectedGuests: Int,
    val posterUri: String? = null,
    val cashEnabled: Boolean = true,
    val physicalGiftsEnabled: Boolean = true,
    val fundraisingTarget: Double = 0.0,
    val paymentInstructions: String = "",
    val eventStatus: EventStatus = EventStatus.DRAFT
)
