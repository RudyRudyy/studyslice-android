package com.example.studyslice.ui.screens

import android.app.Application
import android.util.Log
import androidx.activity.result.launch
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
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

class TimerViewModel(application: Application) : AndroidViewModel(application) {
    init {
        Log.d("TimerVM", "INIT: ${this.hashCode()}")
    }

    // --- Configuration ---
    private var workDurationMillis = 25 * 60 * 1000L
    private var breakDurationMillis = 5 * 60 * 1000L

    // --- StateFlows for UI observation ---
    private val _currentTimeDisplay = MutableStateFlow(formatMillis(workDurationMillis))
    val currentTimeDisplay: StateFlow<String> = _currentTimeDisplay.asStateFlow()

    private val _timerState = MutableStateFlow(TimerState.IDLE)
    val timerState: StateFlow<TimerState> = _timerState.asStateFlow()

    private val _currentSessionType = MutableStateFlow(SessionType.WORK)
    val currentSessionType: StateFlow<SessionType> = _currentSessionType.asStateFlow()

    private val _progress = MutableStateFlow(1f)
    val progress: StateFlow<Float> = _progress.asStateFlow()

    private val _oneShotEvent = MutableSharedFlow<TimerEvent>()
    val oneShotEvent: SharedFlow<TimerEvent> = _oneShotEvent.asSharedFlow()

    sealed class TimerEvent {
        object PlaySound : TimerEvent()
        object Vibrate : TimerEvent()
    }

    // --- Internal Timer Logic ---
    private var job: Job? = null
    private var remainingTimeMillis = workDurationMillis

    init {
        resetTimerToCurrentSessionType()
    }


    fun startPauseTimer() {
        when (_timerState.value) {
            TimerState.IDLE, TimerState.PAUSED, TimerState.FINISHED -> startTimer()
            TimerState.RUNNING -> pauseTimer()
        }
    }

    private fun startTimer() {
        job?.cancel()
        Log.d("TimerVM", "START_TIMER called by ${this.hashCode()}")
        _timerState.value = TimerState.RUNNING
        job = viewModelScope.launch(Dispatchers.Default) {
            val startTime = System.currentTimeMillis()
            val initialRemainingTime = remainingTimeMillis

            while (isActive && remainingTimeMillis > 0) {
                delay(50L)
                val elapsedSinceStart = System.currentTimeMillis() - startTime
                remainingTimeMillis = initialRemainingTime - elapsedSinceStart

                if (remainingTimeMillis < 0) remainingTimeMillis = 0

                _currentTimeDisplay.value = formatMillis(remainingTimeMillis)
                val totalDuration = if (_currentSessionType.value == SessionType.WORK) workDurationMillis else breakDurationMillis
                _progress.value = if (totalDuration > 0) remainingTimeMillis.toFloat() / totalDuration.toFloat() else 0f
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
        remainingTimeMillis = if (_currentSessionType.value == SessionType.WORK) {
            workDurationMillis
        } else {
            breakDurationMillis
        }
        _currentTimeDisplay.value = formatMillis(remainingTimeMillis)
        _progress.value = 1f
    }

    private fun resetTimerToCurrentSessionType() {
        job?.cancel()
        _timerState.value = TimerState.IDLE
        remainingTimeMillis = if (_currentSessionType.value == SessionType.WORK) {
            workDurationMillis
        } else {
            breakDurationMillis
        }
        _currentTimeDisplay.value = formatMillis(remainingTimeMillis)
        _progress.value = 1f
    }


    private fun onTimerFinished() {
        viewModelScope.launch {
            _oneShotEvent.emit(TimerEvent.PlaySound)
            _oneShotEvent.emit(TimerEvent.Vibrate)
        }
        _timerState.value = TimerState.FINISHED

        // Switch session type automatically
        if (_currentSessionType.value == SessionType.WORK) {
            _currentSessionType.value = SessionType.BREAK
        } else {
            _currentSessionType.value = SessionType.WORK
        }
        resetTimerToCurrentSessionType()
    }

    fun switchToNextSessionType() {
        job?.cancel()

        _currentSessionType.value = if (_currentSessionType.value == SessionType.WORK) {
            SessionType.BREAK
        } else {
            SessionType.WORK
        }
        resetTimerToCurrentSessionType()
    }

    private fun formatMillis(millis: Long): String {
        val totalSeconds = millis / 1000
        val minutes = totalSeconds / 60
        val seconds = totalSeconds % 60
        return String.format("%02d:%02d", minutes, seconds)
    }

    fun setWorkDuration(minutes: Int) {
        workDurationMillis = minutes * 60 * 1000L
        if (_currentSessionType.value == SessionType.WORK && _timerState.value != TimerState.RUNNING) {
            resetTimerToCurrentSessionType()
        }
    }

    fun setBreakDuration(minutes: Int) {
        breakDurationMillis = minutes * 60 * 1000L
        if (_currentSessionType.value == SessionType.BREAK && _timerState.value != TimerState.RUNNING) {
            resetTimerToCurrentSessionType()
        }
    }

    override fun onCleared() {
        super.onCleared()
        Log.d("TimerVM", "ON_CLEARED: ${this.hashCode()}")
        job?.cancel()
    }
}