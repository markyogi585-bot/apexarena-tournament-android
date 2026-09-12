package com.apex.arena.presentation.tournaments

import androidx.compose.foundation.background
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.apex.arena.core.components.ApexTextField
import com.apex.arena.core.components.EmptyStateView
import com.apex.arena.core.components.LoadingStateView
import com.apex.arena.core.theme.Dimensions
import com.apex.arena.core.theme.NeonViolet
import com.apex.arena.core.theme.ObsidianDark
import com.apex.arena.core.theme.SoftLilac
import com.apex.arena.core.theme.TextCrisp
import com.apex.arena.presentation.home.TournamentCardItem

@Composable
fun TournamentListScreen(
    onNavigateToDetails: (String) -> Unit,
    viewModel: TournamentViewModel
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
            text = "TOURNAMENT ARENA",
            fontSize = 22.sp,
            fontWeight = FontWeight.Black,
            letterSpacing = 1.sp,
            color = TextCrisp
        )
        Spacer(modifier = Modifier.height(Dimensions.spaceS))

        // Search Bar
        ApexTextField(
            value = uiState.searchQuery,
            onValueChange = { viewModel.onSearchQueryChange(it) },
            label = "Search Tournaments or Games",
            leadingIcon = Icons.Default.Search
        )
        Spacer(modifier = Modifier.height(Dimensions.spaceS))

        // Filter Chips
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            listOf(null to "All", "SOLO" to "Solo", "SQUAD" to "Squad").forEach { (filterVal, label) ->
                val isSelected = uiState.selectedFilter == filterVal
                FilterChip(
                    selected = isSelected,
                    onClick = { viewModel.onFilterChange(filterVal) },
                    label = { Text(text = label, fontWeight = FontWeight.SemiBold) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = NeonViolet,
                        selectedLabelColor = TextCrisp,
                        containerColor = ObsidianDark,
                        labelColor = SoftLilac
                    )
                )
            }
        }
        Spacer(modifier = Modifier.height(Dimensions.spaceM))

        if (uiState.isLoading) {
            LoadingStateView(message = "Searching matches & tournaments...")
        } else if (uiState.tournaments.isEmpty()) {
            EmptyStateView(
                title = "No Tournaments Found",
                description = "Try adjusting your search query or filters."
            )
        } else {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(uiState.tournaments) { tournament ->
                    TournamentCardItem(
                        tournament = tournament,
                        onClick = { onNavigateToDetails(tournament.id) }
                    )
                    Spacer(modifier = Modifier.height(Dimensions.spaceM))
                }
            }
        }
    }
}
