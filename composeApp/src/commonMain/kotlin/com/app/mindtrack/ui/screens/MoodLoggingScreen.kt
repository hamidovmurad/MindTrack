package com.app.mindtrack.ui.screens

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.app.mindtrack.auth.LocalAuthManager
import com.app.mindtrack.model.MoodEntry
import com.app.mindtrack.storage.LocalDataStore
import com.app.mindtrack.ui.components.*
import com.app.mindtrack.ui.resources.AddIcon
import com.app.mindtrack.ui.resources.BackIcon
import com.app.mindtrack.ui.theme.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import mindtrack.composeapp.generated.resources.Res
import mindtrack.composeapp.generated.resources.img_face_angry
import mindtrack.composeapp.generated.resources.img_face_good
import mindtrack.composeapp.generated.resources.img_face_happy
import mindtrack.composeapp.generated.resources.img_face_sad
import mindtrack.composeapp.generated.resources.img_face_spectacular
import mindtrack.composeapp.generated.resources.img_face_upset
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import kotlin.time.Clock

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
    
    // Dynamic background color that eases between mood colors
    val backgroundColor by animateColorAsState(
        targetValue = selectedMood.color.copy(alpha = 0.12f),
        animationSpec = tween(durationMillis = 500),
        label = "moodBackground"
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(backgroundColor)
    ) {
        WellnessTopAppBar(
            title = "Daily Check-in",
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
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // Mood Selector Card
            WellnessCard {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 28.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        "How are you feeling?",
                        style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.primary,
                        textAlign = TextAlign.Center
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        selectedMood.description,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(horizontal = 32.dp)
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    HorizontalPager(
                        state = pagerState,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(240.dp),
                        contentPadding = PaddingValues(horizontal = 64.dp)
                    ) { page ->
                        val mood = moods[page]
                        val isSelected = pagerState.currentPage == page
                        
                        val pageScale by animateFloatAsState(
                            targetValue = if (isSelected) 1f else 0.8f,
                            label = "pageScale"
                        )
                        val pageAlpha by animateFloatAsState(
                            targetValue = if (isSelected) 1f else 0.5f,
                            label = "pageAlpha"
                        )

                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .scale(pageScale)
                                .alpha(pageAlpha),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(160.dp)
                                    .background(mood.color.copy(alpha = 0.15f), CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Image(
                                    painter = painterResource(mood.image),
                                    contentDescription = mood.name,
                                    modifier = Modifier.size(130.dp)
                                )
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            Text(
                                mood.name,
                                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.ExtraBold),
                                color = mood.color
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

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
                }
            }

            // Context / Influences Section
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    "What's influencing your mood?",
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                    modifier = Modifier.padding(bottom = 16.dp, start = 4.dp)
                )

                val moodContexts = listOf("Sleep", "Work", "Relationships", "Health", "Food", "Exercise", "Hobbies", "Weather")
                
                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    moodContexts.forEach { context ->
                        val isSelected = notes.contains(context)
                        FilterChip(
                            selected = isSelected,
                            onClick = {
                                val currentTags = notes.split(", ").filter { it.isNotBlank() }.toMutableList()
                                if (isSelected) {
                                    currentTags.remove(context)
                                } else {
                                    currentTags.add(context)
                                }
                                notes = currentTags.joinToString(", ")
                            },
                            label = { Text(context) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = selectedMood.color.copy(alpha = 0.2f),
                                selectedLabelColor = selectedMood.color,
                                selectedLeadingIconColor = selectedMood.color
                            ),
                            border = FilterChipDefaults.filterChipBorder(
                                enabled = true,
                                selected = isSelected,
                                borderColor = MaterialTheme.colorScheme.outlineVariant,
                                selectedBorderColor = selectedMood.color
                            ),
                            shape = RoundedCornerShape(12.dp)
                        )
                    }
                }
            }

            // Notes Section
            WellnessCard {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        "Additional Notes",
                        style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                        modifier = Modifier.padding(bottom = 12.dp)
                    )
                    OutlinedTextField(
                        value = notes,
                        onValueChange = { notes = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(120.dp),
                        placeholder = { Text("Write a little more about how you feel...", style = MaterialTheme.typography.bodyMedium) },
                        shape = RoundedCornerShape(16.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = selectedMood.color,
                            unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant
                        )
                    )
                }
            }

            WellnessButton(
                text = "Save Check-In",
                onClick = {
                    scope.launch {
                        isSaving = true
                        delay(1200) // Slightly longer for "mindful" feel

                        val userId = LocalAuthManager.getCurrentUser()?.email ?: "local_guest"
                        val timestamp = Clock.System.now().toEpochMilliseconds()
                        val id = LocalDataStore.createId("mood")
                        val entry = MoodEntry(
                            id = id,
                            userId = userId,
                            timestamp = timestamp,
                            mood = selectedMood.id,
                            note = notes.ifBlank { null }
                        )
                        LocalDataStore.saveDailyMoodEntry(entry)
                        
                        isSaving = false
                        onMoodSaved(entry)
                    }
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
