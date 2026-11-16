package com.example.impahla.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.impahla.data.local.EventEntity
import com.example.impahla.ui.dashboard.toTitleCase

object ImpahlaColors {
    val Background = Color(0xFF170C0B)
    val CardBackground = Color(0xFF261513)
    val ElevatedSurface = Color(0xFF351B1A)
    val PrimaryBurgundy = Color(0xFF7A2028)
    val SecondaryRed = Color(0xFFA83232)
    val AccentRed = Color(0xFFB33A36)
    val PrimaryText = Color(0xFFFFF8F0)
    val SecondaryText = Color(0xFFB8A6A0)
    val Border = Color(0xFF49302D)
    val Success = Color(0xFF34D399)
    val Warning = Color(0xFFE8AD55)

    val PrimaryGradient = Brush.horizontalGradient(
        colors = listOf(PrimaryBurgundy, AccentRed)
    )
}

@Composable
fun ImpahlaTopBar(
    title: String,
    subtitle: String? = null,
    onLogout: (() -> Unit)? = null
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = ImpahlaColors.PrimaryText
            )
            if (!subtitle.isNullOrBlank()) {
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = subtitle,
                    fontSize = 14.sp,
                    color = ImpahlaColors.SecondaryText
                )
            }
        }
        if (onLogout != null) {
            Spacer(modifier = Modifier.width(12.dp))
            Surface(
                onClick = onLogout,
                shape = RoundedCornerShape(12.dp),
                color = ImpahlaColors.CardBackground,
                border = BorderStroke(1.dp, ImpahlaColors.Border)
            ) {
                Box(
                    modifier = Modifier.padding(10.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                        contentDescription = "Logout",
                        tint = ImpahlaColors.PrimaryText,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun ImpahlaPrimaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    icon: ImageVector? = null
) {
    Surface(
        onClick = { if (enabled) onClick() },
        modifier = modifier
            .fillMaxWidth()
            .shadow(elevation = 6.dp, shape = RoundedCornerShape(16.dp), spotColor = ImpahlaColors.PrimaryBurgundy),
        shape = RoundedCornerShape(16.dp),
        color = Color.Transparent,
        border = BorderStroke(1.dp, ImpahlaColors.AccentRed.copy(alpha = 0.5f))
    ) {
        Box(
            modifier = Modifier
                .background(
                    brush = if (enabled) ImpahlaColors.PrimaryGradient else Brush.linearGradient(
                        listOf(ImpahlaColors.CardBackground, ImpahlaColors.ElevatedSurface)
                    )
                )
                .padding(vertical = 16.dp, horizontal = 20.dp),
            contentAlignment = Alignment.Center
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                if (icon != null) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = ImpahlaColors.PrimaryText,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                }
                Text(
                    text = text,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (enabled) ImpahlaColors.PrimaryText else ImpahlaColors.SecondaryText
                )
            }
        }
    }
}

@Composable
fun ImpahlaStatCard(
    title: String,
    value: String,
    icon: ImageVector,
    modifier: Modifier = Modifier,
    iconTint: Color = ImpahlaColors.PrimaryBurgundy,
    iconBg: Color = ImpahlaColors.PrimaryBurgundy.copy(alpha = 0.15f)
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        color = ImpahlaColors.CardBackground,
        border = BorderStroke(1.dp, ImpahlaColors.Border)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = title,
                    fontSize = 13.sp,
                    color = ImpahlaColors.SecondaryText,
                    fontWeight = FontWeight.Medium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(4.dp))
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
            }
            Text(
                text = value,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = ImpahlaColors.PrimaryText,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
fun ImpahlaSectionHeader(
    title: String,
    count: Int? = null,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = ImpahlaColors.PrimaryText
        )
        if (count != null) {
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = ImpahlaColors.PrimaryBurgundy,
                border = BorderStroke(1.dp, ImpahlaColors.AccentRed.copy(alpha = 0.4f))
            ) {
                Text(
                    text = count.toString(),
                    color = ImpahlaColors.PrimaryText,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                )
            }
        }
    }
}

@Composable
fun ImpahlaCategoryBadge(categoryStr: String) {
    val formattedText = toTitleCase(categoryStr)
    val (bgColor, textColor) = when (categoryStr.uppercase()) {
        "KITCHEN_PARTY" -> Color(0xFF5C232B) to Color(0xFFF0B3BA)
        "WEDDING" -> Color(0xFF4A2B18) to ImpahlaColors.Warning
        "BRIDAL_SHOWER" -> Color(0xFF522238) to Color(0xFFE8B3D0)
        "BIRTHDAY" -> Color(0xFF26394A) to Color(0xFF90CDF4)
        else -> ImpahlaColors.ElevatedSurface to ImpahlaColors.SecondaryText
    }

    Surface(
        color = bgColor,
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(1.dp, textColor.copy(alpha = 0.3f))
    ) {
        Text(
            text = formattedText,
            color = textColor,
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
        )
    }
}

@Composable
fun ImpahlaEventCard(
    event: EventEntity,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = ImpahlaColors.CardBackground,
        border = BorderStroke(1.dp, ImpahlaColors.Border)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = toTitleCase(event.eventName),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = ImpahlaColors.PrimaryText,
                    modifier = Modifier.weight(1f),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.width(8.dp))
                ImpahlaCategoryBadge(categoryStr = event.eventCategory.name)
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = null,
                        tint = ImpahlaColors.SecondaryText,
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = toTitleCase(event.venue),
                        fontSize = 14.sp,
                        color = ImpahlaColors.SecondaryText,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                Icon(
                    imageVector = Icons.Default.ChevronRight,
                    contentDescription = "Details",
                    tint = ImpahlaColors.SecondaryText,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

@Composable
fun ImpahlaTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    singleLine: Boolean = true,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    trailingIcon: @Composable (() -> Unit)? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    isError: Boolean = false
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        singleLine = singleLine,
        visualTransformation = visualTransformation,
        keyboardOptions = keyboardOptions,
        trailingIcon = trailingIcon,
        leadingIcon = leadingIcon,
        isError = isError,
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = ImpahlaColors.CardBackground,
            unfocusedContainerColor = ImpahlaColors.CardBackground,
            focusedBorderColor = ImpahlaColors.PrimaryBurgundy,
            unfocusedBorderColor = ImpahlaColors.Border,
            focusedLabelColor = ImpahlaColors.PrimaryText,
            unfocusedLabelColor = ImpahlaColors.SecondaryText,
            focusedTextColor = ImpahlaColors.PrimaryText,
            unfocusedTextColor = ImpahlaColors.PrimaryText,
            cursorColor = ImpahlaColors.PrimaryText
        )
    )
}
