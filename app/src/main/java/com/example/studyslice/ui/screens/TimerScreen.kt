package com.example.studyslice.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.studyslice.ui.screens.SessionType // Import your SessionType
import com.example.studyslice.ui.screens.TimerState // Import your TimerState
import com.example.studyslice.ui.screens.TimerViewModel // Import your ViewModel
import androidx.compose.material3.CircularProgressIndicator

@Composable
fun TimerScreen(
    navController: NavController,
    timerViewModel: TimerViewModel = viewModel() // Obtain ViewModel instance
) {
    val currentTimeDisplay by timerViewModel.currentTimeDisplay.collectAsState()
    val timerState by timerViewModel.timerState.collectAsState()
    val currentSessionType by timerViewModel.currentSessionType.collectAsState()
    val progress by timerViewModel.progress.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = if (currentSessionType == SessionType.WORK) "Work Session" else "Break Time!",
            fontSize = 24.sp,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Simple Circular Progress Indicator (can be made more elaborate)
        Box(contentAlignment = Alignment.Center) {
            CircularProgressIndicator(
                progress = progress, // <--- Corrected: Pass the Float value directly
                modifier = Modifier.size(200.dp),
                strokeWidth = 8.dp
            )
            Text(
                text = currentTimeDisplay,
                fontSize = 48.sp,
                style = MaterialTheme.typography.headlineLarge
            )
        }


        Spacer(modifier = Modifier.height(32.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(
                onClick = { timerViewModel.startPauseTimer() },
                enabled = timerState != TimerState.FINISHED // Can disable if finished before reset
            ) {
                Text(
                    when (timerState) {
                        TimerState.RUNNING -> "Pause"
                        TimerState.PAUSED, TimerState.IDLE, TimerState.FINISHED -> "Start"
                    }
                )
            }

            Button(
                onClick = { timerViewModel.resetTimer() },
                enabled = timerState != TimerState.IDLE || progress < 1.0f
            ) {
                Text("Reset")
            }
        }
    }
}
