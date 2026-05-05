package com.app.mindtrack.model

import com.app.mindtrack.model.MoodEntry
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class MoodStatsTest {
    @Test
    fun averageMood_empty() {
        val avg = MoodStats.averageMood(emptyList<MoodEntry>())
        assertNull(avg)
    }

    @Test
    fun averageMood_nonEmpty() {
        val entries = listOf<MoodEntry>(
            MoodEntry("1", "u1", 1L, 5),
            MoodEntry("2", "u1", 2L, 7),
            MoodEntry("3", "u1", 3L, 8)
        )
        val avg = MoodStats.averageMood(entries)
        assertEquals((5 + 7 + 8) / 3.0, avg)
    }

    @Test
    fun trend_notEnoughData() {
        val entries = listOf(
            MoodEntry("1", "u1", 1L, 5),
            MoodEntry("2", "u1", 2L, 6)
        )
        val trend = MoodStats.trend(entries, window = 2)
        assertNull(trend)
    }
}



