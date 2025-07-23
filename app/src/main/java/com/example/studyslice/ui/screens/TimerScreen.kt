package com.example.studyslice.ui.screens

import android.content.Context
import android.media.MediaPlayer
import android.util.Log
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.SwapHoriz
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
    val currentTimeDisplay by timerViewModel.currentTimeDisplay.collectAsState()
    val timerState by timerViewModel.timerState.collectAsState()
    val currentSessionType by timerViewModel.currentSessionType.collectAsState()
    val progress by timerViewModel.progress.collectAsState()

    val context = LocalContext.current

    LaunchedEffect(key1 = timerViewModel) {
        timerViewModel.oneShotEvent.collectLatest { event ->
            when (event) {
                is TimerViewModel.TimerEvent.PlaySound -> {
                    playSound(context, R.raw.alarm_sound)
                }
                is TimerViewModel.TimerEvent.Vibrate -> {
                    vibratePhone(context)
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
        Text(
            text = if (currentSessionType == SessionType.WORK) "Work Session" else "Break Time!",
            fontSize = 24.sp,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Box(contentAlignment = Alignment.Center) {
            CircularProgressIndicator(
                progress =  progress ,
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
            ) {
                Text(
                    when (timerState) {
                        TimerState.RUNNING -> "Pause"
                        else -> "Start"
                    }
                )
            }

            Button(
                onClick = { timerViewModel.resetTimer() },
            ) {
                Text("Reset")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { timerViewModel.switchToNextSessionType() },
            // Optional: Add some styling or an icon
            // enabled = timerState != TimerState.RUNNING // You might want to disable this if the timer is running
        ) {
            Icon(Icons.Filled.SwapHoriz, contentDescription = "Switch Session")
            Spacer(Modifier.size(ButtonDefaults.IconSpacing))
            Text(
                text = if (currentSessionType == SessionType.WORK) "Switch to Break" else "Switch to Work"
            )
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

private fun vibratePhone(context: Context, durationMillis: Long = 500) { // Default duration 500ms
    val vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        val vibratorManager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
        vibratorManager.defaultVibrator
    } else {
        @Suppress("DEPRECATION")
        context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
    }

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        // For Android Oreo (API 26) and above, use VibrationEffect
        // Create a one-shot vibration
        vibrator.vibrate(VibrationEffect.createOneShot(durationMillis, VibrationEffect.DEFAULT_AMPLITUDE))
    } else {
        // For older versions (deprecated)
        @Suppress("DEPRECATION")
        vibrator.vibrate(durationMillis)
    }
}