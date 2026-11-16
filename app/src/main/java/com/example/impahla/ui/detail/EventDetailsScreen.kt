package com.example.impahla.ui.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventDetailsScreen(
    viewModel: EventDetailsViewModel,
    onNavigateBack: () -> Unit,
    onNavigateToContribute: (Long) -> Unit
) {
    val event by viewModel.event.collectAsState()
    val programme by viewModel.programme.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Event Details", fontWeight = FontWeight.Bold) }
            )
        }
    ) { paddingValues ->
        if (event == null) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(28.dp),
                verticalArrangement = Arrangement.Center
            ) {
                Text("Loading event details or event not found...")
            }
        } else {
            val currEvent = event!!
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Spacer(modifier = Modifier.height(8.dp))
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Text(text = currEvent.eventName, fontSize = 22.sp, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(text = "Category: ${currEvent.eventCategory}", fontSize = 14.sp, color = MaterialTheme.colorScheme.primary)
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(text = "Venue: ${currEvent.venue}", fontSize = 15.sp, fontWeight = FontWeight.Medium)
                        Text(text = "Location: ${currEvent.location}", fontSize = 14.sp)
                        Text(text = "Expected Guests: ${currEvent.expectedGuests}", fontSize = 14.sp)
                        if (currEvent.description.isNotBlank()) {
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(text = currEvent.description, fontSize = 14.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }
                }

                Text(text = "Event Programme", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                if (programme.isEmpty()) {
                    Text(text = "No programme activities specified.", color = MaterialTheme.colorScheme.onSurfaceVariant)
                } else {
                    programme.forEach { item ->
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text(text = "${item.startTime} - ${item.activityName}", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                                if (item.description.isNotBlank()) {
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(text = item.description, fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = { onNavigateToContribute(currEvent.eventId) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text(text = "CONTRIBUTE TO THIS EVENT", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
