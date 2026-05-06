package com.app.mindtrack.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
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
            // Habits list
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
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
    WellnessCard(modifier = modifier) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = habit.enabled,
                onCheckedChange = { onComplete() },
                modifier = Modifier.padding(end = 16.dp)
            )

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 16.dp)
            ) {
                Text(
                    habit.title,
                    style = MaterialTheme.typography.titleMedium,
                    color = if (habit.enabled) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                )
                habit.description?.let {
                    if (it.isNotEmpty()) {
                        Text(
                            it,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }
                }
                habit.frequency?.let {
                    if (it.isNotEmpty()) {
                        WellnessTag(text = it, modifier = Modifier.padding(top = 8.dp))
                    }
                }
            }

            IconButton(onClick = onDelete) {
                DeleteIcon()
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
            WellnessTextField(
                value = title,
                onValueChange = { title = it },
                label = "Habit name",
                modifier = Modifier.padding(bottom = 16.dp)
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









