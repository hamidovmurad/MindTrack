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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.app.mindtrack.model.Medication
import com.app.mindtrack.ui.resources.AddIcon
import com.app.mindtrack.ui.resources.DeleteIcon
import com.app.mindtrack.ui.resources.PharmacyIcon
import com.app.mindtrack.ui.resources.CloseIcon
import com.app.mindtrack.ui.resources.BackIcon
import kotlin.random.Random

/**
 * Medication management screen: list medications and add new ones.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MedicationScreen(
    medicationsState: List<Medication> = emptyList(),
    onMedicationDelete: (Medication) -> Unit = {},
    onAddMedicationClick: () -> Unit = {},
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
            title = { Text("Medications") },
            navigationIcon = {
                IconButton(onClick = onBackClick) {
                    BackIcon()
                }
            },
            actions = {
                IconButton(onClick = onAddMedicationClick) {
                    AddIcon()
                }
            }
        )

        if (medicationsState.isEmpty()) {
            // Empty state
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                PharmacyIcon(
                    modifier = Modifier
                        .size(64.dp)
                        .padding(bottom = 16.dp)
                )
                Text(
                    "No medications",
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Text(
                    "Add your medications here",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(bottom = 24.dp)
                )
                Button(onClick = onAddMedicationClick) {
                    Text("Add Medication")
                }
            }
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
                    val textColor = if (medication.active) Color(0xFF51CF66) else Color.Gray
                    Surface(
                        color = statusColor.copy(alpha = 0.2f),
                        shape = ButtonDefaults.shape,
                    ) {
                        Text(
                            if (medication.active) "Active" else "Inactive",
                            style = MaterialTheme.typography.labelSmall,
                            color = textColor,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
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
 * Screen to add a new medication.
 */
@OptIn(ExperimentalMaterial3Api::class)
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

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        TopAppBar(
            title = { Text("Add Medication") },
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
                "Manage your medications",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            // Medication name
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Medication name") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                singleLine = true
            )

            // Dosage
            OutlinedTextField(
                value = dosage,
                onValueChange = { dosage = it },
                label = { Text("Dosage (optional)") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                placeholder = { Text("e.g., 500mg") },
                singleLine = true
            )

            // Schedule
            OutlinedTextField(
                value = schedule,
                onValueChange = { schedule = it },
                label = { Text("Schedule (optional)") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 32.dp),
                placeholder = { Text("e.g., Twice daily with meals") },
                maxLines = 2
            )

            Spacer(modifier = Modifier.weight(1f))

            // Add button
            Button(
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
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                enabled = !isSaving && name.isNotEmpty()
            ) {
                if (isSaving) {
                    CircularProgressIndicator(
                        color = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.size(20.dp)
                    )
                } else {
                    Text("Add Medication")
                }
            }
        }
    }
}













