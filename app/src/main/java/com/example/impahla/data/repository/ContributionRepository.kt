package com.example.impahla.data.repository

import com.example.impahla.data.local.ContributionDao
import com.example.impahla.data.local.ContributionEntity
import com.example.impahla.model.ContributionStatus
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class ContributionRepository(private val contributionDao: ContributionDao) {

    val allContributions: Flow<List<ContributionEntity>> = contributionDao.getAllContributions()
    val totalReceivedCash: Flow<Double> = contributionDao.getTotalReceivedCash()
    val totalPromisedCash: Flow<Double> = contributionDao.getTotalPromisedCash()
    val contributorCount: Flow<Int> = contributionDao.getContributorCount()
    val outstandingGiftsCount: Flow<Int> = contributionDao.getOutstandingGiftsCount()

    fun getContributionsByStatus(status: ContributionStatus): Flow<List<ContributionEntity>> {
        return contributionDao.getContributionsByStatus(status)
    }

    fun getContributionById(id: Long): Flow<ContributionEntity?> {
        return contributionDao.getContributionById(id)
    }

    fun searchContributions(query: String): Flow<List<ContributionEntity>> {
        return contributionDao.searchContributions(query)
    }

    suspend fun insertContribution(contribution: ContributionEntity): Long = withContext(Dispatchers.IO) {
        contributionDao.insertContribution(contribution)
    }

    suspend fun updateContribution(contribution: ContributionEntity): Int = withContext(Dispatchers.IO) {
        contributionDao.updateContribution(contribution)
    }

    suspend fun deleteContribution(contribution: ContributionEntity): Int = withContext(Dispatchers.IO) {
        contributionDao.deleteContribution(contribution)
    }

    suspend fun markAsReceived(contribution: ContributionEntity) = withContext(Dispatchers.IO) {
        val updated = contribution.copy(status = ContributionStatus.RECEIVED)
        contributionDao.updateContribution(updated)
    }
}
