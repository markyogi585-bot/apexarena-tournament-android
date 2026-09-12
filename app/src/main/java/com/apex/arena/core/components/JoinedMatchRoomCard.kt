package com.apex.arena.core.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.MeetingRoom
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.apex.arena.core.theme.CyanGlow
import com.apex.arena.core.theme.Dimensions
import com.apex.arena.core.theme.ElevatedSurface
import com.apex.arena.core.theme.EmeraldSuccess
import com.apex.arena.core.theme.MidnightCard
import com.apex.arena.core.theme.NeonViolet
import com.apex.arena.core.theme.RadiantRose
import com.apex.arena.core.theme.SoftLilac
import com.apex.arena.core.theme.TextCrisp
import com.apex.arena.core.theme.TextMuted

@Composable
fun JoinedMatchRoomCard(
    tournamentTitle: String = "Apex Champions Pro League 2026",
    roomId: String = "ARENA-ROOM-8821",
    roomPass: String = "APEX-PASS-2026",
    roomPassword: String = roomPass,
    slotNumber: Any = "Slot #12",
    startTime: String = "Starts in 15m",
    modifier: Modifier = Modifier
) {
    val clipboard = LocalClipboardManager.current
    var isCopied by remember { mutableStateOf(false) }

    val slotDisplay = if (slotNumber is Int) "Slot #$slotNumber" else slotNumber.toString()

    Card(
        modifier = modifier
            .fillMaxWidth()
            .border(
                1.5.dp,
                Brush.horizontalGradient(listOf(EmeraldSuccess, CyanGlow, NeonViolet)),
                RoundedCornerShape(Dimensions.cardRadius)
            ),
        shape = RoundedCornerShape(Dimensions.cardRadius),
        colors = CardDefaults.cardColors(containerColor = ElevatedSurface)
    ) {
        Column(modifier = Modifier.padding(Dimensions.spaceM)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.MeetingRoom,
                        contentDescription = null,
                        tint = EmeraldSuccess,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "YOUR MATCH ROOM (ACTIVE)",
                        color = EmeraldSuccess,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Black,
                        letterSpacing = 1.sp
                    )
                }
                StatusBadge(status = BadgeStatus.JOINED)
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = tournamentTitle,
                color = TextCrisp,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "$slotDisplay • $startTime",
                color = SoftLilac,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(Dimensions.spaceM))

            // Room Credentials Box
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MidnightCard, RoundedCornerShape(Dimensions.buttonRadius))
                    .padding(12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(text = "ROOM ID", color = TextMuted, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    Text(text = roomId, color = TextCrisp, fontSize = 16.sp, fontWeight = FontWeight.Black)
                }

                Column {
                    Text(text = "PASSWORD", color = TextMuted, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    Text(text = roomPassword, color = RadiantRose, fontSize = 16.sp, fontWeight = FontWeight.Black)
                }

                IconButton(
                    onClick = {
                        clipboard.setText(AnnotatedString("Room: $roomId Pass: $roomPassword"))
                        isCopied = true
                    },
                    modifier = Modifier.size(36.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.ContentCopy,
                        contentDescription = "Copy Credentials",
                        tint = if (isCopied) EmeraldSuccess else SoftLilac
                    )
                }
            }
        }
    }
}
