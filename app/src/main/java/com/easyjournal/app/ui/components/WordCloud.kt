package com.easyjournal.app.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.easyjournal.app.ui.state.WordCloudItem
import com.easyjournal.app.ui.theme.AccentBlue
import com.easyjournal.app.ui.theme.AccentCoral
import com.easyjournal.app.ui.theme.AccentLavender
import com.easyjournal.app.ui.theme.AccentMint
import com.easyjournal.app.ui.theme.AccentPeach

@Composable
fun WordCloud(
    wordCloudData: List<WordCloudItem>,
    modifier: Modifier = Modifier
) {
    if (wordCloudData.isEmpty()) {
        Box(
            modifier = modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "No word cloud data available",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        return
    }

    Column(modifier = modifier) {
        Text(
            text = "Common Themes",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurface
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            items(wordCloudData.take(15)) { item ->
                WordCloudItem(
                    word = item.word,
                    size = item.size,
                    frequency = item.frequency
                )
            }
        }
    }
}

@Composable
private fun WordCloudItem(
    word: String,
    size: Float,
    frequency: Int
) {
    val colors = listOf(AccentBlue, AccentLavender, AccentMint, AccentPeach, AccentCoral)
    val color = colors[frequency % colors.size]
    
    Text(
        text = word,
        fontSize = (12 + size * 4).sp,
        fontWeight = FontWeight.Medium,
        color = color,
        modifier = Modifier.padding(vertical = 2.dp)
    )
} 