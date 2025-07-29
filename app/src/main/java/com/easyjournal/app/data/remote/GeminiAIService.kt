package com.easyjournal.app.data.remote

import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GeminiAIService {
    private val model = GenerativeModel(
        modelName = "gemini-pro",
        apiKey = "AIzaSyBDUsrorlx7oI9hQj8dn54ODTWyK0v_uUQ"
    )

    suspend fun generateSummary(prompt: String): String = withContext(Dispatchers.IO) {
        try {
            val response = model.generateContent(
                content {
                    text("You are a supportive and empathetic AI assistant that helps users reflect on their journal entries. Provide warm, encouraging summaries that highlight patterns and growth. Keep the response under 200 words and focus on emotional insights and personal growth themes.\n\nUser's journal entries:\n$prompt")
                }
            )
            response.text ?: "Unable to generate summary."
        } catch (e: Exception) {
            "Unable to generate AI summary at this time. Please try again later."
        }
    }
} 