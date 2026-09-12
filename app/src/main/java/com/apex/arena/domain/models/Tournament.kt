package com.apex.arena.domain.models

import kotlinx.serialization.Serializable

@Serializable
data class Profile(
    val id: String,
    val username: String,
    val displayName: String,
    val email: String = "",
    val avatarUrl: String? = null,
    val gameId: String? = null,
    val tier: String = "Diamond II",
    val points: Int = 0,
    val rank: Int = 0,
    val wins: Int = 0,
    val losses: Int = 0,
    val tournamentsPlayed: Int = 0,
    val winRate: Float = if (wins + losses > 0) (wins.toFloat() / (wins + losses)) * 100 else 0f
)

@Serializable
data class Tournament(
    val id: String,
    val title: String,
    val gameTitle: String = "BGMI",
    val game: String = gameTitle,
    val format: String = "SQUAD", // SOLO, DUO, SQUAD, KNOCKOUT
    val status: String = "OPEN", // OPEN, FULL, REGISTRATION_CLOSED, LIVE, COMPLETED
    val entryFee: Double = 0.0,
    val prizePool: Double = 0.0,
    val maxParticipants: Int = 16,
    val currentParticipants: Int = 0,
    val registeredParticipants: Int = currentParticipants,
    val bannerUrl: String? = null,
    val rulesText: String = "",
    val startTime: String = "",
    val registrationDeadline: String = "",
    val organizerName: String = "Krafton Official Gaming",
    val roomId: String? = null,
    val roomPassword: String? = null,
    val userSlotNumber: Int? = null
)
