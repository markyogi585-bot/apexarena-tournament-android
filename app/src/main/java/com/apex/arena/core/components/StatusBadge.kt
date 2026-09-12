package com.apex.arena.core.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.apex.arena.core.theme.AmberWarning
import com.apex.arena.core.theme.CrimsonError
import com.apex.arena.core.theme.Dimensions
import com.apex.arena.core.theme.EmeraldSuccess
import com.apex.arena.core.theme.NeonViolet
import com.apex.arena.core.theme.TextCrisp

enum class BadgeStatus {
    LIVE,
    UPCOMING,
    COMPLETED,
    REGISTRATION_OPEN,
    CANCELLED,
    SOLO,
    SQUAD,
    JOINED,
    OPEN,
    FULL,
    ALIVE,
    ELIMINATED
}

@Composable
fun StatusBadge(
    status: BadgeStatus,
    modifier: Modifier = Modifier
) {
    val (bgColor, textColor, label) = when (status) {
        BadgeStatus.LIVE, BadgeStatus.ALIVE -> Triple(EmeraldSuccess.copy(alpha = 0.2f), EmeraldSuccess, "● LIVE")
        BadgeStatus.UPCOMING -> Triple(NeonViolet.copy(alpha = 0.2f), NeonViolet, "UPCOMING")
        BadgeStatus.COMPLETED -> Triple(Color.Gray.copy(alpha = 0.2f), Color.LightGray, "COMPLETED")
        BadgeStatus.REGISTRATION_OPEN, BadgeStatus.OPEN -> Triple(AmberWarning.copy(alpha = 0.2f), AmberWarning, "OPEN")
        BadgeStatus.CANCELLED, BadgeStatus.ELIMINATED -> Triple(CrimsonError.copy(alpha = 0.2f), CrimsonError, if (status == BadgeStatus.ELIMINATED) "ELIMINATED" else "CANCELLED")
        BadgeStatus.FULL -> Triple(CrimsonError.copy(alpha = 0.2f), CrimsonError, "FULL")
        BadgeStatus.SOLO -> Triple(Color(0xFF38BDF8).copy(alpha = 0.2f), Color(0xFF38BDF8), "SOLO")
        BadgeStatus.SQUAD -> Triple(Color(0xFFA855F7).copy(alpha = 0.2f), Color(0xFFA855F7), "SQUAD")
        BadgeStatus.JOINED -> Triple(EmeraldSuccess.copy(alpha = 0.25f), EmeraldSuccess, "✓ JOINED")
    }

    Box(
        modifier = modifier
            .background(bgColor, RoundedCornerShape(Dimensions.badgeRadius))
            .border(1.dp, textColor.copy(alpha = 0.4f), RoundedCornerShape(Dimensions.badgeRadius))
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Text(
            text = label,
            color = textColor,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 0.5.sp
        )
    }
}

@Composable
fun StatusBadge(
    status: String,
    modifier: Modifier = Modifier
) {
    val badgeStatus = when (status.uppercase()) {
        "LIVE" -> BadgeStatus.LIVE
        "ALIVE" -> BadgeStatus.ALIVE
        "UPCOMING", "SCHEDULED" -> BadgeStatus.UPCOMING
        "COMPLETED" -> BadgeStatus.COMPLETED
        "OPEN", "REGISTRATION_OPEN" -> BadgeStatus.OPEN
        "FULL" -> BadgeStatus.FULL
        "CANCELLED" -> BadgeStatus.CANCELLED
        "ELIMINATED" -> BadgeStatus.ELIMINATED
        "SOLO" -> BadgeStatus.SOLO
        "SQUAD" -> BadgeStatus.SQUAD
        "JOINED" -> BadgeStatus.JOINED
        else -> BadgeStatus.UPCOMING
    }
    StatusBadge(status = badgeStatus, modifier = modifier)
}
