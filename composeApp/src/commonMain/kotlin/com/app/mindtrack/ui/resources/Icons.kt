package com.app.mindtrack.ui.resources

import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.jetbrains.compose.resources.DrawableResource
import mindtrack.composeapp.generated.resources.Res
import mindtrack.composeapp.generated.resources.alarm
import mindtrack.composeapp.generated.resources.arrow_left
import mindtrack.composeapp.generated.resources.check_circle
import mindtrack.composeapp.generated.resources.chevron_small_right
import mindtrack.composeapp.generated.resources.cross
import mindtrack.composeapp.generated.resources.document_filled
import mindtrack.composeapp.generated.resources.home_2
import mindtrack.composeapp.generated.resources.info_circle
import mindtrack.composeapp.generated.resources.message_circle_dots
import mindtrack.composeapp.generated.resources.pill
import mindtrack.composeapp.generated.resources.plus_circle
import mindtrack.composeapp.generated.resources.question_circle
import mindtrack.composeapp.generated.resources.settings
import mindtrack.composeapp.generated.resources.trash_2
import mindtrack.composeapp.generated.resources.trending_up
import mindtrack.composeapp.generated.resources.user_1
import mindtrack.composeapp.generated.resources.waterdrop
import org.jetbrains.compose.resources.painterResource

/**
 * Drawable-backed icons for multiplatform compatibility.
 * If you need a new icon, add the XML drawable under composeResources/drawable.
 */
@Composable
private fun ResourceIcon(
    resource: DrawableResource,
    modifier: Modifier = Modifier,
) {
    Icon(
        painter = painterResource(resource),
        contentDescription = null,
        modifier = modifier
    )
}

@Composable
fun AddIcon(modifier: Modifier = Modifier) {
    ResourceIcon(Res.drawable.plus_circle, modifier)
}

@Composable
fun BackIcon(modifier: Modifier = Modifier) {
    ResourceIcon(Res.drawable.arrow_left, modifier)
}

@Composable
fun DeleteIcon(modifier: Modifier = Modifier) {
    ResourceIcon(Res.drawable.trash_2, modifier)
}

@Composable
fun TrendingUpIcon(modifier: Modifier = Modifier) {
    ResourceIcon(Res.drawable.trending_up, modifier)
}

@Composable
fun CheckCircleIcon(modifier: Modifier = Modifier) {
    ResourceIcon(Res.drawable.check_circle, modifier)
}

@Composable
fun PharmacyIcon(modifier: Modifier = Modifier) {
    ResourceIcon(Res.drawable.pill, modifier)
}

@Composable
fun CloseIcon(modifier: Modifier = Modifier) {
    ResourceIcon(Res.drawable.cross, modifier)
}

@Composable
fun DashboardIcon(modifier: Modifier = Modifier) {
    ResourceIcon(Res.drawable.home_2, modifier)
}

@Composable
fun HabitIcon(modifier: Modifier = Modifier) {
    ResourceIcon(Res.drawable.check_circle, modifier)
}

@Composable
fun AssistantIcon(modifier: Modifier = Modifier) {
    ResourceIcon(Res.drawable.message_circle_dots, modifier)
}

@Composable
fun WaterIcon(modifier: Modifier = Modifier) {
    ResourceIcon(Res.drawable.waterdrop, modifier)
}

@Composable
fun SettingsIcon(modifier: Modifier = Modifier) {
    ResourceIcon(Res.drawable.settings, modifier)
}

@Composable
fun ProfileIcon(modifier: Modifier = Modifier) {
    ResourceIcon(Res.drawable.user_1, modifier)
}

@Composable
fun ReminderIcon(modifier: Modifier = Modifier) {
    ResourceIcon(Res.drawable.alarm, modifier)
}

@Composable
fun InfoIcon(modifier: Modifier = Modifier) {
    ResourceIcon(Res.drawable.info_circle, modifier)
}

@Composable
fun DocumentIcon(modifier: Modifier = Modifier) {
    ResourceIcon(Res.drawable.document_filled, modifier)
}

@Composable
fun QuestionIcon(modifier: Modifier = Modifier) {
    ResourceIcon(Res.drawable.question_circle, modifier)
}

@Composable
fun ChevronRightIcon(modifier: Modifier = Modifier) {
    ResourceIcon(Res.drawable.chevron_small_right, modifier)
}

