package com.example.impahla.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.impahla.model.ContributionStatus
import kotlinx.coroutines.flow.Flow

@Dao
interface ContributionDao {

    @Query("SELECT * FROM contributions ORDER BY timestamp DESC")
    fun getAllContributions(): Flow<List<ContributionEntity>>

    @Query("SELECT * FROM contributions WHERE status = :status ORDER BY timestamp DESC")
    fun getContributionsByStatus(status: ContributionStatus): Flow<List<ContributionEntity>>

    @Query("SELECT * FROM contributions WHERE id = :id")
    fun getContributionById(id: Long): Flow<ContributionEntity?>

    @Query("SELECT COALESCE(SUM(cashAmount), 0.0) FROM contributions WHERE status = 'RECEIVED'")
    fun getTotalReceivedCash(): Flow<Double>

    @Query("SELECT COALESCE(SUM(cashAmount), 0.0) FROM contributions WHERE status = 'PROMISED'")
    fun getTotalPromisedCash(): Flow<Double>

    @Query("SELECT COUNT(DISTINCT contributorName) FROM contributions")
    fun getContributorCount(): Flow<Int>

    @Query("SELECT COUNT(*) FROM contributions WHERE (type = 'GIFT' OR type = 'BOTH') AND status = 'PROMISED'")
    fun getOutstandingGiftsCount(): Flow<Int>

    @Query("SELECT * FROM contributions WHERE contributorName LIKE '%' || :query || '%' OR giftDescription LIKE '%' || :query || '%' ORDER BY timestamp DESC")
    fun searchContributions(query: String): Flow<List<ContributionEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertContribution(contribution: ContributionEntity): Long

    @Update
    fun updateContribution(contribution: ContributionEntity): Int

    @Delete
    fun deleteContribution(contribution: ContributionEntity): Int
}
