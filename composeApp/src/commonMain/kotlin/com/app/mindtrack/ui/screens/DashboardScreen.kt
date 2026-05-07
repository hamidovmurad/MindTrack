package com.app.mindtrack.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app.mindtrack.auth.LocalAuthManager
import com.app.mindtrack.common.getCurrentHour
import com.app.mindtrack.model.Habit
import com.app.mindtrack.model.MoodEntry
import com.app.mindtrack.model.MoodStats
import com.app.mindtrack.ui.components.*
import com.app.mindtrack.ui.resources.*
import com.app.mindtrack.ui.theme.MoodAngry
import com.app.mindtrack.ui.theme.MoodGood
import com.app.mindtrack.ui.theme.MoodHappy
import com.app.mindtrack.ui.theme.MoodSad
import com.app.mindtrack.ui.theme.MoodSpectacular
import com.app.mindtrack.ui.theme.MoodUpset
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import mindtrack.composeapp.generated.resources.Res
import mindtrack.composeapp.generated.resources.img_face_angry
import mindtrack.composeapp.generated.resources.img_face_good
import mindtrack.composeapp.generated.resources.img_face_happy
import mindtrack.composeapp.generated.resources.img_face_sad
import mindtrack.composeapp.generated.resources.img_face_spectacular
import mindtrack.composeapp.generated.resources.img_face_upset
import mindtrack.composeapp.generated.resources.img_home_banner1
import mindtrack.composeapp.generated.resources.img_home_banner2
import mindtrack.composeapp.generated.resources.img_home_banner3
import mindtrack.composeapp.generated.resources.img_home_banner4
import mindtrack.composeapp.generated.resources.img_home_banner5
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import kotlin.math.roundToInt
import kotlin.time.Clock
import kotlinx.datetime.Clock as KtClock

/**
 * Dashboard screen showing mood trends, habit completion, and general wellness overview.
 */
@Composable
fun DashboardScreen(
    moodEntries: List<MoodEntry> = emptyList(),
    habits: List<Habit> = emptyList(),
    onMoodLoggingClick: () -> Unit = {},
    onHabitsClick: () -> Unit = {},
    onNutritionClick: () -> Unit = {},
    onMedsClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    WellnessScreenLayout(
        title = "Dashboard",
        showTopBar = false,
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // Greeting card - Borderless Banner at the top
            GreetingCard(onMoodLoggingClick = onMoodLoggingClick)

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .wellnessPadding(),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                // Quick Actions Section
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text(
                        "Quick Actions",
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                    )
                    QuickActionsRow(
                        onMoodClick = onMoodLoggingClick,
                        onHabitClick = onHabitsClick,
                        onWaterClick = onNutritionClick,
                        onMedsClick = onMedsClick
                    )
                }

                // Daily Inspiration
                DailyInspirationCard()

                // Mood insights
                MoodSummaryCard(moodEntries, onMoodLoggingClick)

                // Mood trend visualization
                MoodTrendCard(moodEntries)

                // Habit completion overview
                HabitCompletionCard(habits, onHabitsClick)

                WellnessSpacer(height = 24.dp)
            }
        }
    }
}

