package com.app.mindtrack.ui.navigation

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import com.app.mindtrack.ui.theme.MindTrackTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import com.app.mindtrack.model.Habit
import com.app.mindtrack.model.MoodEntry
import com.app.mindtrack.ui.resources.AssistantIcon
import com.app.mindtrack.ui.resources.DashboardIcon
import com.app.mindtrack.ui.resources.HabitIcon
import com.app.mindtrack.ui.resources.SettingsIcon
import com.app.mindtrack.ui.resources.WaterIcon
import com.app.mindtrack.ui.screens.*

/**
 * App navigation state and routing.
 */
enum class Screen {
    SignIn,
    Dashboard,
    Habits,
    Assistant,
    Nutrition,
    Settings,
    MoodLogging,
    AddHabit,
    Profile,
    Reminders,
    EmergencyInfo,
    TermsPolicy,
    Faq,
    Medications,
    AddMedication
}

private val bottomTabs = listOf(
    Screen.Dashboard,
    Screen.Habits,
    Screen.Assistant,
    Screen.Nutrition,
    Screen.Settings
)

private fun isBottomTab(screen: Screen) = screen in bottomTabs

/**
 * Main navigation container. Manages screen transitions and passes state.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MindTrackNavigation() {
    var currentScreen by remember { mutableStateOf(Screen.SignIn) }
    var selectedTab by remember { mutableStateOf(Screen.Dashboard) }
    var isAuthenticated by remember { mutableStateOf(false) }

    // Mock data for demo (in real app, this would come from Firebase/ViewModel)
    var moodEntries by remember { mutableStateOf<List<MoodEntry>>(emptyList()) }
    var habits by remember { mutableStateOf<List<Habit>>(emptyList()) }

    MindTrackTheme {
        when {
            !isAuthenticated -> {
                SignInScreen(
                    onSignInSuccess = {
                        isAuthenticated = true
                        selectedTab = Screen.Dashboard
                        currentScreen = Screen.Dashboard
                    },
                    onBypassLoginClick = {
                        isAuthenticated = true
                        selectedTab = Screen.Dashboard
                        currentScreen = Screen.Dashboard
                    }
                )
            }

            isBottomTab(currentScreen) -> {
                Scaffold(
                    bottomBar = {
                        MindTrackBottomBar(
                            selectedScreen = currentScreen,
                            onScreenSelected = { screen ->
                                selectedTab = screen
                                currentScreen = screen
                            }
                        )
                    }
                ) { innerPadding ->
                    Box(modifier = Modifier.padding(innerPadding).fillMaxSize()) {
                        when (currentScreen) {
                            Screen.Dashboard -> DashboardScreen(
                                moodEntries = moodEntries,
                                habits = habits,
                                onMoodLoggingClick = { currentScreen = Screen.MoodLogging },
                                onHabitClick = { currentScreen = Screen.Habits }
                            )

                            Screen.Habits -> HabitScreen(
                                habitsState = habits,
                                onAddHabitClick = { currentScreen = Screen.AddHabit },
                                onBackClick = { currentScreen = Screen.Dashboard },
                                onHabitComplete = { habit ->
                                    habits = habits.map { h -> if (h.id == habit.id) h.copy(enabled = !h.enabled) else h }
                                },
                                onHabitDelete = { habit ->
                                    habits = habits.filter { it.id != habit.id }
                                }
                            )

                            Screen.Assistant -> AIHealthAssistantScreen()

                            Screen.Nutrition -> NutritionHydrationScreen()

                            Screen.Settings -> SettingsScreen(
                                onProfileClick = { currentScreen = Screen.Profile },
                                onRemindersClick = { currentScreen = Screen.Reminders },
                                onEmergencyClick = { currentScreen = Screen.EmergencyInfo },
                                onTermsClick = { currentScreen = Screen.TermsPolicy },
                                onFaqClick = { currentScreen = Screen.Faq }
                            )

                            else -> Unit
                        }
                    }
                }
            }

            currentScreen == Screen.MoodLogging -> {
                MoodLoggingScreen(
                    onBackClick = { currentScreen = selectedTab },
                    onMoodSaved = { entry ->
                        moodEntries = moodEntries + entry
                        currentScreen = selectedTab
                    }
                )
            }

            currentScreen == Screen.AddHabit -> {
                AddHabitScreen(
                    onHabitCreated = { newHabit ->
                        habits = habits + newHabit
                        currentScreen = Screen.Habits
                        selectedTab = Screen.Habits
                    },
                    onDismiss = { currentScreen = Screen.Habits }
                )
            }

            currentScreen == Screen.Profile -> {
                ProfileScreen(onBackClick = { currentScreen = Screen.Settings })
            }

            currentScreen == Screen.Reminders -> {
                RemindersScreen(
                    habitsState = habits,
                    onBackClick = { currentScreen = Screen.Settings }
                )
            }

            currentScreen == Screen.EmergencyInfo -> {
                EmergencyInfoScreen(onBackClick = { currentScreen = Screen.Settings })
            }

            currentScreen == Screen.TermsPolicy -> {
                TermsPolicyScreen(onBackClick = { currentScreen = Screen.Settings })
            }

            currentScreen == Screen.Faq -> {
                FaqScreen(onBackClick = { currentScreen = Screen.Settings })
            }

            currentScreen == Screen.Medications -> {
                MedicationScreen(
                    medicationsState = emptyList(),
                    onAddMedicationClick = { currentScreen = Screen.AddMedication },
                    onBackClick = { currentScreen = selectedTab },
                    onMedicationDelete = {}
                )
            }

            currentScreen == Screen.AddMedication -> {
                AddMedicationScreen(
                    onMedicationCreated = { _ ->
                        currentScreen = Screen.Medications
                    },
                    onDismiss = { currentScreen = Screen.Medications }
                )
            }
        }
    }
}

@Composable
private fun MindTrackBottomBar(
    selectedScreen: Screen,
    onScreenSelected: (Screen) -> Unit
) {
    NavigationBar {
        NavigationBarItem(
            selected = selectedScreen == Screen.Dashboard,
            onClick = { onScreenSelected(Screen.Dashboard) },
            icon = { DashboardIcon() },
            label = { Text("Dashboard") }
        )
        NavigationBarItem(
            selected = selectedScreen == Screen.Habits,
            onClick = { onScreenSelected(Screen.Habits) },
            icon = { HabitIcon() },
            label = { Text("Habits") }
        )
        NavigationBarItem(
            selected = selectedScreen == Screen.Assistant,
            onClick = { onScreenSelected(Screen.Assistant) },
            icon = { AssistantIcon() },
            label = { Text("Assistant") }
        )
        NavigationBarItem(
            selected = selectedScreen == Screen.Nutrition,
            onClick = { onScreenSelected(Screen.Nutrition) },
            icon = { WaterIcon() },
            label = { Text("Nutrition") }
        )
        NavigationBarItem(
            selected = selectedScreen == Screen.Settings,
            onClick = { onScreenSelected(Screen.Settings) },
            icon = { SettingsIcon() },
            label = { Text("Settings") }
        )
    }
}

