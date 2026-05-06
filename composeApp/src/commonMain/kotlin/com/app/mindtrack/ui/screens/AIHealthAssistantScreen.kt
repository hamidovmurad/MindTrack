package com.app.mindtrack.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.horizontalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.app.mindtrack.ui.resources.AssistantIcon
import com.app.mindtrack.ui.components.*

private data class ChatMessage(
    val text: String,
    val isUser: Boolean
)

/**
 * Simple placeholder AI Health Assistant screen.
 * Shows example wellness guidance and prompt chips.
 */
@Composable
fun AIHealthAssistantScreen(
    modifier: Modifier = Modifier
) {
    var prompt by remember { mutableStateOf("") }
    var messages by remember {
        mutableStateOf(
            listOf(
                ChatMessage("Hi, I'm your MindTrack assistant. I can help with mood, habits, hydration, and routine ideas.", false),
                ChatMessage("Try asking: 'How can I improve my sleep routine?'", false)
            )
        )
    }

    val quickPrompts = listOf(
        "Improve sleep",
        "Reduce stress",
        "Build a habit",
        "Drink more water"
    )

    WellnessScreenLayout(
        title = "Health Assistant",
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .wellnessPadding(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Introduction card
            WellnessCard {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(20.dp)
                ) {
                    AssistantIcon(modifier = Modifier.size(28.dp))
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text("Wellness assistant", style = MaterialTheme.typography.titleMedium)
                        Text("This is a placeholder AI screen for your final project.", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
            }

            // Chat messages
            WellnessCard {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text("Chat", style = MaterialTheme.typography.titleMedium, modifier = Modifier.padding(bottom = 4.dp))
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        messages.forEach { message ->
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = if (message.isUser) Arrangement.End else Arrangement.Start
                            ) {
                                Surface(
                                    color = if (message.isUser) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant,
                                    shape = MaterialTheme.shapes.large,
                                    modifier = Modifier.widthIn(max = 280.dp)
                                ) {
                                    Text(
                                        message.text,
                                        modifier = Modifier.padding(12.dp),
                                        color = if (message.isUser) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurfaceVariant,
                                        style = MaterialTheme.typography.bodySmall
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Quick prompts & input
            WellnessCard {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text("Quick prompts", style = MaterialTheme.typography.titleMedium)
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        quickPrompts.forEach { item ->
                            AssistChip(
                                onClick = {
                                    prompt = item
                                    messages = messages + ChatMessage(item, true) + ChatMessage(
                                        when (item) {
                                            "Improve sleep" -> "Try a fixed bedtime, reduce screen time 1 hour before bed, and keep your room cool."
                                            "Reduce stress" -> "Try 4-7-8 breathing, short walks, and logging your mood once a day."
                                            "Build a habit" -> "Start with one tiny action, same time daily, and track streaks."
                                            else -> "Set a water goal, keep a bottle nearby, and add reminders every 2 hours."
                                        },
                                        false
                                    )
                                },
                                label = { Text(item) }
                            )
                        }
                    }

                    WellnessSpacer(height = 8.dp)

                    WellnessTextField(
                        value = prompt,
                        onValueChange = { prompt = it },
                        label = "Ask the assistant"
                    )

                    WellnessButton(
                        text = "Send",
                        onClick = {
                            if (prompt.isNotBlank()) {
                                messages = messages + ChatMessage(prompt, true) + ChatMessage(
                                    "Thanks! For now, this assistant is a demo. Next step: connect your chosen AI service and personalize replies from mood and habit data.",
                                    false
                                )
                                prompt = ""
                            }
                        },
                        enabled = prompt.isNotBlank()
                    )
                }
            }

            WellnessSpacer(height = 16.dp)
        }
    }
}
