# Apex Arena — UI Component & Bottom Sheet Specifications

## Modular Modal Bottom Sheets (16 Total)
1. **`QuickJoinSheet`**: Entry fee vs wallet balance validation, one-tap slot reservation.
2. **`AddCashModalSheet`**: Quick amount chips (+₹100, +₹500, +₹1000), payment gateway picker.
3. **`WithdrawModalSheet`**: Instant IMPS/UPI VPA input with balance ceiling enforcement.
4. **`MatchRoomCredentialsSheet`**: One-tap clipboard copy for Room ID and Password with slot number.
5. **`FilterTournamentsSheet`**: Multi-select chips for Game, Format (SOLO/DUO/SQUAD), and Entry Fee.
6. **`FilterLeaderboardSheet`**: Region, Season, and Tier filter selectors.
7. **`MatchDisputeReportSheet`**: Category radio options (Hacking, Wrong Results, Collusion) + explanation.
8. **`KycUploadSheet`**: Document type (PAN, Aadhaar, Passport) + Govt ID number input.
9. **`CreateTeamSheet`**: Squad title, tag, and logo emblem selector.
10. **`TeamInviteMemberSheet`**: GamerTag search and instant roster invite dispatcher.
11. **`TransactionFilterSheet`**: Filter ledger by Deposit, Withdrawal, Prize, or Entry.
12. **`NotificationDetailSheet`**: Deep-link preview and direct action trigger.
13. **`AvatarSelectorSheet`**: 12 curated esports avatar avatars and badges.
14. **`ReportPlayerSheet`**: Anti-cheat and toxicity player reporting modal.
15. **`TermsOfServiceSheet`**: Fair play, anti-cheat, and refund terms modal.
16. **`SessionTimeoutAlertSheet`**: Auto-reconnect and session restoration prompt.

## Core Component Foundations
- **Scaffold & Bars**: `ArenaTopBar`, `ApexBottomBar` with pill indicator.
- **Buttons**: `ApexButton` (Primary CTA with loading state & rose gradient), `ApexOutlinedButton`, `ApexSecondaryButton`.
- **Inputs**: `ApexTextField` with leading vector icons and Obsidian Neon focus glow.
- **Badges**: `StatusBadge` supporting `LIVE`, `OPEN`, `FULL`, `COMPLETED`, `JOINED`, `ALIVE`, `ELIMINATED`.
- **Animations**: `ShimmerAnimation` for skeleton loaders, `infiniteRepeatable` pulse for live esports matches.
