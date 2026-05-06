package com.app.mindtrack.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.app.mindtrack.model.Medication
import com.app.mindtrack.ui.resources.AddIcon
import com.app.mindtrack.ui.resources.DeleteIcon
import com.app.mindtrack.ui.resources.PharmacyIcon
import com.app.mindtrack.ui.resources.CloseIcon
import com.app.mindtrack.ui.resources.BackIcon
import com.app.mindtrack.ui.components.*
import kotlin.random.Random

/**
 * Medication management screen: list medications and add new ones.
 */
@Composable
fun MedicationScreen(
    medicationsState: List<Medication> = emptyList(),
    onMedicationDelete: (Medication) -> Unit = {},
    onAddMedicationClick: () -> Unit = {},
    onBackClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    WellnessScreenLayout(
        title = "Medications",
        navigationIcon = {
            IconButton(onClick = onBackClick) {
                BackIcon()
            }
        },
        actions = {
            IconButton(onClick = onAddMedicationClick) {
                AddIcon()
            }
        },
        isScrollable = false,  // LazyColumn handles scrolling
        modifier = modifier
    ) {
        if (medicationsState.isEmpty()) {
            // Empty state
            WellnessEmptyState(
                icon = { PharmacyIcon(modifier = Modifier.size(64.dp)) },
                title = "No medications",
                description = "Add your medications here",
                action = {
                    WellnessButton(
                        text = "Add Medication",
                        onClick = onAddMedicationClick
                    )
                }
            )
        } else {
            // Medications list
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(medicationsState) { medication ->
                    MedicationCard(
                        medication = medication,
                        onDelete = { onMedicationDelete(medication) }
                    )
                }
            }
        }
    }
}

/**
 * Individual medication card.
 */
@Composable
fun MedicationCard(
    medication: Medication,
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
            // Medication icon
            PharmacyIcon(
                modifier = Modifier
                    .size(40.dp)
                    .padding(end = 16.dp)
            )

            // Medication info
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 16.dp)
            ) {
                Text(
                    medication.name,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary
                )
                medication.dosage?.let {
                    if (it.isNotEmpty()) {
                        Text(
                            "Dosage: $it",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }
                }
                medication.schedule?.let {
                    if (it.isNotEmpty()) {
                        Text(
                            "Schedule: $it",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(top = 2.dp)
                        )
                    }
                }
                // Active status badge
                Row(modifier = Modifier.padding(top = 8.dp)) {
                    val statusColor = if (medication.active) Color(0xFF51CF66) else Color.Gray
                    WellnessTag(
                        text = if (medication.active) "Active" else "Inactive",
                        backgroundColor = statusColor.copy(alpha = 0.2f),
                        textColor = statusColor
                    )
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
 * Screen to add a new medication.
 */
@Composable
fun AddMedicationScreen(
    onMedicationCreated: (Medication) -> Unit = {},
    onDismiss: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    var name by remember { mutableStateOf("") }
    var dosage by remember { mutableStateOf("") }
    var schedule by remember { mutableStateOf("") }
    var isSaving by remember { mutableStateOf(false) }

    WellnessScreenLayout(
        title = "Add Medication",
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
                "Manage your medications",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            // Medication name
            WellnessTextField(
                value = name,
                onValueChange = { name = it },
                label = "Medication name",
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Dosage
            WellnessTextField(
                value = dosage,
                onValueChange = { dosage = it },
                label = "Dosage (optional)",
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Schedule
            OutlinedTextField(
                value = schedule,
                onValueChange = { schedule = it },
                label = { Text("Schedule (optional)") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .padding(bottom = 32.dp),
                placeholder = { Text("e.g., Twice daily with meals") },
                maxLines = 2
            )

            Spacer(modifier = Modifier.weight(1f))

            // Add button
            WellnessButton(
                text = "Add Medication",
                onClick = {
                    isSaving = true
                    val newMedication = Medication(
                        id = "med_${Random.nextInt(1000, 9999)}",
                        userId = "user_placeholder",
                        name = name,
                        dosage = if (dosage.isEmpty()) null else dosage,
                        schedule = if (schedule.isEmpty()) null else schedule,
                        active = true
                    )
                    onMedicationCreated(newMedication)
                    isSaving = false
                },
                enabled = !isSaving && name.isNotEmpty(),
                isLoading = isSaving
            )
        }
    }
}
