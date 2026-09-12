package com.apex.arena.presentation.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.apex.arena.core.components.ApexOutlinedButton
import com.apex.arena.core.components.ApexPrimaryButton
import com.apex.arena.core.components.LoadingStateView
import com.apex.arena.core.theme.BorderViolet
import com.apex.arena.core.theme.CrimsonError
import com.apex.arena.core.theme.Dimensions
import com.apex.arena.core.theme.EmeraldSuccess
import com.apex.arena.core.theme.MidnightCard
import com.apex.arena.core.theme.NeonViolet
import com.apex.arena.core.theme.ObsidianDark
import com.apex.arena.core.theme.RadiantRose
import com.apex.arena.core.theme.SoftLilac
import com.apex.arena.core.theme.TextCrisp
import com.apex.arena.core.theme.TextMuted

@Composable
fun ProfileScreen(
    onNavigateToEditProfile: () -> Unit,
    onNavigateToSettings: () -> Unit,
    onNavigateToWallet: () -> Unit,
    onNavigateToTeams: () -> Unit = {},
    onNavigateToKyc: () -> Unit = {},
    onNavigateToRewards: () -> Unit = {},
    onNavigateToFaq: () -> Unit = {},
    onNavigateToTerms: () -> Unit = {},
    onLoggedOut: () -> Unit,
    viewModel: ProfileViewModel
) {
    val uiState by viewModel.uiState.collectAsState()
    val profile = uiState.profile

    if (uiState.isLoading || profile == null) {
        LoadingStateView(message = "Summoning Gladiator Dossier...")
        return
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(ObsidianDark)
            .padding(horizontal = Dimensions.spaceM)
    ) {
        item {
            Spacer(modifier = Modifier.height(Dimensions.spaceM))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "GLADIATOR PROFILE",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 1.sp,
                    color = TextCrisp
                )
                IconButton(onClick = onNavigateToSettings) {
                    Icon(
                        imageVector = Icons.Default.Settings,
                        contentDescription = "Settings",
                        tint = TextCrisp
                    )
                }
            }
            Spacer(modifier = Modifier.height(Dimensions.spaceL))

            // Avatar & Name
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .clip(CircleShape)
                        .background(NeonViolet.copy(alpha = 0.2f))
                        .border(2.dp, NeonViolet, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = profile.username.take(1),
                        color = TextCrisp,
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                Spacer(modifier = Modifier.height(Dimensions.spaceS))
                Text(
                    text = profile.displayName,
                    color = TextCrisp,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "@${profile.username} • In-Game ID: ${profile.gameId ?: "98234718"}",
                    color = TextMuted,
                    fontSize = 13.sp
                )
            }
            Spacer(modifier = Modifier.height(Dimensions.spaceL))

            // Stats Matrix Card
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
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    StatBox(title = "WINS", value = "${profile.wins}", color = EmeraldSuccess)
                    StatBox(title = "LOSSES", value = "${profile.losses}", color = CrimsonError)
                    StatBox(title = "WIN RATE", value = "${profile.winRate.toInt()}%", color = NeonViolet)
                    StatBox(title = "POINTS", value = "${profile.points}", color = RadiantRose)
                }
            }
            Spacer(modifier = Modifier.height(Dimensions.spaceL))

            // Wallet Shortcut Card
            ApexPrimaryButton(
                text = "OPEN GLADIATOR WALLET & PASSBOOK",
                onClick = onNavigateToWallet
            )
            Spacer(modifier = Modifier.height(Dimensions.spaceM))

            // Quick Hub Rows
            ProfileMenuRow(title = "My Esports Squads", icon = androidx.compose.material.icons.Icons.Default.Shield, onClick = onNavigateToTeams)
            ProfileMenuRow(title = "KYC Identity Compliance", icon = androidx.compose.material.icons.Icons.Default.Security, onClick = onNavigateToKyc)
            ProfileMenuRow(title = "Daily Streaks & Arena Pass", icon = androidx.compose.material.icons.Icons.Default.Stars, onClick = onNavigateToRewards)
            ProfileMenuRow(title = "Help & Support Center", icon = androidx.compose.material.icons.Icons.Default.Help, onClick = onNavigateToFaq)
            ProfileMenuRow(title = "Fair Play & Anti-Cheat Policy", icon = androidx.compose.material.icons.Icons.Default.Gavel, onClick = onNavigateToTerms)

            Spacer(modifier = Modifier.height(Dimensions.spaceM))

            // Action Buttons
            ApexOutlinedButton(
                text = "Edit Profile & In-Game ID",
                onClick = onNavigateToEditProfile
            )
            Spacer(modifier = Modifier.height(Dimensions.spaceS))

            ApexOutlinedButton(
                text = "Log Out of Arena",
                onClick = { viewModel.logout(onLoggedOut) },
                borderColor = CrimsonError
            )
            Spacer(modifier = Modifier.height(Dimensions.spaceXL))
        }
    }
}

@Composable
fun ProfileMenuRow(
    title: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp)
            .border(1.dp, BorderViolet.copy(alpha = 0.5f), RoundedCornerShape(10.dp)),
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(containerColor = MidnightCard),
        onClick = onClick
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Icon(icon, contentDescription = null, tint = NeonViolet, modifier = Modifier.size(20.dp))
                Text(title, color = TextCrisp, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
            }
            Icon(androidx.compose.material.icons.Icons.Default.ChevronRight, contentDescription = null, tint = TextMuted, modifier = Modifier.size(18.dp))
        }
    }
}

@Composable
fun StatBox(title: String, value: String, color: androidx.compose.ui.graphics.Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = title, color = TextMuted, fontSize = 10.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(2.dp))
        Text(text = value, color = color, fontSize = 18.sp, fontWeight = FontWeight.Black)
    }
}
