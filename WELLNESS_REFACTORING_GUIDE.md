# 🎨 MindTrack Screen Refactoring Guide

## Overview
This guide shows how to upgrade all remaining screens to use the wellness component architecture for consistent, clean styling across the app.

---

## Refactoring Pattern (Quick Template)

### ❌ BEFORE: Generic Material Design
```kotlin
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyScreen(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
    ) {
        TopAppBar(title = { Text("My Screen") })
        
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Content here
        }
    }
}
```

### ✅ AFTER: Wellness Design (Static Content)
```kotlin
@Composable
fun MyScreen(modifier: Modifier = Modifier) {
    WellnessScreenLayout(
        title = "My Screen",
        modifier = modifier
        // isScrollable = true (default) for static Column content
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .wellnessPadding(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Content here
        }
    }
}
```

### ✅ AFTER: Wellness Design (With LazyColumn)
```kotlin
@Composable
fun MyListScreen(modifier: Modifier = Modifier, items: List<Item> = emptyList()) {
    WellnessScreenLayout(
        title = "My List",
        modifier = modifier,
        isScrollable = false  // LazyColumn handles its own scrolling
    ) {
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(items) { item ->
                // Item content
            }
        }
    }
}
```

**⚠️ IMPORTANT: Nested Scrolling Rule**
- Set `isScrollable = true` (default) when using **static Column** content
- Set `isScrollable = false` when content contains **LazyColumn**, **LazyRow**, or other scrollable containers
- This prevents the "measured with infinity constraints" error from nested scrollable layouts

---

## Component Mapping Reference

### WellnessScreenLayout Parameters
```kotlin
WellnessScreenLayout(
    title: String = "",                              // Top bar title
    navigationIcon: @Composable (() -> Unit)? = null, // Back/menu button
    actions: @Composable (RowScope.() -> Unit)? = null, // Right-side buttons
    showTopBar: Boolean = true,                      // Show/hide top app bar
    isScrollable: Boolean = true,                    // ⚠️ IMPORTANT! See nested scrolling rule below
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit                  // Screen content
)
```

**Scrollable Parameter Guide:**
| Scenario | isScrollable | Reason |
|----------|-------------|--------|
| Static Column content | `true` (default) | Column content will be wrapped with verticalScroll() |
| LazyColumn/LazyRow | `false` | These handle their own scrolling; nesting causes errors |
| Form fields + Button | `true` (default) | Static layout, needs scrolling for small screens |
| List of items | `false` | Use LazyColumn, let it handle scrolling |

### Screens to Refactor
1. **MoodLoggingScreen** - Mood entry form
2. **AIHealthAssistantScreen** - AI chat interface  
3. **NutritionHydrationScreen** - Nutrition/water tracking
4. **RemindersScreen** - Medication reminders list
5. **SettingsScreen** - Settings menu (already has some icons)
6. **ProfileScreen** - User profile information
7. **MedicationScreen** - Medication management
8. **InfoScreens.kt** - Terms, FAQ, Emergency screens

---

## Detailed Refactoring Examples

### 🔄 MoodLoggingScreen (Form Screen)
**Status:** Ready to refactor  
**Current File:** `MoodLoggingScreen.kt`

```kotlin
// Add imports
import com.app.mindtrack.ui.components.*

@Composable
fun MoodLoggingScreen(
    onMoodLogged: (MoodEntry) -> Unit = {},
    onBackClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    // Change 1: Replace standard layout
    WellnessScreenLayout(
        title = "Log Your Mood",
        navigationIcon = {
            IconButton(onClick = onBackClick) {
                BackIcon()
            }
        },
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .wellnessPadding(),  // Changed: from .padding(24.dp)
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text("How are you feeling?", style = MaterialTheme.typography.headlineSmall)
            
            // Change 2: Use WellnessCard for input sections
            WellnessCard {
                Column(modifier = Modifier.padding(20.dp)) {
                    // Mood slider UI here
                }
            }
            
            // Change 3: Replace OutlinedTextField
            WellnessTextField(
                value = notes,
                onValueChange = { notes = it },
                label = "Notes (optional)",
                maxLines = 3
            )
            
            // Change 4: Replace Button
            WellnessButton(
                text = "Save Mood",
                onClick = { /* save logic */ }
            )
        }
    }
}
```

