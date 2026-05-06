package com.app.mindtrack.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.KeyboardType
import com.app.mindtrack.ui.theme.DeepOceanBlue
import com.app.mindtrack.ui.theme.FreshMint
import com.app.mindtrack.ui.theme.ForestTeal
import com.app.mindtrack.ui.theme.AppBackground

/**
 * Wellness Screen Layout - Main container for all screens with gradient background and scrolling
 * Handles common layout patterns to avoid repetition
 *
 * @param isScrollable Set to false when content contains LazyColumn, LazyRow, or other scrollable containers
 */
@Composable
fun WellnessScreenLayout(
    title: String = "",
    navigationIcon: @Composable (() -> Unit)? = null,
    actions: @Composable (RowScope.() -> Unit)? = null,
    showTopBar: Boolean = true,
    isScrollable: Boolean = true,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    val gradientBrush = Brush.verticalGradient(
        colors = listOf(
            Color(0xFFE8F5E9),
            AppBackground
        )
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(gradientBrush)
    ) {
        if (showTopBar) {
            WellnessTopAppBar(
                title = title,
                navigationIcon = navigationIcon,
                actions = actions
            )
        }

        if (isScrollable) {
            // For static content - apply vertical scroll
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {
                content()
            }
        } else {
            // For scrollable content (LazyColumn, LazyRow, etc.)
            Box(modifier = Modifier.fillMaxSize()) {
                content()
            }
        }
    }
}

/**
 * Wellness Top App Bar - Consistent styling across all screens
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WellnessTopAppBar(
    title: String,
    navigationIcon: @Composable (() -> Unit)? = null,
    actions: @Composable (RowScope.() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    TopAppBar(
        title = {
            Text(
                title,
                style = MaterialTheme.typography.headlineMedium,
                color = DeepOceanBlue
            )
        },
        navigationIcon = navigationIcon ?: {},
        actions = actions ?: {},
        modifier = modifier,
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.White,
            titleContentColor = DeepOceanBlue,
            navigationIconContentColor = DeepOceanBlue,
            actionIconContentColor = DeepOceanBlue
        )
    )
}

/**
 * Wellness Card - Soft-cornered card with elevation and calming styling
 */
@Composable
fun WellnessCard(
    modifier: Modifier = Modifier,
    elevation: Dp = 8.dp,
    content: @Composable () -> Unit
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        color = Color.White,
        shadowElevation = elevation
    ) {
        content()
    }
}

/**
 * Wellness Button - Primary action button with rounded corners and FreshMint color
 */
@Composable
fun WellnessButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    isLoading: Boolean = false,
    leadingIcon: @Composable (() -> Unit)? = null
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp),
        enabled = enabled && !isLoading,
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = FreshMint,
            contentColor = Color.White
        )
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                color = Color.White,
                modifier = Modifier.size(20.dp),
                strokeWidth = 2.dp
            )
        } else {
            if (leadingIcon != null) {
                leadingIcon()
                Spacer(modifier = Modifier.width(8.dp))
            }
            Text(
                text,
                style = MaterialTheme.typography.labelLarge
            )
        }
    }
}

/**
 * Wellness Outlined Button - Secondary action button
 */
@Composable
fun WellnessOutlinedButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    leadingIcon: @Composable (() -> Unit)? = null
) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp),
        enabled = enabled,
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.outlinedButtonColors(
            contentColor = ForestTeal
        ),
        border = BorderStroke(2.dp, ForestTeal)
    ) {
        if (leadingIcon != null) {
            leadingIcon()
            Spacer(modifier = Modifier.width(8.dp))
        }
        Text(
            text,
            style = MaterialTheme.typography.labelLarge
        )
    }
}

/**
 * Wellness Text Field - Styled input with calming appearance
 */
@Composable
fun WellnessTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    placeholder: String = "",
    singleLine: Boolean = true,
    maxLines: Int = 1,
    keyboardType: KeyboardType = KeyboardType.Text
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        modifier = modifier.fillMaxWidth(),
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        singleLine = singleLine,
        maxLines = maxLines,
        shape = RoundedCornerShape(12.dp),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color.White,
            unfocusedContainerColor = Color(0xFFF5F5F5),
            focusedIndicatorColor = FreshMint,
            unfocusedIndicatorColor = Color.Transparent
        )
    )
}

