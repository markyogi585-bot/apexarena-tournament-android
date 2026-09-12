package com.apex.arena.presentation.teams

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
import com.apex.arena.core.components.ApexTextField
import com.apex.arena.core.theme.*

@Composable
fun MyTeamsScreen(
    onNavigateBack: () -> Unit,
    onNavigateToCreateTeam: () -> Unit,
    onNavigateToTeamDetail: (String) -> Unit,
    onNavigateToInvites: () -> Unit
) {
    val teams = listOf(
        Triple("team-1", "Apex Predators [APX]", "4/5 Members • Captain: ApexShadow")
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
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                IconButton(onClick = onNavigateBack) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = ArenaTextPrimary)
                }
                Text("My Esports Squads", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = ArenaTextPrimary)
            }
            IconButton(onClick = onNavigateToInvites) {
                BadgedBox(badge = { Badge { Text("1") } }) {
                    Icon(Icons.Default.Mail, contentDescription = "Invites", tint = ArenaPurpleLight)
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(teams) { (id, name, info) ->
                Card(
                    colors = CardDefaults.cardColors(containerColor = ArenaDarkSurfaceElevated),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onNavigateToTeamDetail(id) }
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
                                    .size(44.dp)
                                    .clip(CircleShape)
                                    .background(ArenaPurplePrimary.copy(alpha = 0.2f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Default.Shield, contentDescription = null, tint = ArenaPurpleLight)
                            }
                            Column {
                                Text(name, fontWeight = FontWeight.Bold, color = ArenaTextPrimary)
                                Text(info, color = ArenaTextSecondary, fontSize = 12.sp)
                            }
                        }
                        Icon(Icons.Default.ChevronRight, contentDescription = null, tint = ArenaTextMuted)
                    }
                }
            }
        }

        ApexButton(
            text = "Create New Squad",
            onClick = onNavigateToCreateTeam,
            modifier = Modifier.fillMaxWidth(),
            leadingIcon = Icons.Default.Add
        )
    }
}

@Composable
fun TeamDetailScreen(
    teamId: String,
    onNavigateBack: () -> Unit
) {
    val members = listOf(
        Pair("ApexShadow", "Captain • BGMI ID: 51239841"),
        Pair("ViperGamer", "Assaulter • BGMI ID: 58829102"),
        Pair("GhostSniper", "Sniper • BGMI ID: 59910293"),
        Pair("ShadowIGL", "Support • BGMI ID: 51102938")
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
            Text("Apex Predators [APX]", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = ArenaTextPrimary)
        }

        Spacer(modifier = Modifier.height(16.dp))

        Card(
            colors = CardDefaults.cardColors(containerColor = ArenaDarkSurfaceElevated),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("24", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = ArenaTextPrimary)
                    Text("Matches", fontSize = 11.sp, color = ArenaTextMuted)
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("19", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = ArenaGreenSuccess)
                    Text("Wins", fontSize = 11.sp, color = ArenaTextMuted)
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("79%", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = ArenaPurpleLight)
                    Text("Win Rate", fontSize = 11.sp, color = ArenaTextMuted)
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        Text("Squad Roster (4/5)", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = ArenaTextPrimary)
        Spacer(modifier = Modifier.height(8.dp))

        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(members) { (name, role) ->
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
                        Column {
                            Text(name, fontWeight = FontWeight.SemiBold, color = ArenaTextPrimary)
                            Text(role, color = ArenaTextSecondary, fontSize = 11.sp)
                        }
                        Icon(Icons.Default.Verified, contentDescription = null, tint = ArenaPurpleLight, modifier = Modifier.size(18.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun CreateTeamScreen(
    onNavigateBack: () -> Unit,
    onTeamCreated: () -> Unit
) {
    var teamName by remember { mutableStateOf("") }
    var teamTag by remember { mutableStateOf("") }

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
                Text("Create Esports Squad", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = ArenaTextPrimary)
            }

            ApexTextField(value = teamName, onValueChange = { teamName = it }, label = "Squad Name (e.g. GodLike Esports)")
            ApexTextField(value = teamTag, onValueChange = { teamTag = it }, label = "Squad Clan Tag [e.g. GOD]")
        }

        ApexButton(
            text = "Register Squad",
            onClick = onTeamCreated,
            enabled = teamName.isNotBlank() && teamTag.isNotBlank(),
            modifier = Modifier.fillMaxWidth(),
            leadingIcon = Icons.Default.GroupAdd
        )
    }
}

@Composable
fun TeamInvitesScreen(
    onNavigateBack: () -> Unit
) {
    var invites by remember {
        mutableStateOf(
            listOf(
                Triple("inv-1", "Vortex Gaming [VRX]", "Invited by: VortexCaptain")
            )
        )
    }

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
            Text("Pending Squad Invites", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = ArenaTextPrimary)
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (invites.isEmpty()) {
            Text("No pending squad invites.", color = ArenaTextSecondary)
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                items(invites) { (id, team, invBy) ->
                    Card(
                        colors = CardDefaults.cardColors(containerColor = ArenaDarkSurfaceElevated),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                            Column {
                                Text(team, fontWeight = FontWeight.Bold, color = ArenaTextPrimary)
                                Text(invBy, color = ArenaTextSecondary, fontSize = 12.sp)
                            }
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                Button(
                                    onClick = { invites = invites.filterNot { it.first == id } },
                                    colors = ButtonDefaults.buttonColors(containerColor = ArenaGreenSuccess),
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text("Accept", color = Color.White)
                                }
                                OutlinedButton(
                                    onClick = { invites = invites.filterNot { it.first == id } },
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text("Decline", color = ArenaErrorRed)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
