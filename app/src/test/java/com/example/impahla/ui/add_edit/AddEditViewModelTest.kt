package com.example.impahla.ui.add_edit

import com.example.impahla.data.local.ContributionDao
import com.example.impahla.data.local.ContributionEntity
import com.example.impahla.data.repository.ContributionRepository
import com.example.impahla.model.ContributionStatus
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

class FakeContributionDao : ContributionDao {
    override fun getAllContributions() = flowOf(emptyList<ContributionEntity>())
    override fun getContributionsByStatus(status: ContributionStatus) = flowOf(emptyList<ContributionEntity>())
    override fun getContributionById(id: Long) = flowOf(null)
    override fun getTotalReceivedCash() = flowOf(0.0)
    override fun getTotalPromisedCash() = flowOf(0.0)
    override fun getContributorCount() = flowOf(0)
    override fun getOutstandingGiftsCount() = flowOf(0)
    override fun searchContributions(query: String) = flowOf(emptyList<ContributionEntity>())
    override fun insertContribution(contribution: ContributionEntity) = 1L
    override fun updateContribution(contribution: ContributionEntity) = 1
    override fun deleteContribution(contribution: ContributionEntity) = 1
}

@OptIn(ExperimentalCoroutinesApi::class)
class AddEditViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private val dao = FakeContributionDao()
    private val repository = ContributionRepository(dao)
    private val viewModel = AddEditViewModel(repository)

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun testNameFiltering() {
        viewModel.onNameChanged("John123!@# Doe")
        assertEquals("John Doe", viewModel.uiState.value.contributorName)
    }

    @Test
    fun testEmptyNameValidation() {
        viewModel.onNameChanged("")
        viewModel.saveContribution {}
        assertNotNull(viewModel.uiState.value.nameError)
    }

    @Test
    fun testShortNameValidation() {
        viewModel.onNameChanged("A")
        viewModel.saveContribution {}
        assertNotNull(viewModel.uiState.value.nameError)
    }

    @Test
    fun testValidNameValidation() {
        viewModel.onNameChanged("Mary-Jane O'Connor")
        viewModel.onCashAmountChanged("100")
        viewModel.saveContribution {}
        assertNull(viewModel.uiState.value.nameError)
    }
}
