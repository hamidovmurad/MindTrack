package com.app.mindtrack.storage

import com.app.mindtrack.auth.StorageProvider
import com.app.mindtrack.model.Habit
import com.app.mindtrack.model.MoodEntry
import kotlin.random.Random
// NOTE: keep this implementation dependency-free to avoid adding kotlinx.serialization.
/**
 * Minimal local datastore for presentation/demo purposes.
 * Persists simple lists of mood entries and habits using the platform KVStore.
 * Data is stored under keys:
 *  - "mood_ids" -> comma separated ids
 *  - "mood_<id>" -> serialized mood entry
 *  - "habit_ids" -> comma separated ids
 *  - "habit_<id>" -> serialized habit
 *
 * Serialization format is lightweight and length-prefixed for free-text fields to avoid delimiter
 * collisions. This is intentionally small and dependency-free for the demo.
 */
object LocalDataStore {
    private const val KEY_MOOD_IDS = "mood_ids"
    private const val KEY_HABIT_IDS = "habit_ids"
    private const val MILLIS_PER_DAY = 24L * 60L * 60L * 1000L

    // --- Public API ---
    fun saveMoodEntry(entry: MoodEntry) {
        val store = StorageProvider.get()
        val id = entry.id
        store.putString("mood_$id", serializeMood(entry))
        val ids = loadIds(store.getString(KEY_MOOD_IDS))
        if (!ids.contains(id)) ids.add(id)
        store.putString(KEY_MOOD_IDS, ids.joinToString(","))
    }

    /**
     * Save a mood entry but ensure there is only one mood per user per calendar day.
     * If an entry exists for the same user/day, it will be overwritten.
     */
    fun saveDailyMoodEntry(entry: MoodEntry) {
        val store = StorageProvider.get()
        val ids = loadIds(store.getString(KEY_MOOD_IDS))
        val existingId = ids.firstOrNull { id ->
            store.getString("mood_$id")?.let { raw ->
                deserializeMood(raw)
            }?.let { it.userId == entry.userId && (it.timestamp / MILLIS_PER_DAY) == (entry.timestamp / MILLIS_PER_DAY) }
                ?: false
        }
        if (existingId != null) {
            // overwrite existing day's entry
            store.putString("mood_$existingId", serializeMood(entry))
        } else {
            // new entry
            saveMoodEntry(entry)
        }
    }

    fun getAllMoodEntries(): List<MoodEntry> {
        val store = StorageProvider.get()
        val ids = loadIds(store.getString(KEY_MOOD_IDS))
        return ids.mapNotNull { id ->
            store.getString("mood_$id")?.let { deserializeMood(it) }
        }
    }

    fun saveHabit(habit: Habit) {
        val store = StorageProvider.get()
        val id = habit.id
        store.putString("habit_$id", serializeHabit(habit))
        val ids = loadIds(store.getString(KEY_HABIT_IDS))
        if (!ids.contains(id)) ids.add(id)
        store.putString(KEY_HABIT_IDS, ids.joinToString(","))
    }

    fun getAllHabits(): List<Habit> {
        val store = StorageProvider.get()
        val ids = loadIds(store.getString(KEY_HABIT_IDS))
        return ids.mapNotNull { id ->
            store.getString("habit_$id")?.let { deserializeHabit(it) }
        }
    }

    fun deleteHabit(habitId: String) {
        val store = StorageProvider.get()
        store.remove("habit_$habitId")
        val ids = loadIds(store.getString(KEY_HABIT_IDS))
        if (ids.remove(habitId)) store.putString(KEY_HABIT_IDS, ids.joinToString(","))
    }

    // Helper to create an ID when creating entries from UI
    fun createId(prefix: String = "id"): String = "${prefix}_${kotlin.math.abs(Random.nextLong())}_${Random.nextInt(0, 9999)}"

    // --- Serialization helpers ---
    // Format for mood: id|userId|timestamp|mood|noteLen:note
    private fun serializeMood(m: MoodEntry): String {
        val note = m.note ?: ""
        return buildString {
            append(m.id)
            append("|")
            append(m.userId)
            append("|")
            append(m.timestamp)
            append("|")
            append(m.mood)
            append("|")
            append(note.length)
            append(":")
            append(note)
        }
    }

    private fun deserializeMood(s: String): MoodEntry? {
        try {
            // Parse exactly 5 sections: id|userId|timestamp|mood|noteLen:note
            val parts = s.split("|", limit = 5)
            if (parts.size < 5) return null
            val id = parts[0]
            val userId = parts[1]
            val timestamp = parts[2].toLongOrNull() ?: 0L
            val mood = parts[3].toIntOrNull() ?: 0
            val lenAndNote = parts[4]
            val colon = lenAndNote.indexOf(":")
            if (colon < 0) return null
            val len = lenAndNote.substring(0, colon).toIntOrNull() ?: 0
            val rawNote = lenAndNote.substring(colon + 1)
            val note = if (len == 0) null else rawNote.take(len)
            return MoodEntry(id = id, userId = userId, timestamp = timestamp, mood = mood, note = note)
        } catch (t: Throwable) {
            return null
        }
    }

    // Format for habit: id|userId|titleLen:title|descLen:desc|frequency|enabled|lastCompletedAt
    private fun serializeHabit(h: Habit): String {
        val title = h.title
        val desc = h.description ?: ""
        return buildString {
            append(h.id)
            append("|")
            append(h.userId)
            append("|")
            append(title.length)
            append(":")
            append(title)
            append("|")
            append(desc.length)
            append(":")
            append(desc)
            append("|")
            append(h.frequency ?: "")
            append("|")
            append(if (h.enabled) "1" else "0")
            append("|")
            append(h.lastCompletedAt?.toString() ?: "")
        }
    }

    private fun deserializeHabit(s: String): Habit? {
        try {
            val firstParts = s.split("|")
            if (firstParts.size < 7) return null
            val id = firstParts[0]
            val userId = firstParts[1]
            // title and desc parsing require reading length:content
            val afterIdUser = s.substringAfter("$id|$userId|")
            val titleLenStr = afterIdUser.substringBefore(":")
            val titleLen = titleLenStr.toIntOrNull() ?: 0
            val afterTitleLen = afterIdUser.substringAfter(":")
            val title = if (titleLen == 0) "" else afterTitleLen.substring(0, kotlin.math.min(titleLen, afterTitleLen.length))
            val afterTitle = afterTitleLen.substringAfter(title)
            val descLenStr = afterTitle.substringBefore(":")
            val descLen = descLenStr.toIntOrNull() ?: 0
            val afterDescLen = afterTitle.substringAfter(":")
            val desc = if (descLen == 0) null else afterDescLen.substring(0, kotlin.math.min(descLen, afterDescLen.length))
            val descSafe = desc ?: ""
            val remainder = afterDescLen.substringAfter(descSafe)
            val remParts = remainder.split("|")
            val frequency = remParts.getOrNull(1)?.takeIf { it.isNotEmpty() }
            val enabled = remParts.getOrNull(2)?.let { it == "1" } ?: true
            val lastCompleted = remParts.getOrNull(3)?.toLongOrNull()

            return Habit(
                id = id,
                userId = userId,
                title = title,
                description = desc,
                frequency = frequency,
                enabled = enabled,
                lastCompletedAt = lastCompleted
            )
        } catch (t: Throwable) {
            return null
        }
    }

    private fun loadIds(raw: String?): MutableList<String> = raw?.split(",")?.filter { it.isNotBlank() }?.toMutableList() ?: mutableListOf()
}




