package com.example.impahla.ui.event

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.impahla.data.local.EventEntity
import com.example.impahla.data.repository.EventRepository
import com.example.impahla.model.EventCategory
import com.example.impahla.model.EventStatus
import kotlinx.coroutines.launch

class CreateEventViewModel(private val eventRepository: EventRepository) : ViewModel() {

    fun createEvent(
        eventName: String,
        category: EventCategory,
        venue: String,
        location: String,
        description: String,
        expectedGuests: Int,
        onSuccess: () -> Unit
    ) {
        if (eventName.isBlank() || venue.isBlank()) return

        viewModelScope.launch {
            val event = EventEntity(
                eventName = eventName.trim(),
                eventCategory = category,
                venue = venue.trim(),
                location = location.trim(),
                description = description.trim(),
                expectedGuests = expectedGuests,
                eventDate = System.currentTimeMillis() + 86400000L * 7,
                startTime = "09:00",
                endTime = "17:00",
                eventStatus = EventStatus.PUBLISHED
            )
            eventRepository.insertEvent(event)
            onSuccess()
        }
    }
}

class CreateEventViewModelFactory(private val eventRepository: EventRepository) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CreateEventViewModel::class.java)) {
            return CreateEventViewModel(eventRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
