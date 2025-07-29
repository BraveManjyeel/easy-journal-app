package com.easyjournal.app.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.easyjournal.app.data.model.Mood
import com.easyjournal.app.ui.state.MoodDataPoint
import com.easyjournal.app.ui.theme.MoodNegative
import com.easyjournal.app.ui.theme.MoodNeutral
import com.easyjournal.app.ui.theme.MoodPositive

@Composable
fun MoodGraph(
    moodData: List<MoodDataPoint>,
    modifier: Modifier = Modifier
) {
    if (moodData.isEmpty()) {
        Box(
            modifier = modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "No mood data available",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        return
    }

    Column(modifier = modifier) {
        Text(
            text = "Your Mood Journey",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurface
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
        ) {
            val width = size.width
            val height = size.height
            val padding = 40f
            val graphWidth = width - 2 * padding
            val graphHeight = height - 2 * padding
            
            // Draw grid lines
            val gridColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.2f)
            val strokeWidth = 1f
            
            // Horizontal grid lines
            for (i in 0..3) {
                val y = padding + (graphHeight / 3) * i
                drawLine(
                    color = gridColor,
                    start = Offset(padding, y),
                    end = Offset(width - padding, y),
                    strokeWidth = strokeWidth
                )
            }
            
            // Vertical grid lines (every 5 days)
            val step = graphWidth / (moodData.size - 1)
            for (i in moodData.indices step 5) {
                val x = padding + step * i
                drawLine(
                    color = gridColor,
                    start = Offset(x, padding),
                    end = Offset(x, height - padding),
                    strokeWidth = strokeWidth
                )
            }
            
            // Draw mood line
            if (moodData.size > 1) {
                val path = Path()
                val pointStep = graphWidth / (moodData.size - 1)
                
                moodData.forEachIndexed { index, dataPoint ->
                    val x = padding + pointStep * index
                    val y = padding + (1f - dataPoint.value) * graphHeight
                    
                    if (index == 0) {
                        path.moveTo(x, y)
                    } else {
                        path.lineTo(x, y)
                    }
                }
                
                // Draw the line
                drawPath(
                    path = path,
                    color = MaterialTheme.colorScheme.primary,
                    style = Stroke(width = 3f)
                )
                
                // Draw points
                moodData.forEachIndexed { index, dataPoint ->
                    val x = padding + pointStep * index
                    val y = padding + (1f - dataPoint.value) * graphHeight
                    val pointColor = when (dataPoint.mood) {
                        Mood.POSITIVE -> MoodPositive
                        Mood.NEUTRAL -> MoodNeutral
                        Mood.NEGATIVE -> MoodNegative
                    }
                    
                    drawCircle(
                        color = pointColor,
                        radius = 6f,
                        center = Offset(x, y)
                    )
                }
            }
        }
        
        Spacer(modifier = Modifier.height(8.dp))
        
        // Legend
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            MoodLegendItem("😊", "Positive", MoodPositive)
            MoodLegendItem("😐", "Neutral", MoodNeutral)
            MoodLegendItem("😔", "Negative", MoodNegative)
        }
    }
}

@Composable
private fun MoodLegendItem(
    emoji: String,
    label: String,
    color: Color
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(text = emoji, style = MaterialTheme.typography.bodyMedium)
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
} 