package com.apex.arena.presentation.notifications

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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DoneAll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.apex.arena.core.components.ApexTopBar
import com.apex.arena.core.components.EmptyStateView
import com.apex.arena.core.components.LoadingStateView
import com.apex.arena.core.theme.BorderViolet
import com.apex.arena.core.theme.Dimensions
import com.apex.arena.core.theme.EmeraldSuccess
import com.apex.arena.core.theme.MidnightCard
import com.apex.arena.core.theme.MutedSlate
import com.apex.arena.core.theme.NeonViolet
import com.apex.arena.core.theme.ObsidianDark
import com.apex.arena.core.theme.SoftLilac
import com.apex.arena.core.theme.TextCrisp
import com.apex.arena.core.theme.TextMuted

@Composable
fun NotificationScreen(
    onNavigateBack: () -> Unit,
    viewModel: NotificationViewModel
) {
    val uiState by viewModel.uiState.collectAsState()
    var selectedFilter by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ObsidianDark)
    ) {
        ApexTopBar(
            title = "Notifications & Dispatch",
            canNavigateBack = true,
            onNavigateBack = onNavigateBack,
            actions = {
                IconButton(onClick = {
                    uiState.notifications.forEach { viewModel.markRead(it.id) }
                }) {
                    Icon(
                        imageVector = Icons.Default.DoneAll,
                        contentDescription = "Mark All Read",
                        tint = SoftLilac
                    )
                }
            }
        )

        // Filter Chips Row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = Dimensions.spaceM, vertical = 6.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            listOf(null to "All", "MATCH" to "Matches", "TOURNAMENT" to "Tournaments", "REWARD" to "Rewards").forEach { (filterVal, label) ->
                val isSelected = selectedFilter == filterVal
                FilterChip(
                    selected = isSelected,
                    onClick = { selectedFilter = filterVal },
                    label = { Text(label, fontWeight = FontWeight.SemiBold, fontSize = 12.sp) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = NeonViolet,
                        selectedLabelColor = TextCrisp,
                        containerColor = MidnightCard,
                        labelColor = SoftLilac
                    )
                )
            }
        }

        if (uiState.isLoading) {
            LoadingStateView(message = "Fetching Dispatch Feed...")
        } else {
            val filteredList = uiState.notifications.filter {
                selectedFilter == null || it.type.equals(selectedFilter, ignoreCase = true)
            }

            if (filteredList.isEmpty()) {
                EmptyStateView(
                    title = "No Notifications",
                    description = "All caught up! Match reminders will appear here."
                )
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = Dimensions.spaceM)
                ) {
                    items(filteredList) { item ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 6.dp)
                                .clickable { viewModel.markRead(item.id) }
                                .border(1.dp, if (!item.isRead) NeonViolet else BorderViolet, RoundedCornerShape(Dimensions.cardRadius)),
                            shape = RoundedCornerShape(Dimensions.cardRadius),
                            colors = CardDefaults.cardColors(
                                containerColor = if (!item.isRead) MidnightCard else MutedSlate
                            )
                        ) {
                            Column(modifier = Modifier.padding(Dimensions.spaceM)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        if (!item.isRead) {
                                            Text(text = "● ", color = NeonViolet, fontSize = 12.sp, fontWeight = FontWeight.Black)
                                        }
                                        Text(
                                            text = item.title,
                                            color = if (!item.isRead) TextCrisp else TextMuted,
                                            fontSize = 15.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                    Text(
                                        text = item.createdAt,
                                        color = SoftLilac,
                                        fontSize = 11.sp
                                    )
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = item.body,
                                    color = TextMuted,
                                    fontSize = 13.sp
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
