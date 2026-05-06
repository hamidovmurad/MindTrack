package com.app.mindtrack.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.DrawableResource
import com.app.mindtrack.model.MoodEntry
import com.app.mindtrack.model.Habit
import com.app.mindtrack.ui.resources.TrendingUpIcon
import com.app.mindtrack.ui.resources.CheckCircleIcon
import com.app.mindtrack.ui.components.*
import com.app.mindtrack.ui.theme.MoodAngry
import com.app.mindtrack.ui.theme.MoodUpset
import com.app.mindtrack.ui.theme.MoodSad
import com.app.mindtrack.ui.theme.MoodGood
import com.app.mindtrack.ui.theme.MoodHappy
import com.app.mindtrack.ui.theme.MoodSpectacular
import mindtrack.composeapp.generated.resources.Res
import mindtrack.composeapp.generated.resources.img_home_banner
import mindtrack.composeapp.generated.resources.img_face_angry
import mindtrack.composeapp.generated.resources.img_face_upset
import mindtrack.composeapp.generated.resources.img_face_sad
import mindtrack.composeapp.generated.resources.img_face_good
import mindtrack.composeapp.generated.resources.img_face_happy
import mindtrack.composeapp.generated.resources.img_face_spectacular
import kotlin.math.roundToInt

/**
 * Dashboard screen showing mood trends, habit completion, and general wellness overview.
 */
@Composable
fun DashboardScreen(
    moodEntries: List<MoodEntry> = emptyList(),
    habits: List<Habit> = emptyList(),
    onMoodLoggingClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    WellnessScreenLayout(
        title = "Dashboard",
        showTopBar = true,
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .wellnessPadding(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Greeting card
            GreetingCard(onMoodLoggingClick = onMoodLoggingClick)

            // Mood summary
            MoodSummaryCard(moodEntries)

            // Mood trend visualization (simple bar chart)
            MoodTrendCard(moodEntries)

            // Habit completion overview
            HabitCompletionCard(habits)

            WellnessSpacer(height = 24.dp)
        }
    }
}

/**
 * Greeting card with encouraging message based on current hour.
 */
@Composable
fun GreetingCard(
    onMoodLoggingClick: () -> Unit = {}
) {
    val greeting = "Welcome back! 🌿"

    WellnessCard {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Image(
                painter = painterResource(Res.drawable.img_home_banner),
                contentDescription = "MindTrack banner",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
                    .clip(RoundedCornerShape(20.dp)),
                contentScale = ContentScale.Crop
            )

            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    greeting,
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    "Let's track your wellness journey today",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Text(
                "Take a gentle check-in whenever you need it.",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            WellnessButton(
                text = "Log Mood",
                onClick = onMoodLoggingClick,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

private data class MoodDisplayItem(
    val name: String,
    val color: Color,
    val image: DrawableResource,
)

private fun moodDisplayForValue(value: Int): MoodDisplayItem = when (value) {
    1, 2 -> MoodDisplayItem("Angry", MoodAngry, Res.drawable.img_face_angry)
    3, 4 -> MoodDisplayItem("Upset", MoodUpset, Res.drawable.img_face_upset)
    5 -> MoodDisplayItem("Sad", MoodSad, Res.drawable.img_face_sad)
    6, 7 -> MoodDisplayItem("Good", MoodGood, Res.drawable.img_face_good)
    8, 9 -> MoodDisplayItem("Happy", MoodHappy, Res.drawable.img_face_happy)
    else -> MoodDisplayItem("Spectacular", MoodSpectacular, Res.drawable.img_face_spectacular)
}

@Composable
private fun MoodStatCard(
    title: String,
    mood: MoodDisplayItem,
    subtitle: String,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(18.dp),
        color = mood.color.copy(alpha = 0.12f)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                title,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Image(
                    painter = painterResource(mood.image),
                    contentDescription = mood.name,
                    modifier = Modifier.size(24.dp)
                )
                Text(
                    mood.name,
                    style = MaterialTheme.typography.titleMedium,
                    color = mood.color
                )
            }
            Text(
                subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
/**
 * Mood summary showing current mood statistics.
 */
@Composable
fun MoodSummaryCard(moodEntries: List<MoodEntry>) {
    WellnessCard {
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
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    MoodStatCard(
                        title = "Average mood",
                        mood = moodDisplayForValue(average.coerceIn(1, 10)),
                        subtitle = "Across your entries",
                        modifier = Modifier.weight(1f)
                    )
                    MoodStatCard(
                        title = "Latest mood",
                        mood = moodDisplayForValue(latest.coerceIn(1, 10)),
                        subtitle = "Most recent check-in",
                        modifier = Modifier.weight(1f)
                    )
                }

                Text(
                    "Total entries: ${moodEntries.size}",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

/**
 * Simple mood trend visualization (bar representation).
 */
@Composable
fun MoodTrendCard(moodEntries: List<MoodEntry>) {
    WellnessCard {
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
                        .height(190.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.Bottom
                ) {
                    recentEntries.forEach { entry ->
                        val barHeightDp = (entry.mood / 10f) * 120f
                        val barHeight = barHeightDp.dp
                        val mood = moodDisplayForValue(entry.mood.coerceIn(1, 10))
                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight(),
                            verticalArrangement = Arrangement.Bottom,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Image(
                                painter = painterResource(mood.image),
                                contentDescription = mood.name,
                                modifier = Modifier.size(22.dp)
                            )

                            Box(
                                modifier = Modifier
                                    .width(22.dp)
                                    .height(barHeight)
                                    .background(
                                        color = mood.color,
                                        shape = RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp)
                                    )
                            )
                            Text(
                                mood.name,
                                style = MaterialTheme.typography.labelSmall,
                                color = mood.color,
                                modifier = Modifier.padding(top = 8.dp)
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
    WellnessCard {
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

