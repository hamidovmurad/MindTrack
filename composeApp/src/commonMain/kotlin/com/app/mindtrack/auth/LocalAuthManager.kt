package com.app.mindtrack.auth

import com.app.mindtrack.auth.StorageProvider

data class User(
    val email: String,
    val name: String
)

object LocalAuthManager {
    private const val KEY_EMAIL = "auth_email"
    private const val KEY_PASSWORD = "auth_password"
    private const val KEY_NAME = "auth_name"
    private const val KEY_LOGGED = "auth_logged"

    fun register(email: String, password: String, name: String): Boolean {
        // If an account already exists with same email, reject
        val store = StorageProvider.get()
        val existing = store.getString(KEY_EMAIL)
        if (existing != null) return false

        store.putString(KEY_EMAIL, email)
        store.putString(KEY_PASSWORD, password)
        store.putString(KEY_NAME, name)
        store.putString(KEY_LOGGED, "true")
        return true
    }

    fun login(email: String, password: String): Boolean {
        val store = StorageProvider.get()
        val storedEmail = store.getString(KEY_EMAIL)
        val storedPassword = store.getString(KEY_PASSWORD)
        if (storedEmail == null || storedPassword == null) return false
        val ok = storedEmail == email && storedPassword == password
        if (ok) store.putString(KEY_LOGGED, "true")
        return ok
    }

    fun logout() {
        // Clear all stored auth and demo app data for presentation purposes
        val store = StorageProvider.get()
        // Clear entire store so moods/habits and other demo data are removed on logout
        store.clear()
    }

    fun getCurrentUser(): User? {
        val store = StorageProvider.get()
        val logged = store.getString(KEY_LOGGED)
        if (logged != "true") return null
        val email = store.getString(KEY_EMAIL) ?: return null
        val name = store.getString(KEY_NAME) ?: ""
        return User(email = email, name = name)
    }

    /**
     * Update local profile fields (email and name). For presentation/demo only.
     */
    fun updateProfile(email: String, name: String): Boolean {
        val store = StorageProvider.get()
        val current = store.getString(KEY_EMAIL)
        // If changing to an email that already exists (and is a different account), reject for simplicity
        if (current != null && current != email) {
            // allow updating email in-place for demo (overwrite)
        }
        store.putString(KEY_EMAIL, email)
        store.putString(KEY_NAME, name)
        store.putString(KEY_LOGGED, "true")
        return true
    }
}



