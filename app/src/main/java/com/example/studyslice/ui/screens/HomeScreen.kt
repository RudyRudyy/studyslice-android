package com.example.studyslice.ui.screens

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.studyslice.ui.theme.StudySliceTheme

@Composable
fun HomeScreen(navController: NavController) { // navController might still be useful for other things, or you could remove it if truly unused
    Log.d("HomeScreen", "Composing HomeScreen")
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "StudySlice",
                style = MaterialTheme.typography.headlineLarge
            )

            // You can add other content here that makes sense for your HomeScreen
            // For example, a welcome message, statistics, a quote, etc.
            Spacer(modifier = Modifier.height(16.dp)) // Optional: if you add more content
            Text(
                text = "Welcome! Use the navigation bar below to start a timer or change settings.",
                style = MaterialTheme.typography.bodyMedium
            )

        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    StudySliceTheme {
        // Create a dummy NavController for preview
        val navController = rememberNavController()
        HomeScreen(navController = navController)
    }
}