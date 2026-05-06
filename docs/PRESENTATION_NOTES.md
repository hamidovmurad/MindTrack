# MindTrack Presentation Notes (Detailed 15-20 Minute Version)

Use this as your full speaker script for class presentation.

## Presentation Goal
- Explain the problem clearly
- Show what was built (and why)
- Demonstrate technical understanding
- Be honest about current limitations and next steps

## Total Duration Target
- Main presentation: 15-17 minutes
- Q&A: 3-5 minutes
- Total session: 18-22 minutes

---

## Slide 1 - Title (1 min)
**Title:** MindTrack: Mental Wellness Companion (Kotlin Multiplatform Project)

**On slide (short text):**
- MindTrack
- University Final Project
- Android + iOS from one codebase

**Speaker script:**
"Good [morning/afternoon], everyone. My name is [Your Name], and this is my final project called MindTrack. MindTrack is a mental wellness companion app designed to help users log daily mood, build healthy habits, and monitor their progress in one simple dashboard. The key technical idea of this project is that I built Android and iOS support from a single Kotlin Multiplatform codebase."

**Transition:**
"Before showing the app, I want to explain the real problem this project tries to solve."

---

## Slide 2 - Problem Statement (1.5 min)
**Title:** Why this project?

**On slide:**
- People skip self-checks in busy daily life
- Existing solutions can be complex or paid
- Need lightweight and student-friendly wellness tracker

**Speaker script:**
"In real life, many people, especially students, experience stress and mood changes but do not consistently track their emotional state. Existing wellness apps are often complex, subscription-based, or overloaded with features. For this project, I focused on a simple, practical daily flow: log mood quickly, keep habits visible, and see basic insights in one place."

"The objective is not to replace medical tools. It is to provide a lightweight awareness tool that encourages consistency and better self-observation."

**Transition:**
"Based on this problem, I defined a clear MVP scope."

---

## Slide 3 - Project Objectives and MVP Scope (1.5 min)
**Title:** What I committed to build

**On slide:**
- Mood logging
- Habit builder
- Dashboard insights
- Profile/settings and local auth
- Cross-platform UI consistency

**Speaker script:**
"For the MVP, I scoped the project into core features only. First, mood logging, where users can select mood with a friendly pager UI and optional notes. Second, habit management, where users can create and manage routines. Third, dashboard insights to summarize mood and habits. Fourth, settings and profile flow including local login/register for demo usage."

"I intentionally prioritized stable UX and architecture over adding too many advanced features."

**Transition:**
"Now I will briefly explain the technology choices behind this MVP."

---

## Slide 4 - Technology Stack and Rationale (2 min)
**Title:** Tech stack and why it was chosen

**On slide:**
- Kotlin Multiplatform
- Compose Multiplatform
- Material 3 + custom wellness design system
- Local persistence via key-value platform storage
- Gradle Kotlin DSL

**Speaker script:**
"I chose Kotlin Multiplatform because it allows shared logic across Android and iOS while keeping platform-specific extensions where needed. I used Compose Multiplatform for UI so that screens and components can be reused. This reduced duplicate code and improved consistency."

"For data persistence in this MVP presentation mode, I implemented local storage through a shared key-value abstraction with platform implementations: SharedPreferences on Android and NSUserDefaults on iOS."

"This approach was practical for a class project timeline and gives a clean path for future backend integration."

**Transition:**
"Next, I will show the high-level architecture."

---

## Slide 5 - Architecture Overview (2 min)
**Title:** High-Level Architecture

**On slide diagram suggestion:**
UI (Compose Screens)
-> Navigation Layer
-> LocalAuthManager + LocalDataStore
-> KVStore Abstraction
-> Android/iOS storage implementation

**Speaker script:**
"The architecture is divided into three major parts. First is UI and navigation in common code. Second is domain/state flow using shared models. Third is data persistence using LocalAuthManager and LocalDataStore."

