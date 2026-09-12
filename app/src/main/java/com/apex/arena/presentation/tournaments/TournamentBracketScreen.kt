package com.apex.arena.presentation.tournaments

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.apex.arena.core.components.ApexTopBar
import com.apex.arena.core.components.BadgeStatus
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
fun TournamentBracketScreen(
    tournamentId: String,
    onNavigateBack: () -> Unit,
    viewModel: TournamentViewModel
) {
    val uiState by viewModel.uiState.collectAsState()
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ObsidianDark)
    ) {
        ApexTopBar(
            title = "Knockout Bracket",
            canNavigateBack = true,
            onNavigateBack = onNavigateBack
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(Dimensions.spaceM)
        ) {
            Text(
                text = "KNOCKOUT ELIMINATION TREE",
                color = SoftLilac,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp
            )
            Spacer(modifier = Modifier.height(Dimensions.spaceM))

            // Horizontal Bracket Layout
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .horizontalScroll(scrollState),
                horizontalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                // Round 1 Column
                BracketRoundColumn(
                    roundTitle = "QUARTER FINALS",
                    matches = uiState.bracketMatches
                )

                // Semi Finals Column
                BracketRoundColumn(
                    roundTitle = "SEMI FINALS",
                    matches = listOf(
                        Match("b1", tournamentId, "", 2, 1, "GodLike Esports", "TBD", null, null, 0, 0, null, "SCHEDULED", "Next Round"),
                        Match("b2", tournamentId, "", 2, 2, "ApexStriker", "TBD", null, null, 0, 0, null, "SCHEDULED", "Next Round")
                    )
                )

                // Grand Finals Column
                BracketRoundColumn(
                    roundTitle = "GRAND FINALS",
                    matches = listOf(
                        Match("b3", tournamentId, "", 3, 1, "TBD", "TBD", null, null, 0, 0, null, "SCHEDULED", "Championship")
                    )
                )
            }
        }
    }
}

@Composable
fun BracketRoundColumn(
    roundTitle: String,
    matches: List<Match>
) {
    Column(
        modifier = Modifier.width(220.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(NeonViolet.copy(alpha = 0.2f), RoundedCornerShape(Dimensions.badgeRadius))
                .padding(vertical = 6.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(text = roundTitle, color = SoftLilac, fontSize = 12.sp, fontWeight = FontWeight.Bold)
        }

        matches.forEach { match ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, BorderViolet, RoundedCornerShape(Dimensions.buttonRadius)),
                shape = RoundedCornerShape(Dimensions.buttonRadius),
                colors = CardDefaults.cardColors(containerColor = MidnightCard)
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = match.participantAName, color = TextCrisp, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                        Text(text = "${match.scoreA}", color = if (match.scoreA > match.scoreB) EmeraldSuccess else TextMuted, fontSize = 13.sp, fontWeight = FontWeight.Black)
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = match.participantBName, color = TextCrisp, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                        Text(text = "${match.scoreB}", color = if (match.scoreB > match.scoreA) EmeraldSuccess else TextMuted, fontSize = 13.sp, fontWeight = FontWeight.Black)
                    }
                }
            }
        }
    }
}
