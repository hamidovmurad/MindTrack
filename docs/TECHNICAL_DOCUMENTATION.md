# MindTrack Technical Documentation

## 1. Project Overview

MindTrack is a Kotlin Multiplatform (KMP) university project focused on mental wellness tracking.
It uses a shared Compose Multiplatform UI for Android and iOS.

### MVP Goals
- Daily mood logging
- Habit/routine management
- Dashboard insights
- Basic settings/info screens
- Presentation-mode local authentication

## 2. Architecture

### 2.1 Modules
- `composeApp`: Shared UI + platform entry points
- `shared`: Shared domain models and core logic
- `server`: Optional backend module (not required for MVP demo)

### 2.2 UI & Navigation
- Main navigation: `composeApp/src/commonMain/kotlin/com/app/mindtrack/ui/navigation/Navigation.kt`
- Bottom tabs:
  - Dashboard
  - Habits
  - Assistant
  - Nutrition
  - Settings
- Authentication gate is controlled by local state (`isAuthenticated`) and `LocalAuthManager.getCurrentUser()`.

### 2.3 Design System
- Reusable wellness UI components in `WellnessComponents.kt`
- Material 3 theme with custom wellness palette in:
  - `composeApp/src/commonMain/kotlin/com/app/mindtrack/ui/theme/Color.kt`
  - `composeApp/src/commonMain/kotlin/com/app/mindtrack/ui/theme/Theme.kt`
- Shared drawable resource icons in `ui/resources/Icons.kt`

## 3. Data Layer (MVP Local Persistence)

### 3.1 Local Key-Value Abstraction
- Common interface:
  - `composeApp/src/commonMain/kotlin/com/app/mindtrack/auth/KeyValueStorage.kt`
- Android implementation:
  - `composeApp/src/androidMain/kotlin/com/app/mindtrack/auth/KeyValueStorageAndroid.kt`
  - Backed by `SharedPreferences`
- iOS implementation:
  - `composeApp/src/iosMain/kotlin/com/app/mindtrack/auth/KeyValueStorageIOS.kt`
  - Backed by `NSUserDefaults`

### 3.2 Auth (Presentation Mode)
- Local auth manager:
  - `composeApp/src/commonMain/kotlin/com/app/mindtrack/auth/LocalAuthManager.kt`
- Supports:
  - `register(email, password, name)`
  - `login(email, password)`
  - `logout()` (clears local store)
  - `getCurrentUser()`
  - `updateProfile(email, name)`

### 3.3 Local Data Store
- `composeApp/src/commonMain/kotlin/com/app/mindtrack/storage/LocalDataStore.kt`
- Stores:
  - Mood entries (`MoodEntry`)
  - Habits (`Habit`)
- Mood behavior:
  - `saveDailyMoodEntry(...)` ensures one mood per user per day in MVP flow

## 4. Core User Flows

### 4.1 Login/Register
1. User opens SignIn screen
2. Chooses Local Account mode
3. Registers or logs in
4. On success, navigates to Dashboard
5. Session persists on Android (and on iOS when storage init is called)

### 4.2 Daily Mood Logging
1. Dashboard -> Log Mood
2. User selects mood (pager), optional note/context
3. Save triggers local persistence via `LocalDataStore`
4. Navigation reloads mood list and returns to Dashboard

### 4.3 Habit Creation
1. Habits tab -> Add Habit
2. User enters title/description/frequency
3. Habit saved in local data store
4. Habits screen reloads persisted list

### 4.4 Logout
- Available from Settings and Profile
- Calls `LocalAuthManager.logout()`
- Clears local auth and local app data for demo reset

## 5. Build and Run

## Android
```bash
./gradlew :composeApp:assembleDebug
./gradlew :composeApp:installDebug
```

## iOS
```bash
cd iosApp
open iosApp.xcodeproj
```

## Full test/build check used during review
```bash
./gradlew test --no-daemon
```

## 6. Project Review Findings (Current Snapshot)

### High
1. iOS persistence initialization is not wired in entry point
- Evidence:
  - `initPlatformStorage()` exists in `composeApp/src/iosMain/kotlin/com/app/mindtrack/auth/KeyValueStorageIOS.kt:25`
  - `MainViewController` does not call it in `composeApp/src/iosMain/kotlin/com/app/mindtrack/MainViewController.kt:5`
- Impact:
  - On iOS, app can fall back to in-memory store, so auth/profile/mood/habits may not persist across relaunch.

### Medium
2. Mood daily overwrite uses existing storage key but may store a different internal id
- Evidence:
  - `saveDailyMoodEntry` writes `store.putString("mood_$existingId", serializeMood(entry))`
  - `entry.id` may differ from `existingId`
  - File: `composeApp/src/commonMain/kotlin/com/app/mindtrack/storage/LocalDataStore.kt:48-50`
- Impact:
  - Internal id/key mismatch can complicate future operations (analytics/delete/export).

3. Credentials stored in plain text for demo auth
- Evidence:
  - Password written as raw string in `LocalAuthManager.register(...)`
  - File: `composeApp/src/commonMain/kotlin/com/app/mindtrack/auth/LocalAuthManager.kt:23`
- Impact:
  - Not safe for production; acceptable only for classroom presentation mode.

### Low
4. Documentation drift vs current implementation
- Evidence:
  - `docs/UI_SCREENS.md` still references old mood slider and 1..10 model in several sections.
- Impact:
  - Can confuse reviewers/instructors if demo differs from docs.

## 7. Testing Status and Gaps

### Verified
- Gradle test/build task succeeds locally:
  - `./gradlew test --no-daemon`

### Gaps
- No dedicated unit tests for `LocalDataStore` serialization/deserialization edge cases
- No automated UI tests for register/login/mood/habit flows
- No iOS persistence smoke test proving relaunch persistence

## 8. Suggested Post-Presentation Improvements

1. Call iOS `initPlatformStorage()` at app startup.
2. Replace ad-hoc serialization with `kotlinx.serialization` JSON.
3. Move local auth password storage to secure storage (Keychain/EncryptedSharedPreferences).
4. Add tests for data store and auth flows.
5. Align all docs with the new pager-based mood UX and current feature behavior.

## 9. Glossary
- KMP: Kotlin Multiplatform
- Compose Multiplatform: Shared declarative UI for Android/iOS
- KVStore: Key-value storage abstraction used for MVP persistence
- MVP: Minimum Viable Product

---
This document is designed for class presentation and codebase handoff.

