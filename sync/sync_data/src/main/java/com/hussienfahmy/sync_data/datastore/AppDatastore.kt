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

    suspend fun isFirstTimeInstall(): Boolean {
        val preferences = datastore.data.first()
        return preferences[firstInstallKey] ?: true
    }

    suspend fun setFirstTimeInstall(isFirstTimeInstall: Boolean) {
        datastore.edit { preferences ->
            preferences[firstInstallKey] = isFirstTimeInstall
        }
    }

    companion object {
        private val firstInstallKey = booleanPreferencesKey("first_install")
    }
}