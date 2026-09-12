package com.apex.arena.presentation.matches

import androidx.compose.foundation.background
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
import com.apex.arena.core.components.ApexTextField
import com.apex.arena.core.components.JoinedMatchRoomCard
import com.apex.arena.core.components.StatusBadge
import com.apex.arena.core.theme.*

@Composable
fun LiveMatchRoomScreen(
    matchId: String,
    onNavigateBack: () -> Unit,
    onNavigateToDispute: (String) -> Unit
) {
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
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(onClick = onNavigateBack) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = ArenaTextPrimary)
            }
            StatusBadge(status = "LIVE")
            IconButton(onClick = { onNavigateToDispute(matchId) }) {
                Icon(Icons.Default.ReportProblem, contentDescription = "Dispute", tint = ArenaAccentRose)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        JoinedMatchRoomCard(
            roomId = "ARENA-ROOM-8821",
            roomPass = "APEX-PASS-2026",
            slotNumber = 12
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text("Live Match Standings & Kill Feed", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = ArenaTextPrimary)

        Spacer(modifier = Modifier.height(8.dp))

        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            val standings = listOf(
                Triple("#1 GodLike Esports", "14 Kills", "ALIVE"),
                Triple("#2 Team Soul", "11 Kills", "ALIVE"),
                Triple("#3 Team XSpark", "8 Kills", "ELIMINATED"),
                Triple("#4 Global Esports", "6 Kills", "ELIMINATED")
            )
            items(standings) { (team, kills, state) ->
                Card(
                    colors = CardDefaults.cardColors(containerColor = ArenaDarkSurfaceElevated),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(team, fontWeight = FontWeight.SemiBold, color = ArenaTextPrimary)
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                            Text(kills, color = ArenaPurpleLight, fontSize = 12.sp)
                            StatusBadge(status = state)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun MatchDisputeScreen(
    matchId: String,
    onNavigateBack: () -> Unit
) {
    var reason by remember { mutableStateOf("Hacking / Config modification") }
    var description by remember { mutableStateOf("") }
    var isSubmitted by remember { mutableStateOf(false) }

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
                Text("File Match Dispute Ticket", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = ArenaTextPrimary)
            }

            if (!isSubmitted) {
                Text("Match ID: #$matchId", color = ArenaTextSecondary, fontSize = 12.sp)

                ApexTextField(
                    value = reason,
                    onValueChange = { reason = it },
                    label = "Dispute Category"
                )

                ApexTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = "Detailed Description & Proof Link (Drive / YouTube)",
                    singleLine = false
                )
            } else {
                Card(
                    colors = CardDefaults.cardColors(containerColor = ArenaDarkSurfaceElevated),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Icon(Icons.Default.CheckCircle, contentDescription = null, tint = ArenaGreenSuccess, modifier = Modifier.size(48.dp))
                        Text("Dispute Under Review", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = ArenaTextPrimary)
                        Text("Our referee team is reviewing server logs and recorded POV footage. Resolution will be published within 2 hours.", color = ArenaTextSecondary, fontSize = 12.sp)
                    }
                }
            }
        }

        ApexButton(
            text = if (!isSubmitted) "Submit Official Dispute" else "Return to Matches",
            onClick = {
                if (!isSubmitted) isSubmitted = true else onNavigateBack()
            },
            modifier = Modifier.fillMaxWidth(),
            leadingIcon = if (!isSubmitted) Icons.Default.Gavel else Icons.Default.ArrowBack
        )
    }
}
