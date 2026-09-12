package com.apex.arena.core.navigation

sealed class Screen(val route: String) {
    data object Login : Screen("login")
    data object Register : Screen("register")
    data object Home : Screen("home")
    data object Tournaments : Screen("tournaments")
    data object TournamentDetails : Screen("tournament_details/{tournamentId}") {
        fun createRoute(tournamentId: String) = "tournament_details/$tournamentId"
    }
    data object TournamentBracket : Screen("tournament_bracket/{tournamentId}") {
        fun createRoute(tournamentId: String) = "tournament_bracket/$tournamentId"
    }
    data object Matches : Screen("matches")
    data object MatchDetail : Screen("match_detail/{matchId}") {
        fun createRoute(matchId: String) = "match_detail/$matchId"
    }
    data object Leaderboard : Screen("leaderboard")
    data object Notifications : Screen("notifications")
    data object Profile : Screen("profile")
    data object EditProfile : Screen("edit_profile")
    data object Settings : Screen("settings")
}
