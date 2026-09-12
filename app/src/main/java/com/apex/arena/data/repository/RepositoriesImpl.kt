package com.apex.arena.data.repository

import com.apex.arena.domain.models.Match
import com.apex.arena.domain.models.NotificationItem
import com.apex.arena.domain.models.Profile
import com.apex.arena.domain.models.Tournament
import com.apex.arena.domain.repository.AuthRepository
import com.apex.arena.domain.repository.LeaderboardRepository
import com.apex.arena.domain.repository.MatchRepository
import com.apex.arena.domain.repository.NotificationRepository
import com.apex.arena.domain.repository.ProfileRepository
import com.apex.arena.domain.repository.TournamentRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow

class AuthRepositoryImpl : AuthRepository {
    private val _currentUserId = MutableStateFlow<String?>("user_demo_01")
    override val currentUserId: Flow<String?> = _currentUserId.asStateFlow()

    private var cachedProfile = Profile(
        id = "user_demo_01",
        username = "ApexStriker",
        displayName = "Alex 'Apex' Mercer",
        gameId = "98234718",
        tier = "DIAMOND",
        points = 2450,
        wins = 42,
        losses = 12
    )

    override suspend fun login(email: String, password: String): Result<Profile> {
        return if (email.isNotBlank() && password.length >= 6) {
            _currentUserId.value = cachedProfile.id
            Result.success(cachedProfile)
        } else {
            Result.failure(IllegalArgumentException("Invalid email or password (min 6 characters required)."))
        }
    }

    override suspend fun register(email: String, password: String, username: String): Result<Profile> {
        return if (username.length >= 3 && password.length >= 6) {
            cachedProfile = cachedProfile.copy(username = username, displayName = username)
            _currentUserId.value = cachedProfile.id
            Result.success(cachedProfile)
        } else {
            Result.failure(IllegalArgumentException("Username must be at least 3 characters and password at least 6."))
        }
    }

    override suspend fun logout(): Result<Unit> {
        _currentUserId.value = null
        return Result.success(Unit)
    }

    override suspend fun restoreSession(): Result<Profile?> {
        return Result.success(cachedProfile)
    }

    override suspend fun forgotPassword(email: String): Result<Unit> {
        return Result.success(Unit)
    }
}

class TournamentRepositoryImpl : TournamentRepository {
    private val sampleTournaments = listOf(
        Tournament(
            id = "t1",
            title = "Apex Masters Winter Invitational",
            gameTitle = "BGMI / PUBG Mobile",
            format = "SQUAD",
            status = "REGISTRATION_OPEN",
            entryFee = 0.0,
            prizePool = 50000.0,
            maxParticipants = 32,
            currentParticipants = 24,
            rulesText = "Standard Esports rules. Squad of 4. Emulators strictly prohibited. Anti-cheat mandatory.",
            startTime = "Today, 8:00 PM IST",
            registrationDeadline = "Today, 7:00 PM IST"
        ),
        Tournament(
            id = "t2",
            title = "Valorant Spike Rush 1v1",
            gameTitle = "Valorant",
            format = "SOLO",
            status = "UPCOMING",
            entryFee = 100.0,
            prizePool = 15000.0,
            maxParticipants = 64,
            currentParticipants = 48,
            rulesText = "Single elimination knockout. Best of 3 rounds in finals.",
            startTime = "Tomorrow, 6:00 PM IST",
            registrationDeadline = "Tomorrow, 4:00 PM IST"
        ),
        Tournament(
            id = "t3",
            title = "Free Fire Pro Clash Championship",
            gameTitle = "Free Fire MAX",
            format = "SQUAD",
            status = "ONGOING",
            entryFee = 0.0,
            prizePool = 25000.0,
            maxParticipants = 48,
            currentParticipants = 48,
            rulesText = "Clash Squad knockout format. Map: Bermuda / Purgatory.",
            startTime = "Live Now",
            registrationDeadline = "Closed"
        )
    )

    override fun getTournaments(searchQuery: String, filterFormat: String?): Flow<List<Tournament>> = flow {
        val filtered = sampleTournaments.filter {
            (searchQuery.isBlank() || it.title.contains(searchQuery, ignoreCase = true) || it.gameTitle.contains(searchQuery, ignoreCase = true)) &&
            (filterFormat == null || it.format.equals(filterFormat, ignoreCase = true))
        }
        emit(filtered)
    }

