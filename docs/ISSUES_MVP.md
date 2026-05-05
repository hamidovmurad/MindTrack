MindTrack — MVP Issues & Implementation Plan

User answers (from review):
1) Backend: Firebase for database+backend (selected)
2) AI Assistant: deferred (discuss later)
3) Privacy/Costs: Use free tiers / minimal paid services
4) Feature priority: all features equal; treat as separate flows
5) Platforms: Android + iOS using shared Compose
6) Offline: not important; minimal data sync is acceptable
7) Auth: Firebase Auth (Google, Apple, Email with verification)
8) Emergency features: informative (not critical for MVP)
9) Project scope: simple, university final project

MVP Scope (minimal working product):
- Mood tracking: create and view mood entries
- Habit builder: create habits and mark complete
- Dashboard: simple trends summary and latest entries
- Medication reminders: simple medication CRUD and schedule display
- Authentication: Firebase Auth (Google / Apple / Email)

Top-level tasks (mapped to repo modules)
- shared/
  - Add domain models (done)
  - Implement business logic for mood/habit aggregation
  - Add serialization when API choice is finalized
- composeApp/
  - Authentication UI (Firebase sign-in)
  - Mood entry screen, Habit list + create
  - Dashboard screen with simple charts
- iosApp/
  - Shared Compose view entrypoint; mirror Android flows
- server/ (optional)
  - Not required for Firebase flow; keep for future self-hosting

Immediate next actions (I can do now)
- Create GitHub issue skeletons from this file (checklist per feature)
- Implement shared business logic and unit tests for MoodEntry/Habit
- Add Firebase client integration sample in `composeApp/` (auth + simple read/write)

Notes and recommendations
- Use Firebase Firestore for quick prototyping and offline support if needed later.
- Use SQLDelight only if local-first storage with structured queries and migrations is desired.
- Keep AI Assistant out of MVP; implement stub endpoints so UI can show placeholder behavior.

If you want, I will now:
A) Create issue skeletons in `docs/` and optionally open PR drafts
B) Implement simple Firebase-auth sample in `composeApp/` (requires adding Firebase SDKs)
C) Implement basic shared business logic + unit tests (recommended first dev task)

Choose A / B / C (or ask for something else) and I'll continue.
