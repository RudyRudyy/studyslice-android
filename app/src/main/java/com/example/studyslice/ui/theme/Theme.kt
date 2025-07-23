package com.example.studyslice.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.lightColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val LightColors = lightColorScheme(
    primary = PRIMARY_RED,
    onPrimary = ON_PRIMARY_LIGHT,
    primaryContainer = LIGHT_RED_ACCENT,
    // onPrimaryContainer = ...

    secondary = ACCENT_GREEN_SAGE,
    onSecondary = ON_SECONDARY_LIGHT,
    // secondaryContainer = ...
    // onSecondaryContainer = ...

    tertiary = ACCENT_CREAM,
    // onTertiary = ...
    // tertiaryContainer = ...
    // onTertiaryContainer = ...

    error = Color(0xFFB00020), // Standard error color
    onError = Color(0xFFFFFFFF),
    // errorContainer = ...
    // onErrorContainer = ...

    background = BACKGROUND_PALE_PINK,
    onBackground = ON_BACKGROUND_LIGHT,

    surface = SURFACE_LIGHT,
    onSurface = ON_SURFACE_LIGHT,
    // ... other light theme slots
)

private val DarkColors = darkColorScheme(
    primary = DARK_PRIMARY_RED,       // Or PRIMARY_RED if it works well
    onPrimary = ON_PRIMARY_DARK,      // Usually black or white depending on primary's brightness
    primaryContainer = PRIMARY_VARIANT_LIGHT, // Example: Using a light theme variant for dark container
    // onPrimaryContainer = ...

    secondary = DARK_ACCENT_GREEN_SAGE,
    onSecondary = ON_SECONDARY_DARK,    // Usually black or white
    // secondaryContainer = ...
    // onSecondaryContainer = ...

    tertiary = DARK_ACCENT_CREAM,
    // onTertiary = ...
    // tertiaryContainer = ...
    // onTertiaryContainer = ...

    error = Color(0xFFCF6679),         // Material recommended dark error color
    onError = Color(0xFF000000),       // Black text on CF6679
    // errorContainer = ...
    // onErrorContainer = ...

    background = DARK_BACKGROUND,
    onBackground = ON_BACKGROUND_DARK,

    surface = DARK_SURFACE,
    onSurface = ON_SURFACE_DARK,
    // ... other dark theme slots (surfaceVariant, outline, etc.)
    // You can generate a full baseline M3 theme using the Material Theme Builder
    // (https://m3.material.io/theme-builder) to get all the slots.
)

@Composable
fun StudySliceTheme(
    darkTheme: Boolean = isSystemInDarkTheme(), // This now determines which scheme is used
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColors // ***** APPLY DARKCOLORS IF darkTheme is true *****
        else -> LightColors
    }
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            // For dark theme, status bar is often same as background or surface
            window.statusBarColor = colorScheme.background.toArgb() // Or colorScheme.surface.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        shapes = AppShapes,
        content = content
    )
}

