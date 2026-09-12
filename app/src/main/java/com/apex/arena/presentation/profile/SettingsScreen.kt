package com.apex.arena.presentation.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.apex.arena.core.components.ApexTopBar
import com.apex.arena.core.theme.BorderViolet
import com.apex.arena.core.theme.Dimensions
import com.apex.arena.core.theme.MidnightCard
import com.apex.arena.core.theme.NeonViolet
import com.apex.arena.core.theme.ObsidianDark
import com.apex.arena.core.theme.SoftLilac
import com.apex.arena.core.theme.TextCrisp
import com.apex.arena.core.theme.TextMuted

@Composable
fun SettingsScreen(
    onNavigateBack: () -> Unit
) {
    var matchAlerts by remember { mutableStateOf(true) }
    var highContrast by remember { mutableStateOf(true) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ObsidianDark)
    ) {
        ApexTopBar(
            title = "Settings & Privacy",
            canNavigateBack = true,
            onNavigateBack = onNavigateBack
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(Dimensions.spaceM)
        ) {
            Text(text = "PREFERENCES", color = SoftLilac, fontSize = 12.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
            Spacer(modifier = Modifier.height(Dimensions.spaceS))

            SettingsToggleRow(
                icon = Icons.Default.Notifications,
                title = "Match Push Alerts",
                subtitle = "Receive notifications 15m prior to room launch",
                checked = matchAlerts,
                onCheckedChange = { matchAlerts = it }
            )
            Spacer(modifier = Modifier.height(Dimensions.spaceM))

            SettingsToggleRow(
                icon = Icons.Default.Palette,
                title = "Esports Obsidian Mode",
                subtitle = "High contrast OLED optimized rendering",
                checked = highContrast,
                onCheckedChange = { highContrast = it }
            )
            Spacer(modifier = Modifier.height(Dimensions.spaceL))

            Text(text = "SECURITY & COMPLIANCE", color = SoftLilac, fontSize = 12.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
            Spacer(modifier = Modifier.height(Dimensions.spaceS))

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, BorderViolet, RoundedCornerShape(Dimensions.cardRadius)),
                shape = RoundedCornerShape(Dimensions.cardRadius),
                colors = CardDefaults.cardColors(containerColor = MidnightCard)
            ) {
                Column(modifier = Modifier.padding(Dimensions.spaceM)) {
                    Text(text = "Row Level Security Active", color = TextCrisp, fontSize = 15.sp, fontWeight = FontWeight.Bold)
                    Text(text = "All tournament registrations and wallet transactions are cryptographically signed and secured via Supabase PostgreSQL RLS.", color = TextMuted, fontSize = 13.sp)
                    Spacer(modifier = Modifier.height(Dimensions.spaceM))
                    Text(text = "Version: 1.0.0 (Production Build)", color = SoftLilac, fontSize = 12.sp)
                }
            }
        }
    }
}

@Composable
fun SettingsToggleRow(
    icon: ImageVector,
    title: String,
    subtitle: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, BorderViolet, RoundedCornerShape(Dimensions.cardRadius)),
        shape = RoundedCornerShape(Dimensions.cardRadius),
        colors = CardDefaults.cardColors(containerColor = MidnightCard)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Dimensions.spaceM),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(modifier = Modifier.weight(1f), verticalAlignment = Alignment.CenterVertically) {
                Icon(imageVector = icon, contentDescription = null, tint = NeonViolet)
                Spacer(modifier = Modifier.padding(horizontal = 6.dp))
                Column {
                    Text(text = title, color = TextCrisp, fontSize = 15.sp, fontWeight = FontWeight.Bold)
                    Text(text = subtitle, color = TextMuted, fontSize = 12.sp)
                }
            }
            Switch(
                checked = checked,
                onCheckedChange = onCheckedChange,
                colors = SwitchDefaults.colors(
                    checkedThumbColor = TextCrisp,
                    checkedTrackColor = NeonViolet
                )
            )
        }
    }
}
