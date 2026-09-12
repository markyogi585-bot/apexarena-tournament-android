package com.apex.arena.core.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.apex.arena.core.theme.BorderViolet
import com.apex.arena.core.theme.Dimensions
import com.apex.arena.core.theme.MidnightCard
import com.apex.arena.core.theme.NeonViolet
import com.apex.arena.core.theme.SoftLilac
import com.apex.arena.core.theme.TextCrisp

@Composable
fun ArenaAnnouncementTicker(
    message: String,
    modifier: Modifier = Modifier,
    title: String = "DISPATCH"
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(MidnightCard, RoundedCornerShape(Dimensions.buttonRadius))
            .border(1.dp, BorderViolet, RoundedCornerShape(Dimensions.buttonRadius))
            .padding(horizontal = 12.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.Campaign,
            contentDescription = "Announcement",
            tint = NeonViolet,
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = "$title:",
            color = SoftLilac,
            fontSize = 11.sp,
            fontWeight = FontWeight.Black,
            letterSpacing = 0.5.sp
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
            text = message,
            color = TextCrisp,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            maxLines = 1,
            modifier = Modifier.weight(1f)
        )
    }
}
