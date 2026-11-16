package com.example.impahla.ui.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.impahla.data.local.ContributionEntity
import com.example.impahla.data.repository.ContributionRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

data class DashboardUiState(
    val totalReceivedCash: Double = 0.0,
    val totalPromisedCash: Double = 0.0,
    val contributorCount: Int = 0,
    val outstandingGiftsCount: Int = 0,
    val recentContributions: List<ContributionEntity> = emptyList()
)

class DashboardViewModel(
    private val repository: ContributionRepository
) : ViewModel() {

    val uiState: StateFlow<DashboardUiState> = combine(
        repository.totalReceivedCash,
        repository.totalPromisedCash,
        repository.contributorCount,
        repository.outstandingGiftsCount,
        repository.allContributions
    ) { received, promised, contributors, gifts, contributions ->
        DashboardUiState(
            totalReceivedCash = received,
            totalPromisedCash = promised,
            contributorCount = contributors,
            outstandingGiftsCount = gifts,
            recentContributions = contributions.take(5)
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = DashboardUiState()
    )
}

class DashboardViewModelFactory(
    private val repository: ContributionRepository
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DashboardViewModel::class.java)) {
            return DashboardViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