@Composable
private fun QuickActionsRow(
    onMoodClick: () -> Unit,
    onHabitClick: () -> Unit,
    onWaterClick: () -> Unit,
    onMedsClick: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        QuickActionButton(
            icon = { AddIcon(modifier = Modifier.size(20.dp)) },
            label = "Mood",
            onClick = onMoodClick,
            modifier = Modifier.weight(1f)
        )
        QuickActionButton(
            icon = { HabitIcon(modifier = Modifier.size(20.dp)) },
            label = "Habits",
            onClick = onHabitClick,
            modifier = Modifier.weight(1f)
        )
        QuickActionButton(
            icon = { WaterIcon(modifier = Modifier.size(20.dp)) },
            label = "Water",
            onClick = onWaterClick,
            modifier = Modifier.weight(1f)
        )
        QuickActionButton(
            icon = { PharmacyIcon(modifier = Modifier.size(20.dp)) },
            label = "Meds",
            onClick = onMedsClick,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun QuickActionButton(
    icon: @Composable () -> Unit,
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        onClick = onClick,
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        color = Color.White,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
    ) {
        Column(
            modifier = Modifier.padding(vertical = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            icon()
            Text(
                label,
                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Medium),
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun DailyInspirationCard() {
    WellnessCard {
        Row(
            modifier = Modifier.padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .background(MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text("✨", fontSize = 22.sp)
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(
                    "Daily Inspiration",
                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    "\"The only way to do great work is to love what you do.\"",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontStyle = FontStyle.Italic,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                )
            }
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
    val user = LocalAuthManager.getCurrentUser()
    val firstName = user?.name?.substringBefore(" ") ?: "there"
    val (greeting, subtext) = when (getCurrentHour()) {
        in 5..11 -> "Good Morning, $firstName! ☀️" to "Ready to start your day with a positive check-in?"
        in 12..17 -> "Good Afternoon, $firstName! 🌤️" to "Taking a moment for yourself is the best part of the day."
        in 18..21 -> "Good Evening, $firstName! 🌙" to "Reflect on your day and unwind with a gentle check-in."
        else -> "Ready for Rest, $firstName? ✨" to "A final check-in can help clear your mind for sleep."
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(280.dp)
    ) {

        val imageList = listOf(
            Res.drawable.img_home_banner1,
            Res.drawable.img_home_banner2,
            Res.drawable.img_home_banner3,
            Res.drawable.img_home_banner4,
            Res.drawable.img_home_banner5
        )
        val randomBanner = remember { imageList.random() }

        // Full bleed background image
        Image(
            painter = painterResource(randomBanner),
            contentDescription = "MindTrack banner",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
        
        // Gradient overlay for text readability
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    androidx.compose.ui.graphics.Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent,
                            Color.Black.copy(alpha = 0.6f)
                        )
                    )
                )
        )

        // Content positioned over the banner
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.Bottom
        ) {
            Text(
                greeting,
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = (-1).sp
                ),
                color = Color.White
            )
            Text(
                subtext,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.White.copy(alpha = 0.9f),
                lineHeight = 20.sp
            )
            
            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = onMoodLoggingClick,
                shape = RoundedCornerShape(12.dp),
                contentPadding = PaddingValues(horizontal = 24.dp, vertical = 10.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White,
                    contentColor = MaterialTheme.colorScheme.primary
                ),
                modifier = Modifier.wrapContentWidth()
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    AddIcon(modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        "How are you feeling?",
                        style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold)
                    )
                }
            }
        }
    }
}

private data class MoodDisplayItem(
    val name: String,
    val color: Color,
    val image: DrawableResource,
)

