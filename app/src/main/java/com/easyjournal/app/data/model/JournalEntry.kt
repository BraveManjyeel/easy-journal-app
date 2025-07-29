package com.easyjournal.app.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity(tableName = "journal_entries")
data class JournalEntry(
    @PrimaryKey val date: LocalDate,
    val question: String,
    val answer: String,
    val timestamp: Long = System.currentTimeMillis(),
    val mood: Mood = Mood.NEUTRAL
)

enum class Mood {
    POSITIVE, NEUTRAL, NEGATIVE
} 