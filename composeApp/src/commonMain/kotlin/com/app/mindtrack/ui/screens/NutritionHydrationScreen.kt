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
import com.app.mindtrack.ui.resources.WaterIcon
import kotlin.math.roundToInt

/**
 * Daily water tracking screen with goal setting and progress visualization.
 */
@OptIn(ExperimentalMaterial3Api::class)
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

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
    ) {
        TopAppBar(title = { Text("Nutrition & Hydration") })

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        WaterIcon(modifier = Modifier.size(28.dp))
                        Spacer(modifier = Modifier.width(12.dp))
                        Text("Daily water tracker", style = MaterialTheme.typography.titleMedium)
                    }
                    Text("Set your daily goal and track how much you drink.", color = MaterialTheme.colorScheme.onSurfaceVariant)

                    OutlinedTextField(
                        value = goalMlText,
                        onValueChange = { goalMlText = it.filter { ch -> ch.isDigit() } },
                        label = { Text("Daily goal (ml)") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )

                    Text("Goal: $goalMl ml • Intake: $intakeMl ml")
                    LinearProgressIndicator(progress = { progress }, modifier = Modifier.fillMaxWidth())
                    Text("$cups cups of $goalCups cups")
                }
            }

            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text("Quick add", style = MaterialTheme.typography.titleMedium)
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                        Button(onClick = { intakeMl += 250 }, modifier = Modifier.weight(1f)) { Text("+250 ml") }
                        Button(onClick = { intakeMl += 500 }, modifier = Modifier.weight(1f)) { Text("+500 ml") }
                    }
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                        OutlinedButton(onClick = { intakeMl = 0 }, modifier = Modifier.weight(1f)) { Text("Reset") }
                        OutlinedButton(onClick = { intakeMl = (goalMl * 0.5f).toInt() }, modifier = Modifier.weight(1f)) { Text("Half goal") }
                    }
                }
            }

            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Nutrition tips", style = MaterialTheme.typography.titleMedium)
                    Text("• Keep a bottle near your desk")
                    Text("• Pair water with meals")
                    Text("• Track tea/coffee separately if needed")
                    Text("• Aim for small, repeated drinking goals")
                }
            }
        }
    }
}

