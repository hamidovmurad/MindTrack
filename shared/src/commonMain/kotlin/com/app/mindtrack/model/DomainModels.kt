package com.app.mindtrack.model

// Domain models for the MindTrack shared module.
// Keep these simple POJOs for now; we can add kotlinx.serialization or other
// annotations later once the serialization strategy is chosen.

/**
 * Represents a user of the app.
 */
data class User(
    val id: String,
    val displayName: String? = null,
    val email: String? = null,
    // Use 0L as a neutral default for multiplatform common code; callers should
    // set a proper timestamp (epoch millis) where needed.
    val createdAt: Long = 0L
)

/**
 * Represents one mood entry (a single mood measurement by the user).
 * mood is an integer rating (e.g. 1..10). timestamp is epoch millis.
 */
data class MoodEntry(
    val id: String,
    val userId: String,
    val timestamp: Long,
    val mood: Int,
    val note: String? = null
)

/**
 * A habit or routine item created by the user.
 */
data class Habit(
    val id: String,
    val userId: String,
    val title: String,
    val description: String? = null,
    val frequency: String? = null, // e.g. "daily", "weekly" or cron-like expression
    val enabled: Boolean = true,
    val lastCompletedAt: Long? = null
)

/**
 * Medication reminder / management record.
 */
data class Medication(
    val id: String,
    val userId: String,
    val name: String,
    val dosage: String? = null,
    val schedule: String? = null, // simple textual schedule for MVP
    val active: Boolean = true
)

/**
 * Appointment / doctor management entity.
 */
data class Appointment(
    val id: String,
    val userId: String,
    val title: String,
    val dateTimeMillis: Long,
    val doctorName: String? = null,
    val notes: String? = null
)

/**
 * Emergency contact entry.
 */
data class EmergencyContact(
    val id: String,
    val userId: String,
    val name: String,
    val phone: String,
    val relation: String? = null
)



