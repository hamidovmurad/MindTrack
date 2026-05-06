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

    // Breathing animation for lotus icon
    val animatedScale by rememberInfiniteTransition().animateFloat(
        initialValue = 1f,
        targetValue = 1.1f,
        animationSpec = infiniteRepeatable(
            animation = tween(3000, easing = EaseInOutQuad),
            repeatMode = RepeatMode.Reverse
        )
    )

    // Gradient background (calming blues and greens)
    val gradientBrush = Brush.verticalGradient(
        colors = listOf(
            Color(0xFFE8F5E9), // Light mint
            AppBackground
        )
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(gradientBrush)
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

        if (!isCodeSent) {
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
        color = Color.White,
        shadowElevation = 8.dp
    ) {
        content()
    }
}
