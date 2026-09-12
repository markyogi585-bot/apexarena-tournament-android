# ApexArena UI/UX Pro Max Design System

## 1. Visual Direction & Style
- **Theme**: Obsidian Esports Neon (High Contrast, Immersive, Dynamic).
- **Primary Style**: Tactical Dark Interface with Electric Glow accents.
- **Elevation**: 3-level layered elevation system (Surface `#1E1C35`, Variant `#27273B`, Backdrop `#0F0F23`).

## 2. Color Tokens (Material 3 Mappings)

```kotlin
val ObsidianDark = Color(0xFF0F0F23)      // Background
val MidnightCard = Color(0xFF1E1C35)      // Card / Surface
val MutedSlate = Color(0xFF27273B)        // Surface Variant
val NeonViolet = Color(0xFF7C3AED)        // Primary
val SoftLilac = Color(0xFFA78BFA)         // Secondary
val RadiantRose = Color(0xFFF43F5E)       // Accent / CTA
val TextCrisp = Color(0xFFF8FAFC)         // On Background / Text
val TextMuted = Color(0xFF94A3B8)         // Secondary Text
val BorderViolet = Color(0xFF4C1D95)      // Structural Border
val EmeraldSuccess = Color(0xFF10B981)    // Live Status & Wins
val CrimsonError = Color(0xFFEF4444)      // Alerts & Losses
```

## 3. Typography Hierarchy
- **Display / Header**: Russo One / Chakra Petch / SansSerif Bold (Uppercase tracking +1sp for esports headers).
- **Body / Subtitle**: Inter / Roboto System Typography with 1.4x line-height for optimum readability.
- **Data / Stats**: Monospaced tabular figures for scores, timers, and countdowns.

## 4. Component Rules
- **Buttons**: Minimum touch target 48dp, corner radius 12dp, glow shadow on primary actions.
- **Cards**: 16dp padding, 1dp subtle border (`0xFF4C1D95`), no harsh gradient fills.
- **States**: Skeleton/Shimmer shimmer effect on all network loads, informative empty states with actionable CTA, friendly error cards with 1-tap retry.
- **Navigation**: Persistent 5-tab BottomNavigationBar (Home, Tournaments, Matches, Leaderboard, Profile).
