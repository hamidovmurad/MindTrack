package com.app.mindtrack

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.app.mindtrack.ui.navigation.MindTrackNavigation

@Composable
@Preview
fun App() {
    MaterialTheme {
        MindTrackNavigation()
    }
}