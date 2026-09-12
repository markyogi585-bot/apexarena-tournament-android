package com.apex.arena.presentation.matches

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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.apex.arena.core.components.ApexPrimaryButton
import com.apex.arena.core.components.ApexTopBar
import com.apex.arena.core.components.BadgeStatus
import com.apex.arena.core.components.LoadingStateView
import com.apex.arena.core.components.StatusBadge
import com.apex.arena.core.theme.BorderViolet
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
fun MatchDetailScreen(
    matchId: String,
    onNavigateBack: () -> Unit,
    viewModel: MatchesViewModel
) {
    LaunchedEffect(matchId) {
        viewModel.loadMatchDetail(matchId)
    }

    val uiState by viewModel.uiState.collectAsState()
    val match = uiState.selectedMatch

    if (uiState.isLoading || match == null) {
        LoadingStateView(message = "Loading Match Room...")
        return
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ObsidianDark)
    ) {
        ApexTopBar(
            title = "Match Center",
            canNavigateBack = true,
            onNavigateBack = onNavigateBack
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(Dimensions.spaceM)
        ) {
            Text(
                text = match.tournamentTitle,
                color = SoftLilac,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp
            )
            Spacer(modifier = Modifier.height(Dimensions.spaceS))
            StatusBadge(
                status = when (match.status) {
                    "LIVE" -> BadgeStatus.LIVE
                    "COMPLETED" -> BadgeStatus.COMPLETED
                    else -> BadgeStatus.UPCOMING
                }
            )
            Spacer(modifier = Modifier.height(Dimensions.spaceL))

            // Scoreboard Arena Card
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
                        .padding(Dimensions.spaceL),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(text = match.participantAName, color = TextCrisp, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(text = "${match.scoreA}", color = NeonViolet, fontSize = 36.sp, fontWeight = FontWeight.Black)
                    }

                    Text(text = "VS", color = TextMuted, fontSize = 16.sp, fontWeight = FontWeight.Black)

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(text = match.participantBName, color = TextCrisp, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(text = "${match.scoreB}", color = RadiantRose, fontSize = 36.sp, fontWeight = FontWeight.Black)
                    }
                }
            }
            Spacer(modifier = Modifier.height(Dimensions.spaceL))

            Text(text = "MATCH PROTOCOLS", color = SoftLilac, fontSize = 13.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
            Spacer(modifier = Modifier.height(Dimensions.spaceS))
            Text(text = "• Scheduled Time: ${match.scheduledTime}", color = TextMuted, fontSize = 14.sp)
            Text(text = "• Room ID & Password will be provided 15m prior to start.", color = TextMuted, fontSize = 14.sp)
            Text(text = "• Disconnect grace period: 5 minutes.", color = TextMuted, fontSize = 14.sp)
            Spacer(modifier = Modifier.weight(1f))

            ApexPrimaryButton(
                text = "SUBMIT SCREENSHOT / DISPUTE",
                onClick = {}
            )
            Spacer(modifier = Modifier.height(Dimensions.spaceM))
        }
    }
}