"Navigation controls screen transitions and app-level state such as authentication and tab selection. LocalAuthManager handles register/login/logout and profile updates for presentation mode. LocalDataStore handles mood and habit persistence."

"Platform-specific code is isolated behind KVStore abstraction so common code does not depend directly on Android or iOS APIs."

**Transition:**
"Now I will walk through the app screens quickly before live demo."

---

## Slide 6 - Screen Flow and UX Journey (1.5 min)
**Title:** End-to-end user journey

**On slide:**
- Sign In
- Dashboard
- Mood Logging
- Habits / Add Habit
- Settings / Profile / Logout

**Speaker script:**
"The user flow starts at sign-in. After authentication, user enters dashboard as the central hub. From dashboard, user can log mood and then return to updated overview. Habits tab allows creating and managing routines. Settings includes profile information and logout/reset for demo."

"The design principle was to keep important actions reachable in one or two taps."

**Transition:**
"Let me now show the live demo and explain each step while I perform it."

---

## Slide 7 - Live Demo Part 1: Login + Dashboard (2 min)
**Title:** Demo: Getting started

**On slide:**
- Local register/login
- Persistent session
- Dashboard overview

**Live narration script:**
"First, I open the app and use Local Account mode to register or log in. For this classroom MVP, local auth is used for easier demonstration without backend setup."

"After login, app opens dashboard. Dashboard displays a greeting card, mood overview, trend section, and habit completion information."

"This screen is designed to be the daily check-in center."

**Transition:**
"Now I will log mood and show that dashboard updates from saved local data."

---

## Slide 8 - Live Demo Part 2: Mood Logging (2 min)
**Title:** Demo: Daily mood logging flow

**On slide:**
- Mood face pager
- Optional notes + context chips
- Save and return to dashboard

**Live narration script:**
"Here in mood logging, user chooses mood via horizontal pager with visual faces and color-coded indicators. The user can add optional notes and context chips like sleep, work, relationships."

"When I press Save Check-In, the mood is persisted locally and app returns to dashboard. In current MVP logic, one mood entry per day per user is used to keep daily trend clean and easy to read."

"Now on dashboard, you can see that mood data has updated."

**Transition:**
"Next I will create a habit and show persistence behavior."

---

## Slide 9 - Live Demo Part 3: Habits + Profile + Logout (2.5 min)
**Title:** Demo: habits and account flow

**On slide:**
- Add habit
- Habit list update
- Profile edit
- Logout clears local demo data

**Live narration script:**
"Now in Habits tab, I add a habit with title, optional description, and frequency. After create, list reloads from local storage."

"Then in Profile, I can update name and email in local auth data."

"Finally, from Settings/Profile, I trigger logout. In this demo mode, logout also clears local app data so the app can be reset quickly for repeated classroom demonstrations."

**Transition:**
"After demo, I want to explain key UI/UX design decisions."

---

## Slide 10 - UI/UX Design Decisions (1.5 min)
**Title:** Wellness-focused design approach

**On slide:**
- Soft colors and rounded cards
- Reusable wellness components
- Friendly wording and motivational tone
- Bottom navigation for feature discoverability

**Speaker script:**
"The app uses calming visual language: soft palette, rounded shapes, spacious layout, and supportive microcopy. I created shared wellness components to avoid repeated styling and keep all screens consistent."

"This also made development faster, because once a component is improved, all screens benefit."

**Transition:**
"Now I will explain how data persistence works technically."

---

## Slide 11 - Data and Persistence Design (1.5 min)
**Title:** Local data strategy in MVP

**On slide:**
- LocalAuthManager
- LocalDataStore
- KVStore abstraction
- Android: SharedPreferences / iOS: NSUserDefaults

**Speaker script:**
"The persistence layer is intentionally simple for MVP. Auth and profile are handled by LocalAuthManager. Mood and habits are handled by LocalDataStore. Both use shared KVStore abstraction."

"Android uses SharedPreferences. iOS uses NSUserDefaults implementation. This design separates business logic from platform APIs and prepares codebase for future replacement with secure storage or backend sync."

