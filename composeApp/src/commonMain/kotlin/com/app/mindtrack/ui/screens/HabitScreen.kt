package com.app.mindtrack.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.foundation.background
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.foundation.shape.CircleShape
import com.app.mindtrack.model.Habit
import com.app.mindtrack.ui.resources.AddIcon
import com.app.mindtrack.ui.resources.DeleteIcon
import com.app.mindtrack.ui.resources.CheckCircleIcon
import com.app.mindtrack.ui.resources.CloseIcon
import com.app.mindtrack.ui.resources.BackIcon
import com.app.mindtrack.ui.components.*
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import kotlin.random.Random
import com.app.mindtrack.storage.LocalDataStore
import com.app.mindtrack.auth.LocalAuthManager

/**
 * Habit management screen: list habits with ability to mark complete or add new habit.
 */
@Composable
fun HabitScreen(
    habitsState: List<Habit> = emptyList(),
    onHabitComplete: (Habit) -> Unit = {},
    onHabitDelete: (Habit) -> Unit = {},
    onAddHabitClick: () -> Unit = {},
    onBackClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    WellnessScreenLayout(
        title = "My Habits",
        actions = {
            IconButton(onClick = onAddHabitClick) {
                AddIcon()
            }
        },
        isScrollable = false,  // LazyColumn handles scrolling
        modifier = modifier
    ) {
        if (habitsState.isEmpty()) {
            // Empty state
            WellnessEmptyState(
                icon = { CheckCircleIcon(modifier = Modifier.size(64.dp)) },
                title = "No habits yet",
                description = "Create your first habit to get started!",
                action = {
                    WellnessButton(
                        text = "Create Habit",
                        onClick = onAddHabitClick
                    )
                }
            )
        } else {
            // Habits list with summary
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    val completed = habitsState.count { it.enabled }
                    val total = habitsState.size
                    HabitSummaryCard(completed, total)
                }

                item {
                    Text(
                        "Your Daily Habits",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }

                items(habitsState) { habit ->
                    HabitCard(
                        habit = habit,
                        onComplete = { onHabitComplete(habit) },
                        onDelete = { onHabitDelete(habit) }
                    )
                }
            }
        }
    }
}

@Composable
private fun HabitSummaryCard(completed: Int, total: Int) {
    val progress = if (total > 0) completed.toFloat() / total else 0f
    WellnessCard {
        Row(
            modifier = Modifier.padding(20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Box(contentAlignment = Alignment.Center, modifier = Modifier.size(64.dp)) {
                CircularProgressIndicator(
                    progress = { progress },
                    modifier = Modifier.fillMaxSize(),
                    strokeWidth = 6.dp,
                    strokeCap = StrokeCap.Round,
                    trackColor = MaterialTheme.colorScheme.surfaceVariant
                )
                Text(
                    "${(progress * 100).toInt()}%",
                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
                )
            }
            
            Column {
                Text(
                    "Daily Progress",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )
                Text(
                    "$completed of $total habits completed today",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

/**
 * Individual habit card with completion checkbox.
 */
@Composable
fun HabitCard(
    habit: Habit,
    onComplete: () -> Unit = {},
    onDelete: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val backgroundColor = if (habit.enabled) {
        MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.15f)
    } else {
        MaterialTheme.colorScheme.surface
    }

    WellnessCard(
        modifier = modifier,
        containerColor = backgroundColor
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onComplete,
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(
                        if (habit.enabled) MaterialTheme.colorScheme.primary
                        else MaterialTheme.colorScheme.surfaceVariant
                    )
            ) {
                CheckCircleIcon(
                    modifier = Modifier.size(24.dp)
                )
            }

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 16.dp)
            ) {
                Text(
                    habit.title,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.SemiBold,
                        textDecoration = if (habit.enabled) androidx.compose.ui.text.style.TextDecoration.LineThrough else null
                    ),
                    color = if (habit.enabled) MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f) 
                            else MaterialTheme.colorScheme.onSurface
                )
                habit.description?.let {
                    if (it.isNotEmpty()) {
                        Text(
                            it,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            maxLines = 1,
                            overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
                        )
                    }
                }
            }

            IconButton(onClick = onDelete) {
                DeleteIcon(modifier = Modifier.size(20.dp))
            }
        }
    }
}

/**
 * Screen to add a new habit (modal/dialog-like).
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddHabitScreen(
    onHabitCreated: (Habit) -> Unit = {},
    onDismiss: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var frequency by remember { mutableStateOf("daily") }
    var isSaving by remember { mutableStateOf(false) }

    WellnessScreenLayout(
        title = "Create Habit",
        navigationIcon = {
            IconButton(onClick = onDismiss) {
                CloseIcon()
            }
        },
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .wellnessPadding()
        ) {
            Text(
                "Build a new routine",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            // Habit title
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Habit name") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            )

            // Description
            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Description (optional)") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .padding(bottom = 16.dp),
                maxLines = 3
            )

            // Frequency dropdown
            Text(
                "Frequency",
                style = MaterialTheme.typography.labelMedium,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            var expanded by remember { mutableStateOf(false) }
            val frequencies = listOf("daily", "weekly", "monthly")

            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 32.dp)
            ) {
                OutlinedTextField(
                    value = frequency,
                    onValueChange = {},
                    readOnly = true,
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                    modifier = Modifier
                        .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable, enabled = true)
                        .fillMaxWidth()
                )
                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    frequencies.forEach { freq ->
                        DropdownMenuItem(
                            text = { Text(freq) },
                            onClick = {
                                frequency = freq
                                expanded = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            // Create button
            WellnessButton(
                text = "Create Habit",
                onClick = {
                    isSaving = true
                    val userId = LocalAuthManager.getCurrentUser()?.email ?: "local_guest"
                    val newHabit = Habit(
                        id = LocalDataStore.createId("habit"),
                        userId = userId,
                        title = title,
                        description = if (description.isEmpty()) null else description,
                        frequency = frequency,
                        enabled = true
                    )
                    // persist locally for demo
                    LocalDataStore.saveHabit(newHabit)
                    onHabitCreated(newHabit)
                    isSaving = false
                },
                enabled = !isSaving && title.isNotEmpty(),
                isLoading = isSaving
            )
        }
    }
}









