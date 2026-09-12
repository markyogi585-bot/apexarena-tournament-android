package com.apex.arena.core.navigation

sealed class Screen(val route: String) {
    // Auth & Onboarding
    data object Splash : Screen("splash")
    data object Welcome : Screen("welcome")
    data object Login : Screen("login")
    data object Register : Screen("register")
    data object ForgotPassword : Screen("forgot_password")
    data object ResetPassword : Screen("reset_password")
    data object VerificationOtp : Screen("verification_otp/{email}") {
        fun createRoute(email: String) = "verification_otp/$email"
    }
    data object SessionExpired : Screen("session_expired")

    // Core Dashboard & Search
    data object Home : Screen("home")
    data object GlobalSearch : Screen("global_search")

    // Tournaments
    data object Tournaments : Screen("tournaments")
    data object TournamentDetails : Screen("tournament_details/{tournamentId}") {
        fun createRoute(tournamentId: String) = "tournament_details/$tournamentId"
    }
    data object TournamentRules : Screen("tournament_rules/{tournamentId}") {
        fun createRoute(tournamentId: String) = "tournament_rules/$tournamentId"
    }
    data object TournamentBracket : Screen("tournament_bracket/{tournamentId}") {
        fun createRoute(tournamentId: String) = "tournament_bracket/$tournamentId"
    }
    data object TournamentSchedule : Screen("tournament_schedule/{tournamentId}") {
        fun createRoute(tournamentId: String) = "tournament_schedule/$tournamentId"
    }
    data object TournamentParticipants : Screen("tournament_participants/{tournamentId}") {
        fun createRoute(tournamentId: String) = "tournament_participants/$tournamentId"
    }
    data object JoinTournament : Screen("join_tournament/{tournamentId}") {
        fun createRoute(tournamentId: String) = "join_tournament/$tournamentId"
    }
    data object JoinSuccess : Screen("join_success/{tournamentId}") {
        fun createRoute(tournamentId: String) = "join_success/$tournamentId"
    }

    // Matches & Live Room
    data object Matches : Screen("matches")
    data object MatchDetail : Screen("match_detail/{matchId}") {
        fun createRoute(matchId: String) = "match_detail/$matchId"
    }
    data object LiveMatchRoom : Screen("live_match_room/{matchId}") {
        fun createRoute(matchId: String) = "live_match_room/$matchId"
    }
    data object MatchDispute : Screen("match_dispute/{matchId}") {
        fun createRoute(matchId: String) = "match_dispute/$matchId"
    }

    // Leaderboards
    data object Leaderboard : Screen("leaderboard")

    // Fintech Wallet, Cash & Passbook
    data object Wallet : Screen("wallet")
    data object AddCash : Screen("add_cash")
    data object Withdraw : Screen("withdraw")
    data object KycVerification : Screen("kyc_verification")
    data object TransactionHistory : Screen("transaction_history")
    data object TransactionDetail : Screen("transaction_detail/{transactionId}") {
        fun createRoute(transactionId: String) = "transaction_detail/$transactionId"
    }

    // Rewards & Achievements
    data object RewardsOverview : Screen("rewards_overview")
    data object AchievementTree : Screen("achievement_tree")
    data object AchievementDetail : Screen("achievement_detail/{achievementId}") {
        fun createRoute(achievementId: String) = "achievement_detail/$achievementId"
    }

    // Teams & Rosters
    data object MyTeams : Screen("my_teams")
    data object TeamDetail : Screen("team_detail/{teamId}") {
        fun createRoute(teamId: String) = "team_detail/$teamId"
    }
    data object CreateTeam : Screen("create_team")
    data object TeamInvites : Screen("team_invites")

    // Activity & Notifications
    data object Notifications : Screen("notifications")

    // Account, Profile & Settings
    data object Profile : Screen("profile")
    data object EditProfile : Screen("edit_profile")
    data object Settings : Screen("settings")
    data object SupportFaq : Screen("support_faq")
    data object TermsPrivacy : Screen("terms_privacy")
}
