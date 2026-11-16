package com.example.impahla.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.impahla.model.ContributionStatus

@Composable
fun StatusBadge(
    status: ContributionStatus,
    modifier: Modifier = Modifier
) {
    val (bgColor, textColor, label) = when (status) {
        ContributionStatus.RECEIVED -> Triple(
            Color(0xFFE8F5E9),
            Color(0xFF2E7D32),
            "RECEIVED"
        )
        ContributionStatus.PROMISED -> Triple(
            Color(0xFFFFF3E0),
            Color(0xFFE65100),
            "PROMISED"
        )
    }

    Text(
        text = label,
        color = textColor,
        fontSize = 11.sp,
        fontWeight = FontWeight.Bold,
        modifier = modifier
            .background(color = bgColor, shape = RoundedCornerShape(12.dp))
            .padding(horizontal = 10.dp, vertical = 4.dp)
    )
}
