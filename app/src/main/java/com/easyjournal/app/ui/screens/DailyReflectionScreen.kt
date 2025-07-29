package com.easyjournal.app.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Insights
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.easyjournal.app.ui.components.GradientCard
import com.easyjournal.app.ui.components.PeacefulCard
import com.easyjournal.app.ui.state.JournalEvent
import com.easyjournal.app.ui.state.JournalUiState
import com.easyjournal.app.ui.theme.AccentLavender
import com.easyjournal.app.ui.theme.AccentMint
import com.easyjournal.app.ui.theme.AccentPeach
import com.google.firebase.analytics.FirebaseAnalytics
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DailyReflectionScreen(
    uiState: JournalUiState,
    onEvent: (JournalEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val focusRequester = remember { FocusRequester() }
    val scrollState = rememberScrollState()
    
    val affirmations = listOf(
        stringResource(com.easyjournal.app.R.string.affirmation_1),
        stringResource(com.easyjournal.app.R.string.affirmation_2),
        stringResource(com.easyjournal.app.R.string.affirmation_3),
        stringResource(com.easyjournal.app.R.string.affirmation_4),
        stringResource(com.easyjournal.app.R.string.affirmation_5),
        stringResource(com.easyjournal.app.R.string.affirmation_6),
        stringResource(com.easyjournal.app.R.string.affirmation_7),
        stringResource(com.easyjournal.app.R.string.affirmation_8),
        stringResource(com.easyjournal.app.R.string.affirmation_9),
        stringResource(com.easyjournal.app.R.string.affirmation_10)
    )
    
    val currentAffirmation = remember {
        affirmations[LocalDate.now().dayOfYear % affirmations.size]
    }

    // Track screen view
    LaunchedEffect(Unit) {
        FirebaseAnalytics.getInstance(context).logEvent("screen_view") {
            param("screen_name", "daily_reflection")
            param("screen_class", "DailyReflectionScreen")
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Header
        AnimatedVisibility(
            visible = true,
            enter = fadeIn(animationSpec = tween(1000)) + slideInVertically(
                animationSpec = tween(1000),
                initialOffsetY = { -it }
            )
        ) {
            Text(
                text = "Easy",
                style = MaterialTheme.typography.displayMedium,
                fontWeight = FontWeight.Light,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center
            )
        }
        
        Spacer(modifier = Modifier.height(8.dp))
        
        // Date
        AnimatedVisibility(
            visible = true,
            enter = fadeIn(animationSpec = tween(1000, delayMillis = 300)) + slideInVertically(
                animationSpec = tween(1000, delayMillis = 300),
                initialOffsetY = { -it }
            )
        ) {
            Text(
                text = LocalDate.now().format(DateTimeFormatter.ofPattern("EEEE, MMMM d")),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
        }
        
        Spacer(modifier = Modifier.height(32.dp))
        
        // Question Card
        AnimatedVisibility(
            visible = true,
            enter = fadeIn(animationSpec = tween(1000, delayMillis = 600)) + slideInVertically(
                animationSpec = tween(1000, delayMillis = 600),
                initialOffsetY = { it }
            )
        ) {
            PeacefulCard(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Today's Reflection",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    Text(
                        text = uiState.currentQuestion,
                        style = MaterialTheme.typography.bodyLarge,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        // Answer Input
        AnimatedVisibility(
            visible = true,
            enter = fadeIn(animationSpec = tween(1000, delayMillis = 900)) + slideInVertically(
                animationSpec = tween(1000, delayMillis = 900),
                initialOffsetY = { it }
            )
        ) {
            PeacefulCard(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column {
                    Text(
                        text = "Your Reflection",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    OutlinedTextField(
                        value = uiState.currentAnswer,
                        onValueChange = { onEvent(JournalEvent.SetCurrentAnswer(it)) },
                        placeholder = {
                            Text(
                                text = stringResource(com.easyjournal.app.R.string.hint_answer),
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .focusRequester(focusRequester),
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                        keyboardActions = KeyboardActions(
                            onDone = {
                                if (uiState.currentAnswer.isNotBlank()) {
                                    // Track save attempt
                                    FirebaseAnalytics.getInstance(context).logEvent("save_attempted", null)
                                    onEvent(JournalEvent.SaveEntry(uiState.currentQuestion, uiState.currentAnswer))
                                }
                            }
                        ),
                        maxLines = 3,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = AccentLavender,
                            unfocusedBorderColor = MaterialTheme.colorScheme.outline
                        )
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "${uiState.currentAnswer.length}/160",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        
                        Button(
                            onClick = {
                                if (uiState.currentAnswer.isNotBlank()) {
                                    // Track save button click
                                    FirebaseAnalytics.getInstance(context).logEvent("save_button_clicked", null)
                                    onEvent(JournalEvent.SaveEntry(uiState.currentQuestion, uiState.currentAnswer))
                                }
                            },
                            enabled = uiState.currentAnswer.isNotBlank() && !uiState.isLoading,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = AccentMint
                            )
                        ) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = stringResource(com.easyjournal.app.R.string.btn_save),
                                style = MaterialTheme.typography.labelLarge
                            )
                        }
                    }
                }
            }
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        // Affirmation
        AnimatedVisibility(
            visible = true,
            enter = fadeIn(animationSpec = tween(1000, delayMillis = 1200)) + slideInVertically(
                animationSpec = tween(1000, delayMillis = 1200),
                initialOffsetY = { it }
            )
        ) {
            GradientCard(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = currentAffirmation,
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.Medium
                )
            }
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        // Summary Button
        AnimatedVisibility(
            visible = uiState.entries.isNotEmpty(),
            enter = fadeIn(animationSpec = tween(1000, delayMillis = 1500)) + slideInVertically(
                animationSpec = tween(1000, delayMillis = 1500),
                initialOffsetY = { it }
            )
        ) {
            OutlinedButton(
                onClick = { 
                    // Track summary button click
                    FirebaseAnalytics.getInstance(context).logEvent("summary_button_clicked", null)
                    onEvent(JournalEvent.LoadMonthlySummary)
                    // Navigate to summary screen - this would need to be handled by the parent
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = AccentPeach
                )
            ) {
                Icon(
                    imageVector = Icons.Default.Insights,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = stringResource(com.easyjournal.app.R.string.btn_view_summary),
                    style = MaterialTheme.typography.labelLarge
                )
            }
        }
        
        // Loading indicator
        if (uiState.isLoading) {
            Spacer(modifier = Modifier.height(16.dp))
            CircularProgressIndicator(
                color = AccentLavender,
                modifier = Modifier.size(32.dp)
            )
        }
        
        // Error message
        uiState.error?.let { error ->
            Spacer(modifier = Modifier.height(16.dp))
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
    }
    
    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }
} 