package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = VairalRed,
    onPrimary = Color.White,
    primaryContainer = Color(0xFF4A0A17),
    onPrimaryContainer = Color(0xFFFFD9DF),
    secondary = VairalAccentPurple,
    onSecondary = Color.White,
    secondaryContainer = Color(0xFF33072C),
    onSecondaryContainer = Color(0xFFFFD6FA),
    tertiary = VairalAccentCyan,
    onTertiary = Color.Black,
    background = VairalDarkBg,
    onBackground = VairalTextPrimary,
    surface = VairalSurface,
    onSurface = VairalTextPrimary,
    surfaceVariant = VairalSurfaceVariant,
    onSurfaceVariant = VairalTextSecondary,
    outline = VairalBorder
)

// For consistency with the viral video platform aesthetic, we maintain a dark-first experience
private val LightColorScheme = DarkColorScheme.copy(
    background = Color(0xFF0F1015),
    surface = Color(0xFF161822)
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = true,
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = DarkColorScheme,
        typography = Typography,
        content = content
    )
}
