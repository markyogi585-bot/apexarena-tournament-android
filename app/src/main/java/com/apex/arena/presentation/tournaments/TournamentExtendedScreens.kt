package com.apex.arena.presentation.tournaments

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
import com.apex.arena.core.theme.*

@Composable
fun TournamentRulesScreen(
    tournamentId: String,
    onNavigateBack: () -> Unit
) {
    val rules = listOf(
        "1. Anti-Cheat Policy: Emulators, rooted injectors, and third-party crosshairs are strictly banned. Violation leads to permanent ban.",
        "2. Room Credentials: Room ID & Password will be revealed 15 minutes before scheduled start time in the match lobby.",
        "3. Scoring System: 1 Kill = 1 Point. Placement: #1 = 15 pts, #2 = 12 pts, #3 = 10 pts, #4 = 8 pts, #5 = 6 pts.",
        "4. POV Recording: At least 1 player in squad must record full gameplay POV with voice comms.",
        "5. Dispute Window: Disputes must be filed within 10 minutes after match conclusion."
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
            Text("Tournament Official Rules", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = ArenaTextPrimary)
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(rules) { rule ->
                Card(
                    colors = CardDefaults.cardColors(containerColor = ArenaDarkSurfaceElevated),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(modifier = Modifier.padding(16.dp), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        Icon(Icons.Default.Gavel, contentDescription = null, tint = ArenaAccentRose)
                        Text(rule, color = ArenaTextPrimary, fontSize = 13.sp, lineHeight = 20.sp)
                    }
                }
            }
        }
    }
}

@Composable
fun TournamentScheduleScreen(
    tournamentId: String,
    onNavigateBack: () -> Unit
) {
    val schedules = listOf(
        Triple("Match 1: Erangel", "19:30 IST", "Room opens at 19:15 IST"),
        Triple("Match 2: Miramar", "20:30 IST", "Room opens at 20:15 IST"),
        Triple("Match 3: Sanhok (Finals)", "21:30 IST", "Room opens at 21:15 IST")
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
            Text("Match Schedule & Timeline", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = ArenaTextPrimary)
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            items(schedules) { (round, time, roomInfo) ->
                Card(
                    colors = CardDefaults.cardColors(containerColor = ArenaDarkSurfaceElevated),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text(round, fontWeight = FontWeight.Bold, color = ArenaTextPrimary)
                            Text(time, color = ArenaPurpleLight, fontWeight = FontWeight.SemiBold)
                        }
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            Icon(Icons.Default.AccessTime, contentDescription = null, tint = ArenaTextMuted, modifier = Modifier.size(16.dp))
                            Text(roomInfo, color = ArenaTextSecondary, fontSize = 12.sp)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun TournamentParticipantsScreen(
    tournamentId: String,
    onNavigateBack: () -> Unit
) {
    val participants = listOf(
        Pair("GodLike Esports", "Slot #1 • Captain: JonathanGaming"),
        Pair("Team Soul", "Slot #2 • Captain: Mortal77"),
        Pair("Team XSpark", "Slot #3 • Captain: ScoutOP"),
        Pair("Global Esports", "Slot #4 • Captain: Mavi"),
        Pair("Apex Predators", "Slot #5 • Captain: ApexShadow")
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
            Text("Registered Rosters (24/25)", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = ArenaTextPrimary)
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            items(participants) { (teamName, slotInfo) ->
                Card(
                    colors = CardDefaults.cardColors(containerColor = ArenaDarkSurfaceElevated),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(ArenaPurplePrimary.copy(alpha = 0.2f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.Shield, contentDescription = null, tint = ArenaPurpleLight)
                        }
                        Column {
                            Text(teamName, fontWeight = FontWeight.Bold, color = ArenaTextPrimary)
                            Text(slotInfo, color = ArenaTextSecondary, fontSize = 12.sp)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun JoinTournamentScreen(
    tournamentId: String,
    onNavigateBack: () -> Unit,
    onJoinSuccess: () -> Unit
) {
    var selectedSquad by remember { mutableStateOf("Apex Predators [APX]") }
    var agreedToRules by remember { mutableStateOf(false) }

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
                Text("Tournament Registration", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = ArenaTextPrimary)
            }

            Card(
                colors = CardDefaults.cardColors(containerColor = ArenaDarkSurfaceElevated),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Selected Squad Roster", color = ArenaTextSecondary, fontSize = 12.sp)
                    Text(selectedSquad, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = ArenaTextPrimary)
                    Text("4 Active Players • Verified Roster", color = ArenaGreenSuccess, fontSize = 11.sp)
                }
            }

            Card(
                colors = CardDefaults.cardColors(containerColor = ArenaDarkSurfaceElevated),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Entry Fee", color = ArenaTextSecondary)
                        Text("₹150", fontWeight = FontWeight.Bold, color = ArenaAccentRose)
                    }
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Your Balance", color = ArenaTextSecondary)
                        Text("₹1,650", fontWeight = FontWeight.Bold, color = ArenaGreenSuccess)
                    }
                }
            }

            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Checkbox(
                    checked = agreedToRules,
                    onCheckedChange = { agreedToRules = it },
                    colors = CheckboxDefaults.colors(checkedColor = ArenaPurplePrimary)
                )
                Text("I agree to tournament anti-cheat & conduct rules", color = ArenaTextSecondary, fontSize = 12.sp)
            }
        }

        ApexButton(
            text = "Confirm Entry & Pay ₹150",
            onClick = onJoinSuccess,
            enabled = agreedToRules,
            modifier = Modifier.fillMaxWidth(),
            leadingIcon = Icons.Default.SportsEsports
        )
    }
}

@Composable
fun JoinSuccessScreen(
    tournamentId: String,
    onNavigateToMatches: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(ArenaDarkBase)
            .padding(ArenaSpacing.lg),
        contentAlignment = Alignment.Center
    ) {
        Card(
            colors = CardDefaults.cardColors(containerColor = ArenaDarkSurfaceElevated),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(72.dp)
                        .clip(CircleShape)
                        .background(ArenaGreenSuccess.copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.CheckCircle, contentDescription = null, tint = ArenaGreenSuccess, modifier = Modifier.size(48.dp))
                }
                Text("Registration Confirmed!", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = ArenaTextPrimary)
                Text(
                    "You have successfully joined the tournament. Your slot is reserved and room credentials will unlock 15 minutes before match start.",
                    color = ArenaTextSecondary,
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                    fontSize = 13.sp
                )
                ApexButton(
                    text = "Go to My Matches",
                    onClick = onNavigateToMatches,
                    modifier = Modifier.fillMaxWidth(),
                    leadingIcon = Icons.Default.SportsEsports
                )
            }
        }
    }
}
