package com.easyjournal.app.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.easyjournal.app.data.model.JournalEntry
import com.easyjournal.app.data.model.Mood
import com.easyjournal.app.data.repository.JournalRepository
import com.easyjournal.app.ui.state.JournalEvent
import com.easyjournal.app.ui.state.JournalUiState
import com.easyjournal.app.ui.state.MoodDataPoint
import com.easyjournal.app.ui.state.WordCloudItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class JournalViewModel @Inject constructor(
    private val repository: JournalRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(JournalUiState())
    val uiState: StateFlow<JournalUiState> = _uiState.asStateFlow()

    private val questions = listOf(
        "What made you smile today?",
        "What's one thing you're grateful for?",
        "What challenged you today?",
        "How are you feeling right now?",
        "What would make tomorrow better?",
        "What's something you learned today?",
        "What's your biggest win today?",
        "What's something you're looking forward to?",
        "What's one thing you'd like to improve?",
        "What made you feel proud today?",
        "What's something that made you laugh?",
        "What's your biggest worry right now?",
        "What's something you're excited about?",
        "What's one thing you're proud of?",
        "What's something you're curious about?",
        "What's your biggest goal right now?",
        "What's something that inspired you?",
        "What's one thing you'd like to change?",
        "What's something you're thankful for?",
        "What's your biggest fear?",
        "What's something you're passionate about?",
        "What's one thing you'd like to learn?",
        "What's something that made you think?",
        "What's your biggest dream?",
        "What's something you're working on?",
        "What's one thing you'd like to achieve?",
        "What's something that made you happy?",
        "What's your biggest strength?",
        "What's something you're hopeful about?",
        "What's one thing you'd like to remember?"
    )

    init {
        loadEntries()
        setCurrentQuestion()
    }

    fun handleEvent(event: JournalEvent) {
        when (event) {
            is JournalEvent.SaveEntry -> saveEntry(event.question, event.answer)
            is JournalEvent.UpdateEntry -> updateEntry(event.entry)
            is JournalEvent.DeleteEntry -> deleteEntry(event.entry)
            JournalEvent.LoadEntries -> loadEntries()
            JournalEvent.LoadMonthlySummary -> loadMonthlySummary()
            JournalEvent.ClearError -> clearError()
            JournalEvent.ClearNavigationFlag -> clearNavigationFlag()
            is JournalEvent.SetCurrentAnswer -> setCurrentAnswer(event.answer)
            is JournalEvent.SetCurrentQuestion -> setCurrentQuestion(event.question)
            JournalEvent.ToggleEditMode -> toggleEditMode()
        }
    }

    private fun loadEntries() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                repository.getAllEntries().collect { entries ->
                    _uiState.update { 
                        it.copy(
                            entries = entries,
                            isLoading = false,
                            error = null
                        )
                    }
                }
            } catch (e: Exception) {
                _uiState.update { 
                    it.copy(
                        isLoading = false,
                        error = "Failed to load entries: ${e.message}"
                    )
                }
            }
        }
    }

    private fun saveEntry(question: String, answer: String) {
        if (answer.isBlank()) return

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                val today = LocalDate.now()
                val mood = repository.analyzeSentiment(answer)
                val entry = JournalEntry(
                    date = today,
                    question = question,
                    answer = answer,
                    mood = mood
                )
                
                repository.saveEntry(entry)
                _uiState.update { 
                    it.copy(
                        isLoading = false,
                        currentAnswer = "",
                        error = null
                    )
                }
                setCurrentQuestion()
            } catch (e: Exception) {
                _uiState.update { 
                    it.copy(
                        isLoading = false,
                        error = "Failed to save entry: ${e.message}"
                    )
                }
            }
        }
    }

    private fun updateEntry(entry: JournalEntry) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                repository.updateEntry(entry)
                _uiState.update { 
                    it.copy(
                        isLoading = false,
                        error = null
                    )
                }
            } catch (e: Exception) {
                _uiState.update { 
                    it.copy(
                        isLoading = false,
                        error = "Failed to update entry: ${e.message}"
                    )
                }
            }
        }
    }

    private fun deleteEntry(entry: JournalEntry) {
        viewModelScope.launch {
            try {
                repository.deleteEntry(entry)
            } catch (e: Exception) {
                _uiState.update { 
                    it.copy(error = "Failed to delete entry: ${e.message}")
                }
            }
        }
    }

    private fun loadMonthlySummary() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                val entries = repository.getLast30Entries()
                if (entries.size >= 30) {
                    val summary = repository.generateMonthlySummary(entries)
                    val moodData = generateMoodData(entries)
                    val wordCloudData = generateWordCloudData(entries)
                    
                    _uiState.update { 
                        it.copy(
                            monthlySummary = summary,
                            moodData = moodData,
                            wordCloudData = wordCloudData,
                            showSummary = true,
                            shouldNavigateToSummary = true,
                            isLoading = false,
                            error = null
                        )
                    }
                } else {
                    _uiState.update { 
                        it.copy(
                            isLoading = false,
                            error = "Need 30 entries to generate summary"
                        )
                    }
                }
            } catch (e: Exception) {
                _uiState.update { 
                    it.copy(
                        isLoading = false,
                        error = "Failed to load summary: ${e.message}"
                    )
                }
            }
        }
    }

    private fun generateMoodData(entries: List<JournalEntry>): List<MoodDataPoint> {
        val formatter = DateTimeFormatter.ofPattern("MM/dd")
        return entries.map { entry ->
            val moodValue = when (entry.mood) {
                Mood.POSITIVE -> 1.0f
                Mood.NEUTRAL -> 0.5f
                Mood.NEGATIVE -> 0.0f
            }
            MoodDataPoint(
                date = entry.date.format(formatter),
                mood = entry.mood,
                value = moodValue
            )
        }
    }

    private fun generateWordCloudData(entries: List<JournalEntry>): List<WordCloudItem> {
        val stopWords = setOf(
            "the", "a", "an", "and", "or", "but", "in", "on", "at", "to", "for", "of", "with", "by",
            "is", "are", "was", "were", "be", "been", "being", "have", "has", "had", "do", "does", "did",
            "will", "would", "could", "should", "may", "might", "must", "can", "this", "that", "these", "those",
            "i", "you", "he", "she", "it", "we", "they", "me", "him", "her", "us", "them", "my", "your", "his", "her", "its", "our", "their"
        )
        
        val wordCount = mutableMapOf<String, Int>()
        entries.forEach { entry ->
            entry.answer.lowercase()
                .split(Regex("\\s+"))
                .filter { it.length > 2 && it !in stopWords }
                .forEach { word ->
                    wordCount[word] = wordCount.getOrDefault(word, 0) + 1
                }
        }
        
        val maxCount = wordCount.values.maxOrNull() ?: 1
        return wordCount.entries
            .sortedByDescending { it.value }
            .take(20)
            .map { (word, count) ->
                WordCloudItem(
                    word = word,
                    frequency = count,
                    size = (count.toFloat() / maxCount) * 3 + 1
                )
            }
    }

    private fun setCurrentQuestion() {
        val today = LocalDate.now()
        val dayOfYear = today.dayOfYear
        val questionIndex = (dayOfYear - 1) % questions.size
        _uiState.update { it.copy(currentQuestion = questions[questionIndex]) }
    }

    private fun setCurrentQuestion(question: String) {
        _uiState.update { it.copy(currentQuestion = question) }
    }

    private fun setCurrentAnswer(answer: String) {
        _uiState.update { it.copy(currentAnswer = answer) }
    }

    private fun clearError() {
        _uiState.update { it.copy(error = null) }
    }

    private fun toggleEditMode() {
        _uiState.update { it.copy(isEditing = !it.isEditing) }
    }

    private fun clearNavigationFlag() {
        _uiState.update { it.copy(shouldNavigateToSummary = false) }
    }
} 