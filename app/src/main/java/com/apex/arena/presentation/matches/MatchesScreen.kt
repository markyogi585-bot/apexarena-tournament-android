package com.apex.arena.presentation.matches

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.apex.arena.core.components.BadgeStatus
import com.apex.arena.core.components.EmptyStateView
import com.apex.arena.core.components.LoadingStateView
import com.apex.arena.core.components.StatusBadge
import com.apex.arena.core.theme.BorderViolet
import com.apex.arena.core.theme.Dimensions
import com.apex.arena.core.theme.EmeraldSuccess
import com.apex.arena.core.theme.MidnightCard
import com.apex.arena.core.theme.NeonViolet
import com.apex.arena.core.theme.ObsidianDark
import com.apex.arena.core.theme.SoftLilac
import com.apex.arena.core.theme.TextCrisp
import com.apex.arena.core.theme.TextMuted
import com.apex.arena.domain.models.Match

@Composable
fun MatchesScreen(
    onNavigateToMatchDetail: (String) -> Unit,
    viewModel: MatchesViewModel
) {
    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ObsidianDark)
            .padding(horizontal = Dimensions.spaceM)
    ) {
        Spacer(modifier = Modifier.height(Dimensions.spaceM))
        Text(
            text = "MY MATCHES",
            fontSize = 22.sp,
            fontWeight = FontWeight.Black,
            letterSpacing = 1.sp,
            color = TextCrisp
        )
        Spacer(modifier = Modifier.height(Dimensions.spaceM))

        // Tabs
        TabRow(
            selectedTabIndex = uiState.selectedTab,
            containerColor = ObsidianDark,
            contentColor = NeonViolet,
            indicator = { tabPositions ->
                TabRowDefaults.SecondaryIndicator(
                    modifier = Modifier.tabIndicatorOffset(tabPositions[uiState.selectedTab]),
                    color = NeonViolet
                )
            }
        ) {
            Tab(
                selected = uiState.selectedTab == 0,
                onClick = { viewModel.onTabSelected(0) },
                text = { Text("UPCOMING & LIVE", fontWeight = FontWeight.Bold) }
            )
            Tab(
                selected = uiState.selectedTab == 1,
                onClick = { viewModel.onTabSelected(1) },
                text = { Text("COMPLETED", fontWeight = FontWeight.Bold) }
            )
        }
        Spacer(modifier = Modifier.height(Dimensions.spaceM))

        if (uiState.isLoading) {
            LoadingStateView(message = "Tracking Active Matches...")
        } else {
            val filteredMatches = if (uiState.selectedTab == 0) {
                uiState.matches.filter { it.status != "COMPLETED" }
            } else {
                uiState.matches.filter { it.status == "COMPLETED" }
            }

            if (filteredMatches.isEmpty()) {
                EmptyStateView(
                    title = "No Matches Found",
                    description = "Join an ongoing tournament to schedule your next battle."
                )
            } else {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(filteredMatches) { match ->
                        MatchCardItem(
                            match = match,
                            onClick = { onNavigateToMatchDetail(match.id) }
                        )
                        Spacer(modifier = Modifier.height(Dimensions.spaceM))
                    }
                }
            }
        }
    }
}

@Composable
fun MatchCardItem(
    match: Match,
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
                Text(
                    text = match.tournamentTitle,
                    color = SoftLilac,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
                StatusBadge(
                    status = when (match.status) {
                        "LIVE" -> BadgeStatus.LIVE
                        "COMPLETED" -> BadgeStatus.COMPLETED
                        else -> BadgeStatus.UPCOMING
                    }
                )
            }
            Spacer(modifier = Modifier.height(Dimensions.spaceM))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(text = match.participantAName, color = TextCrisp, fontSize = 15.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = match.participantBName, color = TextCrisp, fontSize = 15.sp, fontWeight = FontWeight.Bold)
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text(text = "${match.scoreA}", color = if (match.scoreA > match.scoreB) EmeraldSuccess else TextMuted, fontSize = 16.sp, fontWeight = FontWeight.Black)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = "${match.scoreB}", color = if (match.scoreB > match.scoreA) EmeraldSuccess else TextMuted, fontSize = 16.sp, fontWeight = FontWeight.Black)
                }
            }
        }
    }
}
