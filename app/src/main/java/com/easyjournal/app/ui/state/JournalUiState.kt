package com.easyjournal.app.ui.state

import com.easyjournal.app.data.model.JournalEntry
import com.easyjournal.app.data.model.Mood

data class JournalUiState(
    val entries: List<JournalEntry> = emptyList(),
    val currentEntry: JournalEntry? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
    val showSummary: Boolean = false,
    val monthlySummary: String = "",
    val moodData: List<MoodDataPoint> = emptyList(),
    val wordCloudData: List<WordCloudItem> = emptyList(),
    val currentQuestion: String = "",
    val currentAnswer: String = "",
    val isEditing: Boolean = false,
    val shouldNavigateToSummary: Boolean = false
)

data class MoodDataPoint(
    val date: String,
    val mood: Mood,
    val value: Float
)

data class WordCloudItem(
    val word: String,
    val frequency: Int,
    val size: Float
)

sealed class JournalEvent {
    data class SaveEntry(val question: String, val answer: String) : JournalEvent()
    data class UpdateEntry(val entry: JournalEntry) : JournalEvent()
    data class DeleteEntry(val entry: JournalEntry) : JournalEvent()
    object LoadEntries : JournalEvent()
    object LoadMonthlySummary : JournalEvent()
    object ClearError : JournalEvent()
    object ClearNavigationFlag : JournalEvent()
    data class SetCurrentAnswer(val answer: String) : JournalEvent()
    data class SetCurrentQuestion(val question: String) : JournalEvent()
    object ToggleEditMode : JournalEvent()
} 