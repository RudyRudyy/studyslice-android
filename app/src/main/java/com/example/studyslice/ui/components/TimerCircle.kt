package com.example.studyslice.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.studyslice.ui.theme.PrimaryRed
import com.example.studyslice.ui.theme.AccentSage
import com.example.studyslice.ui.theme.StudySliceTheme

@Composable
fun TimerCircle(
    modifier: Modifier = Modifier,
    progress: Float = 1f, // 1f is full, 0f is empty
    timeText: String = "25:00",
    isWorkSession: Boolean = true,
    strokeWidth: Float = 20f
) {
    // Animate the progress change
    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        animationSpec = tween(durationMillis = 300),
        label = "progressAnimation"
    )

    // Determine colors based on session type
    val progressColor = if (isWorkSession) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary
    val backgroundColor = if (isWorkSession)
        MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
    else
        MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.3f)

    Box(
        modifier = modifier.size(280.dp),
        contentAlignment = Alignment.Center
    ) {
        // Background and progress circles
        Canvas(modifier = Modifier.size(280.dp)) {
            // Background circle
            drawArc(
                color = backgroundColor,
                startAngle = -90f,
                sweepAngle = 360f,
                useCenter = false,
                topLeft = Offset(strokeWidth, strokeWidth),
                size = Size(size.width - strokeWidth * 2, size.height - strokeWidth * 2),
                style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
            )

            // Progress arc
            drawArc(
                color = progressColor,
                startAngle = -90f,
                sweepAngle = 360f * animatedProgress,
                useCenter = false,
                topLeft = Offset(strokeWidth, strokeWidth),
                size = Size(size.width - strokeWidth * 2, size.height - strokeWidth * 2),
                style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
            )
        }

        // Timer text
        Text(
            text = timeText,
            style = MaterialTheme.typography.displayLarge,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
    }
}

@Preview
@Composable
fun TimerCirclePreview() {
    StudySliceTheme {
        TimerCircle(progress = 0.75f, isWorkSession = true)
    }
}

@Preview
@Composable
fun BreakTimerCirclePreview() {
    StudySliceTheme {
        TimerCircle(progress = 0.5f, timeText = "05:00", isWorkSession = false)
    }
}