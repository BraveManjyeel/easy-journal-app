package com.easyjournal.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.easyjournal.app.ui.screens.DailyReflectionScreen
import com.easyjournal.app.ui.screens.MonthlySummaryScreen
import com.easyjournal.app.ui.theme.EasyJournalTheme
import com.easyjournal.app.ui.viewmodel.JournalViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            EasyJournalTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    EasyJournalApp()
                }
            }
        }
    }
}

@Composable
fun EasyJournalApp() {
    val navController = rememberNavController()
    val viewModel: JournalViewModel = hiltViewModel()
    val uiState by viewModel.uiState.collectAsState()

    // Handle navigation to summary screen
    LaunchedEffect(uiState.shouldNavigateToSummary) {
        if (uiState.shouldNavigateToSummary) {
            navController.navigate("monthly_summary")
            // Reset the navigation flag
            viewModel.handleEvent(JournalEvent.ClearNavigationFlag)
        }
    }

    NavHost(
        navController = navController,
        startDestination = "daily_reflection"
    ) {
        composable("daily_reflection") {
            DailyReflectionScreen(
                uiState = uiState,
                onEvent = viewModel::handleEvent,
                modifier = Modifier.fillMaxSize()
            )
        }
        
        composable("monthly_summary") {
            MonthlySummaryScreen(
                uiState = uiState,
                onEvent = viewModel::handleEvent,
                onBackClick = { navController.popBackStack() },
                modifier = Modifier.fillMaxSize()
            )
        }
    }
} 