/**
 * Wellness Empty State - Centered message for empty lists
 */
@Composable
fun WellnessEmptyState(
    icon: @Composable () -> Unit,
    title: String,
    description: String,
    modifier: Modifier = Modifier,
    action: @Composable (() -> Unit)? = null
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Icon with breathing animation
        val animatedScale by rememberInfiniteTransition().animateFloat(
            initialValue = 1f,
            targetValue = 1.08f,
            animationSpec = infiniteRepeatable(
                animation = tween(3000, easing = EaseInOutQuad),
                repeatMode = RepeatMode.Reverse
            )
        )

        Box(
            modifier = Modifier
                .scale(animatedScale)
                .alpha(0.8f),
            contentAlignment = Alignment.Center
        ) {
            icon()
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            title,
            style = MaterialTheme.typography.headlineSmall,
            color = DeepOceanBlue,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Text(
            description,
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Gray,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        if (action != null) {
            action()
        }
    }
}

/**
 * Wellness Info Card - For displaying statistics or key information
 */
@Composable
fun WellnessInfoCard(
    label: String,
    value: String,
    modifier: Modifier = Modifier,
    valueColor: Color = FreshMint,
    icon: @Composable (() -> Unit)? = null
) {
    WellnessCard(modifier = modifier) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (icon != null) {
                icon()
                Spacer(modifier = Modifier.height(8.dp))
            }

            Text(
                label,
                style = MaterialTheme.typography.labelSmall,
                color = Color.Gray,
                modifier = Modifier.padding(bottom = 4.dp)
            )

            Text(
                value,
                style = MaterialTheme.typography.displaySmall,
                color = valueColor
            )
        }
    }
}

/**
 * Wellness Section Header - Used to organize content into sections
 */
@Composable
fun WellnessSectionHeader(
    title: String,
    modifier: Modifier = Modifier,
    icon: @Composable (() -> Unit)? = null
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (icon != null) {
            icon()
            Spacer(modifier = Modifier.width(12.dp))
        }

        Text(
            title,
            style = MaterialTheme.typography.titleLarge,
            color = DeepOceanBlue
        )
    }
}

/**
 * Wellness Divider - Subtle spacing between sections
 */
@Composable
fun WellnessDivider(
    modifier: Modifier = Modifier,
    color: Color = Color(0xFFE0E0E0),
    thickness: Dp = 1.dp,
    alpha: Float = 0.5f
) {
    Divider(
        modifier = modifier
            .fillMaxWidth()
            .alpha(alpha),
        color = color,
        thickness = thickness
    )
}

/**
 * Wellness Content Padding - Standard padding for screen content
 */
fun Modifier.wellnessPadding(
    horizontal: Dp = 24.dp,
    vertical: Dp = 16.dp
): Modifier = this.padding(horizontal = horizontal, vertical = vertical)

/**
 * Wellness Spacer - Standard spacing between elements
 */
@Composable
fun WellnessSpacer(height: Dp = 16.dp) {
    Spacer(modifier = Modifier.height(height))
}

/**
 * Wellness Progress Card - Display progress with visual indicator
 */
@Composable
fun WellnessProgressCard(
    title: String,
    progress: Float,
    progressText: String,
    modifier: Modifier = Modifier
) {
    WellnessCard(modifier = modifier) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Text(
                title,
                style = MaterialTheme.typography.titleMedium,
                color = DeepOceanBlue,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            LinearProgressIndicator(
                progress = { progress.coerceIn(0f, 1f) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp),
                color = FreshMint,
                trackColor = Color(0xFFE0E0E0)
            )

            Text(
                progressText,
                style = MaterialTheme.typography.labelSmall,
                color = Color.Gray,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}

/**
 * Wellness Tag - Small label/badge component
 */
@Composable
fun WellnessTag(
    text: String,
    modifier: Modifier = Modifier,
    backgroundColor: Color = Color(0xFFF0F8FF),
    textColor: Color = ForestTeal
) {
    Surface(
        modifier = modifier,
        color = backgroundColor,
        shape = RoundedCornerShape(12.dp)
    ) {
        Text(
            text,
            style = MaterialTheme.typography.labelSmall,
            color = textColor,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
        )
    }
}




