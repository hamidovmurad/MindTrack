package com.app.mindtrack.auth

import platform.Foundation.NSUserDefaults

class IOSKVStore : KVStore {
    private val defaults = NSUserDefaults.standardUserDefaults()

    override fun putString(key: String, value: String) {
        defaults.setObject(value, forKey = key)
    }

    override fun getString(key: String): String? = defaults.stringForKey(key)

    override fun remove(key: String) {
        defaults.removeObjectForKey(key)
    }

    override fun clear() {
        val dict = defaults.dictionaryRepresentation()
        val keys = dict.keys.mapNotNull { it as? String }
        keys.forEach { defaults.removeObjectForKey(it) }
    }
}

fun initPlatformStorage() {
    // iOS initialization point - set the StorageProvider to use NSUserDefaults
    StorageProvider.setStore(IOSKVStore())
}



