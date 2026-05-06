package com.app.mindtrack.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app.mindtrack.ui.theme.FreshMint
import com.app.mindtrack.ui.theme.ForestTeal
import com.app.mindtrack.ui.theme.DeepOceanBlue
import com.app.mindtrack.ui.theme.AppBackground
import org.jetbrains.compose.resources.painterResource
import mindtrack.composeapp.generated.resources.Res
import mindtrack.composeapp.generated.resources.lotus
import mindtrack.composeapp.generated.resources.breathing_circle
import androidx.compose.foundation.BorderStroke
import com.app.mindtrack.auth.LocalAuthManager
import com.app.mindtrack.auth.User

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

    // Breathing animation for lotus icon
    val animatedScale by rememberInfiniteTransition().animateFloat(
        initialValue = 1f,
        targetValue = 1.1f,
        animationSpec = infiniteRepeatable(
            animation = tween(3000, easing = EaseInOutQuad),
            repeatMode = RepeatMode.Reverse
        )
    )


    Column(
        modifier = modifier
            .fillMaxSize()
            .background(AppBackground)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 32.dp, vertical = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        // Animated Lotus Icon (calming visual)
        Icon(
            painter = painterResource(Res.drawable.lotus),
            contentDescription = "MindTrack meditation",
            tint = ForestTeal,
            modifier = Modifier
                .size(100.dp)
                .scale(animatedScale)
                .alpha(0.9f)
        )

        Spacer(modifier = Modifier.height(20.dp))

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

        // Mode toggle: email verification vs local account
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Button(onClick = { isLocalMode = false }, modifier = Modifier.weight(1f)) {
                Text("Email Verification")
            }
            Button(onClick = { isLocalMode = true }, modifier = Modifier.weight(1f)) {
                Text("Local Account")
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        if (isLocalMode) {
            // Local register / login flow
            BreathingCard {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        TextButton(onClick = { isRegisterMode = true }) { Text("Register") }
                        TextButton(onClick = { isRegisterMode = false }) { Text("Login") }
                    }

                    if (isRegisterMode) {
                        TextField(
                            value = localName,
                            onValueChange = { localName = it },
                            label = { Text("Full Name") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true
                        )
                    }

                    TextField(
                        value = email,
                        onValueChange = { email = it },
                        label = { Text("Email Address") },
                        modifier = Modifier
                            .fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp),
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color(0xFFF5F5F5),
                            focusedIndicatorColor = FreshMint,
                            unfocusedIndicatorColor = Color.Transparent
                        )
                    )

                    TextField(
                        value = localPassword,
                        onValueChange = { localPassword = it },
                        label = { Text("Password") },
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp)
                    )

                    if (localError.isNotEmpty()) {
                        Text(localError, color = MaterialTheme.colorScheme.error)
                    }

                    Button(
                        onClick = {
                            localError = ""
                            if (isRegisterMode) {
                                if (email.isBlank() || localPassword.isBlank() || localName.isBlank()) {
                                    localError = "Please provide name, email and password"
                                    return@Button
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
                                    return@Button
                                }
                                val ok = LocalAuthManager.login(email.trim(), localPassword)
                                if (ok) onSignInSuccess() else localError = "Invalid credentials"
                            }
                        },
                        modifier = Modifier.fillMaxWidth().height(56.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = FreshMint,
                            contentColor = Color.White
                        )
                    ) {
                        Text(if (isRegisterMode) "Register & Continue" else "Login")
                    }

                    OutlinedButton(onClick = {
                        // Clear local stored data (logout / reset)
                        LocalAuthManager.logout()
                        localError = "Local data cleared"
                        email = ""
                        localPassword = ""
                        localName = ""
                    }, modifier = Modifier.fillMaxWidth()) {
                        Text("Clear Local Data / Logout")
                    }
                }
            }
        } else if (!isCodeSent) {
            // Step 1: Email Entry
            BreathingCard {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        "Enter your email",
                        style = MaterialTheme.typography.titleMedium,
                        color = DeepOceanBlue,
                        textAlign = TextAlign.Center
                    )

                    Text(
                        "We'll send a verification code to get you started",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Gray,
                        textAlign = TextAlign.Center
                    )

                    TextField(
                        value = email,
                        onValueChange = { email = it },
                        label = { Text("Email Address") },
                        modifier = Modifier
                            .fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp),
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color(0xFFF5F5F5),
                            focusedIndicatorColor = FreshMint,
                            unfocusedIndicatorColor = Color.Transparent
                        )
                    )

                    // Error message with caring tone
                    if (errorMessage.isNotEmpty()) {
                        Text(
                            errorMessage,
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(
                                    Color(0xFFFFEBEE),
                                    shape = RoundedCornerShape(12.dp)
                                )
                                .padding(12.dp),
                            textAlign = TextAlign.Center
                        )
                    }

                    // Send Code button with rounded style
                    Button(
                        onClick = {
                            isVerifying = true
                            errorMessage = ""
                            // TODO: Call Firebase Auth to send verification code
                            isCodeSent = true
                            isVerifying = false
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        enabled = !isVerifying && email.isNotEmpty() && email.contains("@"),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = FreshMint,
                            contentColor = Color.White
                        )
                    ) {
                        if (isVerifying) {
                            CircularProgressIndicator(
                                color = Color.White,
                                modifier = Modifier.size(20.dp),
                                strokeWidth = 2.dp
                            )
                        } else {
                            Text(
                                "Send Verification Code",
                                style = MaterialTheme.typography.labelLarge
                            )
                        }
                    }
                }
            }
        } else {
            // Step 2: Verification Code Entry
            BreathingCard {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        "Verify Your Identity",
                        style = MaterialTheme.typography.titleMedium,
                        color = DeepOceanBlue,
                        textAlign = TextAlign.Center
                    )

                    Text(
                        "Enter the verification code sent to:\n$email",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Gray,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    TextField(
                        value = verificationCode,
                        onValueChange = { verificationCode = it },
                        label = { Text("6-digit Code") },
                        modifier = Modifier
                            .fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp),
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color(0xFFF5F5F5),
                            focusedIndicatorColor = FreshMint,
                            unfocusedIndicatorColor = Color.Transparent
                        )
                    )

                    // Error message
                    if (errorMessage.isNotEmpty()) {
                        Text(
                            errorMessage,
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(
                                    Color(0xFFFFEBEE),
                                    shape = RoundedCornerShape(12.dp)
                                )
                                .padding(12.dp),
                            textAlign = TextAlign.Center
                        )
                    }

                    // Verify button
                    Button(
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
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        enabled = !isVerifying && verificationCode.length == 6,
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = FreshMint,
                            contentColor = Color.White
                        )
                    ) {
                        if (isVerifying) {
                            CircularProgressIndicator(
                                color = Color.White,
                                modifier = Modifier.size(20.dp),
                                strokeWidth = 2.dp
                            )
                        } else {
                            Text(
                                "Verify Code",
                                style = MaterialTheme.typography.labelLarge
                            )
                        }
                    }

                    // Back button with wellness styling
                    OutlinedButton(
                        onClick = {
                            isCodeSent = false
                            verificationCode = ""
                            errorMessage = ""
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = ForestTeal
                        ),
                        border = BorderStroke(2.dp, ForestTeal)
                    ) {
                        Text(
                            "Go Back",
                            style = MaterialTheme.typography.labelLarge
                        )
                    }
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

/**
 * Calming card container with soft shadow and rounded corners
 */
@Composable
private fun BreathingCard(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Surface(
        modifier = modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        color = Color.White
    ) {
        content()
    }
}
