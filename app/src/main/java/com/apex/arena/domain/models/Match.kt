package com.apex.arena.domain.models

import kotlinx.serialization.Serializable

@Serializable
data class Match(
    val id: String,
    val tournamentId: String,
    val tournamentTitle: String = "",
    val roundNumber: Int = 1,
    val roundName: String = "Round $roundNumber",
    val matchOrder: Int = 1,
    val participantAName: String = "Player 1",
    val participantBName: String = "Player 2",
    val team1Name: String = participantAName,
    val team2Name: String = participantBName,
    val participantAAvatar: String? = null,
    val participantBAvatar: String? = null,
    val scoreA: Int = 0,
    val scoreB: Int = 0,
    val team1Score: Int = scoreA,
    val team2Score: Int = scoreB,
    val winnerName: String? = null,
    val winnerTeamId: String? = winnerName,
    val status: String = "SCHEDULED", // SCHEDULED, LIVE, COMPLETED, UPCOMING
    val scheduledTime: String = "Today, 19:30 IST",
    val roomId: String? = null,
    val roomPass: String? = null
)

@Serializable
data class NotificationItem(
    val id: String,
    val userId: String = "",
    val title: String,
    val body: String,
    val type: String = "SYSTEM", // SYSTEM, MATCH, TOURNAMENT, REWARD, TEAM
    val isRead: Boolean = false,
    val createdAt: String = "Just now"
)
