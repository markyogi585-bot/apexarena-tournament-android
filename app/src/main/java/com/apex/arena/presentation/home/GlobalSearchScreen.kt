package com.apex.arena.presentation.home

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
import com.apex.arena.core.components.ApexTextField
import com.apex.arena.core.components.StatusBadge
import com.apex.arena.core.theme.*

@Composable
fun GlobalSearchScreen(
    onNavigateBack: () -> Unit,
    onNavigateToTournament: (String) -> Unit,
    onNavigateToMatch: (String) -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("All") }

    val recentSearches = listOf("BGMI Pro League", "GodLike Squad", "Valorant Tactical", "Free Fire Knockout")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ArenaDarkBase)
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(horizontal = ArenaSpacing.lg)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            IconButton(onClick = onNavigateBack) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = ArenaTextPrimary)
            }
            ApexTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                label = "Search tournaments, teams, matches...",
                leadingIcon = Icons.Default.Search,
                modifier = Modifier.weight(1f)
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            listOf("All", "Tournaments", "Squads", "Matches").forEach { category ->
                FilterChip(
                    selected = selectedCategory == category,
                    onClick = { selectedCategory = category },
                    label = { Text(category) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = ArenaPurplePrimary,
                        containerColor = ArenaDarkSurface
                    )
                )
            }
        }

        if (searchQuery.isBlank()) {
            Text("Recent Searches", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = ArenaTextPrimary, modifier = Modifier.padding(top = 16.dp, bottom = 8.dp))
            recentSearches.forEach { search ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .clickable { searchQuery = search }
                        .padding(vertical = 12.dp, horizontal = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        Icon(Icons.Default.History, contentDescription = null, tint = ArenaTextMuted, modifier = Modifier.size(18.dp))
                        Text(search, color = ArenaTextSecondary)
                    }
                    Icon(Icons.Default.NorthWest, contentDescription = null, tint = ArenaTextMuted, modifier = Modifier.size(16.dp))
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(top = 16.dp, bottom = 24.dp)
            ) {
                item {
                    Text("Search Results for '$searchQuery'", color = ArenaPurpleLight, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                }
                items(listOf("tourn-1", "tourn-2", "tourn-3")) { id ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onNavigateToTournament(id) },
                        colors = CardDefaults.cardColors(containerColor = ArenaDarkSurfaceElevated),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                Text("Apex Tournament #$id", fontWeight = FontWeight.Bold, color = ArenaTextPrimary)
                                Text("BGMI • Prize: ₹50,000", color = ArenaTextSecondary, fontSize = 12.sp)
                            }
                            StatusBadge(status = "OPEN")
                        }
                    }
                }
            }
        }
    }
}
