# Arena UI Component Specification

## 1. Icon-First Architectural Rules
* **100% Vector Icons**: Standard Material 3 / Lucide vectors with uniform optical bounds (18dp for inline, 24dp for navigation & controls, 32dp for status).
* **Zero Emojis**: Text emojis (💰, 🏆, 🔥, ⚔️) are strictly forbidden in UI widgets.
* **Semantic Accessibility**: Every icon button supplies a localized `contentDescription` for talkback screen readers.

## 2. Core Reusable Component Library

| Component | Responsibility | Visual Styling |
| :--- | :--- | :--- |
| **`ArenaTopBar`** | Unified header across screens | Deep obsidian (`0xFF0F0F23`), centered branding, action icons |
| **`ArenaBottomNav`** | 5-tab main application navigation | Home, Tournaments, Matches, Ranks, Profile with active violet indicator |
| **`ArenaPrimaryButton`** | Main action CTA | Electric Violet (`0xFF7C3AED`), 12dp radius, loading state spinner |
| **`ArenaAccentButton`** | Destructive / High-priority action | Radiant Rose (`0xFFF43F5E`), high contrast |
| **`ArenaOutlinedButton`** | Secondary action / Navigation | 1dp border, transparent background, crisp typography |
| **`StatusBadge`** | Live, Upcoming, Joined, Solo/Squad tags | Pill container with 20% alpha background + 100% text color |
| **`ShimmerPlaceholder`** | Skeleton loading animation | 1200ms linear gradient shimmer brush |
| **`ArenaAnnouncementTicker`** | Live dispatch & emergency notice | Neon Violet / Amber alert bar with auto-scroll |
| **`JoinedMatchRoomCard`** | Displays room ID & password to registered players | Elevated surface with 1-tap Copy credentials buttons |
