package com.example.impahla.ui.explore

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.impahla.data.local.EventEntity
import com.example.impahla.data.repository.EventRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

class ExploreEventsViewModel(eventRepository: EventRepository) : ViewModel() {
    val publishedEvents: StateFlow<List<EventEntity>> = eventRepository.publishedEvents
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
}

class ExploreEventsViewModelFactory(private val eventRepository: EventRepository) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ExploreEventsViewModel::class.java)) {
            return ExploreEventsViewModel(eventRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
