package com.example.studyslice.ui.components

import android.util.Log
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.studyslice.ui.Screen // Your Screen sealed class

@Composable
fun StudySliceBottomNavigationBar(navController: NavController) {
    val items = listOf(
        Screen.Home,
        Screen.Timer,
        Screen.Settings
    )

    NavigationBar {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        items.forEach { screen ->
            NavigationBarItem(
                icon = { Icon(screen.icon, contentDescription = screen.label) },
                label = { Text(screen.label) },
                selected = currentRoute == screen.route,
                onClick = {
                    Log.d("BottomNav", "Attempting to navigate to: ${screen.route}. Current route: ${navController.currentDestination?.route}")

                    navController.navigate(screen.route) {
                        navController.graph.startDestinationRoute?.let { route ->
                            popUpTo(route) {
                                saveState = true
                            }
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                    Log.d("BottomNav", "Navigated. New current route: ${navController.currentDestination?.route}")
                }
            )
        }
    }
}
