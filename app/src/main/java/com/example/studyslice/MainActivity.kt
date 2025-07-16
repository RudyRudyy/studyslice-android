package com.example.studyslice

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
// Remove imports for NavHost, composable, rememberNavController, HomeScreen, TimerScreen, SettingsScreen if they are no longer directly used here.
// Keep StudySliceTheme if it's not applied within MainApp.kt, but it's good practice to have it in MainApp.kt

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // MainApp will now apply StudySliceTheme
            MainApp()
        }
    }
}