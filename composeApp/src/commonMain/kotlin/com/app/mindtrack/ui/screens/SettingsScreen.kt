package com.app.mindtrack.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.app.mindtrack.ui.components.WellnessCard
import com.app.mindtrack.ui.components.WellnessScreenLayout
import com.app.mindtrack.ui.components.WellnessSpacer
import com.app.mindtrack.ui.components.wellnessPadding
import com.app.mindtrack.ui.resources.ChevronRightIcon
import com.app.mindtrack.ui.resources.CloseIcon
import com.app.mindtrack.ui.resources.DocumentIcon
import com.app.mindtrack.ui.resources.InfoIcon
import com.app.mindtrack.ui.resources.ProfileIcon
import com.app.mindtrack.ui.resources.QuestionIcon
import com.app.mindtrack.ui.resources.ReminderIcon

/**
 * Settings hub that links to Profile, Habit Reminders, Emergency Info, Terms, and FAQ.
 */
@Composable
fun SettingsScreen(
    onProfileClick: () -> Unit = {},
    onRemindersClick: () -> Unit = {},
    onEmergencyClick: () -> Unit = {},
    onTermsClick: () -> Unit = {},
    onFaqClick: () -> Unit = {},
    onLogoutClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    WellnessScreenLayout(
        title = "Settings",
        modifier = modifier,
        isScrollable = false
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .wellnessPadding(),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {

            // Header
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    "App Settings",
                    style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold)
                )
                Text(
                    "Personalize your wellness experience",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            // Account Section
            SettingsSection(title = "Account") {
                SettingsItem(
                    icon = { ProfileIcon(modifier = Modifier.size(24.dp)) },
                    title = "Profile",
                    subtitle = "Manage your account and info",
                    onClick = onProfileClick
                )
                SettingsItem(
                    icon = { ReminderIcon(modifier = Modifier.size(24.dp)) },
                    title = "Habit Reminders",
                    subtitle = "Schedule your daily routines",
                    onClick = onRemindersClick
                )
            }

            // Support & Safety
            SettingsSection(title = "Support & Safety") {
                SettingsItem(
                    icon = { InfoIcon(modifier = Modifier.size(24.dp)) },
                    title = "Emergency Features",
                    subtitle = "Support and safety resources",
                    onClick = onEmergencyClick
                )
                SettingsItem(
                    icon = { QuestionIcon(modifier = Modifier.size(24.dp)) },
                    title = "FAQ",
                    subtitle = "Common questions and answers",
                    onClick = onFaqClick
                )
            }

            // Legal & About
            SettingsSection(title = "Legal") {
                SettingsItem(
                    icon = { DocumentIcon(modifier = Modifier.size(24.dp)) },
                    title = "Terms & Policy",
                    subtitle = "Privacy and usage terms",
                    onClick = onTermsClick
                )
            }

            // Logout
            Surface(
                onClick = onLogoutClick,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                color = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.1f),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.error.copy(alpha = 0.2f))
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .background(MaterialTheme.colorScheme.error.copy(alpha = 0.1f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        CloseIcon(modifier = Modifier.size(20.dp))
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            "Logout",
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.error
                        )
                        Text(
                            "Sign out of your account",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.error.copy(alpha = 0.7f)
                        )
                    }
                }
            }

            WellnessSpacer(height = 32.dp)
        }
    }
}

@Composable
private fun SettingsSection(
    title: String,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text(
            title,
            style = MaterialTheme.typography.labelLarge.copy(
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            ),
            modifier = Modifier.padding(horizontal = 4.dp)
        )
        WellnessCard {
            Column(content = content)
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
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Surface(
            modifier = Modifier.size(40.dp),
            shape = CircleShape,
            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
        ) {
            Box(contentAlignment = Alignment.Center) {
                icon()
            }
        }
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(title, style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.SemiBold))
            Text(subtitle, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        ChevronRightIcon(modifier = Modifier.size(20.dp))
    }
}

