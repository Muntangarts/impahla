package com.example.impahla.ui.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.impahla.data.local.EventEntity
import com.example.impahla.data.local.ProgrammeEntity
import com.example.impahla.data.repository.EventRepository
import com.example.impahla.data.repository.ProgrammeRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

class EventDetailsViewModel(
    eventId: Long,
    eventRepository: EventRepository,
    programmeRepository: ProgrammeRepository
) : ViewModel() {

    val event: StateFlow<EventEntity?> = eventRepository.getEventById(eventId)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val programme: StateFlow<List<ProgrammeEntity>> = programmeRepository.getProgrammeForEvent(eventId)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
}

class EventDetailsViewModelFactory(
    private val eventId: Long,
    private val eventRepository: EventRepository,
    private val programmeRepository: ProgrammeRepository
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EventDetailsViewModel::class.java)) {
            return EventDetailsViewModel(eventId, eventRepository, programmeRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
