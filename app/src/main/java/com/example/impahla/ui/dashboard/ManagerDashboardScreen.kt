package com.example.impahla.ui.dashboard

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.CardGiftcard
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.impahla.ui.components.ContributionCard
import com.example.impahla.ui.components.ImpahlaColors
import com.example.impahla.ui.components.ImpahlaSectionHeader
import com.example.impahla.ui.components.ImpahlaStatCard
import com.example.impahla.ui.components.ImpahlaTopBar

@Composable
fun ManagerDashboardScreen(
    viewModel: ManagerDashboardViewModel,
    onNavigateToCreateEvent: () -> Unit,
    onNavigateToMyEvents: () -> Unit,
    onNavigateToContributions: () -> Unit,
    onLogout: () -> Unit
) {
    val events by viewModel.events.collectAsState()
    val contributions by viewModel.contributions.collectAsState()

    val totalCashReceived = contributions.filter { it.status.name == "RECEIVED" }.sumOf { it.cashAmount }
    val totalPromisedCash = contributions.filter { it.status.name == "PLEDGED" }.sumOf { it.cashAmount }
    val outstandingGifts = contributions.filter { it.type.name == "GIFT" && it.status.name == "PLEDGED" }.size

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = ImpahlaColors.Background
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(24.dp))
                ImpahlaTopBar(
                    title = "Event Manager Dashboard",
                    subtitle = "Overview of your events and contributions",
                    onLogout = onLogout
                )
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Surface(
                        onClick = onNavigateToCreateEvent,
                        modifier = Modifier
                            .weight(1f)
                            .height(52.dp)
                            .shadow(elevation = 6.dp, shape = RoundedCornerShape(16.dp), spotColor = ImpahlaColors.PrimaryBurgundy),
                        shape = RoundedCornerShape(16.dp),
                        color = Color.Transparent,
                        border = BorderStroke(1.dp, ImpahlaColors.AccentRed.copy(alpha = 0.5f))
                    ) {
                        Box(
                            modifier = Modifier
                                .background(brush = ImpahlaColors.PrimaryGradient)
                                .fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Add,
                                    contentDescription = null,
                                    tint = ImpahlaColors.PrimaryText,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Create Event", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = ImpahlaColors.PrimaryText)
                            }
                        }
                    }

                    Surface(
                        onClick = onNavigateToMyEvents,
                        modifier = Modifier
                            .weight(1f)
                            .height(52.dp),
                        shape = RoundedCornerShape(16.dp),
                        color = ImpahlaColors.CardBackground,
                        border = BorderStroke(1.dp, ImpahlaColors.Border)
                    ) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Event,
                                    contentDescription = null,
                                    tint = ImpahlaColors.PrimaryText,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("My Events", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = ImpahlaColors.PrimaryText)
                            }
                        }
                    }
                }
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    ImpahlaStatCard(
                        title = "Total Events",
                        value = events.size.toString(),
                        icon = Icons.Default.Event,
                        modifier = Modifier.weight(1f)
                    )
                    ImpahlaStatCard(
                        title = "Contributors",
                        value = contributions.map { it.contributorName }.distinct().size.toString(),
                        icon = Icons.Default.People,
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    ImpahlaStatCard(
                        title = "Cash Received",
                        value = "K%.2f".format(totalCashReceived),
                        icon = Icons.Default.AttachMoney,
                        iconTint = ImpahlaColors.Success,
                        iconBg = ImpahlaColors.Success.copy(alpha = 0.15f),
                        modifier = Modifier.weight(1f)
                    )
                    ImpahlaStatCard(
                        title = "Promised Cash",
                        value = "K%.2f".format(totalPromisedCash),
                        icon = Icons.Default.Schedule,
                        iconTint = ImpahlaColors.Warning,
                        iconBg = ImpahlaColors.Warning.copy(alpha = 0.15f),
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            item {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    color = ImpahlaColors.CardBackground,
                    border = BorderStroke(1.dp, ImpahlaColors.Border)
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(38.dp)
                                    .background(ImpahlaColors.PrimaryBurgundy.copy(alpha = 0.2f), CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.CardGiftcard,
                                    contentDescription = null,
                                    tint = ImpahlaColors.Warning,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text("Outstanding Physical Gifts", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = ImpahlaColors.PrimaryText)
                                Text("Pledged gifts pending delivery", fontSize = 12.sp, color = ImpahlaColors.SecondaryText)
                            }
                        }
                        Text(
                            text = outstandingGifts.toString(),
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            color = ImpahlaColors.Warning
                        )
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(4.dp))
                ImpahlaSectionHeader(
                    title = "Recent Contributions",
                    count = contributions.size
                )
            }

            if (contributions.isEmpty()) {
                item {
                    Text(
                        text = "No contributions recorded yet.",
                        color = ImpahlaColors.SecondaryText,
                        fontSize = 14.sp
                    )
                }
            } else {
                items(contributions.take(5)) { contribution ->
                    ContributionCard(
                        contribution = contribution,
                        onClick = {}
                    )
                }
            }

            item {
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}
