package com.apex.arena.data.repository

import com.apex.arena.domain.models.*
import com.apex.arena.domain.repository.*
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.builtin.Email
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import java.util.UUID

import com.apex.arena.core.network.SupabaseClientProvider

class AuthRepositoryImpl(
    private val supabase: SupabaseClient = SupabaseClientProvider.client
) : AuthRepository {
    private val _currentUserId = MutableStateFlow<String?>("user-demo-123")
    override val currentUserId: Flow<String?> = _currentUserId.asStateFlow()

    override suspend fun login(email: String, password: String): Result<Profile> {
        return try {
            val user = supabase.auth.currentUserOrNull()
            val userId = user?.id ?: "usr_${UUID.randomUUID().toString().take(8)}"
            _currentUserId.value = userId
            Result.success(
                Profile(
                    id = userId,
                    username = email.substringBefore("@"),
                    displayName = email.substringBefore("@").replaceFirstChar { it.uppercase() },
                    email = email,
                    tier = "Diamond II",
                    points = 2450,
                    rank = 14,
                    wins = 42,
                    losses = 12,
                    tournamentsPlayed = 58
                )
            )
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun register(email: String, password: String, username: String): Result<Profile> {
        return try {
            val userId = "usr_${UUID.randomUUID().toString().take(8)}"
            _currentUserId.value = userId
            Result.success(
                Profile(
                    id = userId,
                    username = username,
                    displayName = username,
                    email = email,
                    tier = "Silver I",
                    points = 500,
                    rank = 250,
                    wins = 0,
                    losses = 0,
                    tournamentsPlayed = 0
                )
            )
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun logout(): Result<Unit> {
        _currentUserId.value = null
        return Result.success(Unit)
    }

    override suspend fun restoreSession(): Result<Profile?> {
        val current = _currentUserId.value ?: return Result.success(null)
        return Result.success(
            Profile(
                id = current,
                username = "ApexShadow",
                displayName = "Apex Shadow",
                email = "apexshadow@arena.gg",
                tier = "Diamond II",
                points = 2450,
                rank = 14,
                wins = 42,
                losses = 12,
                tournamentsPlayed = 58
            )
        )
    }

    override suspend fun forgotPassword(email: String): Result<Unit> {
        return Result.success(Unit)
    }

    override suspend fun verifyOtp(email: String, otp: String): Result<Unit> {
        return Result.success(Unit)
    }

    override suspend fun resetPassword(password: String): Result<Unit> {
        return Result.success(Unit)
    }
}

class TournamentRepositoryImpl(
    private val supabase: SupabaseClient = SupabaseClientProvider.client
) : TournamentRepository {
    private val _registeredTournamentIds = MutableStateFlow<Set<String>>(setOf("tourn-1"))
    override val registeredTournamentIds: Flow<Set<String>> = _registeredTournamentIds.asStateFlow()

    private val sampleTournaments = listOf(
        Tournament(
            id = "tourn-1",
            title = "Apex Champions Pro League 2026",
            game = "BGMI",
            format = "SQUAD",
            entryFee = 150.0,
            prizePool = 50000.0,
            maxParticipants = 100,
            registeredParticipants = 84,
            status = "OPEN",
            startTime = "Today, 19:30 IST",
            organizerName = "Krafton Official Gaming",
            bannerUrl = null,
            roomId = "ARENA-ROOM-8821",
            roomPassword = "APEX-PASS-2026",
            userSlotNumber = 12
        ),
        Tournament(
            id = "tourn-2",
            title = "Free Fire MAX Rush Hour Knockout",
            game = "Free Fire",
            format = "SOLO",
            entryFee = 50.0,
            prizePool = 15000.0,
            maxParticipants = 48,
            registeredParticipants = 48,
            status = "LIVE",
            startTime = "Live Now",
            organizerName = "Garena Esports Arena",
            bannerUrl = null
        ),
        Tournament(
            id = "tourn-3",
            title = "Valorant Mobile Tactical Cup #4",
            game = "Valorant",
            format = "SQUAD",
            entryFee = 0.0,
            prizePool = 25000.0,
            maxParticipants = 32,
            registeredParticipants = 19,
            status = "OPEN",
            startTime = "Tomorrow, 16:00 IST",
            organizerName = "Riot Games Asia Pacific",
            bannerUrl = null
        ),
        Tournament(
            id = "tourn-4",
            title = "Call of Duty Warzone Rapid Assault",
            game = "Call of Duty",
            format = "DUO",
            entryFee = 100.0,
            prizePool = 30000.0,
            maxParticipants = 60,
            registeredParticipants = 60,
            status = "FULL",
            startTime = "Tonight, 22:00 IST",
            organizerName = "Activision Blizzard Arena",
            bannerUrl = null
        )
    )

    override fun getTournaments(searchQuery: String, filterFormat: String?, filterGame: String?): Flow<List<Tournament>> = flow {
        var list = sampleTournaments
        if (searchQuery.isNotBlank()) {
            list = list.filter { it.title.contains(searchQuery, ignoreCase = true) || it.game.contains(searchQuery, ignoreCase = true) }
        }
        if (filterFormat != null && filterFormat != "All") {
            list = list.filter { it.format.equals(filterFormat, ignoreCase = true) }
        }
        if (filterGame != null && filterGame != "All") {
            list = list.filter { it.game.equals(filterGame, ignoreCase = true) }
        }
        emit(list)
    }

    override suspend fun getTournamentById(id: String): Result<Tournament> {
        val found = sampleTournaments.find { it.id == id } ?: sampleTournaments.first()
        return Result.success(found)
    }

    override suspend fun registerForTournament(tournamentId: String, userId: String): Result<Unit> {
        val current = _registeredTournamentIds.value.toMutableSet()
        current.add(tournamentId)
        _registeredTournamentIds.value = current
        return Result.success(Unit)
    }

    override suspend fun leaveTournament(tournamentId: String, userId: String): Result<Unit> {
        val current = _registeredTournamentIds.value.toMutableSet()
        current.remove(tournamentId)
        _registeredTournamentIds.value = current
        return Result.success(Unit)
    }

    override fun getTournamentRules(tournamentId: String): Flow<List<TournamentRule>> = flow {
        emit(
            listOf(
                TournamentRule("r1", tournamentId, "Anti-Cheat Integrity", "Use of root injectors, config editing, or emulators will trigger immediate permanent hardware ban.", true),
                TournamentRule("r2", tournamentId, "Room Timing & Delay", "Room credentials unlock 15 minutes prior to start. Max 5 minutes late window before auto-kick.", true),
                TournamentRule("r3", tournamentId, "Point System Standard", "1 Kill = 1 Point. Placement: #1 = 15 pts, #2 = 12 pts, #3 = 10 pts.", false),
                TournamentRule("r4", tournamentId, "Streaming & POV", "At least one player per squad must record raw POV footage with Discord/In-game comms.", true)
            )
        )
    }

    override fun getTournamentSchedule(tournamentId: String): Flow<List<TournamentSchedule>> = flow {
        emit(
            listOf(
                TournamentSchedule("s1", tournamentId, "Round 1 - Erangel", "19:30 IST", "19:15 IST", "Erangel (Sunny)"),
                TournamentSchedule("s2", tournamentId, "Round 2 - Miramar", "20:30 IST", "20:15 IST", "Miramar (Desert)"),
                TournamentSchedule("s3", tournamentId, "Grand Finals - Sanhok", "21:30 IST", "21:15 IST", "Sanhok (Rainy)")
            )
        )
    }

    override fun getTournamentParticipants(tournamentId: String): Flow<List<TournamentParticipant>> = flow {
        emit(
            listOf(
                TournamentParticipant("p1", tournamentId, "team-godl", "GodLike Esports", "SQUAD", 1, 1, "2026-09-12 12:00"),
                TournamentParticipant("p2", tournamentId, "team-soul", "Team Soul", "SQUAD", 2, 2, "2026-09-12 12:05"),
                TournamentParticipant("p3", tournamentId, "team-tx", "Team XSpark", "SQUAD", 3, 3, "2026-09-12 12:10"),
                TournamentParticipant("p4", tournamentId, "team-gl", "Global Esports", "SQUAD", 4, 4, "2026-09-12 12:15")
            )
        )
    }
}

class MatchRepositoryImpl(
    private val supabase: SupabaseClient = SupabaseClientProvider.client
) : MatchRepository {
    private val sampleMatches = listOf(
        Match(
            id = "m-1",
            tournamentId = "tourn-1",
            tournamentTitle = "Apex Champions Pro League 2026",
            roundName = "Quarter Finals - Group A",
            team1Name = "GodLike Esports",
            team2Name = "Team Soul",
            team1Score = 18,
            team2Score = 15,
            winnerTeamId = "GodLike Esports",
            scheduledTime = "Live Now",
            status = "LIVE",
            roomId = "ROOM-9921",
            roomPass = "SOUL-PASS"
        ),
        Match(
            id = "m-2",
            tournamentId = "tourn-1",
            tournamentTitle = "Apex Champions Pro League 2026",
            roundName = "Quarter Finals - Group B",
            team1Name = "Team XSpark",
            team2Name = "Global Esports",
            team1Score = 0,
            team2Score = 0,
            winnerTeamId = null,
            scheduledTime = "Today, 20:30 IST",
            status = "UPCOMING",
            roomId = "ROOM-9922",
            roomPass = "SPARK-PASS"
        ),
        Match(
            id = "m-3",
            tournamentId = "tourn-2",
            tournamentTitle = "Free Fire MAX Rush Hour Knockout",
            roundName = "Grand Final Match",
            team1Name = "ApexShadow",
            team2Name = "ViperX",
            team1Score = 24,
            team2Score = 18,
            winnerTeamId = "ApexShadow",
            scheduledTime = "Yesterday, 21:00 IST",
            status = "COMPLETED"
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

    override suspend fun submitDispute(dispute: MatchDispute): Result<Unit> {
        return Result.success(Unit)
    }
}

class TeamRepositoryImpl(
    private val supabase: SupabaseClient = SupabaseClientProvider.client
) : TeamRepository {
    private val myTeamsList = MutableStateFlow(
        listOf(
            Team(
                id = "team-1",
                name = "Apex Predators",
                tag = "APX",
                captainId = "user-demo-123",
                captainName = "ApexShadow",
                memberCount = 4,
                maxMembers = 5,
                matchesPlayed = 24,
                wins = 19,
                createdAt = "2026-08-01"
            )
        )
    )

    private val teamInvitesList = MutableStateFlow(
        listOf(
            TeamInvite(
                id = "inv-1",
                teamId = "team-2",
                teamName = "Vortex Gaming",
                teamTag = "VRX",
                invitedUserId = "user-demo-123",
                invitedBy = "VortexCaptain",
                status = "PENDING",
                createdAt = "2 hours ago"
            )
        )
    )

    override fun getMyTeams(userId: String): Flow<List<Team>> = myTeamsList.asStateFlow()

    override fun getTeamInvites(userId: String): Flow<List<TeamInvite>> = teamInvitesList.asStateFlow()

    override suspend fun getTeamById(teamId: String): Result<Team> {
        val team = myTeamsList.value.find { it.id == teamId } ?: myTeamsList.value.first()
        return Result.success(team)
    }

    override suspend fun createTeam(name: String, tag: String, captainId: String, captainName: String): Result<Team> {
        val newTeam = Team(
            id = "team_${UUID.randomUUID().toString().take(8)}",
            name = name,
            tag = tag,
            captainId = captainId,
            captainName = captainName,
            memberCount = 1,
            maxMembers = 5,
            matchesPlayed = 0,
            wins = 0,
            createdAt = "2026-09-12"
        )
        myTeamsList.value = myTeamsList.value + newTeam
        return Result.success(newTeam)
    }

    override suspend fun inviteMember(teamId: String, gamerTag: String, invitedBy: String): Result<Unit> {
        return Result.success(Unit)
    }

    override suspend fun respondToInvite(inviteId: String, accept: Boolean): Result<Unit> {
        teamInvitesList.value = teamInvitesList.value.filterNot { it.id == inviteId }
        return Result.success(Unit)
    }
}

class KycRepositoryImpl(
    private val supabase: SupabaseClient = SupabaseClientProvider.client
) : KycRepository {
    private var currentKyc: KycProfile? = KycProfile(
        userId = "user-demo-123",
        fullName = "Apex Shadow",
        documentType = "PAN",
        documentNumber = "ABCDE1234F",
        status = "VERIFIED",
        verifiedAt = "2026-08-15"
    )

    override suspend fun getKycStatus(userId: String): Result<KycProfile?> {
        return Result.success(currentKyc)
    }

    override suspend fun submitKyc(kyc: KycProfile): Result<Unit> {
        currentKyc = kyc.copy(status = "PENDING")
        return Result.success(Unit)
    }
}

class AchievementRepositoryImpl(
    private val supabase: SupabaseClient = SupabaseClientProvider.client
) : AchievementRepository {
    private val achievementsList = listOf(
        Achievement("ach-1", "First Blood", "Register & participate in your first official tournament", "sports_esports", 100, 50.0, 1, 1, true, "2026-08-10"),
        Achievement("ach-2", "Centurion Winner", "Win 10 professional squad matches", "emoji_events", 500, 250.0, 8, 10, false),
        Achievement("ach-3", "Flawless MVP", "Achieve MVP rating in 5 consecutive matches", "military_tech", 1000, 500.0, 3, 5, false),
        Achievement("ach-4", "High Roller", "Participate in tournaments with a prize pool > ₹1,00,000", "diamond", 1500, 1000.0, 2, 5, false)
    )

    private val streaksList = listOf(
        DailyRewardStreak(1, 10, isClaimed = true, isToday = false),
        DailyRewardStreak(2, 20, isClaimed = true, isToday = false),
        DailyRewardStreak(3, 30, isClaimed = true, isToday = false),
        DailyRewardStreak(4, 50, isClaimed = false, isToday = true),
        DailyRewardStreak(5, 75, isClaimed = false, isToday = false),
        DailyRewardStreak(6, 100, isClaimed = false, isToday = false),
        DailyRewardStreak(7, 250, isClaimed = false, isToday = false)
    )

    override fun getAchievements(userId: String): Flow<List<Achievement>> = flow {
        emit(achievementsList)
    }

    override fun getDailyStreaks(userId: String): Flow<List<DailyRewardStreak>> = flow {
        emit(streaksList)
    }

    override suspend fun claimStreakReward(dayNumber: Int, userId: String): Result<Int> {
        return Result.success(50)
    }

    override suspend fun claimAchievementReward(achievementId: String, userId: String): Result<Double> {
        return Result.success(250.0)
    }
}

class LeaderboardRepositoryImpl(
    private val supabase: SupabaseClient = SupabaseClientProvider.client
) : LeaderboardRepository {
    override fun getTopPlayers(category: String): Flow<List<Profile>> = flow {
        emit(
            listOf(
                Profile("p-1", "JonathanGaming", "Jonathan Gaming", "jonathan@arena.gg", "Grandmaster", 5840, 1, 142, 18, 160),
                Profile("p-2", "Mortal77", "Naman Mathur", "mortal@arena.gg", "Master", 5120, 2, 128, 24, 152),
                Profile("p-3", "ScoutOP", "Tanmay Singh", "scout@arena.gg", "Master", 4980, 3, 115, 30, 145),
                Profile("p-4", "GoblinGamer", "Harsh Paudwal", "goblin@arena.gg", "Diamond I", 4650, 4, 98, 22, 120),
                Profile("user-demo-123", "ApexShadow", "Apex Shadow", "apexshadow@arena.gg", "Diamond II", 2450, 14, 42, 12, 58)
            )
        )
    }
}

class ProfileRepositoryImpl(
    private val supabase: SupabaseClient = SupabaseClientProvider.client
) : ProfileRepository {
    private var cachedProfile = Profile(
        id = "user-demo-123",
        username = "ApexShadow",
        displayName = "Apex Shadow",
        email = "apexshadow@arena.gg",
        tier = "Diamond II",
        points = 2450,
        rank = 14,
        wins = 42,
        losses = 12,
        tournamentsPlayed = 58
    )

    override suspend fun getProfile(userId: String): Result<Profile> {
        return Result.success(cachedProfile)
    }

    override suspend fun updateProfile(profile: Profile): Result<Profile> {
        cachedProfile = profile
        return Result.success(profile)
    }
}

class NotificationRepositoryImpl(
    private val supabase: SupabaseClient = SupabaseClientProvider.client
) : NotificationRepository {
    private val _notifications = MutableStateFlow(
        listOf(
            NotificationItem("notif-1", "user-demo-123", "Match Room Credentials", "Room ID and Password for BGMI Pro League Match #4 are now live!", "MATCH", false, "5m ago"),
            NotificationItem("notif-2", "user-demo-123", "Tournament Registration Confirmed", "You have secured Slot #12 in Apex Champions Pro League 2026.", "TOURNAMENT", false, "1h ago"),
            NotificationItem("notif-3", "user-demo-123", "Wallet Credited: ₹500", "Prize winnings for Free Fire Knockout Match #1 credited successfully.", "REWARD", true, "1d ago"),
            NotificationItem("notif-4", "user-demo-123", "Team Invite Received", "Vortex Gaming has invited you to join their squad roster.", "TEAM", true, "2d ago")
        )
    )

    override fun getNotifications(userId: String): Flow<List<NotificationItem>> = _notifications.asStateFlow()

    override suspend fun markAsRead(notificationId: String): Result<Unit> {
        _notifications.value = _notifications.value.map {
            if (it.id == notificationId) it.copy(isRead = true) else it
        }
        return Result.success(Unit)
    }

    override suspend fun markAllAsRead(userId: String): Result<Unit> {
        _notifications.value = _notifications.value.map { it.copy(isRead = true) }
        return Result.success(Unit)
    }
}
