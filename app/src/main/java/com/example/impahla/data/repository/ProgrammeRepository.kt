package com.example.impahla.data.repository

import com.example.impahla.data.local.ProgrammeDao
import com.example.impahla.data.local.ProgrammeEntity
import kotlinx.coroutines.flow.Flow

class ProgrammeRepository(private val programmeDao: ProgrammeDao) {
    fun getProgrammeForEvent(eventId: Long): Flow<List<ProgrammeEntity>> = programmeDao.getProgrammeForEvent(eventId)

    suspend fun insertProgrammeItem(item: ProgrammeEntity): Long = programmeDao.insertProgrammeItem(item)

    suspend fun updateProgrammeItem(item: ProgrammeEntity): Int = programmeDao.updateProgrammeItem(item)

    suspend fun deleteProgrammeItem(item: ProgrammeEntity): Int = programmeDao.deleteProgrammeItem(item)

    suspend fun deleteProgrammeForEvent(eventId: Long) = programmeDao.deleteProgrammeForEvent(eventId)
}
