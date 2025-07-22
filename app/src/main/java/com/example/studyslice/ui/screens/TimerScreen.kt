package com.example.studyslice.ui.screens

import android.content.Context
import android.media.MediaPlayer
import android.util.Log
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import com.example.studyslice.R
import kotlinx.coroutines.flow.collectLatest

@Composable
fun TimerScreen(
    navController: NavController,
    timerViewModel: TimerViewModel = viewModel()
) {
    Log.d("TimerScreen", "Composing TimerScreen. VM: ${timerViewModel.hashCode()}")
    val currentTimeDisplay by timerViewModel.currentTimeDisplay.collectAsState()
    val timerState by timerViewModel.timerState.collectAsState()
    val currentSessionType by timerViewModel.currentSessionType.collectAsState()
    val progress by timerViewModel.progress.collectAsState()

    val context = LocalContext.current

    // Observe one-shot events from ViewModel
    LaunchedEffect(key1 = timerViewModel) { // Re-launch if ViewModel instance changes
        timerViewModel.oneShotEvent.collectLatest { event ->
            when (event) {
                is TimerViewModel.TimerEvent.PlaySound -> {
                    playSound(context, R.raw.alarm_sound) // Replace with your sound file name
                }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // ... (rest of your TimerScreen UI: Text for session, CircularProgressIndicator, Buttons) ...
        Text(
            text = if (currentSessionType == SessionType.WORK) "Work Session" else "Break Time!",
            fontSize = 24.sp,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Box(contentAlignment = Alignment.Center) {
            CircularProgressIndicator(
                progress = progress,
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
                enabled = timerState != TimerState.FINISHED
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

private var mediaPlayer: MediaPlayer? = null

private fun playSound(context: Context, soundResourceId: Int) {
    // Release any previous MediaPlayer instance
    mediaPlayer?.release()
    mediaPlayer = null

    mediaPlayer = MediaPlayer.create(context, soundResourceId)?.apply {
        setOnCompletionListener {
            // Release the MediaPlayer when playback is complete
            it.release()
            mediaPlayer = null // Clear the reference
        }
        start() // Start playback
    }
}