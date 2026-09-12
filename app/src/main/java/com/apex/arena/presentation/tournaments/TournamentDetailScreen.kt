package com.apex.arena.presentation.tournaments

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
import androidx.compose.foundation.lazy.LazyColumn
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
import com.apex.arena.core.components.ApexAccentButton
import com.apex.arena.core.components.ApexOutlinedButton
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
fun TournamentDetailScreen(
    tournamentId: String,
    onNavigateBack: () -> Unit,
    onNavigateToBracket: (String) -> Unit,
    viewModel: TournamentViewModel
) {
    LaunchedEffect(tournamentId) {
        viewModel.loadTournamentDetails(tournamentId)
    }

    val uiState by viewModel.uiState.collectAsState()
    val tournament = uiState.selectedTournament

    if (uiState.isLoading || tournament == null) {
        LoadingStateView(message = "Loading Tournament Arena...")
        return
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ObsidianDark)
    ) {
        ApexTopBar(
            title = tournament.gameTitle,
            canNavigateBack = true,
            onNavigateBack = onNavigateBack
        )

        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = Dimensions.spaceM)
        ) {
            item {
                Spacer(modifier = Modifier.height(Dimensions.spaceS))
                Text(
                    text = tournament.title,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Black,
                    color = TextCrisp
                )
                Spacer(modifier = Modifier.height(Dimensions.spaceS))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    StatusBadge(status = if (tournament.format == "SOLO") BadgeStatus.SOLO else BadgeStatus.SQUAD)
                    StatusBadge(status = BadgeStatus.REGISTRATION_OPEN)
                }
                Spacer(modifier = Modifier.height(Dimensions.spaceM))
            }

            // Prize Pool & Stats Card
            item {
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
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(text = "TOTAL PRIZE POOL", color = TextMuted, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            Text(text = "₹${tournament.prizePool.toInt()}", color = RadiantRose, fontSize = 24.sp, fontWeight = FontWeight.Black)
                        }
                        Column(horizontalAlignment = Alignment.End) {
                            Text(text = "ENTRY FEE", color = TextMuted, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            Text(
                                text = if (tournament.entryFee == 0.0) "FREE ENTRY" else "₹${tournament.entryFee.toInt()}",
                                color = EmeraldSuccess,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(Dimensions.spaceL))
            }

            // Rules & Schedule
            item {
                Text(text = "OFFICIAL TOURNAMENT RULES", color = SoftLilac, fontSize = 13.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
                Spacer(modifier = Modifier.height(Dimensions.spaceS))
                Text(text = tournament.rulesText, color = TextCrisp, fontSize = 14.sp, lineHeight = 20.sp)
                Spacer(modifier = Modifier.height(Dimensions.spaceL))

                Text(text = "SCHEDULE & DEADLINES", color = SoftLilac, fontSize = 13.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
                Spacer(modifier = Modifier.height(Dimensions.spaceS))
                Text(text = "• Tournament Starts: ${tournament.startTime}", color = TextMuted, fontSize = 14.sp)
                Text(text = "• Registration Closes: ${tournament.registrationDeadline}", color = TextMuted, fontSize = 14.sp)
                Spacer(modifier = Modifier.height(Dimensions.spaceL))

                ApexOutlinedButton(
                    text = "View Knockout Bracket",
                    onClick = { onNavigateToBracket(tournament.id) }
                )
                Spacer(modifier = Modifier.height(Dimensions.spaceL))
            }
        }

        // Action Bottom Bar
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(MidnightCard)
                .padding(Dimensions.spaceM)
        ) {
            if (uiState.isRegistered) {
                ApexAccentButton(
                    text = "✓ REGISTERED FOR ARENA",
                    onClick = {},
                    enabled = false
                )
            } else {
                ApexPrimaryButton(
                    text = "JOIN TOURNAMENT NOW",
                    onClick = { viewModel.registerCurrentPlayer(tournament.id) }
                )
            }
        }
    }
}
