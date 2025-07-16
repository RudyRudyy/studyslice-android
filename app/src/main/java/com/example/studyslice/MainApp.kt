package com.example.studyslice

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.studyslice.ui.Screen
import com.example.studyslice.ui.components.StudySliceBottomNavigationBar
import com.example.studyslice.ui.screens.HomeScreen
import com.example.studyslice.ui.screens.TimerScreen
import com.example.studyslice.ui.screens.SettingsScreen
import com.example.studyslice.ui.theme.StudySliceTheme

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter") // Add this if you don't use the padding parameter immediately, but it's good practice to use it.
@Composable
fun MainApp() {
    StudySliceTheme {
        val navController = rememberNavController()

        Scaffold(
            bottomBar = { StudySliceBottomNavigationBar(navController = navController) }
        ) { innerPadding -> // This innerPadding is provided by Scaffold
            AppNavigationHost(
                navController = navController,
                modifier = Modifier.padding(innerPadding) // Apply the padding here
            )
        }
    }
}

@Composable
fun AppNavigationHost(navController: NavHostController, modifier: Modifier = Modifier) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route,
        modifier = modifier // Apply the modifier containing padding
    ) {
        composable(Screen.Home.route) {
            HomeScreen(navController = navController)
        }
        composable(Screen.Timer.route) {
            TimerScreen(navController = navController)
        }
        composable(Screen.Settings.route) {
            SettingsScreen(navController = navController)
        }
        // Add other destinations as your app grows
    }
}
