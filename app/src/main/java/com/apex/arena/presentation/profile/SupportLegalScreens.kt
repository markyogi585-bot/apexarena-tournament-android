package com.apex.arena.presentation.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
fun SupportFaqScreen(
    onNavigateBack: () -> Unit
) {
    val faqs = listOf(
        Pair("How do I receive tournament room credentials?", "Room ID and Password are automatically published in the 'My Matches' tab 15 minutes prior to tournament start time."),
        Pair("What is the withdrawal turnaround time?", "UPI transfers and IMPS bank payouts are processed instantly (typically under 60 seconds) after KYC verification."),
        Pair("What happens if a player cheats or teams up?", "You can file a formal dispute with match ID and video proof in the Match Details screen. Referees review server logs within 2 hours."),
        Pair("Can I participate without KYC?", "You can participate in all free and entry tournaments. KYC is only mandatory when withdrawing cash above ₹1,000 to bank accounts.")
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
            Text("Help & Support Center", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = ArenaTextPrimary)
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            items(faqs) { (q, a) ->
                var expanded by remember { mutableStateOf(false) }
                Card(
                    colors = CardDefaults.cardColors(containerColor = ArenaDarkSurfaceElevated),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { expanded = !expanded }
                ) {
                    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                            Text(q, fontWeight = FontWeight.SemiBold, color = ArenaTextPrimary, modifier = Modifier.weight(1f))
                            Icon(if (expanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore, contentDescription = null, tint = ArenaPurpleLight)
                        }
                        if (expanded) {
                            HorizontalDivider(color = ArenaBorder)
                            Text(a, color = ArenaTextSecondary, fontSize = 13.sp, lineHeight = 19.sp)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun TermsPrivacyScreen(
    onNavigateBack: () -> Unit
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
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            IconButton(onClick = onNavigateBack) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = ArenaTextPrimary)
            }
            Text("Fair Play & Privacy Policy", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = ArenaTextPrimary)
        }

        Spacer(modifier = Modifier.height(16.dp))

        Card(
            colors = CardDefaults.cardColors(containerColor = ArenaDarkSurfaceElevated),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text("1. Zero Gambling & Skill-Based Gaming", fontWeight = FontWeight.Bold, color = ArenaTextPrimary)
                Text("Apex Arena operates strictly as a skill-based competitive esports platform under Article 19(1)(g) of the Constitution of India.", color = ArenaTextSecondary, fontSize = 12.sp)

                HorizontalDivider(color = ArenaBorder)

                Text("2. Anti-Fraud & Data Security", fontWeight = FontWeight.Bold, color = ArenaTextPrimary)
                Text("Your financial information and government documents are encrypted using AES-256 and processed through RBI-approved payment aggregators.", color = ArenaTextSecondary, fontSize = 12.sp)

                HorizontalDivider(color = ArenaBorder)

                Text("3. Hardware Fingerprinting", fontWeight = FontWeight.Bold, color = ArenaTextPrimary)
                Text("To safeguard tournament integrity, emulator detection and hardware ban logs are maintained.", color = ArenaTextSecondary, fontSize = 12.sp)
            }
        }
    }
}
