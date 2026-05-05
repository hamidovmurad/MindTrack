# MindTrack UI Screens

This document describes the UI screens and navigation structure of the MindTrack app.

## Screens Overview

### 1. Sign-In Screen (`SignInScreen.kt`)
- **Purpose:** User authentication and onboarding
- **Features:**
  - Email + Password sign-in
  - Google Sign-In button
  - Apple Sign-In button (iOS)
  - Sign-up redirect
  - Error messaging
- **Navigation:** → Dashboard (on successful auth)

### 2. Dashboard Screen (`DashboardScreen.kt`)
- **Purpose:** Main hub showing wellness overview and quick actions
- **Features:**
  - Greeting card (morning/afternoon/evening-aware emoji)
  - Mood summary stats (average, latest, total entries)
  - 7-day mood trend visualization (color-coded bar chart)
    - Red (1-3): Terrible/Bad
    - Orange (4-6): Okay
    - Green (7-10): Good/Excellent
  - Habit completion progress (%)
  - Quick action buttons (Log Mood, My Habits)
- **Navigation:**
  - → Mood Logging (via "Log Mood" button)
  - → Habits (via "My Habits" button)

### 3. Mood Logging Screen (`MoodLoggingScreen.kt`)
- **Purpose:** Allow users to log their current mood with context
- **Features:**
  - Large emoji mood visualization (5 categories: 😢 to 😄)
  - Slider to rate mood 1-10
  - Text field for optional notes
  - Quick context chips (Sleep, Food, Exercise, Work, Relationships)
  - Save button
- **Navigation:**
  - ← Back (returns to Dashboard)
  - On save: creates MoodEntry and returns to Dashboard

### 4. Habit Screen (`HabitScreen.kt`)
- **Purpose:** View and manage daily routines and habits
- **Features:**
  - List of user habits with checkboxes
  - Habit title, description, and frequency badge
  - Delete button per habit
  - Add (+) button in top app bar
  - Empty state if no habits
- **Navigation:**
  - Add button → Add Habit screen
  - Checkbox → toggle habit active/inactive
  - Delete button → removes habit from list

### 5. Add Habit Screen (`AddHabitScreen.kt`)
- **Purpose:** Create new habit routines
- **Features:**
  - Text field for habit name
  - Optional description
  - Frequency dropdown (daily, weekly, monthly)
  - Create button
- **Navigation:**
  - ← Close (returns to Habits screen)
  - On create: adds new Habit and returns to Habits screen

### 6. Medication Screen (`MedicationScreen.kt`)
- **Purpose:** Manage medications and reminders
- **Features:**
  - List of medications with dosage and schedule
  - Active/Inactive status badge
  - Pharmacy icon per medication
  - Delete button per medication
  - Add (+) button in top app bar
  - Empty state if no medications
- **Navigation:**
  - Add button → Add Medication screen
  - Delete button → removes medication

### 7. Add Medication Screen (`AddMedicationScreen.kt`)
- **Purpose:** Create new medication entries
- **Features:**
  - Text field for medication name
  - Optional dosage field (e.g., "500mg")
  - Optional schedule field (e.g., "Twice daily with meals")
  - Add button
- **Navigation:**
  - ← Close (returns to Medications screen)
  - On create: adds new Medication and returns to Medications screen

---

## Navigation Structure

```
SignInScreen
    ↓ (on sign-in success)
Dashboard ← main hub
    ├── Mood Logging Screen
    │   └── ← Dashboard (on save or back)
    ├── Habit Screen
    │   ├── Add Habit Screen
    │   │   └── ← Habit Screen (on create or cancel)
    │   └── ← Dashboard (or stay in Habits)
    └── Medication Screen (future integration)
        ├── Add Medication Screen
        │   └── ← Medication Screen (on create or cancel)
        └── ← Dashboard (or stay in Medications)
```

---

## Visualization & Data Display

### Mood Trend Chart
- **Type:** Horizontal bar chart
- **Data:** Last 7 mood entries
- **Colors:**
  - Red: mood 1-3 (terrible)
  - Orange: mood 4-6 (okay)
  - Green: mood 7-10 (good)
- **Interaction:** Visual only (no interactions in MVP)

### Habit Completion Progress
- **Type:** Linear progress bar + percentage
- **Display:** "X of Y habits active" + percentage
- **Calculation:** (active habits / total habits) × 100

### Mood Emoji Feedback
- 😢 Terrible (mood 1-2)
- 😟 Bad (mood 3-4)
- 😐 Okay (mood 5-6)
- 😊 Good (mood 7-8)
- 😄 Excellent (mood 9-10)

---

## State Management (Current)

The app uses **local state** managed in `Navigation.kt`:
- `moodEntries: List<MoodEntry>` — logged moods
- `habits: List<Habit>` — user habits
- `medications: List<Medication>` — user medications (stub, not yet integrated)

**Future:** Migrate to a shared `ViewModel` or Redux-style state management when Firebase integration is implemented.

---

## Building & Testing

### Android

```bash
./gradlew :composeApp:build
./gradlew :composeApp:installDebug
adb shell am start -n com.app.mindtrack/.MainActivity
```

### iOS

```bash
cd /Users/muradhamidov/Desktop/MindTrack/iosApp
open iosApp.xcodeproj
# Build & run in Xcode
```

### Preview / Hot Reload
- Android: Use Android Studio's Live Edit / Compose Preview
- iOS: Use Xcode Previews
- Both: Edit `.kt` files and rebuild

---

## TODO / Future Enhancements

- [ ] Integrate Firebase Auth (sign-in/sign-up)
- [ ] Integrate Firestore for data persistence
- [ ] Add ViewModel + repository pattern for state management
- [ ] Implement mood analytics / trend predictions
- [ ] Add medication reminders / local notifications
- [ ] Add appointments / doctor management screens
- [ ] Implement data export & reports
- [ ] Add dark mode support
- [ ] Offline sync strategy
- [ ] Gamification (badges, streaks)
- [ ] Emergency contact feature
- [ ] Family/caregiver mode

---

## Design System

- **Primary Color:** Material3 default (usually blue/indigo)
- **Material3:** Full Material Design 3 theming via Compose
- **Typography:** Material3 default fonts (Roboto on Android, System on iOS)
- **Spacing:** 4dp grid (4, 8, 12, 16, 24, 32, etc.)
- **Padding:** 16dp standard, 24dp for screen edges, 8-12dp for cards

---

## File Structure

```
composeApp/src/commonMain/kotlin/com/app/mindtrack/
├── ui/
│   ├── screens/
│   │   ├── SignInScreen.kt
│   │   ├── DashboardScreen.kt
│   │   ├── MoodLoggingScreen.kt
│   │   ├── HabitScreen.kt
│   │   ├── MedicationScreen.kt
│   │   └── (other screens)
│   └── navigation/
│       └── Navigation.kt
└── App.kt (main entry point)
```

---

## Color Reference

| Component | Color | Meaning |
|-----------|-------|---------|
| Mood Bar (1-3) | #FF6B6B (Red) | Negative mood |
| Mood Bar (4-6) | #FFA500 (Orange) | Neutral mood |
| Mood Bar (7-10) | #51CF66 (Green) | Positive mood |
| Active Habit | Primary Color | Enabled |
| Active Medication | Green | Active |
| Inactive Medication | Gray | Inactive |

---

## Questions?

- Refer to Material3 docs: https://m3.material.io/
- Jetpack Compose docs: https://developer.android.com/jetpack/compose
- MindTrack README.md for project overview

