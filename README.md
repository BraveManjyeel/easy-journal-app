# Easy Journal App

A minimal, peaceful, and aesthetic Android journaling app that helps users reflect daily by asking one reflective question per day, stores one short sentence answer, and after 30 days provides an AI-based monthly summary using Google's Gemini AI.

## Features

### 🧘‍♀️ Aesthetic and Peaceful UI/UX
- Soft pastel color palette (light blues, lavender, soft beige)
- Smooth transitions and animated question fade-in
- Rounded cards with gentle shadows
- Calming fonts and peaceful background gradients
- Time-of-day dependent background themes

### 📅 Daily Reflection Flow
- One reflective question per day (30 different questions)
- Short sentence answers (max 160 characters)
- Local storage with Room database
- Overwrite previous entries for the same day
- Soft haptic feedback on submission

### 🧠 30-Day AI Summary
- **Mood Graph**: Sentiment analysis with visual line graph
- **Word Cloud**: Common themes from all entries
- **AI-Generated Summary**: Personalized monthly reflection using Google's Gemini AI

### 🛠️ Technical Features
- Offline-first design (except AI summary)
- No login required
- Export functionality (PDF/image)
- Positive affirmations displayed daily

## Tech Stack

- **UI**: Jetpack Compose with Material3
- **Architecture**: MVVM with Clean Architecture
- **Database**: Room with Kotlin Coroutines
- **Dependency Injection**: Hilt
- **AI**: Google Gemini AI (free tier)
- **Navigation**: Compose Navigation
- **State Management**: StateFlow

## Project Structure

```
app/src/main/java/com/easyjournal/app/
├── data/
│   ├── local/
│   │   ├── AppDatabase.kt
│   │   ├── JournalDao.kt
│   │   └── Converters.kt
│   ├── model/
│   │   └── JournalEntry.kt
│   ├── remote/
│   │   └── GeminiAIService.kt
│   └── repository/
│       └── JournalRepository.kt
├── di/
│   └── AppModule.kt
├── ui/
│   ├── components/
│   │   ├── PeacefulCard.kt
│   │   ├── MoodGraph.kt
│   │   └── WordCloud.kt
│   ├── screens/
│   │   ├── DailyReflectionScreen.kt
│   │   └── MonthlySummaryScreen.kt
│   ├── state/
│   │   └── JournalUiState.kt
│   ├── theme/
│   │   ├── Color.kt
│   │   ├── Theme.kt
│   │   └── Type.kt
│   └── viewmodel/
│       └── JournalViewModel.kt
├── EasyJournalApplication.kt
└── MainActivity.kt
```

## Setup Instructions

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd easy-journal-app
   ```

2. **Open in Android Studio**
   - Open Android Studio
   - Select "Open an existing Android Studio project"
   - Navigate to the project directory and select it

3. **Configure Gemini AI API**
   - The Gemini API key is already configured in the code
   - For production, replace the API key in `GeminiAIService.kt`

4. **Build and Run**
   - Connect an Android device or start an emulator
   - Click "Run" in Android Studio
   - The app will install and launch on your device

## Configuration

### Gemini AI API Setup
The app uses Google's Gemini Pro model for generating monthly summaries. To configure:

1. Get an API key from [Google AI Studio](https://makersuite.google.com/app/apikey)
2. The API key is automatically read from environment variables
3. For cloud deployment, add `GEMINI_API_KEY` to GitHub secrets

### Firebase Setup (Optional)
For cloud sync functionality:

1. Create a Firebase project
2. Download `google-services.json` to `app/`
3. Enable Firestore in Firebase Console
4. Update Firebase rules for security

## Features in Detail

### Daily Questions
The app cycles through 30 different reflective questions:
- "What made you smile today?"
- "What's one thing you're grateful for?"
- "What challenged you today?"
- And 27 more...

### Sentiment Analysis
Simple keyword-based sentiment analysis:
- **Positive words**: happy, joy, excited, grateful, etc.
- **Negative words**: sad, angry, frustrated, worried, etc.
- **Neutral**: Default when no clear sentiment detected

### Mood Graph
- Visual representation of daily mood over 30 days
- Color-coded points (green=positive, blue=neutral, red=negative)
- Smooth line connecting daily moods

### Word Cloud
- Extracts common words from all entries
- Filters out stop words
- Size indicates frequency
- Color-coded for visual appeal

### AI Summary
- Uses Google's Gemini Pro model
- Analyzes 30 days of entries
- Generates warm, supportive monthly reflection
- Highlights patterns and growth

## Architecture

The app follows Clean Architecture principles:

- **Presentation Layer**: Compose UI, ViewModels
- **Domain Layer**: Use cases, repositories
- **Data Layer**: Room database, AI services

### Key Design Patterns
- **MVVM**: ViewModels manage UI state
- **Repository Pattern**: Abstracts data sources
- **Dependency Injection**: Hilt for DI
- **Observer Pattern**: StateFlow for reactive UI

## Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Add tests if applicable
5. Submit a pull request

## License

This project is licensed under the MIT License - see the LICENSE file for details.

## Acknowledgments

- Google for Gemini AI and Jetpack Compose
- Android community for best practices and libraries

## Future Enhancements

- [ ] Dark mode support
- [ ] Cloud sync with Firebase
- [ ] Export to PDF/image
- [ ] Calendar view for past entries
- [ ] Custom questions
- [ ] Reminder notifications
- [ ] Multiple languages
- [ ] Widget support

## Why Gemini AI?

- **Free Tier**: 15 requests per minute, 1500 requests per day
- **Better Performance**: Optimized for mobile apps
- **Google Integration**: Works seamlessly with Android
- **Privacy**: Data stays within Google's infrastructure
- **Cost Effective**: Much cheaper than OpenAI for this use case 