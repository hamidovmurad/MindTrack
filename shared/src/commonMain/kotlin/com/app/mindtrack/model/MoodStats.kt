package com.app.mindtrack.model

import com.app.mindtrack.model.MoodEntry

/**
 * Simple business logic helpers for mood analytics used in the MVP.
 */
object MoodStats {
    /**
     * Compute the average mood from a list of MoodEntry. Returns null for empty list.
     */
    fun averageMood(entries: List<MoodEntry>): Double? {
        if (entries.isEmpty()) return null
        val sum = entries.sumOf { it.mood }
        return sum.toDouble() / entries.size
    }

    /**
     * Simple trend: compare average of the last 'window' entries to the previous window.
     * Returns positive value if mood improved, negative if it declined, 0 if equal, or null
     * if not enough data.
     */
    fun trend(entries: List<MoodEntry>, window: Int = 3): Double? {
        if (entries.size < window * 2) return null
        val sorted = entries.sortedBy { it.timestamp }
        val lastWindow = sorted.takeLast(window)
        val prevWindow = sorted.dropLast(window).takeLast(window)
        val lastAvg = averageMood(lastWindow) ?: return null
        val prevAvg = averageMood(prevWindow) ?: return null
        return lastAvg - prevAvg
    }
}


