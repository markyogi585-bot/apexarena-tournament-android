package com.apex.arena.core.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.navArgument
import com.apex.arena.core.components.ApexBottomBar
import com.apex.arena.data.repository.*
import com.apex.arena.presentation.auth.*
import com.apex.arena.presentation.home.*
import com.apex.arena.presentation.leaderboard.*
import com.apex.arena.presentation.matches.*
import com.apex.arena.presentation.notifications.*
import com.apex.arena.presentation.profile.*
import com.apex.arena.presentation.rewards.*
import com.apex.arena.presentation.teams.*
import com.apex.arena.presentation.tournaments.*
import com.apex.arena.presentation.wallet.*

@Composable
fun AppNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    // Single-instance repositories for deterministic state resolution
    val authRepo = remember { AuthRepositoryImpl() }
    val tournamentRepo = remember { TournamentRepositoryImpl() }
    val matchRepo = remember { MatchRepositoryImpl() }
    val leaderboardRepo = remember { LeaderboardRepositoryImpl() }
    val profileRepo = remember { ProfileRepositoryImpl() }
    val notificationRepo = remember { NotificationRepositoryImpl() }
    val walletRepo = remember { WalletRepositoryImpl() }
    val teamRepo = remember { TeamRepositoryImpl() }
    val kycRepo = remember { KycRepositoryImpl() }
    val achievementRepo = remember { AchievementRepositoryImpl() }

    // Core ViewModels
    val authViewModel = remember { AuthViewModel(authRepo) }
    val homeViewModel = remember { HomeViewModel(tournamentRepo, matchRepo, profileRepo, leaderboardRepo) }
    val tournamentViewModel = remember { TournamentViewModel(tournamentRepo, matchRepo) }
    val matchesViewModel = remember { MatchesViewModel(matchRepo) }
    val leaderboardViewModel = remember { LeaderboardViewModel(leaderboardRepo) }
    val notificationViewModel = remember { NotificationViewModel(notificationRepo) }
    val profileViewModel = remember { ProfileViewModel(profileRepo, authRepo) }
    val walletViewModel = remember { WalletViewModel(walletRepo) }

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
            // Splash & Onboarding
            composable(Screen.Splash.route) {
                SplashScreen(onNavigateNext = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Splash.route) { inclusive = true }
                    }
                })
            }

            composable(Screen.Welcome.route) {
                WelcomeScreen(
                    onNavigateToLogin = { navController.navigate(Screen.Login.route) },
                    onNavigateToRegister = { navController.navigate(Screen.Register.route) }
                )
            }

            // Auth Suite
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

            composable(Screen.ForgotPassword.route) {
                ForgotPasswordScreen(
                    onNavigateBack = { navController.popBackStack() },
                    onNavigateToOtp = { email -> navController.navigate(Screen.VerificationOtp.createRoute(email)) }
                )
            }

            composable(
                route = Screen.VerificationOtp.route,
                arguments = listOf(navArgument("email") { type = NavType.StringType })
            ) { backStackEntry ->
                val email = backStackEntry.arguments?.getString("email") ?: ""
                VerificationOtpScreen(
                    email = email,
                    onNavigateBack = { navController.popBackStack() },
                    onVerified = { navController.navigate(Screen.ResetPassword.route) }
                )
            }

            composable(Screen.ResetPassword.route) {
                ResetPasswordScreen(
                    onNavigateToLogin = {
                        navController.navigate(Screen.Login.route) {
                            popUpTo(Screen.ResetPassword.route) { inclusive = true }
                        }
                    }
                )
            }

            composable(Screen.SessionExpired.route) {
                SessionExpiredScreen(
                    onReAuthenticate = {
                        navController.navigate(Screen.Login.route) {
                            popUpTo(0) { inclusive = true }
                        }
                    }
                )
            }

            // Command Center & Search
            composable(Screen.Home.route) {
                HomeScreen(
                    onNavigateToTournamentDetails = { id ->
                        navController.navigate(Screen.TournamentDetails.createRoute(id))
                    },
                    onNavigateToNotifications = { navController.navigate(Screen.Notifications.route) },
                    onNavigateToWallet = { navController.navigate(Screen.Wallet.route) },
                    onNavigateToSearch = { navController.navigate(Screen.GlobalSearch.route) },
                    viewModel = homeViewModel
                )
            }

            composable(Screen.GlobalSearch.route) {
                GlobalSearchScreen(
                    onNavigateBack = { navController.popBackStack() },
                    onNavigateToTournament = { id -> navController.navigate(Screen.TournamentDetails.createRoute(id)) },
                    onNavigateToMatch = { id -> navController.navigate(Screen.MatchDetail.createRoute(id)) }
                )
            }

            // Tournament Suite
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
                val id = backStackEntry.arguments?.getString("tournamentId") ?: "tourn-1"
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
                route = Screen.TournamentRules.route,
                arguments = listOf(navArgument("tournamentId") { type = NavType.StringType })
            ) { backStackEntry ->
                val id = backStackEntry.arguments?.getString("tournamentId") ?: "tourn-1"
                TournamentRulesScreen(tournamentId = id, onNavigateBack = { navController.popBackStack() })
            }

            composable(
                route = Screen.TournamentSchedule.route,
                arguments = listOf(navArgument("tournamentId") { type = NavType.StringType })
            ) { backStackEntry ->
                val id = backStackEntry.arguments?.getString("tournamentId") ?: "tourn-1"
                TournamentScheduleScreen(tournamentId = id, onNavigateBack = { navController.popBackStack() })
            }

            composable(
                route = Screen.TournamentParticipants.route,
                arguments = listOf(navArgument("tournamentId") { type = NavType.StringType })
            ) { backStackEntry ->
                val id = backStackEntry.arguments?.getString("tournamentId") ?: "tourn-1"
                TournamentParticipantsScreen(tournamentId = id, onNavigateBack = { navController.popBackStack() })
            }

            composable(
                route = Screen.TournamentBracket.route,
                arguments = listOf(navArgument("tournamentId") { type = NavType.StringType })
            ) { backStackEntry ->
                val id = backStackEntry.arguments?.getString("tournamentId") ?: "tourn-1"
                TournamentBracketScreen(
                    tournamentId = id,
                    onNavigateBack = { navController.popBackStack() },
                    viewModel = tournamentViewModel
                )
            }

            composable(
                route = Screen.JoinTournament.route,
                arguments = listOf(navArgument("tournamentId") { type = NavType.StringType })
            ) { backStackEntry ->
                val id = backStackEntry.arguments?.getString("tournamentId") ?: "tourn-1"
                JoinTournamentScreen(
                    tournamentId = id,
                    onNavigateBack = { navController.popBackStack() },
                    onJoinSuccess = { navController.navigate(Screen.JoinSuccess.createRoute(id)) }
                )
            }

            composable(
                route = Screen.JoinSuccess.route,
                arguments = listOf(navArgument("tournamentId") { type = NavType.StringType })
            ) { backStackEntry ->
                val id = backStackEntry.arguments?.getString("tournamentId") ?: "tourn-1"
                JoinSuccessScreen(
                    tournamentId = id,
                    onNavigateToMatches = {
                        navController.navigate(Screen.Matches.route) {
                            popUpTo(Screen.Home.route)
                        }
                    }
                )
            }

            // Matches & Live Match Room
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
                val id = backStackEntry.arguments?.getString("matchId") ?: "m-1"
                MatchDetailScreen(
                    matchId = id,
                    onNavigateBack = { navController.popBackStack() },
                    viewModel = matchesViewModel
                )
            }

            composable(
                route = Screen.LiveMatchRoom.route,
                arguments = listOf(navArgument("matchId") { type = NavType.StringType })
            ) { backStackEntry ->
                val id = backStackEntry.arguments?.getString("matchId") ?: "m-1"
                LiveMatchRoomScreen(
                    matchId = id,
                    onNavigateBack = { navController.popBackStack() },
                    onNavigateToDispute = { mId -> navController.navigate(Screen.MatchDispute.createRoute(mId)) }
                )
            }

            composable(
                route = Screen.MatchDispute.route,
                arguments = listOf(navArgument("matchId") { type = NavType.StringType })
            ) { backStackEntry ->
                val id = backStackEntry.arguments?.getString("matchId") ?: "m-1"
                MatchDisputeScreen(
                    matchId = id,
                    onNavigateBack = { navController.popBackStack() }
                )
            }

            // Leaderboard
            composable(Screen.Leaderboard.route) {
                LeaderboardScreen(viewModel = leaderboardViewModel)
            }

            // Fintech Wallet Suite
            composable(Screen.Wallet.route) {
                WalletScreen(
                    onNavigateBack = { navController.popBackStack() },
                    viewModel = walletViewModel
                )
            }

            composable(Screen.AddCash.route) {
                AddCashScreen(
                    onNavigateBack = { navController.popBackStack() },
                    onPaymentSuccess = { navController.popBackStack() }
                )
            }

            composable(Screen.Withdraw.route) {
                WithdrawScreen(
                    onNavigateBack = { navController.popBackStack() },
                    onWithdrawSuccess = { navController.popBackStack() }
                )
            }

            composable(Screen.KycVerification.route) {
                KycVerificationScreen(
                    onNavigateBack = { navController.popBackStack() },
                    onSubmitSuccess = { navController.popBackStack() }
                )
            }

            composable(Screen.TransactionHistory.route) {
                TransactionHistoryScreen(
                    onNavigateBack = { navController.popBackStack() },
                    onNavigateToDetail = { id -> navController.navigate(Screen.TransactionDetail.createRoute(id)) }
                )
            }

            composable(
                route = Screen.TransactionDetail.route,
                arguments = listOf(navArgument("transactionId") { type = NavType.StringType })
            ) { backStackEntry ->
                val id = backStackEntry.arguments?.getString("transactionId") ?: "tx-1"
                TransactionDetailScreen(
                    transactionId = id,
                    onNavigateBack = { navController.popBackStack() }
                )
            }

            // Rewards & Achievements
            composable(Screen.RewardsOverview.route) {
                RewardsOverviewScreen(
                    onNavigateBack = { navController.popBackStack() },
                    onNavigateToAchievements = { navController.navigate(Screen.AchievementTree.route) }
                )
            }

            composable(Screen.AchievementTree.route) {
                AchievementTreeScreen(
                    onNavigateBack = { navController.popBackStack() },
                    onNavigateToDetail = { id -> navController.navigate(Screen.AchievementDetail.createRoute(id)) }
                )
            }

            composable(
                route = Screen.AchievementDetail.route,
                arguments = listOf(navArgument("achievementId") { type = NavType.StringType })
            ) { backStackEntry ->
                val id = backStackEntry.arguments?.getString("achievementId") ?: "ach-1"
                AchievementDetailScreen(
                    achievementId = id,
                    onNavigateBack = { navController.popBackStack() }
                )
            }

            // Teams & Rosters
            composable(Screen.MyTeams.route) {
                MyTeamsScreen(
                    onNavigateBack = { navController.popBackStack() },
                    onNavigateToCreateTeam = { navController.navigate(Screen.CreateTeam.route) },
                    onNavigateToTeamDetail = { id -> navController.navigate(Screen.TeamDetail.createRoute(id)) },
                    onNavigateToInvites = { navController.navigate(Screen.TeamInvites.route) }
                )
            }

            composable(
                route = Screen.TeamDetail.route,
                arguments = listOf(navArgument("teamId") { type = NavType.StringType })
            ) { backStackEntry ->
                val id = backStackEntry.arguments?.getString("teamId") ?: "team-1"
                TeamDetailScreen(
                    teamId = id,
                    onNavigateBack = { navController.popBackStack() }
                )
            }

            composable(Screen.CreateTeam.route) {
                CreateTeamScreen(
                    onNavigateBack = { navController.popBackStack() },
                    onTeamCreated = { navController.popBackStack() }
                )
            }

            composable(Screen.TeamInvites.route) {
                TeamInvitesScreen(
                    onNavigateBack = { navController.popBackStack() }
                )
            }

            // Notifications
            composable(Screen.Notifications.route) {
                NotificationScreen(
                    onNavigateBack = { navController.popBackStack() },
                    viewModel = notificationViewModel
                )
            }

            // Profile & Settings
            composable(Screen.Profile.route) {
                ProfileScreen(
                    onNavigateToEditProfile = { navController.navigate(Screen.EditProfile.route) },
                    onNavigateToSettings = { navController.navigate(Screen.Settings.route) },
                    onNavigateToWallet = { navController.navigate(Screen.Wallet.route) },
                    onNavigateToTeams = { navController.navigate(Screen.MyTeams.route) },
                    onNavigateToKyc = { navController.navigate(Screen.KycVerification.route) },
                    onNavigateToRewards = { navController.navigate(Screen.RewardsOverview.route) },
                    onNavigateToFaq = { navController.navigate(Screen.SupportFaq.route) },
                    onNavigateToTerms = { navController.navigate(Screen.TermsPrivacy.route) },
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

            composable(Screen.SupportFaq.route) {
                SupportFaqScreen(
                    onNavigateBack = { navController.popBackStack() }
                )
            }

            composable(Screen.TermsPrivacy.route) {
                TermsPrivacyScreen(
                    onNavigateBack = { navController.popBackStack() }
                )
            }
        }
    }
}
