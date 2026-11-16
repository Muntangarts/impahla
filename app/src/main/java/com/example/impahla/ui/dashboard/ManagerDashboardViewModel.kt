package com.example.impahla.ui.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.impahla.data.local.ContributionEntity
import com.example.impahla.data.local.EventEntity
import com.example.impahla.data.repository.ContributionRepository
import com.example.impahla.data.repository.EventRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

class ManagerDashboardViewModel(
    eventRepository: EventRepository,
    contributionRepository: ContributionRepository
) : ViewModel() {

    val events: StateFlow<List<EventEntity>> = eventRepository.allEvents
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val contributions: StateFlow<List<ContributionEntity>> = contributionRepository.allContributions
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
}

class ManagerDashboardViewModelFactory(
    private val eventRepository: EventRepository,
    private val contributionRepository: ContributionRepository
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ManagerDashboardViewModel::class.java)) {
            return ManagerDashboardViewModel(eventRepository, contributionRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
