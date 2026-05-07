package com.app.mindtrack.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.background
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app.mindtrack.ui.resources.AssistantIcon
import com.app.mindtrack.ui.resources.ChevronRightIcon
import com.app.mindtrack.ui.components.*
import kotlinx.coroutines.launch
import com.app.mindtrack.ui.theme.FreshMint

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
    val scope = rememberCoroutineScope()
    val listState = rememberLazyListState()
    
    var messages by remember {
        mutableStateOf(
            listOf(
                ChatMessage("Hi! I'm your MindTrack AI assistant. I can help with mood tracking, habit formation, or just give you a wellness tip. What's on your mind?", false),
            )
        )
    }

    val quickPrompts = listOf(
        "How to sleep better?",
        "Tips for stress",
        "Habit ideas",
        "Water benefits"
    )

    fun sendMessage(text: String) {
        if (text.isBlank()) return
        messages = messages + ChatMessage(text, true)
        prompt = ""
        
        scope.launch {
            // Simulated AI thinking and response
            messages = messages + ChatMessage("...", false) // Typing indicator
            kotlinx.coroutines.delay(1000)
            messages = messages.filter { it.text != "..." }
            
            val response = when {
                text.contains("sleep", ignoreCase = true) -> "Consistency is key! Try going to bed at the same time every night and avoid screens 30 minutes before sleep."
                text.contains("stress", ignoreCase = true) -> "Taking 5 deep breaths can significantly lower cortisol. Would you like a quick breathing exercise?"
                text.contains("habit", ignoreCase = true) -> "The best way to build a habit is 'habit stacking'. Attach a new habit to an existing one, like doing 5 squats after brushing your teeth!"
                text.contains("water", ignoreCase = true) -> "Drinking water improves focus and energy. Try to drink a glass before every meal to stay on track."
                else -> "That's interesting! Tell me more about how you're feeling today, and I'll do my best to support you."
            }
            messages = messages + ChatMessage(response, false)
            listState.animateScrollToItem(messages.size - 1)
        }
    }

    WellnessScreenLayout(
        title = "Health Assistant",
        isScrollable = false,
        modifier = modifier
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Chat area
            LazyColumn(
                state = listState,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentPadding = PaddingValues(20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(messages) { message ->
                    ChatBubble(message)
                }
            }

            // Quick Prompts
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 8.dp)
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                quickPrompts.forEach { item ->
                    AssistChip(
                        onClick = { sendMessage(item) },
                        label = { Text(item) },
                        shape = RoundedCornerShape(16.dp),
                        colors = AssistChipDefaults.assistChipColors(
                            containerColor = MaterialTheme.colorScheme.surface,
                            labelColor = MaterialTheme.colorScheme.primary
                        )
                    )
                }
            }

            // Input area
            Surface(
                tonalElevation = 2.dp,
                shadowElevation = 8.dp,
                color = MaterialTheme.colorScheme.surface
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .padding(bottom = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    TextField(
                        value = prompt,
                        onValueChange = { prompt = it },
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(24.dp)),
                        placeholder = { Text("Ask me anything...") },
                        colors = TextFieldDefaults.colors(
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                        ),
                        maxLines = 3
                    )

                    IconButton(
                        onClick = { sendMessage(prompt) },
                        modifier = Modifier
                            .size(48.dp)
                            .clip(CircleShape)
                            .background(if (prompt.isNotBlank()) FreshMint else MaterialTheme.colorScheme.surfaceVariant),
                        enabled = prompt.isNotBlank()
                    ) {
                        ChevronRightIcon(
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ChatBubble(message: ChatMessage) {
    val isUser = message.isUser
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (isUser) Arrangement.End else Arrangement.Start
    ) {
        if (!isUser) {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.2f))
                    .align(Alignment.Bottom),
                contentAlignment = Alignment.Center
            ) {
                AssistantIcon(modifier = Modifier.size(18.dp))
            }
            Spacer(modifier = Modifier.width(8.dp))
        }

        Surface(
            color = if (isUser) FreshMint else MaterialTheme.colorScheme.surface,
            shape = RoundedCornerShape(
                topStart = 16.dp,
                topEnd = 16.dp,
                bottomStart = if (isUser) 16.dp else 4.dp,
                bottomEnd = if (isUser) 4.dp else 16.dp
            ),
            tonalElevation = if (isUser) 0.dp else 2.dp,
            shadowElevation = if (isUser) 2.dp else 0.dp,
            modifier = Modifier.widthIn(max = 280.dp)
        ) {
            Text(
                text = message.text,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
                color = if (isUser) Color.White else MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.bodyMedium.copy(lineHeight = 20.sp)
            )
        }
    }
}
