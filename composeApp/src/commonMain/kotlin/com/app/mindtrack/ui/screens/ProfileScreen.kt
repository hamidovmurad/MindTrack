package com.app.mindtrack.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app.mindtrack.ui.resources.*
import com.app.mindtrack.ui.theme.FreshMint
import com.app.mindtrack.auth.LocalAuthManager
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
        title = "My Profile",
        navigationIcon = {
            IconButton(onClick = onBackClick) {
                BackIcon()
            }
        },
        modifier = modifier
    ) {
        val user = LocalAuthManager.getCurrentUser()
        var name by remember { mutableStateOf(user?.name ?: "") }
        var email by remember { mutableStateOf(user?.email ?: "") }
        var statusMsg by remember { mutableStateOf("") }
        val joinDate = "Member since 2026" // Demo data

        Column(
            modifier = Modifier
                .fillMaxSize()
                .wellnessPadding(),
            verticalArrangement = Arrangement.spacedBy(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Profile Header
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(vertical = 8.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Surface(
                        modifier = Modifier.size(100.dp),
                        shape = CircleShape,
                        color = MaterialTheme.colorScheme.primaryContainer
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            ProfileIcon(modifier = Modifier.size(50.dp))
                        }
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    name.ifEmpty { "Wellness Traveler" },
                    style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold)
                )
                Text(
                    joinDate,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            // Information Section
            WellnessCard {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        "Personal Information",
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                    )
                    
                    WellnessTextField(
                        value = name,
                        onValueChange = { name = it },
                        label = "Full Name"
                    )
                    
                    WellnessTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = "Email Address"
                    )

                    if (statusMsg.isNotEmpty()) {
                        Text(
                            statusMsg,
                            style = MaterialTheme.typography.labelSmall,
                            color = if (statusMsg.contains("saved")) FreshMint else MaterialTheme.colorScheme.error,
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                        )
                    }

                    WellnessButton(
                        text = "Update Profile",
                        onClick = {
                            val ok = LocalAuthManager.updateProfile(email.trim(), name.trim())
                            statusMsg = if (ok) "Profile saved successfully" else "Failed to save profile"
                        }
                    )
                }
            }

            // Wellness Stats Summary
            WellnessCard {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        "Wellness Status",
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        ProfileStatItem(
                            icon = { TrendingUpIcon(modifier = Modifier.size(20.dp)) },
                            label = "Mood",
                            value = "Active",
                            modifier = Modifier.weight(1f)
                        )
                        ProfileStatItem(
                            icon = { HabitIcon(modifier = Modifier.size(20.dp)) },
                            label = "Habits",
                            value = "4 Daily",
                            modifier = Modifier.weight(1f)
                        )
                        ProfileStatItem(
                            icon = { ReminderIcon(modifier = Modifier.size(20.dp)) },
                            label = "Sync",
                            value = "Cloud",
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }

            // Account Actions
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(
                    onClick = {
                        LocalAuthManager.logout()
                        onLogoutClick()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.error),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.error)
                ) {
                    Text("Logout & Clear Data", style = MaterialTheme.typography.labelLarge)
                }
            }

            WellnessSpacer(height = 24.dp)
        }
    }
}

@Composable
private fun ProfileStatItem(
    icon: @Composable () -> Unit,
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            icon()
            Text(label, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Text(value, style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold))
        }
    }
}

