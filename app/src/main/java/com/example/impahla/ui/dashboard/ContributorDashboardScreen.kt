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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Explore
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.impahla.ui.components.EmptyState
import com.example.impahla.ui.components.ImpahlaColors
import com.example.impahla.ui.components.ImpahlaEventCard
import com.example.impahla.ui.components.ImpahlaSectionHeader
import com.example.impahla.ui.components.ImpahlaTopBar

fun toTitleCase(input: String): String {
    if (input.isBlank()) return ""
    return input.lowercase().split("_", " ")
        .filter { it.isNotEmpty() }
        .joinToString(" ") { word ->
            word.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }
        }
}

@Composable
fun ExploreEventsButton(onClick: () -> Unit) {
    Surface(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .shadow(elevation = 6.dp, shape = RoundedCornerShape(16.dp), spotColor = ImpahlaColors.PrimaryBurgundy),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, ImpahlaColors.AccentRed.copy(alpha = 0.5f))
    ) {
        Box(
            modifier = Modifier
                .background(brush = ImpahlaColors.PrimaryGradient)
                .padding(horizontal = 20.dp, vertical = 16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .background(Color.White.copy(alpha = 0.15f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Explore,
                            contentDescription = null,
                            tint = ImpahlaColors.PrimaryText,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                    Text(
                        text = "Explore Published Events",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = ImpahlaColors.PrimaryText
                    )
                }
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = null,
                    tint = ImpahlaColors.PrimaryText,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

@Composable
fun FinancialSummaryCard(
    title: String,
    amount: String,
    icon: ImageVector,
    iconTint: Color,
    iconBg: Color,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        color = ImpahlaColors.CardBackground,
        border = BorderStroke(1.dp, ImpahlaColors.Border)
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .background(iconBg, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = iconTint,
                        modifier = Modifier.size(18.dp)
                    )
                }
                Text(
                    text = title,
                    fontSize = 12.sp,
                    color = ImpahlaColors.SecondaryText,
                    fontWeight = FontWeight.Medium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            Text(
                text = amount,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = ImpahlaColors.PrimaryText,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
fun ContributorDashboardScreen(
    viewModel: ContributorDashboardViewModel,
    onNavigateToExplore: () -> Unit,
    onLogout: () -> Unit,
    onEventClick: ((Long) -> Unit)? = null
) {
    val publishedEvents by viewModel.publishedEvents.collectAsState()
    val contributions by viewModel.contributions.collectAsState()

    val totalCashContributed = contributions.filter { it.status.name == "RECEIVED" }.sumOf { it.cashAmount }
    val totalPromisedCash = contributions.filter { it.status.name == "PLEDGED" }.sumOf { it.cashAmount }

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
                Spacer(modifier = Modifier.height(28.dp))
                ImpahlaTopBar(
                    title = "Contributor Dashboard",
                    subtitle = "Your contributions, your impact.",
                    onLogout = onLogout
                )
            }

            item {
                ExploreEventsButton(onClick = onNavigateToExplore)
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    FinancialSummaryCard(
                        title = "Cash Contributed",
                        amount = "K%.2f".format(totalCashContributed),
                        icon = Icons.Default.AccountBalanceWallet,
                        iconTint = ImpahlaColors.Success,
                        iconBg = ImpahlaColors.Success.copy(alpha = 0.15f),
                        modifier = Modifier.weight(1f)
                    )
                    FinancialSummaryCard(
                        title = "Promised Pledges",
                        amount = "K%.2f".format(totalPromisedCash),
                        icon = Icons.Default.Schedule,
                        iconTint = ImpahlaColors.Warning,
                        iconBg = ImpahlaColors.Warning.copy(alpha = 0.15f),
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            item {
                Spacer(modifier = Modifier.height(4.dp))
                ImpahlaSectionHeader(
                    title = "Published Events",
                    count = publishedEvents.size
                )
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
                        onClick = {
                            if (onEventClick != null) {
                                onEventClick(event.eventId)
                            } else {
                                onNavigateToExplore()
                            }
                        }
                    )
                }
            }

            item {
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}
