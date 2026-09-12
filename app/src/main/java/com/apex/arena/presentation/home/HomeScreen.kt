package com.apex.arena.presentation.home

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Search
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
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.apex.arena.core.components.BadgeStatus
import com.apex.arena.core.components.ErrorStateView
import com.apex.arena.core.components.LoadingStateView
import com.apex.arena.core.components.StatusBadge
import com.apex.arena.core.theme.BorderViolet
import com.apex.arena.core.theme.CyanGlow
import com.apex.arena.core.theme.Dimensions
import com.apex.arena.core.theme.EmeraldSuccess
import com.apex.arena.core.theme.MidnightCard
import com.apex.arena.core.theme.MutedSlate
import com.apex.arena.core.theme.NeonViolet
import com.apex.arena.core.theme.ObsidianDark
import com.apex.arena.core.theme.RadiantRose
import com.apex.arena.core.theme.SoftLilac
import com.apex.arena.core.theme.TextCrisp
import com.apex.arena.core.theme.TextMuted
import com.apex.arena.domain.models.Tournament

@Composable
fun HomeScreen(
    onNavigateToTournamentDetails: (String) -> Unit,
    onNavigateToNotifications: () -> Unit,
    onNavigateToWallet: () -> Unit,
    onNavigateToSearch: () -> Unit = {},
    viewModel: HomeViewModel
) {
    val uiState by viewModel.uiState.collectAsState()

    // Live Pulse Animation for Esports match card
    val infiniteTransition = rememberInfiniteTransition(label = "livePulse")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(
            animation = tween(800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulseScale"
    )

    if (uiState.isLoading) {
        LoadingStateView(message = "Summoning Arena Dashboard...")
        return
    }

    if (uiState.errorMessage != null) {
        ErrorStateView(
            errorMessage = uiState.errorMessage ?: "Unknown error",
            onRetry = { viewModel.loadDashboardData() }
        )
        return
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(ObsidianDark)
            .padding(horizontal = Dimensions.spaceM)
    ) {
        // Header with Avatar and Wallet Pill
        item {
            Spacer(modifier = Modifier.height(Dimensions.spaceM))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .background(NeonViolet.copy(alpha = 0.3f))
                            .border(1.5.dp, NeonViolet, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = uiState.profile?.username?.take(1) ?: "A",
                            color = TextCrisp,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp
                        )
                    }
                    Spacer(modifier = Modifier.width(Dimensions.spaceS))
                    Column {
                        Text(text = "Welcome Back,", color = TextMuted, fontSize = 11.sp)
                        Text(
                            text = uiState.profile?.displayName ?: "ApexStriker",
                            color = TextCrisp,
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp
                        )
                    }
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = onNavigateToSearch) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search",
                            tint = TextCrisp
                        )
                    }

                    // Quick Wallet Pill
                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .background(MidnightCard)
                            .border(1.dp, BorderViolet, RoundedCornerShape(20.dp))
                            .clickable(onClick = onNavigateToWallet)
                            .padding(horizontal = 12.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.AccountBalanceWallet,
                            contentDescription = "Wallet",
                            tint = EmeraldSuccess,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "₹1,650",
                            color = TextCrisp,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Black
                        )
                    }

                    Spacer(modifier = Modifier.width(4.dp))
                    IconButton(onClick = onNavigateToNotifications) {
                        Icon(
                            imageVector = Icons.Default.Notifications,
                            contentDescription = "Notifications",
                            tint = TextCrisp
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(Dimensions.spaceL))
        }

        // Tier Division Banner Card
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(
                        1.dp,
                        Brush.horizontalGradient(listOf(NeonViolet, RadiantRose)),
                        RoundedCornerShape(Dimensions.cardRadius)
                    ),
                shape = RoundedCornerShape(Dimensions.cardRadius),
                colors = CardDefaults.cardColors(containerColor = MidnightCard)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(Dimensions.spaceM),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "CURRENT TIER",
                            color = SoftLilac,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        )
                        Text(
                            text = "${uiState.profile?.tier ?: "DIAMOND"} DIVISION",
                            color = TextCrisp,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Black
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "${uiState.profile?.points ?: 2450} APEX POINTS",
                            color = TextMuted,
                            fontSize = 13.sp
                        )
                    }
                    Icon(
                        imageVector = Icons.Default.EmojiEvents,
                        contentDescription = null,
                        tint = NeonViolet,
                        modifier = Modifier.size(48.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.height(Dimensions.spaceL))
        }

        // Featured Tournaments Section
        item {
            Text(
                text = "FEATURED TOURNAMENTS",
                color = TextCrisp,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.5.sp
            )
            Spacer(modifier = Modifier.height(Dimensions.spaceS))
        }

        items(uiState.featuredTournaments) { tournament ->
            TournamentCardItem(
                tournament = tournament,
                onClick = { onNavigateToTournamentDetails(tournament.id) }
            )
            Spacer(modifier = Modifier.height(Dimensions.spaceM))
        }

        // Live & Upcoming Battles Section
        item {
            Spacer(modifier = Modifier.height(Dimensions.spaceS))
            Text(
                text = "LIVE & UPCOMING BATTLES",
                color = TextCrisp,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.5.sp
            )
            Spacer(modifier = Modifier.height(Dimensions.spaceS))
        }

        items(uiState.liveMatches) { match ->
            val isLive = match.status == "LIVE"
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = Dimensions.spaceS)
                    .scale(if (isLive) pulseScale else 1f)
                    .border(
                        1.dp,
                        if (isLive) EmeraldSuccess else BorderViolet.copy(alpha = 0.6f),
                        RoundedCornerShape(Dimensions.buttonRadius)
                    ),
                shape = RoundedCornerShape(Dimensions.buttonRadius),
                colors = CardDefaults.cardColors(containerColor = MutedSlate)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(Dimensions.spaceM),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = match.tournamentTitle,
                            color = SoftLilac,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "${match.participantAName} vs ${match.participantBName}",
                            color = TextCrisp,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    StatusBadge(
                        status = if (isLive) BadgeStatus.LIVE else BadgeStatus.UPCOMING
                    )
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(Dimensions.spaceXL))
        }
    }
}

