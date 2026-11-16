package com.example.impahla.data.repository

import com.example.impahla.data.local.EventDao
import com.example.impahla.data.local.EventEntity
import kotlinx.coroutines.flow.Flow

class EventRepository(private val eventDao: EventDao) {
    val allEvents: Flow<List<EventEntity>> = eventDao.getAllEvents()
    val publishedEvents: Flow<List<EventEntity>> = eventDao.getPublishedEvents()

    fun getEventsByManager(managerId: Long): Flow<List<EventEntity>> = eventDao.getEventsByManager(managerId)

    fun getEventById(eventId: Long): Flow<EventEntity?> = eventDao.getEventById(eventId)

    suspend fun insertEvent(event: EventEntity): Long = eventDao.insertEvent(event)

    suspend fun updateEvent(event: EventEntity): Int = eventDao.updateEvent(event)

    suspend fun deleteEvent(event: EventEntity): Int = eventDao.deleteEvent(event)
}