---

### 🤖 AIHealthAssistantScreen (Chat Interface)
**Status:** Ready to refactor  
**Current File:** `AIHealthAssistantScreen.kt`

```kotlin
@Composable
fun AIHealthAssistantScreen(
    onBackClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    WellnessScreenLayout(
        title = "AI Health Assistant",
        navigationIcon = {
            IconButton(onClick = onBackClick) { BackIcon() }
        },
        modifier = modifier
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Chat messages using LazyColumn
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .wellnessPadding(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(messages) { message ->
                    // Wrap each message in WellnessCard
                    WellnessCard {
                        Text(
                            message.text,
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                }
            }
            
            // Input area
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .wellnessPadding(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                WellnessTextField(
                    value = userMessage,
                    onValueChange = { userMessage = it },
                    label = "Type your question...",
                    modifier = Modifier.weight(1f)
                )
                WellnessButton(
                    text = "Send",
                    onClick = { /* send */ },
                    modifier = Modifier.width(80.dp)
                )
            }
        }
    }
}
```

---

### 💧 NutritionHydrationScreen (Tracking UI)
**Status:** Ready to refactor  
**Current File:** `NutritionHydrationScreen.kt`

```kotlin
@Composable
fun NutritionHydrationScreen(
    waterIntake: Int = 0,
    waterGoal: Int = 2000,
    onBackClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    WellnessScreenLayout(
        title = "Nutrition & Hydration",
        navigationIcon = {
            IconButton(onClick = onBackClick) { BackIcon() }
        },
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .wellnessPadding(),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Text(
                "Daily Water Intake",
                style = MaterialTheme.typography.titleLarge,
                color = DeepOceanBlue
            )
            
            // Progress card using WellnessProgressCard
            WellnessProgressCard(
                title = "Water Intake",
                progress = waterIntake.toFloat() / waterGoal,
                progressText = "$waterIntake / $waterGoal ml"
            )
            
            // Info cards using WellnessInfoCard
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                WellnessInfoCard(
                    label = "Remaining",
                    value = "${waterGoal - waterIntake} ml",
                    modifier = Modifier.weight(1f),
                    valueColor = FreshMint
                )
                WellnessInfoCard(
                    label = "Percentage",
                    value = "${(waterIntake * 100 / waterGoal)}%",
                    modifier = Modifier.weight(1f),
                    valueColor = FreshMint
                )
            }
            
            // Action buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                WellnessButton(
                    text = "Log 250ml",
                    onClick = { /* log */ },
                    modifier = Modifier.weight(1f)
                )
                WellnessButton(
                    text = "Log 500ml",
                    onClick = { /* log */ },
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}
```

---

### 🔔 RemindersScreen (List View)
**Status:** Ready to refactor  
**Current File:** `RemindersScreen.kt`

```kotlin
@Composable
fun RemindersScreen(
    reminders: List<Reminder> = emptyList(),
    onBackClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    WellnessScreenLayout(
        title = "Medication Reminders",
        navigationIcon = {
            IconButton(onClick = onBackClick) { BackIcon() }
        },
        modifier = modifier
    ) {
        if (reminders.isEmpty()) {
            WellnessEmptyState(
                icon = { PillIcon(modifier = Modifier.size(64.dp)) },
                title = "No reminders set",
                description = "Set up medication reminders to stay on track"
            )
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .wellnessPadding(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(reminders) { reminder ->
                    WellnessCard {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            PillIcon(modifier = Modifier.size(24.dp))
                            
                            Column(
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(horizontal = 12.dp)
                            ) {
                                Text(
                                    reminder.medicationName,
                                    style = MaterialTheme.typography.titleMedium
                                )
                                WellnessTag(text = reminder.time)
                            }
                            
                            Checkbox(
                                checked = reminder.completed,
                                onCheckedChange = { /* update */ }
                            )
                        }
                    }
                }
            }
        }
    }
}
```

---

### ⚙️ SettingsScreen (Menu List)
**Status:** Partially refactored  
**Current File:** `SettingsScreen.kt`

