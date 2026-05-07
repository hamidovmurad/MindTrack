package com.app.mindtrack.ui.screens

import androidx.compose.animation.core.EaseInOutQuad
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app.mindtrack.auth.LocalAuthManager
import com.app.mindtrack.ui.components.WellnessButton
import com.app.mindtrack.ui.components.WellnessCard
import com.app.mindtrack.ui.components.WellnessOutlinedButton
import com.app.mindtrack.ui.components.WellnessTextField
import com.app.mindtrack.ui.theme.AppBackground
import com.app.mindtrack.ui.theme.DeepOceanBlue
import com.app.mindtrack.ui.theme.ForestTeal
import com.app.mindtrack.ui.theme.FreshMint
import mindtrack.composeapp.generated.resources.Res
import mindtrack.composeapp.generated.resources.img_face_happy
import mindtrack.composeapp.generated.resources.img_logo
import mindtrack.composeapp.generated.resources.lotus
import org.jetbrains.compose.resources.painterResource

/**
 * Wellness-focused email sign-in screen for MindTrack.
 * Meditation-style design with calming visuals and motivational messaging.
 * Uses Firebase email verification.
 * TODO: Integrate with Firebase Authentication.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignInScreen(
    onSignInSuccess: () -> Unit = {},
    onBypassLoginClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    var email by remember { mutableStateOf("") }
    var verificationCode by remember { mutableStateOf("") }
    var isCodeSent by remember { mutableStateOf(false) }
    var isVerifying by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    var isLocalMode by remember { mutableStateOf(false) }
    var isRegisterMode by remember { mutableStateOf(true) }
    var localName by remember { mutableStateOf("") }
    var localPassword by remember { mutableStateOf("") }
    var localError by remember { mutableStateOf("") }


    Column(
        modifier = modifier
            .fillMaxSize()
            .background(AppBackground)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 32.dp, vertical = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Spacer(modifier = Modifier.height(72.dp))



        // Header - Wellness focused
        Text(
            "Start Your Wellness Journey",
            style = MaterialTheme.typography.headlineLarge.copy(
                fontSize = 28.sp,
                letterSpacing = 0.5.sp
            ),
            color = DeepOceanBlue,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp),
            textAlign = TextAlign.Center
        )

        // Motivational tagline
        Text(
            "Take the first step towards better mental health",
            style = MaterialTheme.typography.bodyLarge.copy(
                fontSize = 16.sp,
                lineHeight = 24.sp
            ),
            color = ForestTeal,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Mode toggle: modern tab-style
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            shape = RoundedCornerShape(24.dp),
            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
        ) {
            Row(modifier = Modifier.fillMaxSize()) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .clip(RoundedCornerShape(24.dp))
                        .background(if (!isLocalMode) FreshMint else Color.Transparent)
                        .clickable { isLocalMode = false },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "Email Link",
                        style = MaterialTheme.typography.labelLarge,
                        color = if (!isLocalMode) Color.White else ForestTeal
                    )
                }
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .clip(RoundedCornerShape(24.dp))
                        .background(if (isLocalMode) FreshMint else Color.Transparent)
                        .clickable { isLocalMode = true },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "Local Account",
                        style = MaterialTheme.typography.labelLarge,
                        color = if (isLocalMode) Color.White else ForestTeal
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        if (isLocalMode) {
            // Local register / login flow
            WellnessCard {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Register / Login sub-toggle
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        TextButton(
                            onClick = { isRegisterMode = true },
                            colors = ButtonDefaults.textButtonColors(
                                contentColor = if (isRegisterMode) FreshMint else Color.Gray
                            )
                        ) {
                            Text("Register", fontWeight = if (isRegisterMode) FontWeight.Bold else FontWeight.Normal)
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                        TextButton(
                            onClick = { isRegisterMode = false },
                            colors = ButtonDefaults.textButtonColors(
                                contentColor = if (!isRegisterMode) FreshMint else Color.Gray
                            )
                        ) {
                            Text("Login", fontWeight = if (!isRegisterMode) FontWeight.Bold else FontWeight.Normal)
                        }
                    }

                    if (isRegisterMode) {
                        WellnessTextField(
                            value = localName,
                            onValueChange = { localName = it },
                            label = "Full Name"
                        )
                    }

                    WellnessTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = "Email Address",
                        keyboardType = KeyboardType.Email
                    )

                    TextField(
                        value = localPassword,
                        onValueChange = { localPassword = it },
                        label = { Text("Password") },
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        visualTransformation = PasswordVisualTransformation(),
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp),
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color(0xFFF5F5F5),
                            focusedIndicatorColor = FreshMint,
                            unfocusedIndicatorColor = Color.Transparent
                        )
                    )

                    if (localError.isNotEmpty()) {
                        Text(
                            localError,
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodySmall,
                            textAlign = TextAlign.Center
                        )
                    }

                    WellnessButton(
                        text = if (isRegisterMode) "Register & Continue" else "Login",
                        onClick = {
                            localError = ""
                            if (isRegisterMode) {
                                if (email.isBlank() || localPassword.isBlank() || localName.isBlank()) {
                                    localError = "Please provide name, email and password"
                                    return@WellnessButton
                                }
                                val ok = LocalAuthManager.register(email.trim(), localPassword, localName.trim())
                                if (ok) {
                                    onSignInSuccess()
                                } else {
                                    localError = "An account already exists"
                                }
                            } else {
                                if (email.isBlank() || localPassword.isBlank()) {
                                    localError = "Please provide email and password"
                                    return@WellnessButton
                                }
                                val ok = LocalAuthManager.login(email.trim(), localPassword)
                                if (ok) onSignInSuccess() else localError = "Invalid credentials"
                            }
                        }
                    )

                    TextButton(onClick = {
                        LocalAuthManager.logout()
                        localError = "Local data cleared"
                        email = ""
                        localPassword = ""
                        localName = ""
                    }) {
                        Text("Reset / Logout", color = Color.Gray, style = MaterialTheme.typography.labelSmall)
                    }
                }
            }
        } else if (!isCodeSent) {
            // Step 1: Email Entry
            WellnessCard {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        "Enter your email",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = DeepOceanBlue,
                        textAlign = TextAlign.Center
                    )

                    Text(
                        "We'll send a verification code to get you started",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Gray,
                        textAlign = TextAlign.Center
                    )

                    WellnessTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = "Email Address",
                        keyboardType = KeyboardType.Email
                    )

                    if (errorMessage.isNotEmpty()) {
                        Text(
                            errorMessage,
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color(0xFFFFEBEE), shape = RoundedCornerShape(12.dp))
                                .padding(12.dp),
                            textAlign = TextAlign.Center
                        )
                    }

                    WellnessButton(
                        text = "Send Verification Code",
                        onClick = {
                            isVerifying = true
                            errorMessage = ""
                            // Simulate code sending
                            isCodeSent = true
                            isVerifying = false
                        },
                        enabled = !isVerifying && email.isNotEmpty() && email.contains("@"),
                        isLoading = isVerifying
                    )
                }
            }
        } else {
            // Step 2: Verification Code Entry
            WellnessCard {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        "Verify Your Identity",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = DeepOceanBlue,
                        textAlign = TextAlign.Center
                    )

                    Text(
                        "Enter the verification code sent to:\n$email",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Gray,
                        textAlign = TextAlign.Center
                    )

                    WellnessTextField(
                        value = verificationCode,
                        onValueChange = { verificationCode = it },
                        label = "6-digit Code",
                        keyboardType = KeyboardType.Number
                    )

                    if (errorMessage.isNotEmpty()) {
                        Text(
                            errorMessage,
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color(0xFFFFEBEE), shape = RoundedCornerShape(12.dp))
                                .padding(12.dp),
                            textAlign = TextAlign.Center
                        )
                    }

                    WellnessButton(
                        text = "Verify Code",
                        onClick = {
                            isVerifying = true
                            errorMessage = ""
                            if (verificationCode.length == 6) {
                                onSignInSuccess()
                            } else {
                                errorMessage = "Code must be 6 digits"
                                isVerifying = false
                            }
                        },
                        enabled = !isVerifying && verificationCode.length == 6,
                        isLoading = isVerifying
                    )

                    WellnessOutlinedButton(
                        text = "Go Back",
                        onClick = {
                            isCodeSent = false
                            verificationCode = ""
                            errorMessage = ""
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(40.dp))

        // Bypass login for demo (wellness-friendly text)
        TextButton(
            onClick = onBypassLoginClick,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            Text(
                "Explore all features (demo mode)",
                style = MaterialTheme.typography.labelSmall,
                color = ForestTeal
            )
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}
