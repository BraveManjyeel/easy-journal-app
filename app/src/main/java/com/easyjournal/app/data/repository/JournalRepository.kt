package com.easyjournal.app.data.repository

import com.easyjournal.app.data.local.JournalDao
import com.easyjournal.app.data.model.JournalEntry
import com.easyjournal.app.data.model.Mood
import com.easyjournal.app.data.remote.GeminiAIService
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class JournalRepository @Inject constructor(
    private val journalDao: JournalDao,
    private val geminiAIService: GeminiAIService
) {
    fun getAllEntries(): Flow<List<JournalEntry>> = journalDao.getAllEntries()

    suspend fun getEntryByDate(date: LocalDate): JournalEntry? = journalDao.getEntryByDate(date)

    suspend fun saveEntry(entry: JournalEntry) {
        journalDao.insertEntry(entry)
    }

    suspend fun updateEntry(entry: JournalEntry) {
        journalDao.updateEntry(entry)
    }

    suspend fun deleteEntry(entry: JournalEntry) {
        journalDao.deleteEntry(entry)
    }

    suspend fun getLast30Entries(): List<JournalEntry> = journalDao.getLast30Entries()

    suspend fun getEntryCount(): Int = journalDao.getEntryCount()

    suspend fun generateMonthlySummary(entries: List<JournalEntry>): String {
        if (entries.isEmpty()) return "No entries to summarize."
        
        val entriesText = entries.joinToString("\n") { "${it.date}: ${it.answer}" }
        val prompt = "Summarize the emotional and thematic pattern from the following 30 journal entries in a warm, supportive tone:\n\n$entriesText"
        
        return try {
            val summary = geminiAIService.generateSummary(prompt)
            summary
        } catch (e: Exception) {
            "Unable to generate AI summary at this time. Please try again later."
        }
    }

    fun analyzeSentiment(text: String): Mood {
        val positiveWords = setOf(
            "happy", "joy", "excited", "grateful", "blessed", "wonderful", "amazing", "great", "good", "love", "like", "enjoy", "fun", "smile", "laugh", "positive", "optimistic", "hopeful", "proud", "accomplished", "successful", "achieved", "improved", "better", "best", "fantastic", "excellent", "brilliant", "perfect", "beautiful", "peaceful", "calm", "relaxed", "content", "satisfied", "fulfilled", "inspired", "motivated", "energized", "refreshed", "renewed", "blessed", "thankful", "appreciative", "lucky", "fortunate", "privileged"
        )
        
        val negativeWords = setOf(
            "sad", "angry", "frustrated", "disappointed", "worried", "anxious", "stressed", "tired", "exhausted", "overwhelmed", "depressed", "lonely", "isolated", "hurt", "pain", "suffering", "struggling", "difficult", "hard", "challenging", "problem", "issue", "trouble", "bad", "terrible", "awful", "horrible", "miserable", "unhappy", "dissatisfied", "disappointed", "fear", "afraid", "scared", "terrified", "nervous", "tense", "irritated", "annoyed", "upset", "distressed", "hopeless", "helpless", "defeated", "lost", "confused", "uncertain", "doubtful", "skeptical", "cynical", "bitter", "resentful"
        )

        val words = text.lowercase().split(Regex("\\s+"))
        val positiveCount = words.count { it in positiveWords }
        val negativeCount = words.count { it in negativeWords }

        return when {
            positiveCount > negativeCount -> Mood.POSITIVE
            negativeCount > positiveCount -> Mood.NEGATIVE
            else -> Mood.NEUTRAL
        }
    }
} 