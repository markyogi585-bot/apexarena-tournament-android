package com.apex.arena.domain.repository

import com.apex.arena.domain.models.Match
import com.apex.arena.domain.models.NotificationItem
import com.apex.arena.domain.models.Profile
import com.apex.arena.domain.models.Tournament
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    val currentUserId: Flow<String?>
    suspend fun login(email: String, password: String): Result<Profile>
    suspend fun register(email: String, password: String, username: String): Result<Profile>
    suspend fun logout(): Result<Unit>
    suspend fun restoreSession(): Result<Profile?>
    suspend fun forgotPassword(email: String): Result<Unit>
}

interface TournamentRepository {
    fun getTournaments(searchQuery: String = "", filterFormat: String? = null): Flow<List<Tournament>>
    suspend fun getTournamentById(id: String): Result<Tournament>
    suspend fun registerForTournament(tournamentId: String, userId: String): Result<Unit>
    suspend fun leaveTournament(tournamentId: String, userId: String): Result<Unit>
}

interface MatchRepository {
    fun getMyMatches(userId: String): Flow<List<Match>>
    fun getTournamentBracket(tournamentId: String): Flow<List<Match>>
    suspend fun getMatchById(matchId: String): Result<Match>
}

interface LeaderboardRepository {
    fun getTopPlayers(): Flow<List<Profile>>
}

interface ProfileRepository {
    suspend fun getProfile(userId: String): Result<Profile>
    suspend fun updateProfile(profile: Profile): Result<Profile>
}

interface NotificationRepository {
    fun getNotifications(userId: String): Flow<List<NotificationItem>>
    suspend fun markAsRead(notificationId: String): Result<Unit>
}
