# Arena Screen Specification & Information Architecture

## 1. Screen Matrix

### Onboarding & Authentication
* **`LoginScreen`**: Fast email & password authentication with password toggle.
* **`RegisterScreen`**: Gladiator tag registration, email binding, and welcome bonus trigger.

### Command Center (Home)
* **`HomeScreen`**:
  * Top bar: Gladiator Avatar, welcome greeting, Live Wallet Balance Pill (`₹1,650`), Notification bell.
  * Live Broadcast Announcement Ticker (System updates & Match notices).
  * Division Tier Banner (`DIAMOND DIVISION` - 2,450 APEX PTS).
  * **"Your Joined Match Rooms"** (Shows live room ID & password countdown for joined tournaments).
  * Featured Tournaments Carousel.
  * Live Battles Heartbeat Pulse Feed.

### Tournaments & Brackets
* **`TournamentListScreen`**: Search bar, Format filter chips (All, Solo, Squad), Prize pool & slot meters.
* **`TournamentDetailScreen`**: Hero banner, rules breakdown, participant counter, **Sticky Join / Joined State Bar**.
* **`TournamentBracketScreen`**: Horizontal elimination tree (Quarterfinals, Semifinals, Grand Finals) with live match scores.

### Matches & Live Room Center
* **`MatchesScreen`**: Segmented tab controls (Upcoming/Live vs Completed).
* **`MatchDetailScreen`**: Scoreboard duel arena, slot credentials, screenshot dispute filing.

### Fintech Wallet & Rewards
* **`WalletScreen`**:
  * Metallic Obsidian Card with balance eye-toggle.
  * Breakdown: Unlocked Winnings, Deposits, Bonus Cash.
  * Add Money Bottom Sheet with quick amount chips (`₹50`, `₹100`, `₹250`, `₹500`).
  * Instant UPI Withdrawal Bottom Sheet.
  * Live Double-entry Transaction Passbook.

### Leaderboard, Notifications & Profile
* **`LeaderboardScreen`**: Global ladder with Gold, Silver, Bronze badges, player tiers, and win rates.
* **`NotificationScreen`**: Category filters (All, Matches, Tournaments, Rewards) + "Mark All Read".
* **`ProfileScreen`**: Player stats matrix (Wins, Losses, Win-Rate, Points), in-game UID editor, OLED Dark settings.
