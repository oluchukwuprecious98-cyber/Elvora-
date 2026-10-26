package com.example.ui.theme

import android.app.Activity
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val ElvoraColorScheme = darkColorScheme(
    primary = ElvoraElectricViolet,
    onPrimary = ElvoraSilverPure,
    primaryContainer = ElvoraVioletDeep,
    onPrimaryContainer = ElvoraSilverLight,
    secondary = ElvoraVioletBright,
    onSecondary = ElvoraSilverPure,
    secondaryContainer = ElvoraVioletSoft,
    onSecondaryContainer = ElvoraVioletGlow,
    tertiary = ElvoraCyanAccent,
    onTertiary = ElvoraMidnight,
    background = ElvoraMidnight,
    onBackground = ElvoraSilverLight,
    surface = ElvoraSurfaceDark,
    onSurface = ElvoraSilverLight,
    surfaceVariant = ElvoraSurfaceElevated,
    onSurfaceVariant = ElvoraSilverMid,
    outline = ElvoraBorder,
    outlineVariant = ElvoraBorderLight,
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = true,
    dynamicColor: Boolean = false, // Preserve Elvora's signature midnight electric violet branding
    content: @Composable () -> Unit
) {
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = ElvoraMidnight.toArgb()
            window.navigationBarColor = ElvoraMidnight.toArgb()
            val insetsController = WindowCompat.getInsetsController(window, view)
            insetsController.isAppearanceLightStatusBars = false
            insetsController.isAppearanceLightNavigationBars = false
        }
    }

    MaterialTheme(
        colorScheme = ElvoraColorScheme,
        typography = Typography,
        content = content
    )
}
