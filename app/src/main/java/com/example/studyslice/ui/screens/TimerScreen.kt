package com.example.studyslice.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.studyslice.ui.components.TimerCircle
import com.example.studyslice.ui.theme.StudySliceTheme

private var progress by mutableStateOf(0.75f)
private var isWorkSession by mutableStateOf(true)

@Composable
fun TimerScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        TimerCircle(
            progress = progress,
            timeText = "25:00",
            isWorkSession = isWorkSession,
            modifier = Modifier.padding(16.dp)
        )

        Spacer(modifier = Modifier.height(32.dp))

        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier.fillMaxWidth()
        ) {
            Button(onClick = { /* Start timer */ }) {
                Text("Start")
            }

            Button(onClick = { /* Pause timer */ }) {
                Text("Pause")
            }

            Button(onClick = { /* Reset timer */ }) {
                Text("Reset")
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        Button(onClick = { navController.navigate("home") }) {
            Text("Back to Home")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TimerScreenPreview() {
    StudySliceTheme {
        val navController = rememberNavController()
        TimerScreen(navController = navController)
    }
}

@Preview(showBackground = true)
@Composable
fun TimerScreenWorkPreview() {
    StudySliceTheme {
        val navController = rememberNavController()
        // This assumes you've updated your TimerScreen to accept isWorkSession parameter
        // If not, adjust accordingly
        TimerScreen(navController = navController)
    }
}

@Preview(showBackground = true)
@Composable
fun TimerScreenBreakPreview() {
    StudySliceTheme {
        val navController = rememberNavController()
        // This assumes you've updated your TimerScreen to accept isWorkSession parameter
        // If not, adjust accordingly
        TimerScreen(navController = navController)
    }
}