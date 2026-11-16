package com.example.impahla.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface ProgrammeDao {
    @Query("SELECT * FROM programmes WHERE eventId = :eventId ORDER BY displayOrder ASC, startTime ASC")
    fun getProgrammeForEvent(eventId: Long): Flow<List<ProgrammeEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProgrammeItem(item: ProgrammeEntity): Long

    @Update
    suspend fun updateProgrammeItem(item: ProgrammeEntity): Int

    @Delete
    suspend fun deleteProgrammeItem(item: ProgrammeEntity): Int

    @Query("DELETE FROM programmes WHERE eventId = :eventId")
    suspend fun deleteProgrammeForEvent(eventId: Long)
}
