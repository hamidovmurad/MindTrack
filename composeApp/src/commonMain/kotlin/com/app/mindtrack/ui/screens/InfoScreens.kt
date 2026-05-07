package com.app.mindtrack.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.app.mindtrack.ui.resources.*
import com.app.mindtrack.ui.components.*

@Composable
private fun InfoScreenTemplate(
    title: String,
    onBackClick: () -> Unit,
    content: @Composable ColumnScope.() -> Unit
) {
    WellnessScreenLayout(
        title = title,
        navigationIcon = {
            IconButton(onClick = onBackClick) {
                BackIcon()
            }
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .wellnessPadding(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            content()
        }
    }
}

@Composable
fun EmergencyInfoScreen(onBackClick: () -> Unit = {}) {
    InfoScreenTemplate(title = "Emergency Features", onBackClick = onBackClick) {
        WellnessCard {
            Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    InfoIcon(modifier = Modifier.size(24.dp))
                    Spacer(modifier = Modifier.width(12.dp))
                    Text("Support & Safety", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
                }
                Text(
                    "This section provides basic information for support and safety. In a real emergency, please call your local emergency services immediately.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        Text("Emergency Contacts", style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold), modifier = Modifier.padding(top = 8.dp))

        listOf(
            "Crisis Support Hotline" to "1-800-273-8255",
            "Local Emergency" to "911",
            "Wellness Mentor" to "+1 (555) 012-3456"
        ).forEach { (name, phone) ->
            WellnessCard {
                Row(
                    modifier = Modifier.padding(16.dp).fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(name, style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold))
                        Text(phone, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.primary)
                    }
                    IconButton(onClick = {}) {
                        AddIcon(modifier = Modifier.size(24.dp)) // Using AddIcon as a placeholder for a "Call" icon if not available
                    }
                }
            }
        }

        WellnessCard {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Safety Tip:", style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary))
                Text("Sharing your location with a trusted friend can provide extra peace of mind during difficult moments.", style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}

@Composable
fun TermsPolicyScreen(onBackClick: () -> Unit = {}) {
    InfoScreenTemplate(title = "Terms & Policy", onBackClick = onBackClick) {
        WellnessCard {
            Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    DocumentIcon(modifier = Modifier.size(24.dp))
                    Spacer(modifier = Modifier.width(12.dp))
                    Text("MindTrack Terms", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
                }
                
                Text(
                    "MindTrack is a student project and not a medical device. The information provided is for educational and demo purposes only.",
                    style = MaterialTheme.typography.bodyMedium
                )
                
                HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
                
                Text(
                    "Usage Policy",
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    "Use the app responsibly and consult a professional for urgent health concerns. We do not provide clinical advice.",
                    style = MaterialTheme.typography.bodySmall
                )

                Text(
                    "Data Privacy",
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    "Data is stored locally on your device for this demo. We do not collect or share your personal health data in this MVP phase.",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }

        Text(
            "Last updated: May 2026",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
    }
}

@Composable
fun FaqScreen(onBackClick: () -> Unit = {}) {
    InfoScreenTemplate(title = "FAQ", onBackClick = onBackClick) {
        val faqs = listOf(
            "Is the app fully connected to Firebase?" to "Not yet. The UI is ready and Firebase can be connected next for real-time cloud sync.",
            "Can I use the app without login?" to "Yes, the demo bypass is available for UI review and local tracking.",
            "Where is my data saved?" to "For now, it's saved in the app's local storage. In the next phase, it can be synced to Firebase Firestore.",
            "How often should I log my mood?" to "We recommend checking in at least once a day to build a clear trend of your wellness."
        )

        faqs.forEach { (question, answer) ->
            WellnessCard {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Row(verticalAlignment = Alignment.Top) {
                        QuestionIcon(modifier = Modifier.size(20.dp).padding(top = 2.dp))
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                question,
                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.primary
                            )
                            Text(
                                answer,
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }
                }
            }
        }
    }
}
