package com.apex.arena.domain.models

import kotlinx.serialization.Serializable

@Serializable
data class Match(
    val id: String,
    val tournamentId: String,
    val tournamentTitle: String = "",
    val roundNumber: Int,
    val matchOrder: Int,
    val participantAName: String = "Player 1",
    val participantBName: String = "Player 2",
    val participantAAvatar: String? = null,
    val participantBAvatar: String? = null,
    val scoreA: Int = 0,
    val scoreB: Int = 0,
    val winnerName: String? = null,
    val status: String, // SCHEDULED, LIVE, COMPLETED
    val scheduledTime: String
)

@Serializable
data class Team(
    val id: String,
    val name: String,
    val tag: String,
    val leaderId: String,
    val memberCount: Int = 1,
    val avatarUrl: String? = null
)

@Serializable
data class NotificationItem(
    val id: String,
    val title: String,
    val body: String,
    val type: String, // SYSTEM, MATCH, TOURNAMENT, REWARD
    val isRead: Boolean = false,
    val createdAt: String
)
