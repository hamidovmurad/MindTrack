# MindTrack - Mental Health Companion

A multiplatform mental healthcare app built with **Kotlin Multiplatform (KMP)** and **Jetpack Compose**, supporting both Android and iOS from a single codebase.

## Features

- **Mood Tracking** - Daily mood logging with insights
- **Habit Builder** - Create and track daily routines
- **Dashboard** - Centralized view of mood trends and habits
- **Medication Reminders** - Schedule and track medication intake
- **Nutrition & Hydration Tracker** - Set daily goals and track progress
- **AI Health Assistant** - Chat-based health guidance (coming soon)
- **Settings Hub** - Profile management, emergency features, terms & policy, FAQ

## Tech Stack

- **Language**: Kotlin (Multiplatform)
- **UI Framework**: Jetpack Compose Multiplatform
- **Architecture**: MVI/MVVM with Composables
- **Backend**: Firebase (Auth + Firestore) - *In progress*
- **Database**: Local SQLite + Firebase Firestore
- **Build System**: Gradle with Kotlin DSL
- **Platforms**: Android 8.0+ | iOS 14+

## Prerequisites

- **JDK 17+**
- **Android Studio** (latest)
- **Xcode 14+** (for iOS development)
- **Gradle 8.7+**

## Getting Started

### 1. Clone the Repository
```bash
git clone https://github.com/yourusername/MindTrack.git
cd MindTrack
```

### 2. Android Setup
```bash
# Build and run on Android emulator or device
./gradlew composeApp:assembleDebug

# Or run directly
./gradlew composeApp:installDebug
```

### 3. iOS Setup
```bash
# Navigate to iOS app
cd iosApp

# Install dependencies
pod install

# Open in Xcode
open iosApp.xcworkspace

# Build & run (Xcode or command line)
xcodebuild -workspace iosApp.xcworkspace -scheme iosApp -configuration Debug
```

### 4. Firebase Configuration (Optional)
Download `google-services.json` from Firebase Console and place in:
```
composeApp/src/androidMain/google-services.json
```

For iOS, download `GoogleService-Info.plist` and add to Xcode project.
See `FIREBASE_SETUP.md` for detailed instructions.

## Project Structure

```
MindTrack/
├── composeApp/              # Shared Compose UI + Android/iOS entry points
│   └── src/
│       ├── commonMain/      # Shared Kotlin code & UI screens
│       ├── androidMain/     # Android-specific implementations
│       └── iosMain/         # iOS-specific implementations
├── server/                  # Backend server (optional)
├── shared/                  # Shared business logic & models
└── docs/                    # Project documentation
```

## Development

### Running Tests
```bash
./gradlew test
```

### Building for Production
```bash
# Android AAB
./gradlew composeApp:bundleRelease

# iOS Archive
cd iosApp && xcodebuild -workspace iosApp.xcworkspace -scheme iosApp -configuration Release -archivePath build/MindTrack.xcarchive archive
```

## MVP Status

- ✅ Core UI screens (Dashboard, Habits, AI Assistant, Nutrition, Settings)
- ✅ Bottom navigation menu
- ✅ Material3 theming with brand colors
- ✅ Drawable icon system
- 🔄 Firebase Auth integration (in progress)
- 🔄 Local data persistence with SQLite
- ⏳ AI Assistant backend (Phase 1)
- ⏳ Synchronization & offline support (Phase 2)

## Contributing

1. Create a feature branch: `git checkout -b feature/your-feature`
2. Commit changes: `git commit -m "Add your feature"`
3. Push to branch: `git push origin feature/your-feature`
4. Open a Pull Request

## License

This project is licensed under the MIT License - see the LICENSE file for details.

## Support

For issues, questions, or suggestions, please open an issue on GitHub or contact the development team.

---

**Built with ❤️ for mental wellness** | University Student Project

