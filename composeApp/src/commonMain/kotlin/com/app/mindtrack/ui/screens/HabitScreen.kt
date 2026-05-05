package com.app.mindtrack.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import kotlin.random.Random

/**
 * Habit management screen: list habits with ability to mark complete or add new habit.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HabitScreen(
    habitsState: List<Habit> = emptyList(),
    onHabitComplete: (Habit) -> Unit = {},
    onHabitDelete: (Habit) -> Unit = {},
    onAddHabitClick: () -> Unit = {},
    onBackClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Top bar
        TopAppBar(
            title = { Text("My Habits") },
            navigationIcon = {
                IconButton(onClick = onBackClick) {
                    BackIcon()
                }
            },
            actions = {
                IconButton(onClick = onAddHabitClick) {
                    AddIcon()
                }
            }
        )

        if (habitsState.isEmpty()) {
            // Empty state
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                CheckCircleIcon(
                    modifier = Modifier
                        .size(64.dp)
                        .padding(bottom = 16.dp)
                )
                Text(
                    "No habits yet",
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Text(
                    "Create your first habit to get started!",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(bottom = 24.dp)
                )
                Button(onClick = onAddHabitClick) {
                    Text("Create Habit")
                }
            }
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
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Checkbox
            Checkbox(
                checked = habit.enabled,
                onCheckedChange = { onComplete() },
                modifier = Modifier.padding(end = 16.dp)
            )

            // Habit info
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
                // Frequency badge
                habit.frequency?.let {
                    if (it.isNotEmpty()) {
                        Surface(
                            color = MaterialTheme.colorScheme.primaryContainer,
                            shape = ButtonDefaults.shape,
                            modifier = Modifier.padding(top = 8.dp)
                        ) {
                            Text(
                                it,
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onPrimaryContainer,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }
                }
            }

            // Delete button
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

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        TopAppBar(
            title = { Text("Create Habit") },
            navigationIcon = {
                IconButton(onClick = onDismiss) {
                    CloseIcon()
                }
            }
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(24.dp)
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
                    .padding(bottom = 16.dp),
                singleLine = true
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
            Button(
                onClick = {
                    isSaving = true
                    val newHabit = Habit(
                        id = "habit_${Random.nextInt(1000, 9999)}",
                        userId = "user_placeholder",
                        title = title,
                        description = if (description.isEmpty()) null else description,
                        frequency = frequency,
                        enabled = true
                    )
                    onHabitCreated(newHabit)
                    isSaving = false
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                enabled = !isSaving && title.isNotEmpty()
            ) {
                if (isSaving) {
                    CircularProgressIndicator(
                        color = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.size(20.dp)
                    )
                } else {
                    Text("Create Habit")
                }
            }
        }
    }
}









