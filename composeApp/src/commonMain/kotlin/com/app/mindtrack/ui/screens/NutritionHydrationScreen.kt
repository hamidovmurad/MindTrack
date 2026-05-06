package com.app.mindtrack.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
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

    val goalMl = goalMlText.toIntOrNull()?.coerceAtLeast(1) ?: 2000
    val progress = (intakeMl.toFloat() / goalMl.toFloat()).coerceIn(0f, 1f)
    val cups = (intakeMl / 250f).roundToInt()
    val goalCups = (goalMl / 250f).roundToInt().coerceAtLeast(1)

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
            // Header card
            WellnessCard {
                Column(modifier = Modifier.padding(20.dp)) {
                    Row(verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
                        WaterIcon(modifier = Modifier.size(28.dp))
                        Spacer(modifier = Modifier.width(12.dp))
                        Text("Daily water tracker", style = MaterialTheme.typography.titleMedium)
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Set your daily goal and track how much you drink.", color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }

            // Goal setting card
            WellnessCard {
                Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    WellnessTextField(
                        value = goalMlText,
                        onValueChange = { goalMlText = it.filter { ch -> ch.isDigit() } },
                        label = "Daily goal (ml)"
                    )

                    Text("Goal: $goalMl ml • Intake: $intakeMl ml", style = MaterialTheme.typography.labelSmall)
                    LinearProgressIndicator(progress = { progress }, modifier = Modifier.fillMaxWidth())
                    Text("$cups cups of $goalCups cups", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }

            // Quick add buttons
            WellnessCard {
                Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text("Quick add", style = MaterialTheme.typography.titleMedium)
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                        WellnessButton(
                            text = "+250ml",
                            onClick = { intakeMl += 250 },
                            modifier = Modifier.weight(1f)
                        )
                        WellnessButton(
                            text = "+500ml",
                            onClick = { intakeMl += 500 },
                            modifier = Modifier.weight(1f)
                        )
                    }
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                        WellnessOutlinedButton(
                            text = "Reset",
                            onClick = { intakeMl = 0 },
                            modifier = Modifier.weight(1f)
                        )
                        WellnessOutlinedButton(
                            text = "Half goal",
                            onClick = { intakeMl = (goalMl * 0.5f).toInt() },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }

            // Tips card
            WellnessCard {
                Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Nutrition tips", style = MaterialTheme.typography.titleMedium)
                    Text("• Keep a bottle near your desk", style = MaterialTheme.typography.bodySmall)
                    Text("• Pair water with meals", style = MaterialTheme.typography.bodySmall)
                    Text("• Track tea/coffee separately if needed", style = MaterialTheme.typography.bodySmall)
                    Text("• Aim for small, repeated drinking goals", style = MaterialTheme.typography.bodySmall)
                }
            }

            WellnessSpacer(height = 16.dp)
        }
    }
}

