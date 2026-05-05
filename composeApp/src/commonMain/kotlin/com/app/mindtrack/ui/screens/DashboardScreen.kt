package com.app.mindtrack.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.app.mindtrack.model.MoodEntry
import com.app.mindtrack.model.Habit
import com.app.mindtrack.ui.resources.TrendingUpIcon
import com.app.mindtrack.ui.resources.CheckCircleIcon
import kotlin.math.roundToInt

/**
 * Dashboard screen showing mood trends, habit completion, and general wellness overview.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    moodEntries: List<MoodEntry> = emptyList(),
    habits: List<Habit> = emptyList(),
    onMoodLoggingClick: () -> Unit = {},
    onHabitClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
    ) {
        // Top bar
        TopAppBar(title = { Text("Dashboard") })

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Greeting card
            GreetingCard()

            // Mood summary
            MoodSummaryCard(moodEntries)

            // Mood trend visualization (simple bar chart)
            MoodTrendCard(moodEntries)

            // Habit completion overview
            HabitCompletionCard(habits)

            // Quick actions
            QuickActionsCard(
                onMoodLoggingClick = onMoodLoggingClick,
                onHabitClick = onHabitClick
            )

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

/**
 * Greeting card with encouraging message based on current hour.
 */
@Composable
fun GreetingCard() {
    val greeting = "Welcome back! 🌿"

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    greeting,
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    "Let's track your wellness journey today",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }
    }
}

/**
 * Mood summary showing current mood statistics.
 */
@Composable
fun MoodSummaryCard(moodEntries: List<MoodEntry>) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(24.dp)) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                TrendingUpIcon(
                    modifier = Modifier
                        .size(24.dp)
                        .padding(end = 8.dp)
                )
                Text(
                    "Mood Overview",
                    style = MaterialTheme.typography.titleMedium
                )
            }

            if (moodEntries.isEmpty()) {
                Text(
                    "No mood entries yet",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            } else {
                val average = moodEntries.map { it.mood }.average().roundToInt()
                val latest = moodEntries.maxByOrNull { it.timestamp }?.mood ?: 0

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            "Average",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            average.toString(),
                            style = MaterialTheme.typography.displaySmall,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            "Latest",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            latest.toString(),
                            style = MaterialTheme.typography.displaySmall,
                            color = MaterialTheme.colorScheme.tertiary
                        )
                    }
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            "Total Entries",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            moodEntries.size.toString(),
                            style = MaterialTheme.typography.displaySmall,
                            color = MaterialTheme.colorScheme.secondary
                        )
                    }
                }
            }
        }
    }
}

/**
 * Simple mood trend visualization (bar representation).
 */
@Composable
fun MoodTrendCard(moodEntries: List<MoodEntry>) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(24.dp)) {
            Text(
                "7-Day Mood Trend",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            if (moodEntries.isEmpty()) {
                Text(
                    "Log your mood to see trends",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            } else {
                // Simplified bar chart: show last 7 entries (or fewer)
                val recentEntries = moodEntries.sortedBy { it.timestamp }.takeLast(7)

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.Bottom
                ) {
                    recentEntries.forEach { entry ->
                        val barHeightDp = (entry.mood / 10f) * 120f  // calculate as float first
                        val barHeight = barHeightDp.dp  // convert to Dp
                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight(),
                            verticalArrangement = Arrangement.Bottom,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            // Mood bar
                            Box(
                                modifier = Modifier
                                    .width(20.dp)
                                    .height(barHeight)
                                    .background(
                                        color = when {
                                            entry.mood <= 3 -> Color(0xFFFF6B6B) // Red
                                            entry.mood <= 6 -> Color(0xFFFFA500) // Orange
                                            else -> Color(0xFF51CF66)             // Green
                                        },
                                        shape = RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp)
                                    )
                            )
                            Text(
                                entry.mood.toString(),
                                style = MaterialTheme.typography.labelSmall,
                                modifier = Modifier.padding(top = 4.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

/**
 * Habit completion overview.
 */
@Composable
fun HabitCompletionCard(habits: List<Habit>) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(24.dp)) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                CheckCircleIcon(
                    modifier = Modifier
                        .size(24.dp)
                        .padding(end = 8.dp)
                )
                Text(
                    "Habit Completion",
                    style = MaterialTheme.typography.titleMedium
                )
            }

            if (habits.isEmpty()) {
                Text(
                    "Create habits to track your progress",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            } else {
                val completedCount = habits.count { it.enabled }
                val completionRate = ((completedCount.toFloat() / habits.size) * 100).toInt()

                Column {
                    Text(
                        "$completionRate% of habits active",
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    // Progress bar
                    LinearProgressIndicator(
                        progress = { completionRate / 100f },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp)
                    )

                    Text(
                        "$completedCount of ${habits.size} habits active",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
            }
        }
    }
}

/**
 * Quick action buttons for common tasks.
 */
@Composable
fun QuickActionsCard(
    onMoodLoggingClick: () -> Unit = {},
    onHabitClick: () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        ElevatedButton(
            onClick = onMoodLoggingClick,
            modifier = Modifier
                .weight(1f)
                .height(56.dp)
        ) {
            Text("Log Mood")
        }

        ElevatedButton(
            onClick = onHabitClick,
            modifier = Modifier
                .weight(1f)
                .height(56.dp)
        ) {
            Text("My Habits")
        }
    }
}







