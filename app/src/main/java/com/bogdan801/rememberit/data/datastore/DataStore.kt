package com.bogdan801.rememberit.data.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

suspend fun Context.saveToDataStore(key: String, value: Int) {
    val dataStoreKey = intPreferencesKey(key)
    dataStore.edit { settings ->
        settings[dataStoreKey] = value
    }
}

suspend fun Context.readFromDataStore(key: String): Int? {
    val dataStoreKey = intPreferencesKey(key)
    val preferences = dataStore.data.first()
    return preferences[dataStoreKey]
}