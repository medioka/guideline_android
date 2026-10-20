package com.medioka.guideline.app.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = SkyBlueLight,
    onPrimary = Color(0xFF05204A),
    secondary = SunGold,
    onSecondary = Ink,
    tertiary = AquaDark,
    onTertiary = Color(0xFF05204A),
    background = Midnight,
    onBackground = Frost,
    surface = MidnightSurface,
    onSurface = Frost,
    surfaceVariant = NightVariant,
    onSurfaceVariant = NightSlate,
    outline = NightLine,
    error = Color(0xFFFFB4AB),
    onError = Color(0xFF05204A),
)

private val LightColorScheme = lightColorScheme(
    primary = SkyBlue,
    onPrimary = PaperWhite,
    secondary = SunGold,
    onSecondary = Ink,
    tertiary = Aqua,
    onTertiary = PaperWhite,
    background = Mist,
    onBackground = Ink,
    surface = PaperWhite,
    onSurface = Ink,
    surfaceVariant = CloudVariant,
    onSurfaceVariant = Slate,
    outline = CloudLine,
    error = Color(0xFFBA1A1A),
    onError = PaperWhite,
)

@Composable
fun GuidelineAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme,
        typography = Typography,
        content = content,
    )
}
