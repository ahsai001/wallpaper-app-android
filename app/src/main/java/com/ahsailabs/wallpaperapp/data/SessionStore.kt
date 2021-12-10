package com.ahsailabs.wallpaperapp.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Created by ahmad s on 11/10/21.
 */

private val Context.sessionStore: DataStore<Preferences> by preferencesDataStore("session_store")
class SessionStore(private val context: Context) {
    fun getString(key: String): Flow<String> = context.sessionStore.data
        .map { preferences ->
            preferences[stringPreferencesKey(key)] ?: ""
        }

    suspend fun setString(key: String, value: String) {
        context.sessionStore.edit { preferences ->
            preferences[stringPreferencesKey(key)] = value
        }
    }

    fun getInt(key: String): Flow<Int> = context.sessionStore.data
        .map { preferences ->
            preferences[intPreferencesKey(key)] ?: 0
        }

    suspend fun setInt(key: String, value: Int) {
        context.sessionStore.edit { preferences ->
            preferences[intPreferencesKey(key)] = value
        }
    }
}