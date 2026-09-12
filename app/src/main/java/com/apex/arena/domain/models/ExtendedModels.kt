package com.apex.arena.domain.models

import kotlinx.serialization.Serializable

@Serializable
data class Team(
    val id: String,
    val name: String,
    val tag: String,
    val logoUrl: String? = null,
    val captainId: String,
    val captainName: String,
    val memberCount: Int = 1,
    val maxMembers: Int = 5,
    val matchesPlayed: Int = 0,
    val wins: Int = 0,
    val createdAt: String
)

@Serializable
data class TeamMember(
    val id: String,
    val teamId: String,
    val userId: String,
    val gamerTag: String,
    val role: String, // CAPTAIN, MEMBER, SUBSTITUTE
    val joinedAt: String
)

@Serializable
data class TeamInvite(
    val id: String,
    val teamId: String,
    val teamName: String,
    val teamTag: String,
    val invitedUserId: String,
    val invitedBy: String,
    val status: String, // PENDING, ACCEPTED, DECLINED
    val createdAt: String
)

@Serializable
data class TournamentRule(
    val id: String,
    val tournamentId: String,
    val ruleTitle: String,
    val ruleDescription: String,
    val isStrict: Boolean = true
)

@Serializable
data class TournamentSchedule(
    val id: String,
    val tournamentId: String,
    val roundName: String,
    val startTime: String,
    val roomOpenTime: String,
    val mapName: String
)

@Serializable
data class TournamentParticipant(
    val id: String,
    val tournamentId: String,
    val participantId: String,
    val participantName: String,
    val participantType: String, // SOLO, SQUAD
    val slotNumber: Int,
    val seed: Int = 0,
    val registeredAt: String
)

@Serializable
data class MatchDispute(
    val id: String,
    val matchId: String,
    val tournamentId: String,
    val reportedByUserId: String,
    val reportedUserName: String,
    val reason: String,
    val description: String,
    val evidenceUrl: String? = null,
    val status: String, // SUBMITTED, UNDER_REVIEW, RESOLVED, REJECTED
    val createdAt: String
)

@Serializable
data class KycProfile(
    val userId: String,
    val fullName: String,
    val documentType: String, // AADHAAR, PAN, PASSPORT
    val documentNumber: String,
    val documentFrontUrl: String? = null,
    val status: String, // PENDING, VERIFIED, REJECTED
    val verifiedAt: String? = null
)

@Serializable
data class Achievement(
    val id: String,
    val title: String,
    val description: String,
    val iconName: String,
    val xpReward: Int,
    val creditReward: Double,
    val currentProgress: Int,
    val maxProgress: Int,
    val isUnlocked: Boolean = false,
    val unlockedAt: String? = null
)

@Serializable
data class DailyRewardStreak(
    val dayNumber: Int,
    val rewardCoins: Int,
    val isClaimed: Boolean,
    val isToday: Boolean
)
