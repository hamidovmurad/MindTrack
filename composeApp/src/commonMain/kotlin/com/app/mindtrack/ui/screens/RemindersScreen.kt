package com.app.mindtrack.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.app.mindtrack.model.Habit
import com.app.mindtrack.ui.resources.BackIcon
import com.app.mindtrack.ui.resources.ReminderIcon
import kotlin.random.Random

private data class HabitReminder(
    val id: String,
    val title: String,
    val timeLabel: String,
    val enabled: Boolean = true
)

/**
 * Habit reminders screen.
 * Shows upcoming reminders and a simple form to add a reminder label/time.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RemindersScreen(
    habitsState: List<Habit> = emptyList(),
    onBackClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    var reminderTitle by remember { mutableStateOf("") }
    var reminderTime by remember { mutableStateOf("08:00 AM") }
    var reminders by remember {
        mutableStateOf(
            habitsState.mapIndexed { index, habit ->
                HabitReminder(
                    id = "rem_$index",
                    title = habit.title,
                    timeLabel = "Daily at 08:00 AM"
                )
            }
        )
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
    ) {
        TopAppBar(
            title = { Text("Habit Reminders") },
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
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(24.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        ReminderIcon(modifier = Modifier.size(28.dp))
                        Spacer(modifier = Modifier.width(12.dp))
                        Text("Upcoming reminders", style = MaterialTheme.typography.titleMedium)
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    if (reminders.isEmpty()) {
                        Text(
                            "No reminders yet. Add one below.",
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    } else {
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            reminders.forEach { reminder ->
                                Surface(
                                    tonalElevation = 2.dp,
                                    shape = MaterialTheme.shapes.medium,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Row(
                                        modifier = Modifier.padding(16.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Column(modifier = Modifier.weight(1f)) {
                                            Text(reminder.title, style = MaterialTheme.typography.titleSmall)
                                            Text(reminder.timeLabel, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                        }
                                        Switch(
                                            checked = reminder.enabled,
                                            onCheckedChange = { checked ->
                                                reminders = reminders.map {
                                                    if (it.id == reminder.id) it.copy(enabled = checked) else it
                                                }
                                            }
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(24.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text("Add reminder", style = MaterialTheme.typography.titleMedium)

                    OutlinedTextField(
                        value = reminderTitle,
                        onValueChange = { reminderTitle = it },
                        label = { Text("Habit / reminder title") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )

                    OutlinedTextField(
                        value = reminderTime,
                        onValueChange = { reminderTime = it },
                        label = { Text("Time label") },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("e.g. 08:00 AM") },
                        singleLine = true
                    )

                    Button(
                        onClick = {
                            if (reminderTitle.isNotBlank()) {
                                reminders = reminders + HabitReminder(
                                    id = "rem_${Random.nextInt(1000, 9999)}",
                                    title = reminderTitle,
                                    timeLabel = reminderTime
                                )
                                reminderTitle = ""
                                reminderTime = "08:00 AM"
                            }
                        },
                        enabled = reminderTitle.isNotBlank(),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Add Reminder")
                    }
                }
            }
        }
    }
}


