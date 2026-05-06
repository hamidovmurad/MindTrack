package com.app.mindtrack.ui.screens

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.app.mindtrack.model.MoodEntry
import com.app.mindtrack.storage.LocalDataStore
import com.app.mindtrack.auth.LocalAuthManager
import kotlinx.datetime.Clock
import com.app.mindtrack.ui.resources.AddIcon
import com.app.mindtrack.ui.resources.BackIcon
import com.app.mindtrack.ui.components.*
import com.app.mindtrack.ui.theme.*
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import mindtrack.composeapp.generated.resources.Res
import mindtrack.composeapp.generated.resources.img_face_angry
import mindtrack.composeapp.generated.resources.img_face_upset
import mindtrack.composeapp.generated.resources.img_face_sad
import mindtrack.composeapp.generated.resources.img_face_good
import mindtrack.composeapp.generated.resources.img_face_happy
import mindtrack.composeapp.generated.resources.img_face_spectacular
import org.jetbrains.compose.resources.DrawableResource

private data class MoodOption(
    val id: Int,
    val name: String,
    val color: Color,
    val image: DrawableResource,
    val description: String,
    val supportMessage: String
)

/**
 * Mood logging screen with a horizontal pager of mood faces and color-coded indicators.
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MoodLoggingScreen(
    onBackClick: () -> Unit = {},
    onMoodSaved: (MoodEntry) -> Unit = {},
    modifier: Modifier = Modifier
) {
    var notes by remember { mutableStateOf("") }
    var isSaving by remember { mutableStateOf(false) }
    // IDs and timestamps will be generated via LocalDataStore / Random for demo persistence
    val scope = rememberCoroutineScope()

    val moods = remember {
        listOf(
            MoodOption(1, "Angry", MoodAngry, Res.drawable.img_face_angry, "Take a slow breath.", "It’s okay to pause and reset."),
            MoodOption(2, "Upset", MoodUpset, Res.drawable.img_face_upset, "You’re getting through it.", "Small steps still count."),
            MoodOption(3, "Sad", MoodSad, Res.drawable.img_face_sad, "Be gentle with yourself.", "A calm moment can help."),
            MoodOption(4, "Good", MoodGood, Res.drawable.img_face_good, "You seem steady today.", "Keep doing what’s helping."),
            MoodOption(5, "Happy", MoodHappy, Res.drawable.img_face_happy, "Nice energy today.", "Share that feeling forward."),
            MoodOption(6, "Spectacular", MoodSpectacular, Res.drawable.img_face_spectacular, "Wonderful mood.", "That’s a bright check-in.")
        )
    }

    val pagerState = rememberPagerState(initialPage = 3, pageCount = { moods.size })
    val selectedMood = moods[pagerState.currentPage]
    val backgroundColor = selectedMood.color.copy(alpha = 0.1f)

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(backgroundColor)
    ) {
        WellnessTopAppBar(
            title = "Log Your Mood",
            navigationIcon = {
                IconButton(onClick = onBackClick) {
                    BackIcon()
                }
            }
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .wellnessPadding(),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            WellnessCard {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        "How are you feeling today?",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.primary,
                        textAlign = TextAlign.Center
                    )

                    Text(
                        selectedMood.name,
                        style = MaterialTheme.typography.headlineMedium,
                        color = selectedMood.color,
                        textAlign = TextAlign.Center
                    )

                    Text(
                        selectedMood.description,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center
                    )

                    HorizontalPager(
                        state = pagerState,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(220.dp)
                    ) { page ->
                        val mood = moods[page]
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(180.dp)
                                    .background(mood.color.copy(alpha = 0.18f), CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Image(
                                    painter = painterResource(mood.image),
                                    contentDescription = mood.name,
                                    modifier = Modifier.size(150.dp)
                                )
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                mood.supportMessage,
                                style = MaterialTheme.typography.labelMedium,
                                color = mood.color,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(horizontal = 24.dp)
                            )
                        }
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        moods.forEachIndexed { index, mood ->
                            MoodPagerDot(
                                mood = mood,
                                isSelected = index == pagerState.currentPage,
                                onClick = {
                                    scope.launch {
                                        pagerState.animateScrollToPage(index)
                                    }
                                }
                            )
                        }
                    }

                    Text(
                        "Swipe or tap a dot to choose your feeling.",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }

            WellnessCard {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        "Add a short note (optional)",
                        style = MaterialTheme.typography.labelMedium,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )
                    OutlinedTextField(
                        value = notes,
                        onValueChange = { notes = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(100.dp),
                        maxLines = 3,
                        placeholder = { Text("What's on your mind?") }
                    )
                }
            }

            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    "What influenced this feeling?",
                    style = MaterialTheme.typography.labelMedium,
                    modifier = Modifier.padding(bottom = 12.dp)
                )

                val moodContexts = listOf("Sleep", "Food", "Exercise", "Work", "Relationships")
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    moodContexts.forEach { context ->
                        AssistChip(
                            colors = if (notes.contains(context)) AssistChipDefaults.assistChipColors(
                                containerColor = MaterialTheme.colorScheme.primary,
                                labelColor = MaterialTheme.colorScheme.onPrimary
                            ) else AssistChipDefaults.assistChipColors(),
                            onClick = { notes = if (notes.isEmpty()) context else "$notes, $context" },
                            label = { Text(context) }
                        )
                    }
                }
            }

            WellnessSpacer(height = 20.dp)

                    WellnessButton(
                text = "Save Check-In",
                onClick = {
                    isSaving = true
                            // Build a proper MoodEntry and persist locally
                            val userId = LocalAuthManager.getCurrentUser()?.email ?: "local_guest"
                            val timestamp = Clock.System.now().toEpochMilliseconds()
                            val id = LocalDataStore.createId("mood")
                            val entry = MoodEntry(
                                id = id,
                                userId = userId,
                                timestamp = timestamp,
                                mood = selectedMood.id,
                                note = if (notes.isEmpty()) null else notes
                            )
                            // persist locally (one-per-day per user)
                            LocalDataStore.saveDailyMoodEntry(entry)
                            onMoodSaved(entry)
                    isSaving = false
                },
                isLoading = isSaving,
                leadingIcon = { AddIcon() }
            )

            WellnessSpacer(height = 24.dp)
        }
    }
}

@Composable
private fun MoodPagerDot(
    mood: MoodOption,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val scale by animateFloatAsState(
        targetValue = if (isSelected) 2f else 1f,
        label = "moodDotScale"
    )

    Box(
        modifier = Modifier
            .padding(horizontal = 6.dp)
            .size(18.dp)
            .scale(scale)
            .background(mood.color, CircleShape)
            .clickable(onClick = onClick)
    )
}
