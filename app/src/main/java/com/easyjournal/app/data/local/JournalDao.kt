package com.easyjournal.app.data.local

import androidx.room.*
import com.easyjournal.app.data.model.JournalEntry
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

@Dao
interface JournalDao {
    @Query("SELECT * FROM journal_entries ORDER BY date DESC")
    fun getAllEntries(): Flow<List<JournalEntry>>

    @Query("SELECT * FROM journal_entries WHERE date = :date")
    suspend fun getEntryByDate(date: LocalDate): JournalEntry?

    @Query("SELECT * FROM journal_entries ORDER BY date DESC LIMIT 30")
    suspend fun getLast30Entries(): List<JournalEntry>

    @Query("SELECT COUNT(*) FROM journal_entries")
    suspend fun getEntryCount(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEntry(entry: JournalEntry)

    @Update
    suspend fun updateEntry(entry: JournalEntry)

    @Delete
    suspend fun deleteEntry(entry: JournalEntry)

    @Query("DELETE FROM journal_entries WHERE date = :date")
    suspend fun deleteEntryByDate(date: LocalDate)

    @Query("SELECT * FROM journal_entries WHERE date >= :startDate AND date <= :endDate ORDER BY date")
    suspend fun getEntriesInRange(startDate: LocalDate, endDate: LocalDate): List<JournalEntry>
} 