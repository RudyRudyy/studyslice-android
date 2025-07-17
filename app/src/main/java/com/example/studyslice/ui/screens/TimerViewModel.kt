package com.example.studyslice.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

enum class TimerState {
    IDLE,
    RUNNING,
    PAUSED,
    FINISHED
}

enum class SessionType {
    WORK,
    BREAK
}

class TimerViewModel : ViewModel() {

    // --- Configuration ---
    private var workDurationMillis = 25 * 60 * 1000L // 25 minutes
    private var breakDurationMillis = 5 * 60 * 1000L   // 5 minutes

    // --- StateFlows for UI observation ---
    private val _currentTimeDisplay = MutableStateFlow("25:00") // Formatted time
    val currentTimeDisplay: StateFlow<String> = _currentTimeDisplay.asStateFlow()

    private val _timerState = MutableStateFlow(TimerState.IDLE)
    val timerState: StateFlow<TimerState> = _timerState.asStateFlow()

    private val _currentSessionType = MutableStateFlow(SessionType.WORK)
    val currentSessionType: StateFlow<SessionType> = _currentSessionType.asStateFlow()

    private val _progress = MutableStateFlow(1f) // 0.0 to 1.0 for progress indicator
    val progress: StateFlow<Float> = _progress.asStateFlow()


    // Internal Timer Logic
    private var job: Job? = null
    private var remainingTimeMillis = workDurationMillis

    init {
        resetTimer()
    }

    fun startPauseTimer() {
        when (_timerState.value) {
            TimerState.IDLE, TimerState.PAUSED, TimerState.FINISHED -> startTimer()
            TimerState.RUNNING -> pauseTimer()
        }
    }

    private fun startTimer() {
        job?.cancel()
        _timerState.value = TimerState.RUNNING
        job = viewModelScope.launch(Dispatchers.Default) {
            val startTime = System.currentTimeMillis()
            val initialRemainingTime = remainingTimeMillis

            while (isActive && remainingTimeMillis > 0) {
                delay(50L)
                val elapsedSinceStart = System.currentTimeMillis() - startTime
                remainingTimeMillis = initialRemainingTime - elapsedSinceStart

                if (remainingTimeMillis < 0) remainingTimeMillis = 0

                val totalSeconds = remainingTimeMillis / 1000
                val minutes = totalSeconds / 60
                val seconds = totalSeconds % 60
                _currentTimeDisplay.value = String.format("%02d:%02d", minutes, seconds)

                val totalDuration = if (_currentSessionType.value == SessionType.WORK) workDurationMillis else breakDurationMillis
                _progress.value = remainingTimeMillis.toFloat() / totalDuration.toFloat()
            }

            if (remainingTimeMillis <= 0) {
                onTimerFinished()
            }
        }
    }

    private fun pauseTimer() {
        job?.cancel()
        _timerState.value = TimerState.PAUSED
    }

    fun resetTimer() {
        job?.cancel()
        _timerState.value = TimerState.IDLE
        _currentSessionType.value = SessionType.WORK
        remainingTimeMillis = workDurationMillis
        _currentTimeDisplay.value = formatMillis(remainingTimeMillis)
        _progress.value = 1f
    }

    private fun onTimerFinished() {
        _timerState.value = TimerState.FINISHED
        if (_currentSessionType.value == SessionType.WORK) {
            _currentSessionType.value = SessionType.BREAK
            remainingTimeMillis = breakDurationMillis
        } else {
            _currentSessionType.value = SessionType.WORK
            remainingTimeMillis = workDurationMillis
        }
        _currentTimeDisplay.value = formatMillis(remainingTimeMillis)
        _progress.value = 1f
        _timerState.value = TimerState.IDLE
    }

    // format milliseconds to MM:SS
    private fun formatMillis(millis: Long): String {
        val totalSeconds = millis / 1000
        val minutes = totalSeconds / 60
        val seconds = totalSeconds % 60
        return String.format("%02d:%02d", minutes, seconds)
    }

    fun setWorkDuration(minutes: Int) {
        workDurationMillis = minutes * 60 * 1000L
        if (_currentSessionType.value == SessionType.WORK && _timerState.value == TimerState.IDLE) {
            resetTimer()
        }
    }

    fun setBreakDuration(minutes: Int) {
        breakDurationMillis = minutes * 60 * 1000L
        if (_currentSessionType.value == SessionType.BREAK && _timerState.value == TimerState.IDLE) {
            remainingTimeMillis = breakDurationMillis
            _currentTimeDisplay.value = formatMillis(remainingTimeMillis)
            _progress.value = 1f
        }
    }


    override fun onCleared() {
        super.onCleared()
        job?.cancel()
    }
}