**Transition:**
"Now I will present an honest engineering review: what works well and what remains."

---

## Slide 12 - Technical Review: Strengths and Risks (2 min)
**Title:** Current status assessment

**On slide (two columns):**
Strengths:
- Shared codebase and reusable components
- Working local auth + persistence flow
- Consistent UI and navigation

Risks / limitations:
- iOS storage init should be wired at startup
- Plain-text password in demo mode
- Custom string serialization can be improved
- Limited automated test coverage

**Speaker script:**
"Main strengths are cross-platform delivery speed, UI consistency, and functioning MVP data flow."

"Main limitations are known and documented. First, iOS storage initialization must be guaranteed at startup for reliable persistence. Second, password storage is plain text in this demo mode and not production-safe. Third, local datastore uses lightweight custom serialization; JSON serialization would be cleaner long-term. Fourth, test coverage should be expanded."

**Transition:**
"These limitations directly inform the next roadmap."

---

## Slide 13 - Roadmap and Future Work (1.5 min)
**Title:** Next development phases

**On slide:**
1. Security hardening (Keychain/Encrypted storage)
2. Backend sync (Firebase)
3. Better analytics and trend insights
4. Unit/UI tests
5. Production readiness and deployment

**Speaker script:**
"My next phase is to replace demo-level storage with secure platform mechanisms and integrate cloud sync using Firebase for multi-device data continuity."

"Then I will improve analytics, add test automation, and complete production hardening including reliability and privacy controls."

**Transition:**
"Finally, I will summarize the project outcome in one slide."

---

## Slide 14 - Conclusion (1 min)
**Title:** Final summary

**On slide:**
- Cross-platform wellness MVP delivered
- Clean architecture foundation
- Ready for extension toward production

**Speaker script:**
"In summary, MindTrack successfully delivers a cross-platform mental wellness MVP with key user flows: login, mood tracking, habit management, dashboard insights, and settings/profile."

"The project demonstrates both practical product design and technical architecture thinking. It is class-ready now and has a clear, realistic path toward production improvements. Thank you."

---

## Slide 15 - Q&A Backup (Use only if needed, 3-5 min)
**Title:** Questions and Answers

### Likely Question 1: Why Kotlin Multiplatform instead of Flutter/React Native?
**Answer:**
"I selected Kotlin Multiplatform because it lets me share business logic and most UI while staying close to native ecosystems. It was also aligned with course goals and gave clear Android/iOS extensibility."

### Likely Question 2: Is this production secure?
**Answer:**
"Not yet. Current auth storage is presentation mode only. Production version must use Keychain and encrypted Android storage plus stronger auth model."

### Likely Question 3: How do you handle offline and sync?
**Answer:**
"Current MVP is local-first. Planned next phase is cloud sync with Firebase and conflict strategy for multi-device updates."

### Likely Question 4: What was the hardest technical issue?
**Answer:**
"State synchronization and serialization boundaries. For example, ensuring mood save correctly updates dashboard required careful data parsing and refresh flow."

### Likely Question 5: What would you improve first after class?
**Answer:**
"Security hardening for credentials, iOS persistence init finalization, and automated tests for auth and data store."

---

## Slide Asset Checklist
- Sign-in screen (local account mode)
- Dashboard after mood saved
- Mood logging pager with colored dots
- Habits list + create habit
- Profile save + logout
- Simple architecture diagram

---

## Presenter Tips (Important)
- Keep eye contact; do not read every line
- Speak slower during architecture and risks slides
- During demo, narrate each click and expected result
- If demo fails, immediately switch to screenshots and continue confidently
- End with strengths + honest limitations + roadmap (teachers like this balance)

---

## Timing Plan (15-20 mins)
- Slides 1-3: 4 min
- Slides 4-6: 5.5 min
- Slides 7-9 (demo): 6.5 min
- Slides 10-14: 4 min
- Q&A: 3-5 min

Short version (if needed):
- Skip deep details in slides 10-11 and keep total to ~12 minutes before Q&A.

