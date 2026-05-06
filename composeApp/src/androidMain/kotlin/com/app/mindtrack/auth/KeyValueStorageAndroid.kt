package com.app.mindtrack.auth

import android.content.Context
import android.content.SharedPreferences

class AndroidKVStore(context: Context) : KVStore {
    private val prefs: SharedPreferences = context.getSharedPreferences("mindtrack_prefs", Context.MODE_PRIVATE)

    override fun putString(key: String, value: String) { prefs.edit().putString(key, value).apply() }
    override fun getString(key: String): String? = prefs.getString(key, null)
    override fun remove(key: String) { prefs.edit().remove(key).apply() }
    override fun clear() { prefs.edit().clear().apply() }
}

fun initPlatformStorage(context: Context) {
    StorageProvider.setStore(AndroidKVStore(context))
}



