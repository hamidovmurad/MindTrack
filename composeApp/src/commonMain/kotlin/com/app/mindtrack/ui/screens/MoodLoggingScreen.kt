package com.app.mindtrack.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.app.mindtrack.model.MoodEntry
import com.app.mindtrack.ui.resources.BackIcon
import com.app.mindtrack.ui.resources.AddIcon

/**
 * Mood logging screen where users can log their current mood (1-10 scale)
 * with optional notes. Includes mood visualization (emoji-based).
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MoodLoggingScreen(
    onBackClick: () -> Unit = {},
    onMoodSaved: (MoodEntry) -> Unit = {},
    modifier: Modifier = Modifier
) {
    var moodValue by remember { mutableStateOf(5) }
    var notes by remember { mutableStateOf("") }
    var isSaving by remember { mutableStateOf(false) }
    var nextEntryId by remember { mutableStateOf(1L) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
    ) {
        // Top bar
        TopAppBar(
            title = { Text("Log Your Mood") },
            navigationIcon = {
                IconButton(onClick = onBackClick) {
                    BackIcon()
                }
            }
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Mood visualization (emoji-based feedback)
            val moodEmoji = when (moodValue) {
                1, 2 -> "😢"
                3, 4 -> "😟"
                5, 6 -> "😐"
                7, 8 -> "😊"
                else -> "😄"
            }

            Text(
                moodEmoji,
                style = MaterialTheme.typography.displayLarge,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(vertical = 24.dp)
                    .fillMaxWidth()
            )

            // Mood label
            val moodLabel = when (moodValue) {
                1, 2 -> "Terrible"
                3, 4 -> "Bad"
                5, 6 -> "Okay"
                7, 8 -> "Good"
                else -> "Excellent"
            }

            Text(
                moodLabel,
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            // Mood slider (1-10)
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    "How are you feeling?",
                    style = MaterialTheme.typography.labelMedium,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Slider(
                    value = moodValue.toFloat(),
                    onValueChange = { moodValue = it.toInt() },
                    valueRange = 1f..10f,
                    steps = 8,
                    modifier = Modifier.fillMaxWidth()
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("1", style = MaterialTheme.typography.labelSmall)
                    Text("10", style = MaterialTheme.typography.labelSmall)
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Notes field
            OutlinedTextField(
                value = notes,
                onValueChange = { notes = it },
                label = { Text("Add a note (optional)") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                maxLines = 5
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Mood context quick buttons
            Text(
                "What's affecting your mood?",
                style = MaterialTheme.typography.labelMedium,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp)
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
                        onClick = { notes = if (notes.isEmpty()) context else "$notes, $context" },
                        label = { Text(context) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Save button
            Button(
                onClick = {
                    isSaving = true
                    // Create a MoodEntry (in real app, this would save to Firebase)
                    val timestamp = nextEntryId
                    val entry = MoodEntry(
                        id = "temp_$timestamp",
                        userId = "user_placeholder",
                        timestamp = timestamp,
                        mood = moodValue,
                        note = if (notes.isEmpty()) null else notes
                    )
                    nextEntryId += 1
                    onMoodSaved(entry)
                    // Simulate save delay
                    isSaving = false
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                enabled = !isSaving
            ) {
                if (isSaving) {
                    CircularProgressIndicator(
                        color = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.size(20.dp)
                    )
                } else {
                    AddIcon()
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Save Mood Entry")
                }
            }
        }
    }
}






