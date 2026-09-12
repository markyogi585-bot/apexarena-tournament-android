package com.apex.arena.domain.repository

import com.apex.arena.domain.models.*
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    val currentUserId: Flow<String?>
    suspend fun login(email: String, password: String): Result<Profile>
    suspend fun register(email: String, password: String, username: String): Result<Profile>
    suspend fun logout(): Result<Unit>
    suspend fun restoreSession(): Result<Profile?>
    suspend fun forgotPassword(email: String): Result<Unit>
    suspend fun verifyOtp(email: String, otp: String): Result<Unit>
    suspend fun resetPassword(password: String): Result<Unit>
}

interface TournamentRepository {
    fun getTournaments(searchQuery: String = "", filterFormat: String? = null, filterGame: String? = null): Flow<List<Tournament>>
    val registeredTournamentIds: Flow<Set<String>>
    suspend fun getTournamentById(id: String): Result<Tournament>
    suspend fun registerForTournament(tournamentId: String, userId: String): Result<Unit>
    suspend fun leaveTournament(tournamentId: String, userId: String): Result<Unit>
    fun getTournamentRules(tournamentId: String): Flow<List<TournamentRule>>
    fun getTournamentSchedule(tournamentId: String): Flow<List<TournamentSchedule>>
    fun getTournamentParticipants(tournamentId: String): Flow<List<TournamentParticipant>>
}

interface MatchRepository {
    fun getMyMatches(userId: String): Flow<List<Match>>
    fun getTournamentBracket(tournamentId: String): Flow<List<Match>>
    suspend fun getMatchById(matchId: String): Result<Match>
    suspend fun submitDispute(dispute: MatchDispute): Result<Unit>
}

interface TeamRepository {
    fun getMyTeams(userId: String): Flow<List<Team>>
    fun getTeamInvites(userId: String): Flow<List<TeamInvite>>
    suspend fun getTeamById(teamId: String): Result<Team>
    suspend fun createTeam(name: String, tag: String, captainId: String, captainName: String): Result<Team>
    suspend fun inviteMember(teamId: String, gamerTag: String, invitedBy: String): Result<Unit>
    suspend fun respondToInvite(inviteId: String, accept: Boolean): Result<Unit>
}

interface KycRepository {
    suspend fun getKycStatus(userId: String): Result<KycProfile?>
    suspend fun submitKyc(kyc: KycProfile): Result<Unit>
}

interface AchievementRepository {
    fun getAchievements(userId: String): Flow<List<Achievement>>
    fun getDailyStreaks(userId: String): Flow<List<DailyRewardStreak>>
    suspend fun claimStreakReward(dayNumber: Int, userId: String): Result<Int>
    suspend fun claimAchievementReward(achievementId: String, userId: String): Result<Double>
}

interface LeaderboardRepository {
    fun getTopPlayers(category: String = "GLOBAL"): Flow<List<Profile>>
}

interface ProfileRepository {
    suspend fun getProfile(userId: String): Result<Profile>
    suspend fun updateProfile(profile: Profile): Result<Profile>
}

interface NotificationRepository {
    fun getNotifications(userId: String): Flow<List<NotificationItem>>
    suspend fun markAsRead(notificationId: String): Result<Unit>
    suspend fun markAllAsRead(userId: String): Result<Unit>
}
