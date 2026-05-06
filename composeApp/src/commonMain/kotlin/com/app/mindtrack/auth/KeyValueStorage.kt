package com.app.mindtrack.auth

/**
 * Simple key-value store interface and a mutable provider.
 * By default an in-memory map is used so the code works in previews.
 * Platform code should call StorageProvider.setStore(...) to provide a platform-backed implementation.
 */
interface KVStore {
    fun putString(key: String, value: String)
    fun getString(key: String): String?
    fun remove(key: String)
    fun clear()
}

object StorageProvider {
    private var impl: KVStore = object : KVStore {
        private val map = mutableMapOf<String, String>()
        override fun putString(key: String, value: String) { map[key] = value }
        override fun getString(key: String): String? = map[key]
        override fun remove(key: String) { map.remove(key) }
        override fun clear() { map.clear() }
    }

    fun setStore(store: KVStore) { impl = store }
    fun get(): KVStore = impl
}



