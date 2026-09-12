# Apex Arena — Master Build & Architecture Report

## 1. Project Overview
- **Application Name**: Apex Arena (`com.apex.arena`)
- **Visual Design System**: Obsidian Neon Dark Theme (`#0F0F23`, `#7C3AED`, `#F43F5E`, `#10B981`)
- **Technology Stack**: Kotlin 2.0+, Jetpack Compose, Material 3 Foundation with custom Arena token wrapper, Supabase Auth + PostgREST + Realtime, Coroutines & Flow, Ktor Client, Coil 3.0.
- **Repository**: [`markyogi585-bot/apexarena-tournament-android`](https://github.com/markyogi585-bot/apexarena-tournament-android)
- **Latest Green CI/CD Build**: [GitHub Actions Run #15](https://github.com/markyogi585-bot/apexarena-tournament-android/actions/runs/34679594429)
- **Artifact**: `apex-arena-debug-apk` (19.87 MB)

---

## 2. Complete 40-Screen Inventory
1. **`SplashScreen`**: Cold-boot brand screen with smooth 1.2s timeout, gradient radial glow, and auto-routing.
2. **`WelcomeScreen`**: 3-slide esports hero onboarding with value proposition, free registration CTA, and login.
3. **`LoginScreen`**: Obsidian Neon card, email/password validation, biometric shortcut, error banner.
4. **`RegisterScreen`**: Gamertag, Email, Password strength meter, referral code input, and fair play terms checkbox.
5. **`ForgotPasswordScreen`**: Email OTP dispatcher with validation.
6. **`VerificationOtpScreen`**: 6-digit numeric keypad OTP validator with auto-advance.
7. **`ResetPasswordScreen`**: Secure password update form with matching verification.
8. **`SessionExpiredScreen`**: Security modal screen prompting re-authentication.
9. **`HomeScreen`**: Master dashboard with live ticker, live match pulse banner, quick actions, tier division card, and featured tournaments.
10. **`GlobalSearchScreen`**: Debounced search across tournaments, teams, and players with category tabs.
11. **`TournamentListScreen`**: Category filters (BGMI, Free Fire, Valorant, COD), status badges, and pull-to-refresh.
12. **`TournamentDetailScreen`**: Hero banner, prize pool, slot capacity, rules CTA, and sticky registration bar.
13. **`TournamentRulesScreen`**: Anti-cheat policy, room credentials policy, and scoring matrix.
14. **`TournamentScheduleScreen`**: Round-by-round time slots and room opening timeline.
15. **`TournamentParticipantsScreen`**: Registered squad rosters, slot numbers, and captain verification badges.
16. **`TournamentBracketScreen`**: Dynamic horizontal scroll bracket tree (Quarter, Semi, Finals) with winner highlight.
17. **`JoinTournamentScreen`**: Squad roster selector, fee confirmation, and rule agreement checkbox.
18. **`JoinSuccessScreen`**: Celebratory state view with slot number and match lobby link.
19. **`MatchesScreen`**: Tabbed interface (Upcoming, Live, Completed).
20. **`MatchDetailScreen`**: Map, server, ping, slot, and scheduled time.
21. **`LiveMatchRoomScreen`**: Realtime room ID & password, live kill feed, and standings.
22. **`MatchDisputeScreen`**: Formal dispute ticket submission with screenshot proof upload and reason category.
23. **`LeaderboardScreen`**: Global, Game-specific, and Season rank ladder with user sticky card.
24. **`WalletScreen`**: Privacy eye balance toggle, quick action grid, and passbook preview.
25. **`AddCashScreen`**: Instant UPI QR, PhonePe/GPay/Paytm, Credit/Debit cards, and Netbanking.
26. **`WithdrawScreen`**: Instant IMPS bank transfer & UPI VPA payout with fee calculator.
27. **`KycVerificationScreen`**: PAN/Aadhaar/Passport compliance upload for payouts.
28. **`TransactionHistoryScreen`**: Double-entry ledger passbook with date filter.
29. **`TransactionDetailScreen`**: Transaction receipt, UTR reference, and gateway status.
30. **`RewardsOverviewScreen`**: 7-day daily login streak calendar and milestone claim.
31. **`AchievementTreeScreen`**: Tiered achievement branch (First Blood, Centurion, MVP, High Roller).
32. **`AchievementDetailScreen`**: Progress bar, badge tier, and reward unlock claim.
33. **`MyTeamsScreen`**: Esports squad list, captain badges, and win rates.
34. **`TeamDetailScreen`**: Active roster (4/5), match statistics, and roles.
35. **`CreateTeamScreen`**: Squad name, clan tag, and registration.
36. **`TeamInvitesScreen`**: Pending squad invites with Accept/Decline action.
37. **`NotificationScreen`**: Categorized inbox (Matches, Rewards, Teams, Announcements) with swipe-to-read.
38. **`ProfileScreen`**: Player dossier, win rate, rank badge, and quick hub menu.
39. **`EditProfileScreen`**: GamerTag, in-game ID, bio, and avatar customizer.
40. **`SettingsScreen`**: Dark/Light mode toggle, notifications, and security.

---

## 3. 16 Modular Modal Bottom Sheets
1. `QuickJoinSheet`
2. `AddCashModalSheet`
3. `WithdrawModalSheet`
4. `MatchRoomCredentialsSheet`
5. `FilterTournamentsSheet`
6. `FilterLeaderboardSheet`
7. `MatchDisputeReportSheet`
8. `KycUploadSheet`
9. `CreateTeamSheet`
10. `TeamInviteMemberSheet`
11. `TransactionFilterSheet`
12. `NotificationDetailSheet`
13. `AvatarSelectorSheet`
14. `ReportPlayerSheet`
15. `TermsOfServiceSheet`
16. `SessionTimeoutAlertSheet`

---

## 4. Supabase Master Database
- Project URL: `https://kuhhlykxyheatmhipfzs.supabase.co`
- Project Ref: `kuhhlykxyheatmhipfzs`
- Schema File: `supabase/migrations/20260912000003_arena_v2_master_schema.sql` (12 relational tables, RLS, and performance indexes).
