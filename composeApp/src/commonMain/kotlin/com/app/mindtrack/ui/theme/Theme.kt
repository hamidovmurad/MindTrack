package com.app.mindtrack.ui.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

/**
 * Light color scheme using MindTrack brand colors
 */
private val LightColorScheme = lightColorScheme(
    primary = FreshMint,
    onPrimary = OnBrandText,
    primaryContainer = Color(0xFFDEEBF8),
    onPrimaryContainer = FreshMint,

    secondary = ForestTeal,
    onSecondary = OnBrandText,
    secondaryContainer = Color(0xFFD4E8DC),
    onSecondaryContainer = ForestTeal,

    tertiary = FreshMint,
    onTertiary = OnBrandText,
    tertiaryContainer = Color(0xFFDEF3E9),
    onTertiaryContainer = FreshMint,

    error = AppError,
    onError = OnBrandText,
    errorContainer = Color(0xFFFBEDEB),
    onErrorContainer = AppError,

    background = AppBackground,
    onBackground = TextHighEmphasis,

    surface = AppSurface,
    onSurface = TextHighEmphasis,
    surfaceVariant = Color(0xFFF0F2F5),
    onSurfaceVariant = TextMediumEmphasis,

    outline = TextDisabled
)

/**
 * Dark color scheme using MindTrack brand colors
 */
private val DarkColorScheme = darkColorScheme(
    primary = FreshMint,
    onPrimary = Color(0xFF0D2818),
    primaryContainer = Color(0xFF1B4D2E),
    onPrimaryContainer = FreshMint,

    secondary = ForestTeal,
    onSecondary = OnBrandText,
    secondaryContainer = Color(0xFF1E5A3F),
    onSecondaryContainer = ForestTeal,

    tertiary = BrightEmerald,
    onTertiary = Color(0xFF0D2818),
    tertiaryContainer = Color(0xFF1B5E2B),
    onTertiaryContainer = BrightEmerald,

    error = AppError,
    onError = Color(0xFF5D0C0E),
    errorContainer = Color(0xFF8A1A1D),
    onErrorContainer = AppError,

    background = Color(0xFF0F1419),
    onBackground = Color(0xFFE3E6E9),

    surface = Color(0xFF1A1F26),
    onSurface = Color(0xFFE3E6E9),
    surfaceVariant = Color(0xFF2E3339),
    onSurfaceVariant = Color(0xFFC7CBD0),

    outline = Color(0xFF91979D)
)

/**
 * MindTrack theme applying brand colors
 *
 * @param darkTheme Whether to use dark color scheme (defaults to false for light theme)
 * @param content The composable content to apply the theme to
 */
@Composable
fun MindTrackTheme(
    darkTheme: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        content = content
    )
}




