package com.app.mindtrack.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.app.mindtrack.ui.resources.ChevronRightIcon
import com.app.mindtrack.ui.resources.DocumentIcon
import com.app.mindtrack.ui.resources.InfoIcon
import com.app.mindtrack.ui.resources.ProfileIcon
import com.app.mindtrack.ui.resources.ReminderIcon
import com.app.mindtrack.ui.resources.QuestionIcon
import com.app.mindtrack.ui.resources.SettingsIcon

/**
 * Settings hub that links to Profile, Habit Reminders, Emergency Info, Terms, and FAQ.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onProfileClick: () -> Unit = {},
    onRemindersClick: () -> Unit = {},
    onEmergencyClick: () -> Unit = {},
    onTermsClick: () -> Unit = {},
    onFaqClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
    ) {
        TopAppBar(title = { Text("Settings") })

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Card(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier.padding(20.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    SettingsIcon(modifier = Modifier.size(32.dp))
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text("MindTrack settings", style = MaterialTheme.typography.titleMedium)
                        Text("Manage your app info and support screens", color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
            }

            SettingsItem(
                icon = { ProfileIcon() },
                title = "Profile",
                subtitle = "View your account details",
                onClick = onProfileClick
            )
            SettingsItem(
                icon = { ReminderIcon() },
                title = "Habit reminders",
                subtitle = "Open reminder setup",
                onClick = onRemindersClick
            )
            SettingsItem(
                icon = { InfoIcon() },
                title = "Emergency features",
                subtitle = "Basic info for support and safety",
                onClick = onEmergencyClick
            )
            SettingsItem(
                icon = { DocumentIcon() },
                title = "Terms & policy",
                subtitle = "Read app terms and privacy policy",
                onClick = onTermsClick
            )
            SettingsItem(
                icon = { QuestionIcon() },
                title = "FAQ",
                subtitle = "Common questions and answers",
                onClick = onFaqClick
            )
        }
    }
}

@Composable
private fun SettingsItem(
    icon: @Composable () -> Unit,
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            icon()
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(title, style = MaterialTheme.typography.titleSmall)
                Text(subtitle, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            ChevronRightIcon(modifier = Modifier.size(18.dp))
        }
    }
}

