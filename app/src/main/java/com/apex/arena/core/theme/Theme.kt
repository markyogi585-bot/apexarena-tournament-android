package com.apex.arena.core.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
    primary = NeonViolet,
    onPrimary = TextCrisp,
    primaryContainer = BorderViolet,
    onPrimaryContainer = SoftLilac,
    secondary = SoftLilac,
    onSecondary = TextDark,
    tertiary = RadiantRose,
    onTertiary = TextCrisp,
    background = ObsidianDark,
    onBackground = TextCrisp,
    surface = MidnightCard,
    onSurface = TextCrisp,
    surfaceVariant = MutedSlate,
    onSurfaceVariant = TextMuted,
    outline = BorderViolet,
    error = CrimsonError,
    onError = TextCrisp
)

@Composable
fun ApexArenaTheme(
    darkTheme: Boolean = true, // Force high-performance Obsidian Esports Theme
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = DarkColorScheme,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}