    override suspend fun getTournamentById(id: String): Result<Tournament> {
        val item = sampleTournaments.find { it.id == id } ?: sampleTournaments.first()
        return Result.success(item)
    }

    override suspend fun registerForTournament(tournamentId: String, userId: String): Result<Unit> {
        return Result.success(Unit)
    }

    override suspend fun leaveTournament(tournamentId: String, userId: String): Result<Unit> {
        return Result.success(Unit)
    }
}

class MatchRepositoryImpl : MatchRepository {
    private val sampleMatches = listOf(
        Match(
            id = "m1",
            tournamentId = "t3",
            tournamentTitle = "Free Fire Pro Clash Championship",
            roundNumber = 3,
            matchOrder = 1,
            participantAName = "GodLike Esports",
            participantBName = "Soul Warriors",
            scoreA = 2,
            scoreB = 1,
            status = "LIVE",
            scheduledTime = "Live Now"
        ),
        Match(
            id = "m2",
            tournamentId = "t1",
            tournamentTitle = "Apex Masters Winter Invitational",
            roundNumber = 1,
            matchOrder = 2,
            participantAName = "ApexStriker (You)",
            participantBName = "ViperX",
            scoreA = 0,
            scoreB = 0,
            status = "SCHEDULED",
            scheduledTime = "Today, 8:30 PM IST"
        ),
        Match(
            id = "m3",
            tournamentId = "t2",
            tournamentTitle = "Valorant Spike Rush 1v1",
            roundNumber = 2,
            matchOrder = 4,
            participantAName = "ApexStriker (You)",
            participantBName = "ShadowBlade",
            scoreA = 13,
            scoreB = 9,
            winnerName = "ApexStriker",
            status = "COMPLETED",
            scheduledTime = "Yesterday"
        )
    )

    override fun getMyMatches(userId: String): Flow<List<Match>> = flow {
        emit(sampleMatches)
    }

    override fun getTournamentBracket(tournamentId: String): Flow<List<Match>> = flow {
        emit(sampleMatches)
    }

    override suspend fun getMatchById(matchId: String): Result<Match> {
        val match = sampleMatches.find { it.id == matchId } ?: sampleMatches.first()
        return Result.success(match)
    }
}

class LeaderboardRepositoryImpl : LeaderboardRepository {
    override fun getTopPlayers(): Flow<List<Profile>> = flow {
        emit(listOf(
            Profile("u1", "PhantomKing", "Raj 'Phantom' Sharma", null, "88219481", "LEGEND", 3890, 84, 8),
            Profile("u2", "ValkyrieQueen", "Simran 'Valk' Kaur", null, "19284712", "MASTER", 3420, 69, 14),
            Profile("u3", "ApexStriker", "Alex 'Apex' Mercer (You)", null, "98234718", "DIAMOND", 2450, 42, 12),
            Profile("u4", "CyberNinja", "Arjun 'Ninja' Patel", null, "73819283", "PLATINUM", 2100, 38, 19),
            Profile("u5", "GhostRider", "Vikram Rathore", null, "66291038", "GOLD", 1850, 29, 21)
        ))
    }
}

class ProfileRepositoryImpl : ProfileRepository {
    private var currentProfile = Profile(
        id = "user_demo_01",
        username = "ApexStriker",
        displayName = "Alex 'Apex' Mercer",
        gameId = "98234718",
        tier = "DIAMOND",
        points = 2450,
        wins = 42,
        losses = 12
    )

    override suspend fun getProfile(userId: String): Result<Profile> {
        return Result.success(currentProfile)
    }

    override suspend fun updateProfile(profile: Profile): Result<Profile> {
        currentProfile = profile
        return Result.success(currentProfile)
    }
}

class NotificationRepositoryImpl : NotificationRepository {
    override fun getNotifications(userId: String): Flow<List<NotificationItem>> = flow {
        emit(listOf(
            NotificationItem("n1", "Match Ready!", "Your Round 1 match against ViperX is scheduled at 8:30 PM.", "MATCH", false, "10m ago"),
            NotificationItem("n2", "Registration Confirmed", "You have joined Apex Masters Winter Invitational.", "TOURNAMENT", false, "1h ago"),
            NotificationItem("n3", "Season Rewards Claimed", "You earned 250 Apex Points for Diamond Tier placement.", "REWARD", true, "1d ago")
        ))
    }

    override suspend fun markAsRead(notificationId: String): Result<Unit> {
        return Result.success(Unit)
    }
}
