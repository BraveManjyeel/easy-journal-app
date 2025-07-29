package com.easyjournal.app.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.easyjournal.app.ui.components.MoodGraph
import com.easyjournal.app.ui.components.PeacefulCard
import com.easyjournal.app.ui.components.WordCloud
import com.easyjournal.app.ui.state.JournalEvent
import com.easyjournal.app.ui.state.JournalUiState
import com.easyjournal.app.ui.theme.AccentLavender
import com.easyjournal.app.ui.theme.AccentMint

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MonthlySummaryScreen(
    uiState: JournalUiState,
    onEvent: (JournalEvent) -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier.fillMaxSize()
    ) {
        // Top App Bar
        TopAppBar(
            title = {
                Text(
                    text = stringResource(com.easyjournal.app.R.string.title_monthly_summary),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.SemiBold
                )
            },
            navigationIcon = {
                IconButton(onClick = onBackClick) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back"
                    )
                }
            },
            actions = {
                IconButton(
                    onClick = { /* TODO: Implement export */ }
                ) {
                    Icon(
                        imageVector = Icons.Default.Share,
                        contentDescription = "Export"
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.surface
            )
        )

        // Content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // Mood Graph
            AnimatedVisibility(
                visible = uiState.moodData.isNotEmpty(),
                enter = fadeIn(animationSpec = tween(1000)) + slideInVertically(
                    animationSpec = tween(1000),
                    initialOffsetY = { it }
                )
            ) {
                PeacefulCard(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    MoodGraph(
                        moodData = uiState.moodData,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            // Word Cloud
            AnimatedVisibility(
                visible = uiState.wordCloudData.isNotEmpty(),
                enter = fadeIn(animationSpec = tween(1000, delayMillis = 300)) + slideInVertically(
                    animationSpec = tween(1000, delayMillis = 300),
                    initialOffsetY = { it }
                )
            ) {
                PeacefulCard(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    WordCloud(
                        wordCloudData = uiState.wordCloudData,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            // AI Summary
            AnimatedVisibility(
                visible = uiState.monthlySummary.isNotEmpty(),
                enter = fadeIn(animationSpec = tween(1000, delayMillis = 600)) + slideInVertically(
                    animationSpec = tween(1000, delayMillis = 600),
                    initialOffsetY = { it }
                )
            ) {
                PeacefulCard(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column {
                        Text(
                            text = stringResource(com.easyjournal.app.R.string.title_ai_summary),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        Text(
                            text = uiState.monthlySummary,
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurface,
                            lineHeight = MaterialTheme.typography.bodyLarge.lineHeight * 1.2f
                        )
                    }
                }
            }

            // Loading state
            if (uiState.isLoading) {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        CircularProgressIndicator(
                            color = AccentLavender,
                            modifier = Modifier.size(48.dp)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = stringResource(com.easyjournal.app.R.string.loading_summary),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }

            // Error state
            uiState.error?.let { error ->
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer
                    )
                ) {
                    Text(
                        text = error,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onErrorContainer,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }

            // Empty state
            if (!uiState.isLoading && uiState.moodData.isEmpty() && uiState.wordCloudData.isEmpty() && uiState.monthlySummary.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = stringResource(com.easyjournal.app.R.string.msg_need_30_days),
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = onBackClick,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = AccentMint
                            )
                        ) {
                            Text(
                                text = stringResource(com.easyjournal.app.R.string.btn_back),
                                style = MaterialTheme.typography.labelLarge
                            )
                        }
                    }
                }
            }
        }
    }
} 