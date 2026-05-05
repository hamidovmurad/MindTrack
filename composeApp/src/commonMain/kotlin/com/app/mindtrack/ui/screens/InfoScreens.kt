package com.app.mindtrack.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.app.mindtrack.ui.resources.BackIcon

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun infoScreenTitle(title: String, onBackClick: () -> Unit, content: @Composable ColumnScope.() -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
    ) {
        TopAppBar(
            title = { Text(title) },
            navigationIcon = {
                IconButton(onClick = onBackClick) {
                    BackIcon()
                }
            }
        )

        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            content()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmergencyInfoScreen(onBackClick: () -> Unit = {}) {
    infoScreenTitle(title = "Emergency Features", onBackClick = onBackClick) {
        Text("This is a basic information screen for the final project.", style = MaterialTheme.typography.bodyLarge)
        Text("Suggested content:", style = MaterialTheme.typography.titleMedium)
        Text("• Emergency contact list")
        Text("• Quick call / quick message")
        Text("• Location sharing")
        Text("• Crisis support hotline links")
        Text("In MVP, keep this as informational only.", color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TermsPolicyScreen(onBackClick: () -> Unit = {}) {
    infoScreenTitle(title = "Terms & Policy", onBackClick = onBackClick) {
        Text("MindTrack is a student project and not a medical device.")
        Text("Use the app responsibly and consult a professional for urgent health concerns.")
        Text("Data privacy: store only what is needed for the demo and keep user consent visible.")
        Text("You can later replace this text with your final terms and privacy policy.")
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FaqScreen(onBackClick: () -> Unit = {}) {
    infoScreenTitle(title = "FAQ", onBackClick = onBackClick) {
        Text("Q: Is the app fully connected to Firebase?", style = MaterialTheme.typography.titleSmall)
        Text("A: Not yet. The UI is ready and Firebase can be connected next.")
        Spacer(modifier = Modifier.height(8.dp))
        Text("Q: Can I use the app without login?", style = MaterialTheme.typography.titleSmall)
        Text("A: Yes, the demo bypass is available for UI review.")
        Spacer(modifier = Modifier.height(8.dp))
        Text("Q: Where is my data saved?", style = MaterialTheme.typography.titleSmall)
        Text("A: In the next phase, it can be saved to Firebase Firestore.")
    }
}


