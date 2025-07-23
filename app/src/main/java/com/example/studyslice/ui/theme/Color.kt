package com.example.studyslice.ui.theme

import androidx.compose.ui.graphics.Color

// --- Light Theme Colors (as before) ---
val PRIMARY_RED = Color(0xFFE57373)
val PRIMARY_VARIANT_LIGHT = Color(0xFFD32F2F) // Renamed for clarity if used as a primary variant in light
val LIGHT_RED_ACCENT = Color(0xFFFFCDD2)

val ACCENT_GREEN_SAGE = Color(0xFF8D9F87)
val ACCENT_CREAM = Color(0xFFF2EFE9)

val BACKGROUND_PALE_PINK = Color(0xFFFFDDE2)
val SURFACE_LIGHT = Color(0xFFFFFFFF) // Renamed for clarity

val ON_PRIMARY_LIGHT = Color(0xFFFFFFFF)
val ON_SECONDARY_LIGHT = Color(0xFF000000)
val ON_BACKGROUND_LIGHT = Color(0xFF1C1B1F)
val ON_SURFACE_LIGHT = Color(0xFF1C1B1F)

val TOMATO_SKIN_RED = Color(0xFFFF6347)
val TOMATO_FLESH_PINK = Color(0xFFFFA07A)
val TOMATO_LEAF_GREEN = Color(0xFF2E7D32)


// --- Dark Theme Colors ---
val DARK_PRIMARY_RED = Color(0xFFCF6679) // A common desaturated red for dark themes, or use PRIMARY_RED if it has enough contrast
val DARK_PRIMARY_VARIANT = Color(0xFFB00020) // Could be a deeper, more desaturated red

val DARK_ACCENT_GREEN_SAGE = Color(0xFFA5C69C) // Lighter version of sage for dark theme
val DARK_ACCENT_CREAM = Color(0xFF42403B)      // Darker version of cream, or a contrasting dark accent

val DARK_BACKGROUND = Color(0xFF121212)       // Standard Material dark background
val DARK_SURFACE = Color(0xFF1E1E1E)          // Slightly lighter than background for cards, etc. (or use 0xFF242424)

val ON_PRIMARY_DARK = Color(0xFF000000)        // If DARK_PRIMARY_RED is light enough, black text might work, else white
val ON_SECONDARY_DARK = Color(0xFF000000)      // Text on dark accent
val ON_BACKGROUND_DARK = Color(0xFFE0E0E0)     // Light gray text on dark background
val ON_SURFACE_DARK = Color(0xFFE0E0E0)        // Light gray text on dark surfaces