```kotlin
// Already has icon updates, just need layout refresh

@Composable
fun SettingsScreen(
    onProfileClick: () -> Unit = {},
    onEmergencyClick: () -> Unit = {},
    onTermsClick: () -> Unit = {},
    onFAQClick: () -> Unit = {},
    onBackClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    WellnessScreenLayout(
        title = "Settings",
        navigationIcon = {
            IconButton(onClick = onBackClick) { BackIcon() }
        },
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .wellnessPadding(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Each setting item already wrapped in cards, verify they're WellnessCard
            SettingsItem(
                // ... existing code
            )
        }
    }
}
```

---

### 👤 ProfileScreen (Form/Info Display)
**Status:** Ready to refactor  
**Current File:** `ProfileScreen.kt`

```kotlin
@Composable
fun ProfileScreen(
    user: User? = null,
    onBackClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    WellnessScreenLayout(
        title = "My Profile",
        navigationIcon = {
            IconButton(onClick = onBackClick) { BackIcon() }
        },
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .wellnessPadding(),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            if (user != null) {
                WellnessCard {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // Profile info
                        Text(user.name, style = MaterialTheme.typography.titleLarge)
                    }
                }
            }
            
            // Edit button
            WellnessButton(
                text = "Edit Profile",
                onClick = { /* edit */ }
            )
        }
    }
}
```

---

## Quick Checklist

Use this checklist when refactoring each screen:

- [ ] Add import: `import com.app.mindtrack.ui.components.*`
- [ ] Replace outer `Column` + `background` with `WellnessScreenLayout`
- [ ] **Set `isScrollable = false` if using LazyColumn/LazyRow** ⚠️
- [ ] **Set `isScrollable = true` (default) for static Column content** ✅
- [ ] Replace `TopAppBar` call (handled by WellnessScreenLayout)
- [ ] Replace `.padding(16.dp)` with `.wellnessPadding()`
- [ ] Replace `Card()` with `WellnessCard {}`
- [ ] Replace `Button()` with `WellnessButton()`
- [ ] Replace `OutlinedTextField()` with `WellnessTextField()`
- [ ] Use `WellnessSpacer(height)` for vertical spacing
- [ ] Use `WellnessTag()` for badges/labels
- [ ] Replace empty states with `WellnessEmptyState()`
- [ ] Verify text colors use theme colors: `DeepOceanBlue`, `ForestTeal`, `FreshMint`
- [ ] Test Android compilation: `./gradlew :composeApp:compileDebugKotlinAndroid`
- [ ] Test iOS compilation: `./gradlew :composeApp:compileKotlinIosSimulatorArm64`

---

## Architecture Benefits

### Before
- ❌ Code duplication across screens (Card styling, Button styling, etc.)
- ❌ Inconsistent spacing (12dp, 16dp, 24dp mixed throughout)
- ❌ Hard to change design globally (27+ files to modify)
- ❌ New developers must copy/paste old patterns

### After  
- ✅ **Single component library** = consistent styling everywhere
- ✅ **Modifier extensions** for reusable layout patterns
- ✅ **Theme-aware colors** in one place
- ✅ **Global animation timing** (breathing effect, etc.)
- ✅ **Change design once** = updates everywhere automatically
- ✅ **Faster development** = new screens in minutes

---

## Next Steps

1. Apply refactoring pattern to each screen from the list above
2. After each screen, compile both platforms:
   ```bash
   ./gradlew :composeApp:compileDebugKotlinAndroid
   ./gradlew :composeApp:compileKotlinIosSimulatorArm64
   ```
3. Commit changes with clear messages
4. Run full build before deploying

---

## Files Already Refactored ✅
- ✅ `SignInScreen.kt` 
- ✅ `DashboardScreen.kt`
- ✅ `HabitScreen.kt` + `AddHabitScreen()`

## Files Ready for Refactoring 🔄
- MoodLoggingScreen.kt
- AIHealthAssistantScreen.kt
- NutritionHydrationScreen.kt
- RemindersScreen.kt
- SettingsScreen.kt (partial)
- ProfileScreen.kt
- MedicationScreen.kt
- InfoScreens.kt (Terms, FAQ, Emergency)

---

**Last Updated:** May 6, 2026  
**Architecture Version:** 1.0  
**Component Library:** WellnessComponents.kt




