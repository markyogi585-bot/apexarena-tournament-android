package com.apex.arena.presentation.rewards

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.apex.arena.core.components.ApexButton
import com.apex.arena.core.theme.*

@Composable
fun RewardsOverviewScreen(
    onNavigateBack: () -> Unit,
    onNavigateToAchievements: () -> Unit
) {
    var claimedStreak by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ArenaDarkBase)
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(ArenaSpacing.lg)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            IconButton(onClick = onNavigateBack) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = ArenaTextPrimary)
            }
            Text("Daily Streaks & Arena Pass", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = ArenaTextPrimary)
        }

        Spacer(modifier = Modifier.height(16.dp))

        Card(
            colors = CardDefaults.cardColors(containerColor = ArenaDarkSurfaceElevated),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
                Text("7-DAY LOGIN STREAK", color = ArenaPurpleLight, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                Text("Claim daily credits and unlock exclusive VIP tournament access.", color = ArenaTextSecondary, fontSize = 13.sp)

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    (1..7).forEach { day ->
                        Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(if (day <= 3 || (day == 4 && claimedStreak)) ArenaGreenSuccess else if (day == 4) ArenaPurplePrimary else ArenaDarkSurface),
                                contentAlignment = Alignment.Center
                            ) {
                                Text("$day", fontWeight = FontWeight.Bold, color = Color.White, fontSize = 12.sp)
                            }
                            Text("+${day * 10}", color = ArenaTextMuted, fontSize = 10.sp)
                        }
                    }
                }

                ApexButton(
                    text = if (claimedStreak) "Day 4 Claimed (+50 Coins)" else "Claim Today's Bonus (+50 Coins)",
                    onClick = { claimedStreak = true },
                    enabled = !claimedStreak,
                    modifier = Modifier.fillMaxWidth(),
                    leadingIcon = Icons.Default.Stars
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Card(
            colors = CardDefaults.cardColors(containerColor = ArenaDarkSurfaceElevated),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onNavigateToAchievements() }
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    Icon(Icons.Default.EmojiEvents, contentDescription = null, tint = ArenaAccentRose)
                    Column {
                        Text("Achievement Roadmap", fontWeight = FontWeight.Bold, color = ArenaTextPrimary)
                        Text("4/12 Unlocked • ₹1,800 Claimable", color = ArenaTextSecondary, fontSize = 12.sp)
                    }
                }
                Icon(Icons.Default.ChevronRight, contentDescription = null, tint = ArenaTextMuted)
            }
        }
    }
}

@Composable
fun AchievementTreeScreen(
    onNavigateBack: () -> Unit,
    onNavigateToDetail: (String) -> Unit
) {
    val list = listOf(
        Triple("ach-1", "First Blood Champion", true),
        Triple("ach-2", "Centurion Winner (8/10)", false),
        Triple("ach-3", "Flawless MVP Streak (3/5)", false),
        Triple("ach-4", "High Roller Tier (2/5)", false)
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ArenaDarkBase)
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(ArenaSpacing.lg)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            IconButton(onClick = onNavigateBack) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = ArenaTextPrimary)
            }
            Text("Achievement Tree & Badges", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = ArenaTextPrimary)
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            items(list) { (id, name, isUnlocked) ->
                Card(
                    colors = CardDefaults.cardColors(containerColor = ArenaDarkSurfaceElevated),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onNavigateToDetail(id) }
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(CircleShape)
                                    .background(if (isUnlocked) ArenaGreenSuccess.copy(alpha = 0.2f) else ArenaDarkSurface),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Default.MilitaryTech, contentDescription = null, tint = if (isUnlocked) ArenaGreenSuccess else ArenaTextMuted)
                            }
                            Column {
                                Text(name, fontWeight = FontWeight.Bold, color = ArenaTextPrimary)
                                Text(if (isUnlocked) "UNLOCKED • +₹50 Credited" else "IN PROGRESS", color = if (isUnlocked) ArenaGreenSuccess else ArenaPurpleLight, fontSize = 11.sp)
                            }
                        }
                        Icon(Icons.Default.ChevronRight, contentDescription = null, tint = ArenaTextMuted)
                    }
                }
            }
        }
    }
}

@Composable
fun AchievementDetailScreen(
    achievementId: String,
    onNavigateBack: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ArenaDarkBase)
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(ArenaSpacing.lg),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                IconButton(onClick = onNavigateBack) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = ArenaTextPrimary)
                }
                Text("Achievement Details", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = ArenaTextPrimary)
            }

            Card(
                colors = CardDefaults.cardColors(containerColor = ArenaDarkSurfaceElevated),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Icon(Icons.Default.MilitaryTech, contentDescription = null, tint = ArenaAccentRose, modifier = Modifier.size(64.dp))
                    Text("Centurion Winner", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = ArenaTextPrimary)
                    Text("Win 10 professional squad matches in verified tournaments.", color = ArenaTextSecondary, textAlign = androidx.compose.ui.text.style.TextAlign.Center)
                    LinearProgressIndicator(
                        progress = { 0.8f },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp)
                            .clip(RoundedCornerShape(4.dp)),
                        color = ArenaPurplePrimary,
                        trackColor = ArenaDarkSurface
                    )
                    Text("Progress: 8 / 10 Matches Won", color = ArenaPurpleLight, fontSize = 12.sp)
                }
            }
        }

        ApexButton(text = "Claim Reward (+₹250)", onClick = onNavigateBack, enabled = false, modifier = Modifier.fillMaxWidth(), leadingIcon = Icons.Default.Lock)
    }
}
