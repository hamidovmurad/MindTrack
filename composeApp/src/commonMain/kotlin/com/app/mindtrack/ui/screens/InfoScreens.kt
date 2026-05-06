package com.app.mindtrack.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.app.mindtrack.ui.resources.BackIcon
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
        Text("This is a basic information screen for the final project.", style = MaterialTheme.typography.bodyLarge)
        Text("Suggested content:", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.primary)
        Text("• Emergency contact list", style = MaterialTheme.typography.bodyMedium)
        Text("• Quick call / quick message", style = MaterialTheme.typography.bodyMedium)
        Text("• Location sharing", style = MaterialTheme.typography.bodyMedium)
        Text("• Crisis support hotline links", style = MaterialTheme.typography.bodyMedium)
        Text("In MVP, keep this as informational only.", color = MaterialTheme.colorScheme.onSurfaceVariant, style = MaterialTheme.typography.bodySmall)
    }
}

@Composable
fun TermsPolicyScreen(onBackClick: () -> Unit = {}) {
    InfoScreenTemplate(title = "Terms & Policy", onBackClick = onBackClick) {
        Text("MindTrack is a student project and not a medical device.", style = MaterialTheme.typography.bodyMedium)
        Text("Use the app responsibly and consult a professional for urgent health concerns.", style = MaterialTheme.typography.bodyMedium)
        Text("Data privacy: store only what is needed for the demo and keep user consent visible.", style = MaterialTheme.typography.bodyMedium)
        Text("You can later replace this text with your final terms and privacy policy.", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}

@Composable
fun FaqScreen(onBackClick: () -> Unit = {}) {
    InfoScreenTemplate(title = "FAQ", onBackClick = onBackClick) {
        Text("Q: Is the app fully connected to Firebase?", style = MaterialTheme.typography.titleSmall, color = MaterialTheme.colorScheme.primary)
        Text("A: Not yet. The UI is ready and Firebase can be connected next.", style = MaterialTheme.typography.bodySmall)

        WellnessSpacer(height = 8.dp)

        Text("Q: Can I use the app without login?", style = MaterialTheme.typography.titleSmall, color = MaterialTheme.colorScheme.primary)
        Text("A: Yes, the demo bypass is available for UI review.", style = MaterialTheme.typography.bodySmall)

        WellnessSpacer(height = 8.dp)

        Text("Q: Where is my data saved?", style = MaterialTheme.typography.titleSmall, color = MaterialTheme.colorScheme.primary)
        Text("A: In the next phase, it can be saved to Firebase Firestore.", style = MaterialTheme.typography.bodySmall)
    }
}
