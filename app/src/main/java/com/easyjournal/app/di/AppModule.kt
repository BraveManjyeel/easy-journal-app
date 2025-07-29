package com.easyjournal.app.di

import android.content.Context
import com.easyjournal.app.data.local.AppDatabase
import com.easyjournal.app.data.local.JournalDao
import com.easyjournal.app.data.remote.GeminiAIService
import com.easyjournal.app.data.repository.JournalRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return AppDatabase.getDatabase(context)
    }

    @Provides
    @Singleton
    fun provideJournalDao(database: AppDatabase): JournalDao {
        return database.journalDao()
    }

    @Provides
    @Singleton
    fun provideGeminiAIService(): GeminiAIService {
        return GeminiAIService()
    }

    @Provides
    @Singleton
    fun provideJournalRepository(
        journalDao: JournalDao,
        geminiAIService: GeminiAIService
    ): JournalRepository {
        return JournalRepository(journalDao, geminiAIService)
    }
} 