@Composable
fun TournamentCardItem(
    tournament: Tournament,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .border(1.dp, BorderViolet, RoundedCornerShape(Dimensions.cardRadius)),
        shape = RoundedCornerShape(Dimensions.cardRadius),
        colors = CardDefaults.cardColors(containerColor = MidnightCard)
    ) {
        Column(modifier = Modifier.padding(Dimensions.spaceM)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                StatusBadge(
                    status = if (tournament.format == "SOLO") BadgeStatus.SOLO else BadgeStatus.SQUAD
                )
                StatusBadge(
                    status = when (tournament.status) {
                        "ONGOING" -> BadgeStatus.LIVE
                        "REGISTRATION_OPEN" -> BadgeStatus.REGISTRATION_OPEN
                        else -> BadgeStatus.UPCOMING
                    }
                )
            }
            Spacer(modifier = Modifier.height(Dimensions.spaceS))
            Text(
                text = tournament.title,
                color = TextCrisp,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = tournament.gameTitle,
                color = TextMuted,
                fontSize = 13.sp
            )
            Spacer(modifier = Modifier.height(Dimensions.spaceM))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(text = "PRIZE POOL", color = TextMuted, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    Text(text = "₹${tournament.prizePool.toInt()}", color = RadiantRose, fontSize = 15.sp, fontWeight = FontWeight.Black)
                }
                Column {
                    Text(text = "SLOTS", color = TextMuted, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    Text(text = "${tournament.currentParticipants}/${tournament.maxParticipants}", color = TextCrisp, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                }
                Column {
                    Text(text = "STARTS", color = TextMuted, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    Text(text = tournament.startTime, color = SoftLilac, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                }
            }
        }
    }
}
