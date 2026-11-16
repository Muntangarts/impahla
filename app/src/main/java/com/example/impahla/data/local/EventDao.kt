package com.example.impahla.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface EventDao {
    @Query("SELECT * FROM events ORDER BY eventDate DESC")
    fun getAllEvents(): Flow<List<EventEntity>>

    @Query("SELECT * FROM events WHERE eventStatus = 'PUBLISHED' ORDER BY eventDate DESC")
    fun getPublishedEvents(): Flow<List<EventEntity>>

    @Query("SELECT * FROM events WHERE managerId = :managerId ORDER BY eventDate DESC")
    fun getEventsByManager(managerId: Long): Flow<List<EventEntity>>

    @Query("SELECT * FROM events WHERE eventId = :eventId")
    fun getEventById(eventId: Long): Flow<EventEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEvent(event: EventEntity): Long

    @Update
    suspend fun updateEvent(event: EventEntity): Int

    @Delete
    suspend fun deleteEvent(event: EventEntity): Int
}