private fun moodDisplayForValue(value: Int): MoodDisplayItem = when (value) {
    1 -> MoodDisplayItem("Angry", MoodAngry, Res.drawable.img_face_angry)
    2 -> MoodDisplayItem("Upset", MoodUpset, Res.drawable.img_face_upset)
    3 -> MoodDisplayItem("Sad", MoodSad, Res.drawable.img_face_sad)
    4 -> MoodDisplayItem("Good", MoodGood, Res.drawable.img_face_good)
    5 -> MoodDisplayItem("Happy", MoodHappy, Res.drawable.img_face_happy)
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
fun MoodSummaryCard(moodEntries: List<MoodEntry>, onClick: () -> Unit) {
    WellnessCard(modifier = Modifier.clickable(onClick = onClick)) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 20.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    TrendingUpIcon(
                        modifier = Modifier
                            .size(24.dp)
                            .padding(end = 8.dp)
                    )
                    Text(
                        "Mood Insights",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold)
                    )
                }
                
                if (moodEntries.isNotEmpty()) {
                    Text(
                        "${moodEntries.size} entries",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.7f)
                    )
                }
            }

            if (moodEntries.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        InfoIcon(modifier = Modifier.size(32.dp).padding(bottom = 8.dp))
                        Text(
                            "No mood entries yet. Your insights will appear here.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            } else {
                val average = moodEntries.map { it.mood }.average().roundToInt()
                val latest = moodEntries.maxByOrNull { it.timestamp }?.mood ?: 0
                val trend = MoodStats.trend(moodEntries)

                Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        MoodStatCard(
                            title = "General Vibe",
                            mood = moodDisplayForValue(average.coerceIn(1, 6)),
                            subtitle = "Average",
                            modifier = Modifier.weight(1f)
                        )
                        MoodStatCard(
                            title = "Last Check-in",
                            mood = moodDisplayForValue(latest.coerceIn(1, 6)),
                            subtitle = "Latest",
                            modifier = Modifier.weight(1f)
                        )
                    }

                    if (trend != null) {
                        Surface(
                            color = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.4f),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                val trendText = when {
                                    trend > 0.5 -> "Your mood is trending up! Keep it up. 🚀"
                                    trend < -0.5 -> "You've had a few tough days. Be kind to yourself. 🫂"
                                    else -> "Your mood has been steady lately. ⚖️"
                                }
                                Text(
                                    trendText,
                                    style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium),
                                    color = MaterialTheme.colorScheme.onSecondaryContainer
                                )
                            }
                        }
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
    WellnessCard {
        Column(modifier = Modifier.padding(20.dp)) {
            Text(
                "7-Day Mood Trend",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                modifier = Modifier.padding(bottom = 20.dp)
            )

            if (moodEntries.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        QuestionIcon(modifier = Modifier.size(32.dp).padding(bottom = 8.dp))
                        Text(
                            "Log your mood daily to see your emotional journey.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            } else {
                val recentEntries = moodEntries.sortedBy { it.timestamp }.takeLast(7)

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .padding(horizontal = 4.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.Bottom
                ) {
                    recentEntries.forEach { entry ->
                        val mood = moodDisplayForValue(entry.mood.coerceIn(1, 6))
                        val barHeightRatio = entry.mood / 6f
                        val dayName = try {
                            val localDate = kotlinx.datetime.Instant.fromEpochMilliseconds(entry.timestamp)
                                .toLocalDateTime(TimeZone.currentSystemDefault())
                            localDate.dayOfWeek.name.take(3)
                        } catch (_: Exception) {
                            "???"
                        }
                        
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
                                modifier = Modifier.size(24.dp)
                            )
                            
                            Spacer(modifier = Modifier.height(8.dp))

                            Box(
                                modifier = Modifier
                                    .width(28.dp)
                                    .fillMaxHeight(barHeightRatio * 0.65f)
                                    .background(
                                        brush = androidx.compose.ui.graphics.Brush.verticalGradient(
                                            colors = listOf(
                                                mood.color,
                                                mood.color.copy(alpha = 0.4f)
                                            )
                                        ),
                                        shape = RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp)
                                    )
                            )
                            
                            Text(
                                dayName,
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Medium
                                ),
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
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
fun HabitCompletionCard(habits: List<Habit>, onClick: () -> Unit) {
    WellnessCard(modifier = Modifier.clickable(onClick = onClick)) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                CheckCircleIcon(
                    modifier = Modifier
                        .size(24.dp)
                        .padding(end = 8.dp)
                )
                Text(
                    "Daily Progress",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold)
                )
            }

            if (habits.isEmpty()) {
                Text(
                    "Start your journey by adding daily habits in the Habits tab.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            } else {
                val completedCount = habits.count { it.enabled }
                val totalCount = habits.size
                val completionRate = (completedCount.toFloat() / totalCount)

                Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                "Today's Focus",
                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                            )
                            Text(
                                "$completedCount of $totalCount habits active",
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        Text(
                            "${(completionRate * 100).toInt()}%",
                            style = MaterialTheme.typography.headlineSmall.copy(
                                color = MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.ExtraBold
                            )
                        )
                    }

                    LinearProgressIndicator(
                        progress = { completionRate },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(12.dp)
                            .clip(RoundedCornerShape(6.dp)),
                        trackColor = MaterialTheme.colorScheme.surfaceVariant,
                        color = MaterialTheme.colorScheme.primary
                    )

                    // Quick look at habits
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        items(habits.take(5)) { habit ->
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = if (habit.enabled) MaterialTheme.colorScheme.primaryContainer 
                                        else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                                modifier = Modifier.padding(vertical = 4.dp)
                            ) {
                                Text(
                                    habit.title,
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                                    style = MaterialTheme.typography.labelMedium,
                                    color = if (habit.enabled) MaterialTheme.colorScheme.onPrimaryContainer
                                            else MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

