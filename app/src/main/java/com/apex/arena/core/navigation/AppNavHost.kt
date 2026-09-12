package com.apex.arena.core.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.navArgument
import com.apex.arena.core.components.ApexBottomBar
import com.apex.arena.data.repository.AuthRepositoryImpl
import com.apex.arena.data.repository.LeaderboardRepositoryImpl
import com.apex.arena.data.repository.MatchRepositoryImpl
import com.apex.arena.data.repository.NotificationRepositoryImpl
import com.apex.arena.data.repository.ProfileRepositoryImpl
import com.apex.arena.data.repository.TournamentRepositoryImpl
import com.apex.arena.presentation.auth.AuthViewModel
import com.apex.arena.presentation.auth.LoginScreen
import com.apex.arena.presentation.auth.RegisterScreen
import com.apex.arena.presentation.home.HomeScreen
import com.apex.arena.presentation.home.HomeViewModel
import com.apex.arena.presentation.leaderboard.LeaderboardScreen
import com.apex.arena.presentation.leaderboard.LeaderboardViewModel
import com.apex.arena.presentation.matches.MatchDetailScreen
import com.apex.arena.presentation.matches.MatchesScreen
import com.apex.arena.presentation.matches.MatchesViewModel
import com.apex.arena.presentation.notifications.NotificationScreen
import com.apex.arena.presentation.notifications.NotificationViewModel
import com.apex.arena.presentation.profile.EditProfileScreen
import com.apex.arena.presentation.profile.ProfileScreen
import com.apex.arena.presentation.profile.ProfileViewModel
import com.apex.arena.presentation.profile.SettingsScreen
import com.apex.arena.presentation.tournaments.TournamentBracketScreen
import com.apex.arena.presentation.tournaments.TournamentDetailScreen
import com.apex.arena.presentation.tournaments.TournamentListScreen
import com.apex.arena.presentation.tournaments.TournamentViewModel

@Composable
fun AppNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    // Single-instance repositories for container-free deterministic resolution
    val authRepo = remember { AuthRepositoryImpl() }
    val tournamentRepo = remember { TournamentRepositoryImpl() }
    val matchRepo = remember { MatchRepositoryImpl() }
    val leaderboardRepo = remember { LeaderboardRepositoryImpl() }
    val profileRepo = remember { ProfileRepositoryImpl() }
    val notificationRepo = remember { NotificationRepositoryImpl() }

    // ViewModels
    val authViewModel = remember { AuthViewModel(authRepo) }
    val homeViewModel = remember { HomeViewModel(tournamentRepo, matchRepo, profileRepo, leaderboardRepo) }
    val tournamentViewModel = remember { TournamentViewModel(tournamentRepo, matchRepo) }
    val matchesViewModel = remember { MatchesViewModel(matchRepo) }
    val leaderboardViewModel = remember { LeaderboardViewModel(leaderboardRepo) }
    val notificationViewModel = remember { NotificationViewModel(notificationRepo) }
    val profileViewModel = remember { ProfileViewModel(profileRepo, authRepo) }

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val showBottomBar = currentRoute in listOf(
        Screen.Home.route,
        Screen.Tournaments.route,
        Screen.Matches.route,
        Screen.Leaderboard.route,
        Screen.Profile.route
    )

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                ApexBottomBar(
                    currentRoute = currentRoute,
                    onNavigateToRoute = { route ->
                        navController.navigate(route) {
                            popUpTo(Screen.Home.route) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = modifier.padding(innerPadding)
        ) {
            composable(Screen.Login.route) {
                LoginScreen(
                    onLoginSuccess = {
                        navController.navigate(Screen.Home.route) {
                            popUpTo(Screen.Login.route) { inclusive = true }
                        }
                    },
                    onNavigateToRegister = { navController.navigate(Screen.Register.route) },
                    viewModel = authViewModel
                )
            }

            composable(Screen.Register.route) {
                RegisterScreen(
                    onRegisterSuccess = {
                        navController.navigate(Screen.Home.route) {
                            popUpTo(Screen.Register.route) { inclusive = true }
                        }
                    },
                    onNavigateToLogin = { navController.navigate(Screen.Login.route) },
                    viewModel = authViewModel
                )
            }

            composable(Screen.Home.route) {
                HomeScreen(
                    onNavigateToTournamentDetails = { id ->
                        navController.navigate(Screen.TournamentDetails.createRoute(id))
                    },
                    onNavigateToNotifications = { navController.navigate(Screen.Notifications.route) },
                    viewModel = homeViewModel
                )
            }

            composable(Screen.Tournaments.route) {
                TournamentListScreen(
                    onNavigateToDetails = { id ->
                        navController.navigate(Screen.TournamentDetails.createRoute(id))
                    },
                    viewModel = tournamentViewModel
                )
            }

            composable(
                route = Screen.TournamentDetails.route,
                arguments = listOf(navArgument("tournamentId") { type = NavType.StringType })
            ) { backStackEntry ->
                val id = backStackEntry.arguments?.getString("tournamentId") ?: "t1"
                TournamentDetailScreen(
                    tournamentId = id,
                    onNavigateBack = { navController.popBackStack() },
                    onNavigateToBracket = { tId ->
                        navController.navigate(Screen.TournamentBracket.createRoute(tId))
                    },
                    viewModel = tournamentViewModel
                )
            }

            composable(
                route = Screen.TournamentBracket.route,
                arguments = listOf(navArgument("tournamentId") { type = NavType.StringType })
            ) { backStackEntry ->
                val id = backStackEntry.arguments?.getString("tournamentId") ?: "t1"
                TournamentBracketScreen(
                    tournamentId = id,
                    onNavigateBack = { navController.popBackStack() },
                    viewModel = tournamentViewModel
                )
            }

            composable(Screen.Matches.route) {
                MatchesScreen(
                    onNavigateToMatchDetail = { matchId ->
                        navController.navigate(Screen.MatchDetail.createRoute(matchId))
                    },
                    viewModel = matchesViewModel
                )
            }

            composable(
                route = Screen.MatchDetail.route,
                arguments = listOf(navArgument("matchId") { type = NavType.StringType })
            ) { backStackEntry ->
                val id = backStackEntry.arguments?.getString("matchId") ?: "m1"
                MatchDetailScreen(
                    matchId = id,
                    onNavigateBack = { navController.popBackStack() },
                    viewModel = matchesViewModel
                )
            }

            composable(Screen.Leaderboard.route) {
                LeaderboardScreen(viewModel = leaderboardViewModel)
            }

            composable(Screen.Notifications.route) {
                NotificationScreen(
                    onNavigateBack = { navController.popBackStack() },
                    viewModel = notificationViewModel
                )
            }

            composable(Screen.Profile.route) {
                ProfileScreen(
                    onNavigateToEditProfile = { navController.navigate(Screen.EditProfile.route) },
                    onNavigateToSettings = { navController.navigate(Screen.Settings.route) },
                    onLoggedOut = {
                        navController.navigate(Screen.Login.route) {
                            popUpTo(0) { inclusive = true }
                        }
                    },
                    viewModel = profileViewModel
                )
            }

            composable(Screen.EditProfile.route) {
                EditProfileScreen(
                    onNavigateBack = { navController.popBackStack() },
                    viewModel = profileViewModel
                )
            }

            composable(Screen.Settings.route) {
                SettingsScreen(
                    onNavigateBack = { navController.popBackStack() }
                )
            }
        }
    }
}
