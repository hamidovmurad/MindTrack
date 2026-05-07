package com.app.mindtrack.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.foundation.background
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
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
                title = "No medications logged",
                description = "Keep track of your supplements and prescribed medicines here.",
                action = {
                    WellnessButton(
                        text = "Add First Medication",
                        onClick = onAddMedicationClick
                    )
                }
            )
        } else {
            // Medications list with summary
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    MedicationSummaryCard(medicationsState.count { it.active }, medicationsState.size)
                }

                item {
                    Text(
                        "Current Medications",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }

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

@Composable
private fun MedicationSummaryCard(activeCount: Int, total: Int) {
    WellnessCard {
        Row(
            modifier = Modifier.padding(20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                PharmacyIcon(modifier = Modifier.size(28.dp))
            }
            
            Column {
                Text(
                    "Medication Tracker",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )
                Text(
                    "$activeCount active medications of $total total",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
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
    WellnessCard(
        modifier = modifier
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                modifier = Modifier.size(48.dp),
                shape = CircleShape,
                color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    PharmacyIcon(modifier = Modifier.size(24.dp))
                }
            }

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 16.dp)
            ) {
                Text(
                    medication.name,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold)
                )
                
                if (!medication.dosage.isNullOrEmpty() || !medication.schedule.isNullOrEmpty()) {
                    val info = listOfNotNull(medication.dosage, medication.schedule).joinToString(" • ")
                    Text(
                        info,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(top = 2.dp),
                        maxLines = 1,
                        overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
                    )
                }

                Row(modifier = Modifier.padding(top = 8.dp)) {
                    WellnessTag(
                        text = if (medication.active) "Active" else "Inactive",
                        backgroundColor = if (medication.active) Color(0xFFE7F5E9) else Color(0xFFF1F3F5),
                        textColor = if (medication.active) Color(0xFF2B8A3E) else Color(0xFF495057)
                    )
                }
            }

            IconButton(onClick = onDelete) {
                DeleteIcon(modifier = Modifier.size(20.dp))
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
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Medication name") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            )


            // Dosage
            OutlinedTextField(
                value = dosage,
                onValueChange = { dosage = it },
                label = { Text("Dosage (optional)") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
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
