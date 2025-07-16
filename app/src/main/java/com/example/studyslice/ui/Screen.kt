package com.example.studyslice.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(val route: String, val label: String, val icon: ImageVector) {
    object Home : Screen("home", "Home", Icons.Filled.Home)
    object Timer : Screen("timer", "Timer", Icons.Filled.Star)
    object Settings : Screen("settings", "Settings", Icons.Filled.Settings)
}
