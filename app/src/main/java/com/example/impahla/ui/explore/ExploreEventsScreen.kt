package com.example.impahla.ui.explore

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.impahla.ui.components.EmptyState
import com.example.impahla.ui.components.ImpahlaColors
import com.example.impahla.ui.components.ImpahlaEventCard
import com.example.impahla.ui.components.ImpahlaTopBar

@Composable
fun ExploreEventsScreen(
    viewModel: ExploreEventsViewModel,
    onNavigateBack: () -> Unit,
    onEventClick: (Long) -> Unit
) {
    val publishedEvents by viewModel.publishedEvents.collectAsState()

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = ImpahlaColors.Background
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(28.dp))
                ImpahlaTopBar(
                    title = "Explore Events",
                    subtitle = "Discover upcoming events to join and support"
                )
                Spacer(modifier = Modifier.height(8.dp))
            }

            if (publishedEvents.isEmpty()) {
                item {
                    EmptyState(
                        title = "No Published Events",
                        subtitle = "Check back later for newly published events to join and contribute.",
                        icon = Icons.Default.Explore
                    )
                }
            } else {
                items(publishedEvents) { event ->
                    ImpahlaEventCard(
                        event = event,
                        onClick = { onEventClick(event.eventId) }
                    )
                }
            }

            item {
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}
