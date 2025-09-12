package com.hussienfahmy.sync_data.datastore

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first

class AppDatastore(
    context: Context
) {
    private val Context.dataStore by preferencesDataStore("app_datastore")
    private val datastore = context.dataStore

    suspend fun isInitialSyncDone(): Boolean {
        val preferences = datastore.data.first()
        return preferences[initialSyncDoneKey] ?: false
    }

    suspend fun setInitialSyncDone(isInitialSyncDone: Boolean) {
        datastore.edit { preferences ->
            preferences[initialSyncDoneKey] = isInitialSyncDone
        }
    }

    companion object {
        private val initialSyncDoneKey = booleanPreferencesKey("initial_sync_done")
    }
}