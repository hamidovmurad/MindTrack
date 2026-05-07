package com.app.mindtrack.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.foundation.background
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app.mindtrack.ui.resources.AddIcon
import com.app.mindtrack.ui.resources.BackIcon
import com.app.mindtrack.ui.resources.InfoIcon
import com.app.mindtrack.ui.resources.WaterIcon
import com.app.mindtrack.ui.components.*
import kotlin.math.roundToInt

/**
 * Daily water tracking screen with goal setting and progress visualization.
 */
@Composable
fun NutritionHydrationScreen(
    modifier: Modifier = Modifier
) {
    var goalMlText by remember { mutableStateOf("2000") }
    var intakeMl by remember { mutableStateOf(0) }
    var isEditingGoal by remember { mutableStateOf(false) }

    val goalMl = goalMlText.toIntOrNull()?.coerceAtLeast(1) ?: 2000
    val progress = (intakeMl.toFloat() / goalMl.toFloat()).coerceIn(0f, 1f)
    val cups = (intakeMl / 250f).roundToInt()

    val statusMessage = when {
        progress >= 1f -> "Goal reached! Stay hydrated! 🎉"
        progress >= 0.75f -> "Almost there! One more cup? 💧"
        progress >= 0.5f -> "Halfway to your goal! Keep it up. 👍"
        progress > 0f -> "Great start on your hydration journey."
        else -> "Start your day with a glass of water!"
    }

    WellnessScreenLayout(
        title = "Nutrition & Hydration",
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .wellnessPadding(),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // Main Progress Card
            WellnessCard {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Box(contentAlignment = Alignment.Center, modifier = Modifier.size(200.dp)) {
                        CircularProgressIndicator(
                            progress = { progress },
                            modifier = Modifier.fillMaxSize(),
                            strokeWidth = 12.dp,
                            trackColor = MaterialTheme.colorScheme.surfaceVariant,
                            strokeCap = StrokeCap.Round,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            WaterIcon(modifier = Modifier.size(48.dp))
                            Text(
                                "$intakeMl",
                                style = MaterialTheme.typography.headlineLarge.copy(fontWeight = FontWeight.ExtraBold),
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                "of $goalMl ml",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    Text(
                        statusMessage,
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.primary
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                "$cups",
                                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                            )
                            Text("Cups drunk", style = MaterialTheme.typography.labelSmall)
                        }
                        VerticalDivider(modifier = Modifier.height(32.dp))
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                "${(progress * 100).toInt()}%",
                                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                            )
                            Text("Daily Goal", style = MaterialTheme.typography.labelSmall)
                        }
                    }
                }
            }

            // Quick add buttons
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text(
                    "Quick Add",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.fillMaxWidth()) {
                    WaterAddButton(
                        amount = 250,
                        onClick = { intakeMl += 250 },
                        modifier = Modifier.weight(1f)
                    )
                    WaterAddButton(
                        amount = 500,
                        onClick = { intakeMl += 500 },
                        modifier = Modifier.weight(1f)
                    )
                }
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.fillMaxWidth()) {
                    WellnessOutlinedButton(
                        text = "Reset",
                        onClick = { intakeMl = 0 },
                        modifier = Modifier.weight(1f)
                    )
                    WellnessOutlinedButton(
                        text = "Edit Goal",
                        onClick = { isEditingGoal = true },
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            // Nutrition Tips
            WellnessCard {
                Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        InfoIcon(modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            "Hydration Tips",
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                        )
                    }
                    val tips = listOf(
                        "Drink a glass of water right after waking up.",
                        "Use a reusable bottle to track your intake easily.",
                        "Set reminders if you often forget to drink.",
                        "Foods like cucumber and melon are great for hydration."
                    )
                    tips.forEach { tip ->
                        Row(verticalAlignment = Alignment.Top, modifier = Modifier.padding(vertical = 2.dp)) {
                            Text("•", color = MaterialTheme.colorScheme.primary, modifier = Modifier.padding(end = 12.dp))
                            Text(tip, style = MaterialTheme.typography.bodyMedium)
                        }
                    }
                }
            }

            WellnessSpacer(height = 16.dp)
        }
    }

    if (isEditingGoal) {
        AlertDialog(
            onDismissRequest = { isEditingGoal = false },
            title = { Text("Set Daily Goal") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("How many milliliters of water do you want to drink per day?")
                    WellnessTextField(
                        value = goalMlText,
                        onValueChange = { goalMlText = it.filter { ch -> ch.isDigit() } },
                        label = "Goal (ml)"
                    )
                }
            },
            confirmButton = {
                TextButton(onClick = { isEditingGoal = false }) {
                    Text("Save")
                }
            },
            dismissButton = {
                TextButton(onClick = { isEditingGoal = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
private fun WaterAddButton(
    amount: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        onClick = onClick,
        modifier = modifier.height(64.dp),
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.7f)
    ) {
        Row(
            modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            WaterIcon(modifier = Modifier.size(24.dp))
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                "+$amount ml",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }
    }
}

