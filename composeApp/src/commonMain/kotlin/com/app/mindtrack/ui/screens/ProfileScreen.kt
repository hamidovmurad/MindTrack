package com.app.mindtrack.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.app.mindtrack.ui.resources.BackIcon
import com.app.mindtrack.ui.resources.ProfileIcon
import com.app.mindtrack.ui.components.*

/**
 * Simple profile screen with user info and app preferences.
 */
@Composable
fun ProfileScreen(
    onBackClick: () -> Unit = {},
    onLogoutClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    WellnessScreenLayout(
        title = "Profile",
        navigationIcon = {
            IconButton(onClick = onBackClick) {
                BackIcon()
            }
        },
        modifier = modifier
    ) {
        val user = com.app.mindtrack.auth.LocalAuthManager.getCurrentUser()
        var name by remember { mutableStateOf(user?.name ?: "") }
        var email by remember { mutableStateOf(user?.email ?: "") }
        var statusMsg by remember { mutableStateOf("") }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .wellnessPadding(),
            verticalArrangement = Arrangement.spacedBy(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Profile card
            WellnessCard {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    ProfileIcon(modifier = Modifier.size(64.dp))
                    OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Full name") })
                    OutlinedTextField(value = email, onValueChange = { email = it }, label = { Text("Email") })
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(statusMsg, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }

            // Wellness summary
            WellnessCard {
                Column(modifier = Modifier.padding(24.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text("Wellness Summary", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.primary)
                    Text("Mood tracking: Active", style = MaterialTheme.typography.bodyMedium)
                    Text("Habit reminders: Enabled", style = MaterialTheme.typography.bodyMedium)
                    Text("Last sync: Demo mode", style = MaterialTheme.typography.bodyMedium)
                }
            }

            // App settings / actions
            WellnessCard {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Button(onClick = {
                        val ok = com.app.mindtrack.auth.LocalAuthManager.updateProfile(email.trim(), name.trim())
                        statusMsg = if (ok) "Profile saved" else "Failed to save"
                    }, modifier = Modifier.fillMaxWidth()) { Text("Save Profile") }

                    OutlinedButton(onClick = {
                        com.app.mindtrack.auth.LocalAuthManager.logout()
                        onLogoutClick()
                    }, modifier = Modifier.fillMaxWidth()) { Text("Logout & Clear Local Data") }
                }
            }

            WellnessSpacer(height = 16.dp)
        }
    }
}

